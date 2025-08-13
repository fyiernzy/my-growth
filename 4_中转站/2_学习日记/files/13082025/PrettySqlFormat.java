package com.example.demo.config;


import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PrettySqlFormat implements MessageFormattingStrategy {

    private static final BasicFormatterImpl FORMATTER = new BasicFormatterImpl();
    private static final String FORMAT = " %-18s: %s%n";

    @PostConstruct
    public void register() {
        // Tell P6Spy to use *this* class as its formatter
        com.p6spy.engine.spy.P6SpyOptions.getActiveInstance()
            .setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        if (!Category.STATEMENT.getName().equals(category) || sql == null || sql.trim().isEmpty()) {
            return "";
        }

        LinkedHashMap<String, String> pairs = new LinkedHashMap<>(Map.of(
            "url", url,
            "category", category,
            "execution (ms)", String.valueOf(elapsed)
        ));

        return System.lineSeparator()
               + "\u001B[34m" + "JDBC INFO" + "\u001B[0m"
               + System.lineSeparator()
               + SqlFormattingUtil.tabular(pairs)
               + "\u001B[34m" + "SQL COMMAND" + "\u001B[0m"
               + FORMATTER.format(sql)
               + System.lineSeparator();
//        +String.format(FORMAT, "url", url)
//        + String.format(FORMAT, "category", category)
//        + String.format(FORMAT, "execution (ms)", elapsed)
//        + String.format(FORMAT, "sql", FORMATTER.format(sql));
    }
}
