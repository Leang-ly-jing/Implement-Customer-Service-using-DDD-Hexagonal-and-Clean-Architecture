package kh.edu.istad.platform.application.usecase;

import kh.edu.istad.platform.application.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.application.dto.RegisterBusinessResult;
import kh.edu.istad.platform.application.mapper.RegisterBusinessMapper;
import kh.edu.istad.platform.application.port.out.BusinessRepository;
import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.domain.service.BusinessDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterBusinessUseCase {

    private final BusinessDomainService businessDomainService;
    private final BusinessRepository businessRepository;
    private final RegisterBusinessMapper registerBusinessMapper;

    public Mono<RegisterBusinessResult> execute(RegisterBusinessCommand command) {
        Business business = registerBusinessMapper.toBusiness(command);
        BusinessRegisteredEvent event = businessDomainService.validateAndInitiateBusiness(business);

        return businessRepository.save(event.getBusiness())
                .doOnSuccess(saved -> log.info("Business registered with id: {}", saved.getId().value()))
                .map(saved -> registerBusinessMapper.toResult(saved, "Business Registered Successfully"));
    }
}
