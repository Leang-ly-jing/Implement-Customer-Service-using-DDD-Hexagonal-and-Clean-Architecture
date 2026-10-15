package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final BusinessId businessId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessage;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        businessId = builder.businessId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessage = builder.failureMessage;
    }


    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemPrice();
    }

    public void initializeOrder(){
        super.setId(new OrderId(UUID.randomUUID()));
        this.trackingId = new TrackingId(UUID.randomUUID());
        this.orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void validateInitialOrder(){
        if (orderStatus != null || getId() != null){
            throw new OrderDomainException("Order is not Initialization");
        }

    }

    private void validateTotalPrice(){
        if (price == null || price.amount().compareTo(BigDecimal.ZERO) <= 0){
            throw new OrderDomainException("Total price must greater than ZERO!!");
        }

    }

    private void validateItemPrice(){
        if (items.isEmpty()){
            throw new OrderDomainException("Order item must not be Empty!");
        }
        BigDecimal orderItemTotal = items.stream().map(
                orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal().amount();
                }).reduce(
                        BigDecimal.ZERO, BigDecimal::add
        );
        if (price.amount().compareTo(orderItemTotal) != 0){
            throw new OrderDomainException("Total price = " + price.amount() + "is no equal to Order items Total = " + orderItemTotal);
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price is not valid for product "
                    + orderItem.getProduct().getId().value());
        }
    }

    private void initializeOrderItems(){
        for (OrderItem orderItem : items){
            orderItem.initializeOrderItem(
                    super.getId(), new OrderItemId(UUID.randomUUID())
            );
        }
    }

    public void pay(){
        if (orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("Order is not pay yet!");
        }
        this.orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if (orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order is not approve yet!");
        }
        this.orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessage){
        if (orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order is not initCancel yet!");
        }
        this.orderStatus = OrderStatus.CANCELLING;
        updateFailureMessage(failureMessage);
    }

    public void cancel(List<String> failureMessage){
        if (orderStatus != OrderStatus.CANCELLING
        && orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("Order is not Cancel");
        }
        this.orderStatus = OrderStatus.CANCELLED;
        updateFailureMessage(failureMessage);
    }

    private void updateFailureMessage(List<String> message){
        if (this.failureMessage != null && message != null){
            this.failureMessage.addAll(message.stream().filter(
                    msg -> msg != null && !msg.isBlank()
             ).toList()
            );
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessage() {
        return failureMessage;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private BusinessId businessId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessage;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessage(List<String> val) {
            failureMessage = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
