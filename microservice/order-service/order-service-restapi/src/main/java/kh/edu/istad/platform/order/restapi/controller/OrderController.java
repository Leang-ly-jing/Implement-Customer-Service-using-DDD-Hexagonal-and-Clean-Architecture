package kh.edu.istad.platform.order.restapi.controller;


import jakarta.validation.Valid;
import kh.edu.istad.common.domain.exception.DomainException;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.usecase.CreateOrderUseCase;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.restapi.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderWebMapper orderWebMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return createOrderUseCase.execute(orderWebMapper.toCommand(createOrderRequest))
                .map(orderWebMapper::toResponse);
    }

    @GetMapping
    public Flux<CreateOrderResponse> findAllOrders() {
        throw new DomainException("Not implemented");
    }


}
