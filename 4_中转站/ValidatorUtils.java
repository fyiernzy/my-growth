package com.ifast.ipaymy.backend.util.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ValidatorUtils {
    private static final int DEFAULT_MAX_ULPS = 1;

    // =========================== Empty Checking ================================
    // ===== String =====
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    // ===== Collection =====
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }

    // ===== Map =====
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtils.isNotEmpty(map);
    }

    // ===== Optional =====
    public static boolean isEmpty(Optional<?> optional) {
        return optional == null || optional.isEmpty();
    }

    public static boolean isNotEmpty(Optional<?> optional) {
        return optional != null && optional.isPresent();
    }

    // ===== Arrays =====
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    // ===== Numbers =====
    public static boolean isEmpty(Integer value) {
        return value == null;
    }

    public static boolean isNotEmpty(Integer value) {
        return value != null;
    }

    public static boolean isEmpty(Long value) {
        return value == null;
    }

    public static boolean isNotEmpty(Long value) {
        return value != null;
    }

    public static boolean isEmpty(Double value) {
        return value == null;
    }

    public static boolean isNotEmpty(Double value) {
        return value != null;
    }

    public static boolean isEmpty(BigDecimal value) {
        return value == null;
    }

    public static boolean isNotEmpty(BigDecimal value) {
        return value != null;
    }

    // ===== Boolean =====
    public static boolean isEmpty(Boolean value) {
        return value == null;
    }

    public static boolean isNotEmpty(Boolean value) {
        return value != null;
    }

    public static boolean isTrue(Boolean value) {
        return BooleanUtils.isTrue(value);
    }

    public static boolean isFalse(Boolean value) {
        return BooleanUtils.isFalse(value);
    }

    public static boolean isNotTrue(Boolean value) {
        return BooleanUtils.isNotTrue(value);
    }

    public static boolean isNotFalse(Boolean value) {
        return BooleanUtils.isNotFalse(value);
    }

    // ===== Date/Time Types =====
    public static boolean isEmpty(LocalDate date) {
        return date == null;
    }

    public static boolean isNotEmpty(LocalDate date) {
        return date != null;
    }

    public static boolean isEmpty(LocalDateTime dateTime) {
        return dateTime == null;
    }

    public static boolean isNotEmpty(LocalDateTime dateTime) {
        return dateTime != null;
    }

    public static boolean isEmpty(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null;
    }

    public static boolean isNotEmpty(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null;
    }

    public static boolean isEmpty(Instant instant) {
        return instant == null;
    }

    public static boolean isNotEmpty(Instant instant) {
        return instant != null;
    }

    // ===== Generic Type =====
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    // =========================== Equals Checking ================================
    // ===== Generic Equals Handling =====
    public static <T> boolean isEqual(T a, T b) {
        return Objects.equals(a, b);
    }

    public static <T> boolean isNotEqual(T a, T b) {
        return !Objects.equals(a, b);
    }

    // ===== String Equals Handling =====
    public static boolean isEqualsIgnoreCase(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
    }

    public static boolean isNotEqualsIgnoreCase(String a, String b) {
        return !isEqualsIgnoreCase(a, b);
    }

    // =========================== Number Checking ================================

    // ===== int =====
    public static boolean isZero(int value) {
        return value == 0;
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }

    public static boolean isNegative(int value) {
        return value < 0;
    }

    public static boolean isGreaterThan(int a, int b) {
        return a > b;
    }

    public static boolean isGreaterThanOrEqual(int a, int b) {
        return a >= b;
    }

    public static boolean isLessThan(int a, int b) {
        return a < b;
    }

    public static boolean isLessThanOrEqual(int a, int b) {
        return a <= b;
    }

    public static boolean isBetween(int value, int min, int max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(int value, int min, int max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(int value, int min, int max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(int a, int b) {
        return a == b;
    }

    public static boolean isNotEqual(int a, int b) {
        return a != b;
    }

    // ===== long =====
    public static boolean isZero(long value) {
        return value == 0L;
    }

    public static boolean isPositive(long value) {
        return value > 0L;
    }

    public static boolean isNegative(long value) {
        return value < 0L;
    }

    public static boolean isGreaterThan(long a, long b) {
        return a > b;
    }

    public static boolean isGreaterThanOrEqual(long a, long b) {
        return a >= b;
    }

    public static boolean isLessThan(long a, long b) {
        return a < b;
    }

    public static boolean isLessThanOrEqual(long a, long b) {
        return a <= b;
    }

    public static boolean isBetween(long value, long min, long max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(long value, long min, long max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(long value, long min, long max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(long a, long b) {
        return a == b;
    }

    public static boolean isNotEqual(long a, long b) {
        return a != b;
    }

    // ===== double =====
    public static boolean isZero(double value) {
        return Precision.equals(value, 0.0, DEFAULT_MAX_ULPS);
    }

    public static boolean isZero(double value, double tol) {
        return isValidTolerance(tol) && Precision.equals(value, 0.0, tol);
    }

    public static boolean isPositive(double value) {
        return Precision.compareTo(value, 0.0, DEFAULT_MAX_ULPS) > 0;
    }

    public static boolean isPositive(double value, double tol) {
        return Precision.compareTo(value, 0.0, tol) > 0;
    }

    public static boolean isNegative(double value) {
        return Precision.compareTo(value, 0.0, DEFAULT_MAX_ULPS) < 0;
    }

    public static boolean isNegative(double value, double tol) {
        return Precision.compareTo(value, 0.0, tol) < 0;
    }

    public static boolean isGreaterThan(double a, double b) {
        return Precision.compareTo(a, b, DEFAULT_MAX_ULPS) > 0;
    }

    public static boolean isGreaterThan(double a, double b, double tol) {
        return isValidTolerance(tol) && Precision.compareTo(a, b, tol) > 0;
    }

    public static boolean isGreaterThanOrEqual(double a, double b) {
        return Precision.compareTo(a, b, DEFAULT_MAX_ULPS) >= 0;
    }

    public static boolean isGreaterThanOrEqual(double a, double b, double tol) {
        return isValidTolerance(tol) && Precision.compareTo(a, b, tol) >= 0;
    }

    public static boolean isLessThan(double a, double b) {
        return Precision.compareTo(a, b, DEFAULT_MAX_ULPS) < 0;
    }

    public static boolean isLessThan(double a, double b, double tol) {
        return isValidTolerance(tol) && Precision.compareTo(a, b, tol) < 0;
    }

    public static boolean isLessThanOrEqual(double a, double b) {
        return Precision.compareTo(a, b, DEFAULT_MAX_ULPS) <= 0;
    }

    public static boolean isLessThanOrEqual(double a, double b, double tol) {
        return isValidTolerance(tol) && Precision.compareTo(a, b, tol) <= 0;
    }

    public static boolean isBetween(double value, double min, double max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetween(double value, double min, double max, double tol) {
        return isBetweenExclusive(value, min, max, tol);
    }

    public static boolean isBetweenInclusive(double value, double min, double max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenInclusive(double value, double min, double max, double tol) {
        return isGreaterThanOrEqual(value, min, tol) && isLessThanOrEqual(value, max, tol);
    }

    public static boolean isBetweenExclusive(double value, double min, double max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isBetweenExclusive(double value, double min, double max, double tol) {
        return isGreaterThan(value, min, tol) && isLessThan(value, max, tol);
    }

    public static boolean isEqual(double a, double b) {
        return Precision.equals(a, b, DEFAULT_MAX_ULPS);
    }

    public static boolean isEqual(double a, double b, double tol) {
        return isValidTolerance(tol) && Precision.equals(a, b, tol);
    }

    public static boolean isNotEqual(double a, double b) {
        return !isEqual(a, b);
    }

    public static boolean isNotEqual(double a, double b, double tol) {
        return !isEqual(a, b, tol);
    }

    // ===== float =====
    public static boolean isZero(float value) {
        return Precision.equals(value, 0f, DEFAULT_MAX_ULPS);
    }

    public static boolean isPositive(float value) {
        return value > 0f;
    }

    public static boolean isNegative(float value) {
        return value < 0f;
    }

    /* ---------- pairwise comparisons ---------- */

    public static boolean isGreaterThan(float a, float b) {
        return a > b;
    }

    public static boolean isGreaterThanOrEqual(float a, float b) {
        return a >= b;
    }

    public static boolean isLessThan(float a, float b) {
        return a < b;
    }

    public static boolean isLessThanOrEqual(float a, float b) {
        return a <= b;
    }

    /* ---------- between ---------- */

    public static boolean isBetween(float value, float min, float max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(float value, float min, float max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(float value, float min, float max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(float a, float b) {
        return Precision.equals(a, b, DEFAULT_MAX_ULPS);
    }

    public static boolean isNotEqual(float a, float b) {
        return !isEqual(a, b);
    }

    // ===== short =====
    public static boolean isZero(short value) {
        return value == 0;
    }

    public static boolean isPositive(short value) {
        return value > 0;
    }

    public static boolean isNegative(short value) {
        return value < 0;
    }

    public static boolean isGreaterThan(short a, short b) {
        return a > b;
    }

    public static boolean isGreaterThanOrEqual(short a, short b) {
        return a >= b;
    }

    public static boolean isLessThan(short a, short b) {
        return a < b;
    }

    public static boolean isLessThanOrEqual(short a, short b) {
        return a <= b;
    }

    public static boolean isBetween(short value, short min, short max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(short value, short min, short max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(short value, short min, short max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }


    public static boolean isEqual(short a, short b) {
        return a == b;
    }

    public static boolean isNotEqual(short a, short b) {
        return a != b;
    }

    // ===== byte =====
    public static boolean isZero(byte value) {
        return value == 0;
    }

    public static boolean isPositive(byte value) {
        return value > 0;
    }

    public static boolean isNegative(byte value) {
        return value < 0;
    }

    public static boolean isGreaterThan(byte a, byte b) {
        return a > b;
    }

    public static boolean isGreaterThanOrEqual(byte a, byte b) {
        return a >= b;
    }

    public static boolean isLessThan(byte a, byte b) {
        return a < b;
    }

    public static boolean isLessThanOrEqual(byte a, byte b) {
        return a <= b;
    }

    public static boolean isBetween(byte value, byte min, byte max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(byte value, byte min, byte max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(byte value, byte min, byte max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(byte a, byte b) {
        return a == b;
    }

    public static boolean isNotEqual(byte a, byte b) {
        return a != b;
    }

    // ===== Integer =====
    public static boolean isZero(Integer value) {
        return isNotEmpty(value) && value == 0;
    }

    public static boolean isPositive(Integer value) {
        return isNotEmpty(value) && value > 0;
    }

    public static boolean isNegative(Integer value) {
        return isNotEmpty(value) && value < 0;
    }

    public static boolean isGreaterThan(Integer a, Integer b) {
        return isNotEmpty(a) && isNotEmpty(b) && a > b;
    }

    public static boolean isGreaterThanOrEqual(Integer a, Integer b) {
        return isNotEmpty(a) && isNotEmpty(b) && a >= b;
    }

    public static boolean isLessThan(Integer a, Integer b) {
        return isNotEmpty(a) && isNotEmpty(b) && a < b;
    }

    public static boolean isLessThanOrEqual(Integer a, Integer b) {
        return isNotEmpty(a) && isNotEmpty(b) && a <= b;
    }

    public static boolean isBetween(Integer value, Integer min, Integer max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(Integer value, Integer min, Integer max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(Integer value, Integer min, Integer max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(Integer a, Integer b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) == 0;
    }

    public static boolean isNotEqual(Integer a, Integer b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) != 0;
    }

    // ===== Long =====
    public static boolean isZero(Long value) {
        return isNotEmpty(value) && value == 0L;
    }

    public static boolean isPositive(Long value) {
        return isNotEmpty(value) && value > 0L;
    }

    public static boolean isNegative(Long value) {
        return isNotEmpty(value) && value < 0L;
    }

    public static boolean isGreaterThan(Long a, Long b) {
        return isNotEmpty(a) && isNotEmpty(b) && a > b;
    }

    public static boolean isGreaterThanOrEqual(Long a, Long b) {
        return isNotEmpty(a) && isNotEmpty(b) && a >= b;
    }

    public static boolean isLessThan(Long a, Long b) {
        return isNotEmpty(a) && isNotEmpty(b) && a < b;
    }

    public static boolean isLessThanOrEqual(Long a, Long b) {
        return isNotEmpty(a) && isNotEmpty(b) && a <= b;
    }

    public static boolean isBetween(Long value, Long min, Long max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(Long value, Long min, Long max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(Long value, Long min, Long max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(Long a, Long b) {
        return isNotEmpty(a) && isNotEmpty(b) && (long) a == b;
    }

    public static boolean isNotEqual(Long a, Long b) {
        return !isEqual(a, b);
    }

    // ===== NUMBER CHECKS (Double) =====
    public static boolean isZero(Double value) {
        return isNotEmpty(value) && Precision.equals(value, 0.0);
    }

    public static boolean isZero(Double value, double tol) {
        return isNotEmpty(value) && isValidTolerance(tol) && Precision.equals(value, 0.0, tol);
    }

    public static boolean isPositive(Double value) {
        return isNotEmpty(value) && Precision.compareTo(value, 0.0, DEFAULT_MAX_ULPS) > 0;
    }

    public static boolean isPositive(Double value, double tol) {
        return isNotEmpty(value) && isValidTolerance(tol) && Precision.compareTo(value, 0.0, tol) > 0;
    }

    public static boolean isNegative(Double value) {
        return isNotEmpty(value) && Precision.compareTo(value, 0.0, DEFAULT_MAX_ULPS) < 0;
    }

    public static boolean isNegative(Double value, double tol) {
        return isNotEmpty(value) && isValidTolerance(tol) && Precision.compareTo(value, 0.0, tol) < 0;
    }

    public static boolean isGreaterThan(Double a, Double b) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, DEFAULT_MAX_ULPS) > 0;
    }

    public static boolean isGreaterThan(Double a, Double b, double tol) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, tol) > 0;
    }

    public static boolean isGreaterThanOrEqual(Double a, Double b) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, DEFAULT_MAX_ULPS) >= 0;
    }

    public static boolean isGreaterThanOrEqual(Double a, Double b, double tol) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, tol) >= 0;
    }

    public static boolean isLessThan(Double a, Double b) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, DEFAULT_MAX_ULPS) < 0;
    }

    public static boolean isLessThan(Double a, Double b, double tol) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, tol) < 0;
    }

    public static boolean isLessThanOrEqual(Double a, Double b) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, DEFAULT_MAX_ULPS) <= 0;
    }

    public static boolean isLessThanOrEqual(Double a, Double b, double tol) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.compareTo(a, b, tol) <= 0;
    }

    public static boolean isBetween(Double value, Double min, Double max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetween(Double value, Double min, Double max, double tol) {
        return isBetweenExclusive(value, min, max, tol);
    }

    public static boolean isBetweenInclusive(Double value, Double min, Double max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenInclusive(Double value, Double min, Double max, double tol) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max, tol);
    }

    public static boolean isBetweenExclusive(Double value, Double min, Double max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isBetweenExclusive(Double value, Double min, Double max, double tol) {
        return isGreaterThan(value, min, tol) && isLessThan(value, max, tol);
    }

    public static boolean isEqual(Double a, Double b) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.equals(a, b, DEFAULT_MAX_ULPS);
    }

    public static boolean isEqual(Double a, Double b, double tol) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.equals(a, b, tol);
    }

    public static boolean isNotEqual(Double a, Double b) {
        return isNotEmpty(a) && isNotEmpty(b) && !Precision.equals(a, b, DEFAULT_MAX_ULPS);
    }

    public static boolean isNotEqual(Double a, Double b, double tol) {
        return isNotEmpty(a) && isNotEmpty(b) && !Precision.equals(a, b, tol);
    }

    // ===== NUMBER CHECKS (Float) =====
    public static boolean isZero(Float value) {
        return isNotEmpty(value) && Precision.equals(value, 0f, DEFAULT_MAX_ULPS);
    }

    public static boolean isPositive(Float value) {
        return isNotEmpty(value) && value > 0f;
    }

    public static boolean isNegative(Float value) {
        return isNotEmpty(value) && value < 0f;
    }

    public static boolean isGreaterThan(Float a, Float b) {
        return isNotEmpty(a) && isNotEmpty(b) && a > b;
    }

    public static boolean isGreaterThanOrEqual(Float a, Float b) {
        return isNotEmpty(a) && isNotEmpty(b) && a >= b;
    }

    public static boolean isLessThan(Float a, Float b) {
        return isNotEmpty(a) && isNotEmpty(b) && a < b;
    }

    public static boolean isLessThanOrEqual(Float a, Float b) {
        return isNotEmpty(a) && isNotEmpty(b) && a <= b;
    }

    public static boolean isBetween(Float value, Float min, Float max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(Float value, Float min, Float max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(Float value, Float min, Float max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(Float a, Float b) {
        return isNotEmpty(a) && isNotEmpty(b) && Precision.equals(a, b, DEFAULT_MAX_ULPS);
    }

    public static boolean isNotEqual(Float a, Float b) {
        return isNotEmpty(a) && isNotEmpty(b) && !Precision.equals(a, b, DEFAULT_MAX_ULPS);
    }

    // ===== Short =====
    public static boolean isZero(Short value) {
        return isNotEmpty(value) && value == 0;
    }

    public static boolean isPositive(Short value) {
        return isNotEmpty(value) && value > 0;
    }

    public static boolean isNegative(Short value) {
        return isNotEmpty(value) && value < 0;
    }

    public static boolean isGreaterThan(Short a, Short b) {
        return isNotEmpty(a) && isNotEmpty(b) && a > b;
    }

    public static boolean isGreaterThanOrEqual(Short a, Short b) {
        return isNotEmpty(a) && isNotEmpty(b) && a >= b;
    }

    public static boolean isLessThan(Short a, Short b) {
        return isNotEmpty(a) && isNotEmpty(b) && a < b;
    }

    public static boolean isLessThanOrEqual(Short a, Short b) {
        return isNotEmpty(a) && isNotEmpty(b) && a <= b;
    }

    public static boolean isBetween(Short value, Short min, Short max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(Short value, Short min, Short max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(Short value, Short min, Short max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(Short a, Short b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) == 0;
    }

    public static boolean isNotEqual(Short a, Short b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) != 0;
    }

    // ===== Byte =====
    public static boolean isZero(Byte value) {
        return isNotEmpty(value) && value == 0;
    }

    public static boolean isPositive(Byte value) {
        return isNotEmpty(value) && value > 0;
    }

    public static boolean isNegative(Byte value) {
        return isNotEmpty(value) && value < 0;
    }

    public static boolean isGreaterThan(Byte a, Byte b) {
        return isNotEmpty(a) && isNotEmpty(b) && a > b;
    }

    public static boolean isGreaterThanOrEqual(Byte a, Byte b) {
        return isNotEmpty(a) && isNotEmpty(b) && a >= b;
    }

    public static boolean isLessThan(Byte a, Byte b) {
        return isNotEmpty(a) && isNotEmpty(b) && a < b;
    }

    public static boolean isLessThanOrEqual(Byte a, Byte b) {
        return isNotEmpty(a) && isNotEmpty(b) && a <= b;
    }

    public static boolean isBetween(Byte value, Byte min, Byte max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(Byte value, Byte min, Byte max) {
        return isGreaterThanOrEqual(value, min) && isLessThanOrEqual(value, max);
    }

    public static boolean isBetweenExclusive(Byte value, Byte min, Byte max) {
        return isGreaterThan(value, min) && isLessThan(value, max);
    }

    public static boolean isEqual(Byte a, Byte b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) == 0;
    }

    public static boolean isNotEqual(Byte a, Byte b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) != 0;
    }

    // ===== BigDecimal =====
    public static boolean isZero(BigDecimal value) {
        return isNotEmpty(value) && value.signum() == 0;
    }

    public static boolean isPositive(BigDecimal value) {
        return isNotEmpty(value) && value.signum() > 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return isNotEmpty(value) && value.signum() < 0;
    }

    public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) > 0;
    }

    public static boolean isGreaterThanOrEqual(BigDecimal a, BigDecimal b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) >= 0;
    }

    public static boolean isLessThan(BigDecimal a, BigDecimal b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) < 0;
    }

    public static boolean isLessThanOrEqual(BigDecimal a, BigDecimal b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) <= 0;
    }

    public static boolean isBetween(BigDecimal value, BigDecimal min, BigDecimal max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(BigDecimal value, BigDecimal min, BigDecimal max) {
        return isNotEmpty(value) && isNotEmpty(min) && isNotEmpty(max)
                && min.compareTo(value) <= 0 && value.compareTo(max) <= 0;
    }

    public static boolean isBetweenExclusive(BigDecimal value, BigDecimal min, BigDecimal max) {
        return isNotEmpty(value) && isNotEmpty(min) && isNotEmpty(max)
                && min.compareTo(value) < 0 && value.compareTo(max) < 0;
    }

    public static boolean isEqual(BigDecimal a, BigDecimal b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) == 0;
    }

    public static boolean isNotEqual(BigDecimal a, BigDecimal b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) != 0;
    }

    // ===== BigInteger =====
    public static boolean isZero(BigInteger value) {
        return isNotEmpty(value) && value.signum() == 0;
    }

    public static boolean isPositive(BigInteger value) {
        return isNotEmpty(value) && value.signum() > 0;
    }

    public static boolean isNegative(BigInteger value) {
        return isNotEmpty(value) && value.signum() < 0;
    }

    public static boolean isGreaterThan(BigInteger a, BigInteger b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) > 0;
    }

    public static boolean isGreaterThanOrEqual(BigInteger a, BigInteger b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) >= 0;
    }

    public static boolean isLessThan(BigInteger a, BigInteger b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) < 0;
    }

    public static boolean isLessThanOrEqual(BigInteger a, BigInteger b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) <= 0;
    }

    public static boolean isBetween(BigInteger value, BigInteger min, BigInteger max) {
        return isBetweenExclusive(value, min, max);
    }

    public static boolean isBetweenInclusive(BigInteger value, BigInteger min, BigInteger max) {
        return isNotEmpty(value) && isNotEmpty(min) && isNotEmpty(max)
                && min.compareTo(value) <= 0 && value.compareTo(max) <= 0;
    }

    public static boolean isBetweenExclusive(BigInteger value, BigInteger min, BigInteger max) {
        return isNotEmpty(value) && isNotEmpty(min) && isNotEmpty(max)
                && min.compareTo(value) < 0 && value.compareTo(max) < 0;
    }

    public static boolean isEqual(BigInteger a, BigInteger b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) == 0;
    }

    public static boolean isNotEqual(BigInteger a, BigInteger b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.compareTo(b) != 0;
    }

    // ===== Date/Time Checking (Date) =====
    public static boolean isStrictlyBefore(Date a, Date b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.before(b);
    }

    public static boolean isStrictlyAfter(Date a, Date b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.after(b);
    }

    public static boolean isBeforeOrEqual(Date a, Date b) {
        return isNotEmpty(a) && isNotEmpty(b) && (isEqual(a, b) || a.before(b));
    }

    public static boolean isAfterOrEqual(Date a, Date b) {
        return isNotEmpty(a) && isNotEmpty(b) && (isEqual(a, b) || a.after(b));
    }

    public static boolean isWithinExclusive(Date date, Date start, Date end) {
        return isStrictlyAfter(date, start) && isStrictlyBefore(date, end);
    }

    public static boolean isWithinInclusive(Date date, Date start, Date end) {
        return isAfterOrEqual(date, start) && isBeforeOrEqual(date, end);
    }

    // ===== Date/Time Checking (LocalDate) =====
    public static boolean isStrictlyBefore(LocalDate a, LocalDate b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isBefore(b);
    }

    public static boolean isStrictlyAfter(LocalDate a, LocalDate b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isAfter(b);
    }

    public static boolean isBeforeOrEqual(LocalDate a, LocalDate b) {
        return isEqual(a, b) || isStrictlyBefore(a, b);
    }

    public static boolean isAfterOrEqual(LocalDate a, LocalDate b) {
        return isEqual(a, b) || isStrictlyAfter(a, b);
    }

    public static boolean isWithinExclusive(LocalDate target, LocalDate start, LocalDate end) {
        return isStrictlyAfter(target, start) && isStrictlyBefore(target, end);
    }

    public static boolean isWithinInclusive(LocalDate target, LocalDate start, LocalDate end) {
        return isAfterOrEqual(target, start) && isBeforeOrEqual(target, end);
    }

    // ===== Date/Time Checking (LocalDateTime) =====
    public static boolean isStrictlyBefore(LocalDateTime a, LocalDateTime b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isBefore(b);
    }

    public static boolean isStrictlyAfter(LocalDateTime a, LocalDateTime b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isAfter(b);
    }

    public static boolean isBeforeOrEqual(LocalDateTime a, LocalDateTime b) {
        return isEqual(a, b) || isStrictlyBefore(a, b);
    }

    public static boolean isAfterOrEqual(LocalDateTime a, LocalDateTime b) {
        return isEqual(a, b) || isStrictlyAfter(a, b);
    }

    public static boolean isWithinExclusive(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        return isStrictlyAfter(target, start) && isStrictlyBefore(target, end);
    }

    public static boolean isWithinInclusive(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        return isAfterOrEqual(target, start) && isBeforeOrEqual(target, end);
    }

    // ===== Date/Time Checking (OffsetDateTime) =====
    public static boolean isStrictlyBefore(OffsetDateTime a, OffsetDateTime b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isBefore(b);
    }

    public static boolean isStrictlyAfter(OffsetDateTime a, OffsetDateTime b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isAfter(b);
    }

    public static boolean isBeforeOrEqual(OffsetDateTime a, OffsetDateTime b) {
        return isEqual(a, b) || isStrictlyBefore(a, b);
    }

    public static boolean isAfterOrEqual(OffsetDateTime a, OffsetDateTime b) {
        return isEqual(a, b) || isStrictlyAfter(a, b);
    }

    public static boolean isWithinExclusive(OffsetDateTime target, OffsetDateTime start, OffsetDateTime end) {
        return isStrictlyAfter(target, start) && isStrictlyBefore(target, end);
    }

    public static boolean isWithinInclusive(OffsetDateTime target, OffsetDateTime start, OffsetDateTime end) {
        return isAfterOrEqual(target, start) && isBeforeOrEqual(target, end);
    }

    // ===== Date/Time Checking (Instant) =====
    public static boolean isStrictlyBefore(Instant a, Instant b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isBefore(b);
    }

    public static boolean isStrictlyAfter(Instant a, Instant b) {
        return isNotEmpty(a) && isNotEmpty(b) && a.isAfter(b);
    }

    public static boolean isBeforeOrEqual(Instant a, Instant b) {
        return isEqual(a, b) || isStrictlyBefore(a, b);
    }

    public static boolean isAfterOrEqual(Instant a, Instant b) {
        return isEqual(a, b) || isStrictlyAfter(a, b);
    }

    public static boolean isWithinExclusive(Instant target, Instant start, Instant end) {
        return isStrictlyAfter(target, start) && isStrictlyBefore(target, end);
    }

    public static boolean isWithinInclusive(Instant target, Instant start, Instant end) {
        return isAfterOrEqual(target, start) && isBeforeOrEqual(target, end);
    }

    public static boolean isValidString(String str) {
        if (isBlank(str)) {
            return false;
        }

        return str.codePoints().allMatch(c ->
                Character.isLetterOrDigit(c) ||
                        Character.isWhitespace(c) ||
                        isCommonKeyboardSymbol(c)
        );
    }

    public static boolean isNotValidString(String str) {
        return !isValidString(str);
    }

    private static boolean isCommonKeyboardSymbol(int codePoint) {
        // Basic ASCII symbols: 32–126, excluding letters/digits
        return (codePoint >= 33 && codePoint <= 47) ||     // !"#$%&'()*+,-./
                (codePoint >= 58 && codePoint <= 64) ||     // :;<=>?@
                (codePoint >= 91 && codePoint <= 96) ||     // [\]^_`
                (codePoint >= 123 && codePoint <= 126);     // {|}~
    }

    private static boolean isValidTolerance(double tol) {
        return Precision.compareTo(tol, 0.0, DEFAULT_MAX_ULPS) > 0 && !Double.isInfinite(tol);
    }
}
