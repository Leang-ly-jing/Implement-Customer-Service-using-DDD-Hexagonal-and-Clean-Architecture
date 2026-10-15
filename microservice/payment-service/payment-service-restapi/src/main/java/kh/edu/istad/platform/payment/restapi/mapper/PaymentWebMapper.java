package kh.edu.istad.platform.payment.restapi.mapper;

import kh.edu.istad.platform.application.dto.CreateCreditEntryCommand;
import kh.edu.istad.platform.application.dto.CreditBalanceResult;
import kh.edu.istad.platform.application.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.application.dto.ProcessPaymentResult;
import kh.edu.istad.platform.payment.restapi.dto.CreateCreditRequest;
import kh.edu.istad.platform.payment.restapi.dto.CreditBalanceResponse;
import kh.edu.istad.platform.payment.restapi.dto.ProcessPaymentRequest;
import kh.edu.istad.platform.payment.restapi.dto.ProcessPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebMapper {

    public ProcessPaymentCommand toCommand(ProcessPaymentRequest request) {
        return new ProcessPaymentCommand(
                request.customerId(),
                request.orderId(),
                request.price()
        );
    }

    public ProcessPaymentResponse toResponse(ProcessPaymentResult result) {
        return new ProcessPaymentResponse(
                result.paymentId(),
                result.customerId(),
                result.orderId(),
                result.price(),
                result.paymentStatus(),
                result.createdAt(),
                result.failureMessages()
        );
    }

    public CreateCreditEntryCommand toCommand(CreateCreditRequest request) {
        return new CreateCreditEntryCommand(
                request.customerId(),
                request.totalCreditAmount()
        );
    }

    public CreditBalanceResponse toResponse(CreditBalanceResult result) {
        return new CreditBalanceResponse(
                result.customerId(),
                result.totalCreditAmount()
        );
    }
}
