package kh.edu.istad.platform.order.domain.service;

import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.event.OrderCencellEvent;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Business business);
    OrderPaidEvent payOrder(Order order);
    OrderCencellEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void approveOrder(Order order);
    void cancelOrder(Order order, List<String> failureMessages);
}
