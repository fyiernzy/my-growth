package com.ifast.ipaymy.backend.util.constant;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class SharedConstant {

    public static final String UNKNOWN = "UNKNOWN";

    // Timezone
    public static final String DEFAULT_TIMEZONE = "UTC";

    public static final ZoneOffset DEFAULT_OFFSET_ZONE = ZoneOffset.UTC;

    public static final ZoneId KUALA_LUMPUR_ZONE_ID = ZoneId.of("Asia/Kuala_Lumpur");

    //Excel format
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss XXX";

    public static final String DEFAULT_DECIMAL_FORMAT = "0.00";

    // Punctuation
    public static final String COMMA = ",";

    public static final String EMPTY_STRING = "";

    public static final String PERIOD = ".";

    public static final String DASH = "-";

    public static final String OPEN_CURLY_BRACKET = "{";

    public static final String CLOSE_CURLY_BRACKET = "}";

    public static final String WHITESPACE = " ";

    public static final String SLASH = "/";

    public static final String EXCLAMATION = "!";

    public static final String SPACE = " ";

    public static final String AMPERSAND = "&";

    public static final String COLON = ":";

    public static final String EQUALS = "=";

    public static final String OPEN_SQUARE_BRACKET = "[";

    public static final String CLOSE_SQUARE_BRACKET = "]";

    // Currency
    public static final String CNY = "CNY";

    public static final String CNH = "CNH";

    public static final String BASE_CURRENCY_CODE = "MYR";

    // Request
    public static final String SUCCESS = "SUCCESS";

    public static final String FAILED = "FAILED";

    public static final String PROCESSING = "PROCESSING";

    public static final String AUTHORIZATION = "Authorization";

    public static final String BEARER_WITH_SPACE = "Bearer ";

    public static final String X_INTERNAL_SERVICE = "X-Internal-Service";

    public static final String BATCH_SERVICE = "batch-service";

    // Sql
    public static final String SORT_ASC = "asc";

    public static final String SORT_DESC = "desc";

    // Boolean
    public static final String YES_FLAG = "Y";

    public static final String NO_FLAG = "N";

    // Default country
    public static final String DEFAULT_COUNTRY_CODE = "MY";

    // System User
    public static final String SYSTEM = "system";

    // Server environment
    public static final String DEV = "dev";

    public static final String STAGING = "staging";

    public static final String UAT = "uat";

    public static final String LIVE = "live";

    public static final String LOCAL = "local";

    // SSL
    public static final String TLS = "TLS";

    public static final int DEFAULT_PAGE = 0;

    public static final int DEFAULT_PAGE_SIZE = 10;

    // Retry configuration defaults
    public static final int DEFAULT_MAX_ATTEMPTS = 3;
    public static final int DEFAULT_BACKOFF_MILLISECONDS = 1000;

    public static final int DEFAULT_CONNECT_TIMEOUT = 5;
    public static final int DEFAULT_READ_TIMEOUT = 30;
}
