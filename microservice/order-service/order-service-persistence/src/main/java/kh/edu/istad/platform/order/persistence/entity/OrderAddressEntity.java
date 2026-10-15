package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_address")
@Entity

public class OrderAddressEntity {
    @Id
    private UUID id;
    private  String street;
    private String postalCode;
    private String city;
}
