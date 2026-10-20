//package kh.edu.istad.platform.customer.domain.usecase;
//
//import kh.edu.istad.common.domain.valueobject.Money;
//import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommad;
//import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
//import kh.edu.istad.platform.customer.domain.entity.Customer;
//import kh.edu.istad.platform.customer.domain.event.CustomerIntiatedEvent;
//import kh.edu.istad.platform.customer.domain.mapper.InitiateCustomerMapper;
//import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
//import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class InitiateCustomerUseCase {
//    private final CustomerDomainService customerDomainService;
//    private final CustomerRepository customerRepository;
//    private final InitiateCustomerMapper initiateCustomerMapper;
//
//    public InitiateCustomerResult  execute(InitiateCustomerCommad commad){
//
//        Customer customer = initiateCustomerMapper.toCustomer(commad);
//        CustomerIntiatedEvent event = customerDomainService.customerIntiate(customer);
//        Customer saveCustomer = customerRepository.save(event.getCustomer());
//
//       log.info("initiate success : {}", commad);
//       return initiateCustomerMapper.roResult(saveCustomer);
//    }
//}
