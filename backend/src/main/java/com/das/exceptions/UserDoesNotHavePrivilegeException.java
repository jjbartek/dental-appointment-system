package com.das.exceptions;

public class UserDoesNotHavePrivilegeException extends RuntimeException {
    private Integer id;
    private String privilege;

    public UserDoesNotHavePrivilegeException(Integer id, String privilege) {
        super("User with id " + id + " does not have the privilege to " + privilege);
        this.id = id;
        this.privilege = privilege;
    }
}
