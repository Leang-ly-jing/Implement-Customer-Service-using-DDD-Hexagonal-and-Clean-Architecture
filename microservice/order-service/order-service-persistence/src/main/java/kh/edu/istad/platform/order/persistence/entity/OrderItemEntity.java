package kh.edu.istad.platform.order.persistence.entity;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor@AllArgsConstructor
@Document
@Builder

public class OrderItemEntity {

    private UUID id;
    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;

}
