package com.example.tickets.exception;

public class TicketServiceException extends Exception {
    public TicketServiceException() {
        super();
    }

    public TicketServiceException(String message) {
        super(message);
    }

    public TicketServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketServiceException(Throwable cause) {
        super(cause);
    }

    protected TicketServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
