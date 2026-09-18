package com.ifast.ipaymy.backend.util.querydsl;

import com.ifast.ipaymy.backend.util.constant.SharedConstant;
import com.ifast.ipaymy.backend.util.validator.ValidatorUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.querydsl.QSort;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class QueryDslUtil {

    public static List<OrderSpecifier<?>> buildOrderSpecifiers(Map<String, String> sortMap,
                                                               Map<String, ComparableExpressionBase<?>> sortFieldMap) {
        if (ValidatorUtils.isEmpty(sortMap) || ValidatorUtils.isEmpty(sortFieldMap)) {
            return Collections.emptyList();
        }

        return sortMap.entrySet().stream()
            .filter(entry -> hasValidFieldName(entry.getKey(), sortFieldMap))
            .map(entry -> {
                String fieldName = entry.getKey();
                String direction = entry.getValue();
                ComparableExpressionBase<?> expression = sortFieldMap.get(fieldName);
                return new OrderSpecifier<>(toOrder(direction), expression);
            })
            .collect(Collectors.toUnmodifiableList());
    }

    public static QSort buildQSort(Map<String, String> sortMap,
                                   Map<String, ComparableExpressionBase<?>> sortFieldMap) {
        return new QSort(buildOrderSpecifiers(sortMap, sortFieldMap));
    }

    private static boolean hasValidFieldName(String fieldName,
                                             Map<String, ComparableExpressionBase<?>> sortFieldMap) {
        return ValidatorUtils.isNotEmpty(sortFieldMap)
               && ValidatorUtils.isNotEmpty(sortFieldMap.get(fieldName));
    }

    private static Order toOrder(String direction) {
        // Fallback to use asc as the default value for any invalid direction
        return ValidatorUtils.isEqualsIgnoreCase(SharedConstant.SORT_DESC, direction)
               ? Order.DESC
               : Order.ASC;
    }
}
