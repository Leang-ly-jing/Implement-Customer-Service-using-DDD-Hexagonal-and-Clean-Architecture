package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommad;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitiateCustomerUseCase {
    private final CustomerDomainService customerDomainService;
    public InitiateCustomerResult  execute(InitiateCustomerCommad commad){
       log.info("initiate success : {}", commad);
       return new InitiateCustomerResult(UUID.randomUUID());
    }
}
