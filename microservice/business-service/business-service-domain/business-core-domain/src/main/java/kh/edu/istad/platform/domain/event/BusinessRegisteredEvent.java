package kh.edu.istad.platform.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.domain.entity.Business;

import java.time.ZonedDateTime;

public class BusinessRegisteredEvent implements DomainEvent<Business> {
    private final Business business;
    private final ZonedDateTime createAt;

    public BusinessRegisteredEvent(Business business, ZonedDateTime createAt) {
        this.business = business;
        this.createAt = createAt;
    }

    public Business getBusiness() {
        return business;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }
}
