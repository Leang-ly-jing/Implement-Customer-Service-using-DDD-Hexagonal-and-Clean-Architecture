package kh.edu.istad.platform.customer.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Filtering {
    private final List<Filter> filterList = new ArrayList<>();

    public void addFilter(String key, Operator operator, Object value) {
        filterList.add(new Filter(key, operator, value));
    }

    public enum Operator {
        eq, gt, gte, in, lt, lte, ne, nin, regex;

        public static Operator fromString(String op) {
            for (Operator operator : Operator.values()) {
                if (operator.name().equalsIgnoreCase(op)) {
                    return operator;
                }
            }
            return null;
        }
    }

    @Getter
    public static class Filter {
        private final String key;
        private final Operator operator;
        private final Object value;

        public Filter(String key, Operator operator, Object value) {
            this.key = key;
            this.operator = operator;
            this.value = value;
        }
    }
}
