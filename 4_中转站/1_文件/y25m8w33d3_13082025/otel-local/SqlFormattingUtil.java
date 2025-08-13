package com.example.demo.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class SqlFormattingUtil {

    public static String tabular(LinkedHashMap<String, String> metadata) {
        int maxKeyLen = maxLength(metadata.keySet());
        int maxValLen = maxLength(metadata.values());

        int paddingSize = 3;
        String split = "|";

        String padding = " ".repeat(paddingSize);

        String format = split
                        + padding
                        + "%-"
                        + maxKeyLen
                        + "s"
                        + padding
                        + split
                        + padding
                        + "%-"
                        + maxValLen
                        + "s"
                        + padding
                        + split
                        + "%n";

        String rule = "-".repeat(maxKeyLen + maxValLen + 3 * split.length() + 4 * paddingSize)
                      + System.lineSeparator();

        return rule
               + metadata.sequencedEntrySet().stream()
                   .map(entry -> String.format(format, entry.getKey(), entry.getValue()))
                   .collect(Collectors.joining())
               + rule;
    }

    private static int maxLength(Collection<String> collection) {
        return collection.stream()
            // For each multi-line value, compute its longest line length
            .mapToInt(value -> Arrays.stream(value.split(System.lineSeparator()))
                .mapToInt(String::length)
                .max()
                .orElse(-1))
            .max()
            .orElse(20);
    }

    static class PairsBuilder {

    }
}
