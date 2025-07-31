package com.example.demo.assertions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorsUtil {

    public static boolean isNotBlank(String string) {
        return string != null && string.isBlank();
    }
}
