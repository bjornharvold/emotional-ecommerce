package com.lela.commons.exception;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/28/12
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class PageNotLoadedException extends Exception {
    public PageNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
