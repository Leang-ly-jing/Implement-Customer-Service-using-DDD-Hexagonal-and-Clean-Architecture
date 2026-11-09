package kh.edu.istad.iam.domain.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class AuthorizationDomainException extends DomainException {
    public AuthorizationDomainException(String message) {
        super(message);
    }
    public AuthorizationDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
