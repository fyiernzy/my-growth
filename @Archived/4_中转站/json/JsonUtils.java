package com.ifast.ipaymy.backend.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class JsonUtils {

    private static final ObjectMapper MAPPER = Jackson2ObjectMapperBuilder
        .json()
        .timeZone(TimeZone.getDefault()).createXmlMapper(false)
        .failOnUnknownProperties(false)
        .failOnEmptyBeans(false)
        .featuresToEnable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .simpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .build();

    public static boolean isValidJson(String json) {
        try {
            MAPPER.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String writeValueAsString(Object value) throws IOException {
        return MAPPER.writeValueAsString(value);
    }

    public static String writeValueAsStringSafely(Object value) {
        try {
            return writeValueAsString(value);
        } catch (IOException e) {
            return "{}";
        }
    }

    public static byte[] writeValueAsBytes(Object value) throws IOException {
        return MAPPER.writeValueAsBytes(value);
    }

    public static byte[] writeValueAsBytesSafely(Object value) {
        try {
            return writeValueAsBytes(value);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static <T> T readValue(String json, Class<T> type) throws IOException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T readValue(String json, TypeReference<T> type) throws IOException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T readValue(byte[] bytes, Class<T> type) throws IOException {
        return MAPPER.readValue(bytes, type);
    }

    public static <T> T readValue(byte[] bytes, TypeReference<T> type) throws IOException {
        return MAPPER.readValue(bytes, type);
    }

    public static <T> T readValueSafely(String json, Class<T> type) {
        try {
            return readValue(json, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T readValueSafely(String json, TypeReference<T> type) {
        try {
            return readValue(json, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T readValueSafely(byte[] bytes, Class<T> type) {
        try {
            return readValue(bytes, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T readValueSafely(byte[] bytes, TypeReference<T> type) {
        try {
            return readValue(bytes, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static String pretty(String json) throws IOException {
        JsonNode node = MAPPER.readTree(json);
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    public static String prettySafely(String json) {
        try {
            return pretty(json);
        } catch (IOException e) {
            return json;
        }
    }

    public static String compact(String json) throws IOException {
        JsonNode node = MAPPER.readTree(json);
        return MAPPER.writeValueAsString(node);
    }

    public static String compactSafely(String json) {
        try {
            return compact(json);
        } catch (IOException e) {
            return json;
        }
    }

    public static JsonNode toNode(Object value) {
        return MAPPER.valueToTree(value);
    }

    public static <T> T fromNode(JsonNode node, Class<T> type) throws IOException {
        return MAPPER.treeToValue(node, type);
    }

    public static <T> T fromNodeSafely(JsonNode node, Class<T> type) {
        try {
            return fromNode(node, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T convert(Object value, Class<T> type) {
        return MAPPER.convertValue(value, type);
    }

    public static <T> T convert(Object value, TypeReference<T> type) {
        return MAPPER.convertValue(value, type);
    }

    public static <T> T cloneDeep(T value, Class<T> type) {
        try {
            byte[] bytes = writeValueAsBytes(value);
            return readValue(bytes, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T update(T target, String json) throws IOException {
        ObjectReader reader = MAPPER.readerForUpdating(target);
        return reader.readValue(json);
    }

    public static <T> T updateSafely(T target, String json) {
        try {
            return update(target, json);
        } catch (IOException e) {
            return target;
        }
    }

    public static boolean isJsonObject(String json) {
        try {
            return MAPPER.readTree(json).isObject();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isJsonArray(String json) {
        try {
            return MAPPER.readTree(json).isArray();
        } catch (IOException e) {
            return false;
        }
    }
}
