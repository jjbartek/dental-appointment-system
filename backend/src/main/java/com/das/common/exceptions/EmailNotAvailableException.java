package com.das.common.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailNotAvailableException extends RuntimeException {
    private String email;

    public EmailNotAvailableException(String email) {
        super("Email " + email + " is already in use");
        this.email = email;
    }
}
