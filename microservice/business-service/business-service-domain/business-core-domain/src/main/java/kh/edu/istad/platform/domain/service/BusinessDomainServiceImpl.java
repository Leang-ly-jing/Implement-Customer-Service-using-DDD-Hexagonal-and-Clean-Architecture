package kh.edu.istad.platform.domain.service;

import kh.edu.istad.common.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.domain.event.OrderApprovalEvent;
import kh.edu.istad.platform.domain.event.OrderApprovedEvent;
import kh.edu.istad.platform.domain.event.OrderRejectedEvent;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class BusinessDomainServiceImpl implements BusinessDomainService{
    @Override
    public BusinessRegisteredEvent validateAndInitiateBusiness(Business business) {
        business.validateBusiness();
        business.initializeBusiness();
        return new BusinessRegisteredEvent(business, ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public OrderApprovalEvent validateOrder(Business business, List<String> failureMessages) {
        if (!business.isActive()) {
            failureMessages.add("Business with id " + business.getId().value() + " is currently not active!");
        }
        business.validateOrder(failureMessages);
        if (failureMessages.isEmpty()) {
            business.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(business.getOrderApproval(), business.getId(), failureMessages, ZonedDateTime.now(ZoneOffset.UTC));
        } else {
            business.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(business.getOrderApproval(), business.getId(), failureMessages, ZonedDateTime.now(ZoneOffset.UTC));
        }
    }

}
