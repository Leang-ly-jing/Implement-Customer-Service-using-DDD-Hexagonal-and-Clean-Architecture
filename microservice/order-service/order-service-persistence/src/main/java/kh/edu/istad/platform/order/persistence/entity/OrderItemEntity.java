package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor@AllArgsConstructor
@Entity
@Builder
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    private UUID id;
    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
