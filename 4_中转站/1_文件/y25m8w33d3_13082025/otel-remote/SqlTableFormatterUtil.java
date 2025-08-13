package com.ifast.ipaymy.backend.util.observability.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SqlTableFormatterUtil {
    private static final int DEFAULT_PADDING_SIZE = 3;
    private static final String DEFAULT_SEPARATOR = "|";


    /**
     * Formats the provided metadata into a table-like string for logging.
     *
     * @param metadata LinkedHashMap containing key-value pairs to format.
     * @return A formatted string in a tabular layout.
     */
    public static String formatAsTable(LinkedHashMap<String, String> metadata) {
        /*
          LinkedHashMap is used to maintain the insertion order
        */
        int maxKeyLength = maxLength(metadata.keySet());
        int maxValueLength = maxLength(metadata.values());

        // 1. Generate the constants
        String padding = " ".repeat(DEFAULT_PADDING_SIZE);
        String borderLine = String.format("%s%n", "-".repeat(maxKeyLength + maxValueLength + 3 * DEFAULT_SEPARATOR.length() + 4 * DEFAULT_PADDING_SIZE));

        // 2. Formulate the format
        String keyColumnFormat = DEFAULT_SEPARATOR + padding + "%-" + maxKeyLength + "s" + padding;
        String valueColumnFormat = DEFAULT_SEPARATOR + padding + "%-" + maxValueLength + "s" + padding;
        String rowFormat = keyColumnFormat + valueColumnFormat + DEFAULT_SEPARATOR + "%n";

        // 3. Produce the content using format and metadata
        String content = metadata.sequencedEntrySet().stream()
                .map(entry -> String.format(rowFormat, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining());

        return borderLine + content + borderLine;
    }

    private static int maxLength(Collection<String> collection) {
        return collection.stream()
                // To filter multiline values, such as SQL for well-formatted output
                .filter(SqlTableFormatterUtil::isSingleLine)
                .mapToInt(String::length)
                .max()
                .orElse(20);
    }

    private static boolean isSingleLine(String line) {
        return line.split(System.lineSeparator()).length == 1;
    }
}