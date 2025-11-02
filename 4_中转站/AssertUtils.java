package com.ifast.ipaymy.backend.util.validator;

import com.ifast.ipaymy.backend.util.exception.IfastPayException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.NONE)
public class AssertUtils {

    public static <X extends IfastPayException> void isTrue(boolean condition, Supplier<X> exceptionSupplier) {
        if (!condition) {
            throw exceptionSupplier.get();
        }
    }

    // ============ EMPTY CHECKS ============
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(String str, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(str), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(String str, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(str), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isBlank(String str, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBlank(str), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotBlank(String str, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotBlank(str), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Collection<?> collection, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(collection), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Collection<?> collection, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(collection), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Map<?, ?> map, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(map), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Map<?, ?> map, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(map), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Optional<?> optional, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(optional), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Optional<?> optional, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(optional), ex);
    }

    // ===== EMPTY CHECKS (Array) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Object[] value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Object[] value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (Object) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Object value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Object value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (Boolean) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Boolean value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Boolean value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (Integer) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Integer value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Integer value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (Long) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (Double) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (BigDecimal) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(BigDecimal value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(BigDecimal value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ===== EMPTY CHECKS (Date/Time) =====
    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(LocalDate value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(LocalDate value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(LocalDateTime value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(LocalDateTime value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(OffsetDateTime value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(OffsetDateTime value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    @Contract("!null, _ -> fail")
    public static <X extends IfastPayException> void isEmpty(Instant value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEmpty(value), ex);
    }

    @Contract("null, _ -> fail")
    public static <X extends IfastPayException> void isNotEmpty(Instant value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEmpty(value), ex);
    }

    // ============ EQUALS ============

    // ===== EQUALS (Generic) =====
    public static <T, X extends IfastPayException> void isEqual(T a, T b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <T, X extends IfastPayException> void isNotEqual(T a, T b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== EQUALS (String) =====
    public static <X extends IfastPayException> void isEqualsIgnoreCase(String a, String b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqualsIgnoreCase(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqualsIgnoreCase(String a, String b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqualsIgnoreCase(a, b), ex);
    }

    // ============ NUMBER CHECKS ============

    // ===== NUMBER CHECKS (int) =====
    public static <X extends IfastPayException> void isZero(int value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(int value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(int value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(int a, int b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(int a, int b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(int a, int b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(int a, int b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(int value, int min, int max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(int value, int min, int max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(int a, int b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(int a, int b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (long) =====
    public static <X extends IfastPayException> void isZero(long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(long a, long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(long a, long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(long a, long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(long a, long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(long value, long min, long max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(long value, long min, long max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(long a, long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(long a, long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (double) =====
    public static <X extends IfastPayException> void isZero(double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isZero(double value, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value, tol), ex);
    }

    public static <X extends IfastPayException> void isPositive(double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(double value, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value, tol), ex);
    }

    public static <X extends IfastPayException> void isNegative(double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(double value, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(double a, double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(double a, double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(double a, double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(double a, double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThan(double a, double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(double a, double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(double a, double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(double a, double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isBetween(double value, double min, double max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(double value, double min, double max, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(double value, double min, double max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(double value, double min, double max, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(double value, double min, double max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(double value, double min, double max, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isEqual(double a, double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isEqual(double a, double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(double a, double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(double a, double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b, tol), ex);
    }

    // ===== NUMBER CHECKS (float) =====
    public static <X extends IfastPayException> void isZero(float value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isZero(float value, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value, tol), ex);
    }

    public static <X extends IfastPayException> void isPositive(float value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(float value, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value, tol), ex);
    }

    public static <X extends IfastPayException> void isNegative(float value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(float value, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(float a, float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(float a, float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(float a, float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(float a, float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThan(float a, float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(float a, float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(float a, float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(float a, float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isBetween(float value, float min, float max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(float value, float min, float max, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(float value, float min, float max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(float value, float min, float max, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(float value, float min, float max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(float value, float min, float max, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isEqual(float a, float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isEqual(float a, float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(float a, float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(float a, float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b, tol), ex);
    }

    // ===== NUMBER CHECKS (short) =====
    public static <X extends IfastPayException> void isZero(short value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(short value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(short value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(short a, short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(short a, short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(short a, short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(short a, short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(short value, short min, short max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(short value, short min, short max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(short a, short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(short a, short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (byte) =====
    public static <X extends IfastPayException> void isZero(byte value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(byte value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(byte value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(byte a, byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(byte a, byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(byte a, byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(byte a, byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(byte value, byte min, byte max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(byte value, byte min, byte max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(byte a, byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(byte a, byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (Integer) =====
    public static <X extends IfastPayException> void isZero(Integer value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(Integer value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(Integer value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Integer a, Integer b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Integer a, Integer b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Integer a, Integer b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Integer a, Integer b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Integer value, Integer min, Integer max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(Integer value, Integer min, Integer max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(Integer a, Integer b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(Integer a, Integer b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (Long) =====
    public static <X extends IfastPayException> void isZero(Long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(Long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(Long value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Long a, Long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Long a, Long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Long a, Long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Long a, Long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Long value, Long min, Long max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(Long value, Long min, Long max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(Long a, Long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(Long a, Long b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (Double) =====
    public static <X extends IfastPayException> void isZero(Double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isZero(Double value, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value, tol), ex);
    }

    public static <X extends IfastPayException> void isPositive(Double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(Double value, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value, tol), ex);
    }

    public static <X extends IfastPayException> void isNegative(Double value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(Double value, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Double a, Double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Double a, Double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Double a, Double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Double a, Double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Double a, Double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Double a, Double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Double a, Double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Double a, Double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isBetween(Double value, Double min, Double max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(Double value, Double min, Double max, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Double value, Double min, Double max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Double value, Double min, Double max, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(Double value, Double min, Double max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(Double value, Double min, Double max, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isEqual(Double a, Double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isEqual(Double a, Double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(Double a, Double b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(Double a, Double b, double tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b, tol), ex);
    }

    // ===== NUMBER CHECKS (Float) =====
    public static <X extends IfastPayException> void isZero(Float value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isZero(Float value, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value, tol), ex);
    }

    public static <X extends IfastPayException> void isPositive(Float value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(Float value, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value, tol), ex);
    }

    public static <X extends IfastPayException> void isNegative(Float value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(Float value, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Float a, Float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Float a, Float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Float a, Float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Float a, Float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Float a, Float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Float a, Float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Float a, Float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Float a, Float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isBetween(Float value, Float min, Float max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(Float value, Float min, Float max, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Float value, Float min, Float max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Float value, Float min, Float max, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(Float value, Float min, Float max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetweenExclusive(Float value, Float min, Float max, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenExclusive(value, min, max, tol), ex);
    }

    public static <X extends IfastPayException> void isEqual(Float a, Float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isEqual(Float a, Float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b, tol), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(Float a, Float b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(Float a, Float b, float tol, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b, tol), ex);
    }

    // ===== NUMBER CHECKS (Short) =====
    public static <X extends IfastPayException> void isZero(Short value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(Short value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(Short value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Short a, Short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Short a, Short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Short a, Short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Short a, Short b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Short value, Short min, Short max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(Short value, Short min, Short max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    // ===== NUMBER CHECKS (Byte) =====
    public static <X extends IfastPayException> void isZero(Byte value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(Byte value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(Byte value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(Byte a, Byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(Byte a, Byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(Byte a, Byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(Byte a, Byte b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(Byte value, Byte min, Byte max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(Byte value, Byte min, Byte max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    // ===== NUMBER CHECKS (BigDecimal) =====
    public static <X extends IfastPayException> void isZero(BigDecimal value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(BigDecimal value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(BigDecimal value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(BigDecimal a, BigDecimal b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(BigDecimal a, BigDecimal b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(BigDecimal a, BigDecimal b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(BigDecimal a, BigDecimal b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(BigDecimal value, BigDecimal min, BigDecimal max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(BigDecimal value, BigDecimal min, BigDecimal max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(BigDecimal a, BigDecimal b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(BigDecimal a, BigDecimal b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== NUMBER CHECKS (BigInteger) =====
    public static <X extends IfastPayException> void isZero(BigInteger value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isZero(value), ex);
    }

    public static <X extends IfastPayException> void isPositive(BigInteger value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isPositive(value), ex);
    }

    public static <X extends IfastPayException> void isNegative(BigInteger value, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNegative(value), ex);
    }

    public static <X extends IfastPayException> void isGreaterThan(BigInteger a, BigInteger b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isGreaterThanOrEqual(BigInteger a, BigInteger b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isGreaterThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThan(BigInteger a, BigInteger b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThan(a, b), ex);
    }

    public static <X extends IfastPayException> void isLessThanOrEqual(BigInteger a, BigInteger b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isLessThanOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isBetweenInclusive(BigInteger value, BigInteger min, BigInteger max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetweenInclusive(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isBetween(BigInteger value, BigInteger min, BigInteger max, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBetween(value, min, max), ex);
    }

    public static <X extends IfastPayException> void isEqual(BigInteger a, BigInteger b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isNotEqual(BigInteger a, BigInteger b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotEqual(a, b), ex);
    }

    // ===== Date/Time Checking (Date) =====
    public static <X extends IfastPayException> void isSameDatetime(Date a, Date b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isStrictlyBefore(Date a, Date b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyBefore(a, b), ex);
    }

    public static <X extends IfastPayException> void isStrictlyAfter(Date a, Date b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyAfter(a, b), ex);
    }

    public static <X extends IfastPayException> void isBeforeOrEqual(Date a, Date b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBeforeOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isAfterOrEqual(Date a, Date b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isAfterOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isWithinExclusive(Date date, Date start, Date end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinExclusive(date, start, end), ex);
    }

    public static <X extends IfastPayException> void isWithinInclusive(Date date, Date start, Date end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinInclusive(date, start, end), ex);
    }

    public static <X extends IfastPayException> void isEqual(LocalDate a, LocalDate b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isStrictlyBefore(LocalDate a, LocalDate b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyBefore(a, b), ex);
    }

    // ===== Date/Time Checking (LocalDate) =====
    public static <X extends IfastPayException> void isStrictlyAfter(LocalDate a, LocalDate b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyAfter(a, b), ex);
    }

    public static <X extends IfastPayException> void isBeforeOrEqual(LocalDate a, LocalDate b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBeforeOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isAfterOrEqual(LocalDate a, LocalDate b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isAfterOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isWithinExclusive(LocalDate target, LocalDate start, LocalDate end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinExclusive(target, start, end), ex);
    }

    public static <X extends IfastPayException> void isWithinInclusive(LocalDate target, LocalDate start, LocalDate end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinInclusive(target, start, end), ex);
    }

    public static <X extends IfastPayException> void isStrictlyBefore(LocalDateTime a, LocalDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyBefore(a, b), ex);
    }

    // ===== Date/Time Checking (LocalDateTime) =====
    public static <X extends IfastPayException> void isStrictlyAfter(LocalDateTime a, LocalDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyAfter(a, b), ex);
    }

    public static <X extends IfastPayException> void isBeforeOrEqual(LocalDateTime a, LocalDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBeforeOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isAfterOrEqual(LocalDateTime a, LocalDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isAfterOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isWithinExclusive(LocalDateTime target, LocalDateTime start, LocalDateTime end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinExclusive(target, start, end), ex);
    }

    public static <X extends IfastPayException> void isWithinInclusive(LocalDateTime target, LocalDateTime start, LocalDateTime end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinInclusive(target, start, end), ex);
    }

    // ===== Date/Time Checking (OffsetDateTime) =====
    public static <X extends IfastPayException> void isStrictlyBefore(OffsetDateTime a, OffsetDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyBefore(a, b), ex);
    }

    public static <X extends IfastPayException> void isStrictlyAfter(OffsetDateTime a, OffsetDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyAfter(a, b), ex);
    }

    public static <X extends IfastPayException> void isBeforeOrEqual(OffsetDateTime a, OffsetDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBeforeOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isAfterOrEqual(OffsetDateTime a, OffsetDateTime b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isAfterOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isWithinExclusive(OffsetDateTime target, OffsetDateTime start, OffsetDateTime end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinExclusive(target, start, end), ex);
    }

    public static <X extends IfastPayException> void isWithinInclusive(OffsetDateTime target, OffsetDateTime start, OffsetDateTime end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinInclusive(target, start, end), ex);
    }

    // ===== Date/Time Checking (Instant) =====
    public static <X extends IfastPayException> void isStrictlyBefore(Instant a, Instant b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyBefore(a, b), ex);
    }

    public static <X extends IfastPayException> void isStrictlyAfter(Instant a, Instant b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isStrictlyAfter(a, b), ex);
    }

    public static <X extends IfastPayException> void isBeforeOrEqual(Instant a, Instant b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isBeforeOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isAfterOrEqual(Instant a, Instant b, Supplier<X> ex) {
        isTrue(ValidatorUtils.isAfterOrEqual(a, b), ex);
    }

    public static <X extends IfastPayException> void isWithinExclusive(Instant target, Instant start, Instant end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinExclusive(target, start, end), ex);
    }

    public static <X extends IfastPayException> void isWithinInclusive(Instant target, Instant start, Instant end, Supplier<X> ex) {
        isTrue(ValidatorUtils.isWithinInclusive(target, start, end), ex);
    }

    public static <X extends IfastPayException> void isValidString(String str, Supplier<X> ex) {
        isTrue(ValidatorUtils.isValidString(str), ex);
    }

    public static <X extends IfastPayException> void isNotValidString(String str, Supplier<X> ex) {
        isTrue(ValidatorUtils.isNotValidString(str), ex);
    }
}
