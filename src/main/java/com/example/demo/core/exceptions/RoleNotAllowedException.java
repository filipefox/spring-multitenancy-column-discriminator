package com.example.demo.core.exceptions;

public class RoleNotAllowedException extends RuntimeException {

    public RoleNotAllowedException(String msg) {
        super(msg);
    }
}
