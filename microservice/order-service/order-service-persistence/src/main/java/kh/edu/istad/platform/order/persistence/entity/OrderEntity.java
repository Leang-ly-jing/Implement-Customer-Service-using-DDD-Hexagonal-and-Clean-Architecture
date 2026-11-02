package kh.edu.istad.platform.order.persistence.entity;


import kh.edu.istad.common.domain.valueobject.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class OrderEntity {
    @Id
    private UUID id;
    private UUID customerId;
    private UUID businessId;
    private UUID trackingId;
    private BigDecimal price;

    private OrderStatus orderStatus;
    private String failureMessages;

    private OrderAddressEntity address;
    private List<OrderItemEntity> items;
}
