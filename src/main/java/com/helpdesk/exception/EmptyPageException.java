package com.helpdesk.exception;

public class EmptyPageException extends BaseException {
    public EmptyPageException(int page, String message) {
        super("Page " + page + ": " + message);
    }
}
