package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerSegmentWebMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "segmentId", ignore = true)
    CustomerSegment toDomain(CustomerSegmentCreateRequest request);
}
