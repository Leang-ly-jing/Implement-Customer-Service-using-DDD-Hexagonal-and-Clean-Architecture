package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.application.usecase.CreateCreditEntryUseCase;
import kh.edu.istad.platform.application.usecase.GetCreditBalanceUseCase;
import kh.edu.istad.platform.application.usecase.ProcessPaymentUseCase;
import kh.edu.istad.platform.payment.restapi.dto.CreateCreditRequest;
import kh.edu.istad.platform.payment.restapi.dto.CreditBalanceResponse;
import kh.edu.istad.platform.payment.restapi.dto.ProcessPaymentRequest;
import kh.edu.istad.platform.payment.restapi.dto.ProcessPaymentResponse;
import kh.edu.istad.platform.payment.restapi.mapper.PaymentWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final CreateCreditEntryUseCase createCreditEntryUseCase;
    private final GetCreditBalanceUseCase getCreditBalanceUseCase;
    private final PaymentWebMapper paymentWebMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<ProcessPaymentResponse> processPayment(@Valid @RequestBody ProcessPaymentRequest request) {
        return processPaymentUseCase.execute(paymentWebMapper.toCommand(request))
                .map(paymentWebMapper::toResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/credit")
    public Mono<CreditBalanceResponse> createCredit(@Valid @RequestBody CreateCreditRequest request) {
        return createCreditEntryUseCase.execute(paymentWebMapper.toCommand(request))
                .map(paymentWebMapper::toResponse);
    }

    @GetMapping("/credit/{customerId}")
    public Mono<CreditBalanceResponse> getCreditBalance(@PathVariable("customerId") UUID customerId) {
        return getCreditBalanceUseCase.execute(customerId)
                .map(paymentWebMapper::toResponse);
    }
}
