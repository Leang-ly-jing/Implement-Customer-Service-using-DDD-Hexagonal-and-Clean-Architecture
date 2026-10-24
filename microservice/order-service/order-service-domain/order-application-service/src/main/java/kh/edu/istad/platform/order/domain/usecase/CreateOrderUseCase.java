package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Customer;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;
import kh.edu.istad.platform.order.domain.mapper.CreateOrderMapper;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final CreateOrderMapper createOrderMapper;
    public CreateOrderResult execute(CreateOrderCommand command) {
        checkCustomer(command.customerId());
        Business business = checkBusiness(command);
        Order order = createOrderMapper.toOrder(command);

        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, business);
        Order savedOrder = orderRepository.save(orderCreatedEvent.getOrder());

        log.info("Order is created with id: {}", savedOrder.getId().value());
        return createOrderMapper.toResult(savedOrder, "Order Created Successfully");
    }

    private void checkCustomer(UUID customerId) {
        if (!customerRepository.exists(customerId)) {
            log.warn("Could not find customer with id: {}", customerId);
            throw new OrderDomainException("Could not find customer with id: " + customerId);
        }
    }
    private Business checkBusiness(CreateOrderCommand command) {
        Business business = createOrderMapper.toBusiness(command);
        Optional<Business> businessInformation = businessRepository.findBusinessInformation(business);
        if (businessInformation.isEmpty()) {
            log.warn("Could not find business with id: {}", command.businessId());
            throw new OrderDomainException("Could not find business with id: " + command.businessId());
        }
        return businessInformation.get();
    }
}


