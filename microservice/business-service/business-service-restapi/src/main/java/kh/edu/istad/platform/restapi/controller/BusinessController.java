package kh.edu.istad.platform.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.application.usecase.RegisterBusinessUseCase;
import kh.edu.istad.platform.restapi.dto.RegisterBusinessRequest;
import kh.edu.istad.platform.restapi.dto.RegisterBusinessResponse;
import kh.edu.istad.platform.restapi.mapper.BusinessWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/businesses")
public class BusinessController {

    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final BusinessWebMapper businessWebMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<RegisterBusinessResponse> registerBusiness(@Valid @RequestBody RegisterBusinessRequest request) {
        return registerBusinessUseCase.execute(businessWebMapper.toCommand(request))
                .map(businessWebMapper::toResponse);
    }
}
