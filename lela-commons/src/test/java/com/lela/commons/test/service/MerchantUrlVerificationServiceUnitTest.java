package com.lela.commons.test.service;

import com.lela.commons.exception.PageNotLoadedException;
import com.lela.commons.service.impl.MerchantUrlVerificationServiceImpl;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/28/12
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */

public class MerchantUrlVerificationServiceUnitTest {

    MerchantUrlVerificationServiceImpl merchantUrl = new MerchantUrlVerificationServiceImpl();

    @Ignore //temporarily ignored, TODO: PT Nov 25
    @Test   
    public void verifyValidURLInStockProductWalmart(){
        String url = "http://www.walmart.com/ip/Sony-46-Class-LED-1080p-120Hz-HDTV-KDL-46EX640/20593826?wmlspartner=cVLKzmYcovo&sourceid=01855910470622781733&sourceid=0100000012230215302434&veh=aff";
        try {
            assertTrue("This product should be valid", merchantUrl.verify(url));
        }
        catch(PageNotLoadedException e){
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void verifyValidURLInStockProductNewEgg(){
        String url = "http://www.newegg.com/Product/Product.aspx?Item=N82E16889252173&nm_mc=AFC-C8Junction&cm_mmc=AFC-C8Junction-_-LED%20TV-_-Sony-_-89252173&AID=10440897&PID=5391124&SID=0921121853-4f88f1dcff30fd0617012862-505cb7b44ab5f64092c5d879";
        try {
            assertTrue("This product should be valid", merchantUrl.verify(url));
        }
        catch(PageNotLoadedException e){
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void verifyValidURLOutOfStockProduct()
    {
        String url = "http://www.walmart.com/ip/VIZIO-47-LED-Smart-TV-1080p-240Hz-HDTV-2-ultra-slim-M470SV/15992431";
        try {
            assertFalse("This product should be invalid", merchantUrl.verify(url));
        }
        catch(PageNotLoadedException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void verifyInvalidURL()
    {
        String url = "http://www.bestbuy.com/site/Sony+-+46%22+Class+-+LED+-+1080p+-+120Hz+-+Smart+-+HDTV/4756745.p?id=1218522130050&skuId=4756745&cmp=RMX&ky=1u5pjbYbJlZWph3uiYOwJH0xohLPOZOP9&ci_src=11138&ci_sku=4756745&nAID=11138&ref=39&loc=01&AID=10638425&PID=5391124&SID=0921121848-4f88f1dcff30fd0617012862-505cb6654ab5f64092c5d852&URL=http%3A%2F%2Fwww.bestbuy.com%2Fsite%2FSony%2B-%2B46%2522%2BClass%2B-%2BLED%2B-%2B1080p%2B-%2B120Hz%2B-%2BSmart%2B-%2BHDTV%2F4756745.p%3Fid%3D1218522130050%26skuId%3D4756745%26cmp%3DRMX%26ky%3D1u5pjbYbJlZWph3uiYOwJH0xohLPOZOP9%26ci_src%3D11138%26ci_sku%3D4756745%26nAID%3D11138%26ref%3D39%26loc%3D01&CJPID=5391124";
        try {
            assertFalse("This product should be invalid", merchantUrl.verify(url));
        }
        catch(PageNotLoadedException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void verifyBizzaroURL()
    {
        String url = "http://www.apmebf.com/d566gv32L/v16/KJNNJRSQ/OMSKKLN/J/J/J?p=ary7z9%3DcNHTGLNNOHKIHLL%26960%3Dw884%25IP%25HU%25HUBBB.2tBtvv.r31%25HUe63s9r8%25HUe63s9r8.p74C%25IUX8t1%25IScNHTGLNNOHKIHLL%25HL21_1r%25ISPUR-RNY92r8x32%25HLr1_11r%25ISPUR-RNY92r8x32-_-aRS%25HQik-_-i37wxqp-_-NOHKIHLL<<w884%3A%2F%2FBBB.8z50wrt.r31%3ANF%2Fr0xrz-KIOGGHJ-GFJJFNOM<<V<<";
        //String url = "http://www.apmebf.com/d566gv32L/v16/KJNNJRSQ/OMSKKLN/J/J/J?p=ary7z9%3DcNHTGLNNOHKIHLL%26960%3Dw884%25IP%25HU%25HUBBB.2tBtvv.r31%25HUe63s9r8%25HUe63s9r8.p74C%25IUX8t1%25IScNHTGLNNOHKIHLL%25HL21_1r%25ISPUR-RNY92r8x32%25HLr1_11r%25ISPUR-RNY92r8x32-_-aRS%25HQik-_-i37wxqp-_-NOHKIHLL%3C%3Cw884%3A%2F%2FBBB.8z50wrt.r31%3ANF%2Fr0xrz-KIOGGHJ-GFJJFNOM%3C%3CV%3C%3C";
        //http://www.emjcd.com/79115kjsrB/jqv/87BB7FGE/CAG889B/7/80xpCox6FEE8D9F9G68ACBE997BA9G96yq/988hFF9jAlm988l9F9ljFBFmDGFAli9i?w=ox4D5F%3DiTNZMRTTUNQONRR%26FC6%3D2EEA%25OV%25Na%25NaHHH.8zHz11.x97%25NakC9yFxE%25NakC9yFxE.vDAI%25OadEz7%25OYiTNZMRTTUNQONRR%25NR87_7x%25OYVaX-XTeF8xE398%25NRx7_77x%25OYVaX-XTeF8xE398-_-gXY%25NWoq-_-o9D23wv-_-TUNQONRR<x49!HRA7-DDGJKFJ<2EEA%3A%2F%2FHHH.E5B62xz.x97%3ATL%2Fx63x5-QOUMMNP-MLPPLTUS<<b<<
        try {
            assertTrue("This product should be valid", merchantUrl.verify(url));
        }
        catch(PageNotLoadedException e){
            fail(e.getMessage());
        }
    }
}
