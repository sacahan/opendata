package com.udn.ntpc.od.common.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 將 String 轉換成 Sort，適用於 url。
 * Sample:
 *     field1:desc,field2:asc 
 */
@Component
public class StringToSortObjectConverter extends AbstractObjectConverter<String, Sort> {
    private static final String DEFAULT = "--DEFAULT--";
    private static final String FIELD_DELIMITER = ",";
    private static final String DIRECTION_DELIMITER = ":";
    
    @Override
    protected Sort doConvert(String source) {
        return parseParameterIntoSort(source);
    }

    private Sort parseParameterIntoSort(String source) {
        if (StringUtils.isBlank(source) || StringUtils.equalsIgnoreCase(DEFAULT, source)) {
            return null;
        }

        String[] sources = source.split(FIELD_DELIMITER);
        List<Order> allOrders = new ArrayList<>();
        for (String part : sources) {
            if (part == null) {
                continue;
            }
            String[] elements = part.split(DIRECTION_DELIMITER);
            Direction direction = elements.length == 0 ? null : Direction.fromString(StringUtils.trim(elements[elements.length - 1]));
            for (int i = 0; i < elements.length; i++) {

                if (i == elements.length - 1 && direction != null) {
                    continue;
                }

                String property = elements[i];

                if (StringUtils.isBlank(property)) {
                    continue;
                }

                allOrders.add(new Order(direction, property));
            }
        }
        return allOrders.isEmpty() ? null : Sort.by(allOrders);
    }

}
