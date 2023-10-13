package com.das.users;

public class UserNotAuthorizedException extends RuntimeException {
    private Integer id;
    private String privilege;

    public UserNotAuthorizedException(Integer id, String privilege) {
        super("User with id " + id + " does not have the privilege to " + privilege);
        this.id = id;
        this.privilege = privilege;
    }
}
