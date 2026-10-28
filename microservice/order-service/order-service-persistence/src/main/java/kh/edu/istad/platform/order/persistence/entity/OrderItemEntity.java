package kh.edu.istad.platform.order.persistence.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Table("order_items")
public class OrderItemEntity {
    //composite key
    @Id
    @Embedded.Nullable
    private OrderItemEntityId orderItemEntityId;
    private UUID productId;
    private String failureMessage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
