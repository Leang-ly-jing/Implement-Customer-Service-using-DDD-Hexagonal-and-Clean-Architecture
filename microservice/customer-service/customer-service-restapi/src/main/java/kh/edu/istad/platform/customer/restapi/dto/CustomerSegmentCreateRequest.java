package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotNull;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentLevel;

public record CustomerSegmentCreateRequest(
        @NotNull
        CustomerSegmentLevel segmentLevel
) {
}
