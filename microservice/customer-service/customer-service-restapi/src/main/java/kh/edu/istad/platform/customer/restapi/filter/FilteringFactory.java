package kh.edu.istad.platform.customer.restapi.filter;

import kh.edu.istad.platform.customer.domain.dto.Filtering;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilteringFactory {

    interface Converter {
        Object convert(String value);
    }

    private static final Map<Class<?>, Converter> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(String.class, v -> v);
        CONVERTERS.put(Integer.class, Integer::valueOf);
        CONVERTERS.put(int.class, Integer::valueOf);
        CONVERTERS.put(Long.class, Long::valueOf);
        CONVERTERS.put(long.class, Long::valueOf);
        CONVERTERS.put(Boolean.class, Boolean::valueOf);
        CONVERTERS.put(boolean.class, Boolean::valueOf);
        CONVERTERS.put(Instant.class, Instant::parse);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Filtering parseFromParams(List<String> filterList, Class<T> clazz) {
        Filtering filtering = new Filtering();
        if (filterList == null || filterList.isEmpty()) {
            return filtering;
        }

        for (String filterString : filterList) {
            String[] parts = filterString.split("\\|");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Filter format must be key|operator|value");
            }

            String key = parts[0];
            Filtering.Operator operator = Filtering.Operator.fromString(parts[1]);
            String rawValue = parts[2];

            try {
                Field field = getField(clazz, key);
                Class<?> fieldType = field.getType();

                if (operator == Filtering.Operator.in || operator == Filtering.Operator.nin) {
                    Set<Object> values = new HashSet<>();
                    for (String val : rawValue.split(";")) {
                        values.add(convertValue(fieldType, val));
                    }
                    filtering.addFilter(key, operator, values);
                } else {
                    filtering.addFilter(key, operator, convertValue(fieldType, rawValue));
                }
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Filtering on field '" + key + "' is not supported");
            }
        }
        return filtering;
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object convertValue(Class<?> type, String value) {
        if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, value);
        }
        Converter converter = CONVERTERS.get(type);
        if (converter != null) {
            return converter.convert(value);
        }
        return value;
    }
}
