package kh.edu.istad.platform.order.persistence.entity;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class OrderAddressEntity {
    @Id
    private UUID id;
    private  String street;
    private String postalCode;
    private String city;
}
