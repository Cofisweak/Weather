package com.cofisweak.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.PropertyNotFoundException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesUtil {
    public static String get(String key) {
        String value = System.getenv(key);
        if (value == null) {
            throw new PropertyNotFoundException("Property %s not found!".formatted(key));
        }
        return value;
    }

}
