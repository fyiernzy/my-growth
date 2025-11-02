package com.ifast.ipaymy.backend.util.observability.formatter;


import com.ifast.ipaymy.backend.util.validator.ValidatorUtils;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

@NoArgsConstructor
public class PrettySqlFormat implements MessageFormattingStrategy {

    // Attribute names
    private static final String URL = "url";
    private static final String CATEGORY = "category";
    private static final String EXECUTION_MS = "execution (ms)";

    // Header names
    private static final String JDBC_INFO = "JDBC INFO";
    private static final String SQL_COMMAND = "SQL COMMAND";

    // Formatting options
    private static final String FORMAT = " %-18s: %s%n";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    // Helpers
    private final BasicFormatterImpl FORMATTER = new BasicFormatterImpl();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        if (ValidatorUtils.isNotEqual(Category.STATEMENT.getName(), category) || ValidatorUtils.isBlank(sql)) {
            return null;
        }

        return System.lineSeparator()
                + header(JDBC_INFO)
                + System.lineSeparator()
                + String.format(FORMAT, URL, url)
                + String.format(FORMAT, CATEGORY, category)
                + String.format(FORMAT, EXECUTION_MS, elapsed)
                + header(SQL_COMMAND)
                + FORMATTER.format(sql);
    }

    private String header(String value) {
        return BLUE + value + RESET;
    }
}

