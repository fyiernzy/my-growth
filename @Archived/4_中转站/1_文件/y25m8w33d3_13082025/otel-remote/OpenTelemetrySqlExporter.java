package com.ifast.ipaymy.backend.util.observability.formatter;

import com.ifast.ipaymy.backend.util.observability.helpers.LoggingUtil;
import com.ifast.ipaymy.backend.util.observability.constant.ObservabilityConstants;
import com.ifast.ipaymy.backend.util.observability.helpers.SqlTableFormatterUtil;
import com.ifast.ipaymy.backend.util.validator.ValidatorUtils;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.Logger;

@NoArgsConstructor(staticName = "create")
public class OpenTelemetrySqlExporter implements SpanExporter {

    private static final String JDBC_SCOPE = "io.opentelemetry.jdbc";

    // Attribute names
    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";
    private static final String DB_STATEMENT = "db.statement";
    private static final String DB_SYSTEM = "db.system";
    private static final String DB_NAME = "db.name";
    private static final String DB_OPERATION = "db.operation";
    private static final String DB_SQL_TABLE = "db.sql.table";
    private static final String EXECUTION_MS = "execution (ms)";

    // Attribute keys
    private static final AttributeKey<String> DB_STATEMENT_KEY = AttributeKey.stringKey(DB_STATEMENT);
    private static final AttributeKey<String> DB_SYSTEM_KEY = AttributeKey.stringKey(DB_SYSTEM);
    private static final AttributeKey<String> DB_NAME_KEY = AttributeKey.stringKey(DB_NAME);
    private static final AttributeKey<String> DB_OPERATION_KEY = AttributeKey.stringKey(DB_OPERATION);
    private static final AttributeKey<String> DB_SQL_TABLE_KEY = AttributeKey.stringKey(DB_SQL_TABLE);

    // Formatting options
    private static final String FORMAT = " %-18s: %s%n";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    // Helpers
    private static final Logger logger = Logger.getLogger(OpenTelemetrySqlExporter.class.getName());
    private final AtomicBoolean shutdown = new AtomicBoolean();
    private final BasicFormatterImpl formatter = new BasicFormatterImpl();

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        if (shutdown.get()) {
            return CompletableResultCode.ofFailure();
        }
        spans.stream()
                .filter(this::isFromJdbcInstrumentation)
                .forEach(spanData -> {
                    String message = buildMessage(spanData);
                    Map<String, String> mdc = Map.of(
                            ObservabilityConstants.TRACE_ID, spanData.getTraceId(),
                            ObservabilityConstants.SPAN_ID, spanData.getSpanId()
                    );
                    LoggingUtil.withMdc(mdc, () -> logger.info(message));
                });

        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        CompletableResultCode resultCode = new CompletableResultCode();
        for (Handler handler : logger.getHandlers()) {
            try {
                handler.flush();
            } catch (Throwable t) {
                resultCode.fail();
            }
        }
        return resultCode.succeed();
    }

    @Override
    public CompletableResultCode shutdown() {
        if (!shutdown.compareAndSet(false, true)) {
            logger.info("shutdown() called more than once.");
            return CompletableResultCode.ofSuccess();
        }
        return flush();
    }

    private boolean isFromJdbcInstrumentation(SpanData spanData) {
        return Optional.ofNullable(spanData)
                .map(SpanData::getInstrumentationScopeInfo)
                .map(InstrumentationScopeInfo::getName)
                .map(JDBC_SCOPE::equals)
                .orElse(false);
    }

    private String buildMessage(SpanData spanData) {
        SqlInfo sqlInfo = toSqlInfo(spanData);
        return System.lineSeparator()
               + "\u001B[34m" + "JDBC INFO" + "\u001B[0m"
               + System.lineSeparator()
               + String.format(FORMAT, TRACE_ID, sqlInfo.traceId)
               + String.format(FORMAT, SPAN_ID, sqlInfo.spanId)
               + String.format(FORMAT, DB_SYSTEM, sqlInfo.dbSystem)
               + String.format(FORMAT, DB_NAME, sqlInfo.dbName)
               + String.format(FORMAT, DB_OPERATION, sqlInfo.dbOperation)
               + String.format(FORMAT, DB_SQL_TABLE, sqlInfo.dbSqlTable)
               + String.format(FORMAT, EXECUTION_MS, sqlInfo.executionMs)
               + String.format(FORMAT, "sql", sqlInfo.sql);
//        LinkedHashMap<String, String> pairs = new LinkedHashMap<>();
//        pairs.put(TRACE_ID, sqlInfo.traceId);
//        pairs.put(SPAN_ID, sqlInfo.spanId);
//        pairs.put(DB_SYSTEM, sqlInfo.dbSystem);
//        pairs.put(DB_NAME, sqlInfo.dbName);
//        pairs.put(DB_OPERATION, sqlInfo.dbOperation);
//        pairs.put(DB_SQL_TABLE, sqlInfo.dbSqlTable);
//        pairs.put(EXECUTION_MS, sqlInfo.executionMs);
//
//        return System.lineSeparator()
//                + header("JDBC INFO")
//                + System.lineSeparator()
//                + SqlTableFormatterUtil.formatAsTable(pairs)
//                + header("SQL COMMAND")
//                + sqlInfo.sql
//                + System.lineSeparator();
    }

    private SqlInfo toSqlInfo(SpanData spanData) {
        long startTime = spanData.getStartEpochNanos();
        long endTime = spanData.getEndEpochNanos();
        long executionMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

        Attributes attributes = spanData.getAttributes();

        String dbSystem = orEmpty(attributes.get(DB_SYSTEM_KEY));
        String dbName = orEmpty(attributes.get(DB_NAME_KEY));
        String dbOperation = orEmpty(attributes.get(DB_OPERATION_KEY));
        String dbTable = orEmpty(attributes.get(DB_SQL_TABLE_KEY));
        String sql = Optional.ofNullable(attributes.get(DB_STATEMENT_KEY)).map(formatter::format).orElse("");

        return new SqlInfo(spanData.getTraceId(), spanData.getSpanId(), dbSystem, dbName,
                dbOperation, dbTable, String.valueOf(executionMs), sql);
    }

    private String orEmpty(String value) {
        return ValidatorUtils.isNotBlank(value) ? value : "";
    }

    private String header(String value) {
        return BLUE + value + RESET;
    }

    private record SqlInfo(
            String traceId,
            String spanId,
            String dbSystem,
            String dbName,
            String dbOperation,
            String dbSqlTable,
            String executionMs,
            String sql
    ) {

    }
}