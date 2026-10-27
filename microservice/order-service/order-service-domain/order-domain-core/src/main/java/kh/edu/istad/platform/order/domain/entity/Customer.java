package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private final String familyName;
    private final String givenName;

    private Customer(Builder builder) {
        super.setId(builder.customerId);
        this.familyName = builder.familyName;
        this.givenName = builder.givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public static final class Builder {
        private CustomerId customerId;
        private String familyName;
        private String givenName;

        public static Builder builder() {
            return new Builder();
        }

        public Builder customerId(CustomerId val) {
            this.customerId = val;
            return this;
        }

        public Builder familyName(String val) {
            this.familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            this.givenName = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
