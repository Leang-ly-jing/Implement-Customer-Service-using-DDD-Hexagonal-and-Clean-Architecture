package kh.edu.istad.platform.order.domain.dto;


import java.util.UUID;
import kh.edu.istad.common.domain.valueobject.OrderStatus;
public record  CreateOrderResult (
        UUID trackingId, OrderStatus orderStatus, String message
){
}
