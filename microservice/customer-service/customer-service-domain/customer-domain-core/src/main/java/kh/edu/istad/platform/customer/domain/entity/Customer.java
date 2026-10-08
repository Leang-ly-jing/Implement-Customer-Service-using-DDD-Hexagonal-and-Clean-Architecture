package kh.edu.istad.platform.customer.domain.entity;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;

import java.util.UUID;

public class Customer extends AggregateRoot<CustomerId> {
    private final String userName;
    private String familyName;
    private String givenName;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private  CustomerStatus customerStatus;

    private Customer(Builder builder) {
        super.setId(builder.id);
        userName = builder.userName;
        setFamilyName(builder.familyName);
        setGivenName(builder.givenName);
        email = builder.email;
        phoneNumber = builder.phoneNumber;
        customerStatus = builder.customerStatus;
    }


    //Domain Critical Logic

    public void initiateCustomer(){
        validateCustomer();

        super.setId(new CustomerId(UUID.randomUUID()));

        customerStatus = CustomerStatus.ACTIVE;
    }

    private void validateCustomer(){
        if (super.getId() != null){
            throw new CustomerDomainException("Customer Id must be null!!");
        }
        if (customerStatus != null){
            throw new CustomerDomainException("Customer Status must be null");
        }
    }

    public void updateCustomer(String familyName, String givenName){
        if (familyName == null || givenName == null){
            throw new CustomerDomainException("Family Name and Given Name Must not be null");
        }
        this.familyName = familyName;
        this.givenName = givenName;

    }

    public void deactiveCustomer(){
        if (customerStatus != CustomerStatus.ACTIVE){
            throw new CustomerDomainException("Can not be deactivate because Customer no Active");
        }
        customerStatus =CustomerStatus.INACTIVE;
    }







    public String getUserName() {
        return userName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public static final class Builder {
        private CustomerId id;
        private String userName;
        private String familyName;
        private String givenName;
        private Email email;
        private PhoneNumber phoneNumber;
        private CustomerStatus customerStatus;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CustomerId val) {
            id = val;
            return this;
        }

        public Builder userName(String val) {
            userName = val;
            return this;
        }

        public Builder familyName(String val) {
            familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            givenName = val;
            return this;
        }

        public Builder email(Email val) {
            email = val;
            return this;
        }

        public Builder phoneNumber(PhoneNumber val) {
            phoneNumber = val;
            return this;
        }

        public Builder customerStatus(CustomerStatus val) {
            customerStatus = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
