package kh.edu.istad.platform.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;

public class CreditEntry extends BaseEntity<CreditEntryId> {
    private final CustomerId customerId;
    private Money totalCreditAmount;

    private CreditEntry(Builder builder) {
        super.setId(builder.creditEntryId);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public void addCreditAmount(Money amount) {
        this.totalCreditAmount = new Money(this.totalCreditAmount.amount().add(amount.amount()));
    }

    public void subtractCreditAmount(Money amount) {
        this.totalCreditAmount = new Money(this.totalCreditAmount.amount().subtract(amount.amount()));
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public Builder creditEntryId(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
