package com.ngzhiyang.ifast_itp_assignment.logging;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.springframework.stereotype.Component;

@Component
public class PrettySqlFormat implements MessageFormattingStrategy {
    private static final BasicFormatterImpl FORMATTER = new BasicFormatterImpl();

    @PostConstruct
    public void register() {
        // Tell P6Spy to use *this* class as its formatter
        com.p6spy.engine.spy.P6SpyOptions.getActiveInstance()
                .setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(
            int connectionId,
            String now,
            long elapsed,
            String category,
            String prepared,
            String sql,
            String url) {

        if (!Category.STATEMENT.getName().equals(category) || sql == null || sql.trim().isEmpty()) {
            return "";
        }

        // choose a style based on statement type
        String formatted = FORMATTER.format(sql);

        // include timing & category if you like
        return String.format("[%s] took %dms:\n%s", category, elapsed, formatted);
    }
}

