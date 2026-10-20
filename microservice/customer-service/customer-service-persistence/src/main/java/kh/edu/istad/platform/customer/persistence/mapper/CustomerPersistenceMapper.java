package kh.edu.istad.platform.customer.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerPersistenceMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "phoneNumber.value", target = "phoneNumber")
    @Mapping(source = "segment.id", target = "segmentId")
    CustomerDocument toDocument(Customer customer);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "phoneNumber", target = "phoneNumber.value")
    @Mapping(source = "segmentId", target = "segment")
    Customer toDomain(CustomerDocument customerDocument);

    @Mapping(source = "id.value", target = "id")
    CustomerSegmentDocument toSegmentDocument(CustomerSegment segment);

    @Mapping(source = "id", target = "segmentId")
    CustomerSegment toSegmentDomain(CustomerSegmentDocument segmentDocument);

    default CustomerSegmentId toCustomerSegmentId(String id) {
        if (id == null) {
            return null;
        }
        return new CustomerSegmentId(java.util.UUID.fromString(id));
    }
}
