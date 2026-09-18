package com.ifast.ipaymy.backend.util.observability.formatter;


import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

import java.util.LinkedHashMap;

@NoArgsConstructor
public class PrettySqlFormat implements MessageFormattingStrategy {

    // Attribute names
    private static final String URL = "url";
    private static final String CATEGORY = "category";
    private static final String EXECUTION_MS = "execution (ms)";

    // Formatting options
    private static final String FORMAT = " %-18s: %s%n";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    // Helpers
    private final BasicFormatterImpl FORMATTER = new BasicFormatterImpl();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        if (!Category.STATEMENT.getName().equals(category) || sql == null || sql.trim().isEmpty()) {
            return "";
        }

        LinkedHashMap<String, String> pairs = new LinkedHashMap<>();
        pairs.put(URL, url);
        pairs.put(CATEGORY, category);
        pairs.put(EXECUTION_MS, String.valueOf(elapsed));

        return System.lineSeparator()
                + header("JDBC INFO")
//                + System.lineSeparator()
//                + SqlTableFormatterUtil.formatAsTable(pairs)
//                + header("SQL COMMAND")
//                + formatter.format(sql)
//                + System.lineSeparator();
        +String.format(FORMAT, "url", url)
        + String.format(FORMAT, "category", category)
        + String.format(FORMAT, "execution (ms)", elapsed)
        + String.format(FORMAT, "sql", FORMATTER.format(sql));
    }

    private String header(String value) {
        return BLUE + value + RESET;
    }
}

