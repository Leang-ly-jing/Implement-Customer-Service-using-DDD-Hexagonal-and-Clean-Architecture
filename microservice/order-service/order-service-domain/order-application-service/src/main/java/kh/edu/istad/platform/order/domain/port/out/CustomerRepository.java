package kh.edu.istad.platform.order.domain.port.out;

import java.util.UUID;

public interface CustomerRepository {
    boolean exists(UUID customerId);
}
