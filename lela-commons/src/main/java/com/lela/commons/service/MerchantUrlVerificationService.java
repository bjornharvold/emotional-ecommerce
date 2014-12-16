package com.lela.commons.service;

import com.lela.commons.exception.PageNotLoadedException;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/28/12
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MerchantUrlVerificationService {
    public boolean verify(String url)  throws PageNotLoadedException;
}
