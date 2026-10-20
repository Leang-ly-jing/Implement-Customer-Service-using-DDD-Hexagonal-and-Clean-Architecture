package kh.edu.istad.platform.customer.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentLevel;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;

import java.util.UUID;

public class CustomerSegment extends BaseEntity<CustomerSegmentId> {

    private CustomerSegmentLevel segmentLevel;

    private CustomerSegment(Builder builder) {
        super.setId(builder.segmentId);
        segmentLevel = builder.segmentLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initiateCustomerSegment() {
        validateCustomerSegment();
        super.setId(new CustomerSegmentId(UUID.randomUUID()));
    }

    private void validateCustomerSegment() {
        if (super.getId() != null) {
            throw new CustomerDomainException("Customer Segment Id must be null!!");
        }
        if (segmentLevel == null) {
            throw new CustomerDomainException("Customer Segment Level must not be null!!");
        }
    }

    public void updateCustomerSegment(CustomerSegmentLevel segmentLevel) {
        if (segmentLevel == null) {
            throw new CustomerDomainException("Customer Segment Level must not be null!!");
        }
        this.segmentLevel = segmentLevel;
    }

    public CustomerSegmentLevel getSegmentLevel() {
        return segmentLevel;
    }

    public static final class Builder {
        private CustomerSegmentId segmentId;
        private CustomerSegmentLevel segmentLevel;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CustomerSegmentId val) {
            segmentId = val;
            return this;
        }

        public Builder segmentId(CustomerSegmentId val) {
            segmentId = val;
            return this;
        }

        public Builder segmentLevel(CustomerSegmentLevel val) {
            segmentLevel = val;
            return this;
        }

        public CustomerSegment build() {
            return new CustomerSegment(this);
        }
    }
}
