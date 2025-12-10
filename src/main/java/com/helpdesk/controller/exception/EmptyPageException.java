package com.helpdesk.controller.exception;

public class EmptyPageException extends BaseException {
    public EmptyPageException(int page, String message) {
        super("Page " + page + ": " + message);
    }
}
