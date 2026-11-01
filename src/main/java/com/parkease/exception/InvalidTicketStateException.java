package com.parkease.exception;

public class InvalidTicketStateException extends RuntimeException {
    public InvalidTicketStateException(String message) {
        super(message);
    }
}

