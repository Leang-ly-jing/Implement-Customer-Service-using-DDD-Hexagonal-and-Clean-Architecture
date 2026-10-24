package kh.edu.istad.platform.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.application.usecase.GetBusinessByIdUseCase;
import kh.edu.istad.platform.application.usecase.RegisterBusinessUseCase;
import kh.edu.istad.platform.restapi.dto.BusinessResponse;
import kh.edu.istad.platform.restapi.dto.RegisterBusinessRequest;
import kh.edu.istad.platform.restapi.dto.RegisterBusinessResponse;
import kh.edu.istad.platform.restapi.mapper.BusinessWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/businesses")
public class BusinessController {

    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final BusinessWebMapper businessWebMapper;
    private final GetBusinessByIdUseCase getBusinessByIdUseCase;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<RegisterBusinessResponse> registerBusiness(@Valid @RequestBody RegisterBusinessRequest request) {
        return registerBusinessUseCase.execute(businessWebMapper.toCommand(request))
                .map(businessWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BusinessResponse>> getById(@PathVariable("id") UUID id) {
        return getBusinessByIdUseCase.execute(id)
                .map(businessWebMapper::toDetailResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
