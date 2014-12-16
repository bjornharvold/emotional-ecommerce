package com.lela.commons.exception;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/17/12
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class KissMetricsRuntimeException extends RuntimeException {
    public KissMetricsRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
