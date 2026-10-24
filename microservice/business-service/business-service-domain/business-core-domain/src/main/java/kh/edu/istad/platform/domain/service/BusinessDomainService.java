package kh.edu.istad.platform.domain.service;

import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.domain.event.OrderApprovalEvent;

import java.util.List;

public interface BusinessDomainService {
    BusinessRegisteredEvent validateAndInitiateBusiness(Business business);



    OrderApprovalEvent validateOrder(Business business, List<String> failureMessages);
}
