package com.ifast.ipaymy.backend.util.codec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Base64Utils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final Base64.Encoder STD_ENCODER = Base64.getEncoder();
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder();
    private static final Base64.Decoder STD_DECODER = Base64.getDecoder();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    public static String encodeToString(byte[] value) {
        if (value == null || value.length == 0) {
            return "";
        }
        return STD_ENCODER.encodeToString(value);
    }

    public static String encodeToString(String value) {
        return encodeToString(value, DEFAULT_CHARSET);
    }

    public static String encodeToString(String value, Charset charset) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        Charset cs = charset == null ? DEFAULT_CHARSET : charset;
        return STD_ENCODER.encodeToString(value.getBytes(cs));
    }


    public static byte[] encodeToBytes(byte[] value) {
        if (value == null || value.length == 0) {
            return new byte[0];
        }
        return STD_ENCODER.encode(value);
    }

    public static byte[] encodeToBytes(String value) {
        return encodeToBytes(value, DEFAULT_CHARSET);
    }

    public static byte[] encodeToBytes(String value, Charset charset) {
        if (value == null || value.isEmpty()) {
            return new byte[0];
        }
        Charset cs = charset == null ? DEFAULT_CHARSET : charset;
        return STD_ENCODER.encode(value.getBytes(cs));
    }

    public static String decodeToString(String value) {
        return decodeToString(value, DEFAULT_CHARSET);
    }

    public static String decodeToString(String value, Charset charset) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        Charset cs = charset == null ? DEFAULT_CHARSET : charset;
        return new String(STD_DECODER.decode(value), cs);
    }

    public static byte[] decodeToBytes(String value) {
        if (value == null || value.isEmpty()) {
            return new byte[0];
        }
        return STD_DECODER.decode(value);
    }

    // == URL ==
    public static String encodeUrlToString(byte[] value) {
        if (value == null || value.length == 0) {
            return "";
        }
        return URL_ENCODER.encodeToString(value);
    }

    public static String encodeUrlToString(String value) {
        return encodeUrlToString(value, DEFAULT_CHARSET);
    }

    public static String encodeUrlToString(String value, Charset charset) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        Charset cs = charset == null ? DEFAULT_CHARSET : charset;
        return URL_ENCODER.encodeToString(value.getBytes(cs));
    }

    public static byte[] encodeUrlToBytes(byte[] value) {
        if (value == null || value.length == 0) {
            return new byte[0];
        }
        return URL_ENCODER.encode(value);
    }

    public static byte[] encodeUrlToBytes(String value) {
        return encodeUrlToBytes(value, DEFAULT_CHARSET);
    }

    public static byte[] encodeUrlToBytes(String value, Charset charset) {
        if (value == null || value.isEmpty()) {
            return new byte[0];
        }
        Charset cs = charset == null ? DEFAULT_CHARSET : charset;
        return URL_ENCODER.encode(value.getBytes(cs));
    }

    public static String decodeUrlToString(String value) {
        return decodeUrlToString(value, DEFAULT_CHARSET);
    }

    public static String decodeUrlToString(String value, Charset charset) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        Charset cs = charset == null ? DEFAULT_CHARSET : charset;
        return new String(URL_DECODER.decode(value), cs);
    }

    public static byte[] decodeUrlToBytes(String value) {
        if (value == null || value.isEmpty()) {
            return new byte[0];
        }
        return URL_DECODER.decode(value);
    }
}
