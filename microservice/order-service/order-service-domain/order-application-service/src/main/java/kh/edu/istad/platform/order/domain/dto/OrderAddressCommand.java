package kh.edu.istad.platform.order.domain.dto;

public record OrderAddressCommand (
        String street, String postalCode, String city
){
}
