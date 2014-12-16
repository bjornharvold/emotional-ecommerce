package com.lela.commons.service.impl;

import com.lela.commons.exception.PageNotLoadedException;
import com.lela.commons.service.MerchantUrlVerificationService;
import com.lela.commons.utilities.BrowserHelper;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/28/12
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MerchantUrlVerificationServiceImpl implements MerchantUrlVerificationService {

    public boolean verify(String url) throws PageNotLoadedException
    {
        BrowserHelper browserHelper = new BrowserHelper(url);
        browserHelper.load();


        if(browserHelper.verifyURL()
                && ( browserHelper.verifyPageDoesNotContain("isinStock: false") //walmart is in stock
                //|| ( browserHelper.verifyPageDoesNotContain("out of stock")  //breaks on newegg
                //  && browserHelper.verifyPageDoesNotContain("not available at this time"))
                ))
        {
            return true;
        }
        return false;
    }

}
