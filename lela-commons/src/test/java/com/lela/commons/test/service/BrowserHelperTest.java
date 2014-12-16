package com.lela.commons.test.service;

import com.lela.commons.utilities.BrowserHelper;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/21/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserHelperTest {

    @Test
    public void testGoodUrl() throws Exception
    {
        BrowserHelper browserHelper = new BrowserHelper("http://www.google.com");
        browserHelper.load();
        assertTrue("The url was not good.", browserHelper.verifyURL());
    }

    @Test
    public void testdUrlWithRedirectToGoodUrl() throws Exception
    {
        BrowserHelper browserHelper = new BrowserHelper("http://goo.gl/LRqlb");
        browserHelper.load();
        assertTrue("The redirect url was not good.", browserHelper.verifyURL());
    }

    @Test
    public void testUrlWithRedirectToBadUrl() throws Exception
    {
        BrowserHelper browserHelper = new BrowserHelper("http://goo.gl/juBmB");
        browserHelper.load();
        assertFalse("The redirect url was good.", browserHelper.verifyURL());
    }


    @Test
    public void testBadUrl() throws Exception
    {
        BrowserHelper browserHelper = new BrowserHelper("http://www.bestbuy.com/site/Sony+-+46%22+Class+-+LED+-+1080p+-+120Hz+-+Smart+-+HDTV/4756745.p?id=1218522130050&skuId=4756745&cmp=RMX&ky=1u5pjbYbJlZWph3uiYOwJH0xohLPOZOP9&ci_src=11138&ci_sku=4756745&nAID=11138&ref=39&loc=01&AID=10638425&PID=5391124&SID=0921121848-4f88f1dcff30fd0617012862-505cb6654ab5f64092c5d852&URL=http%3A%2F%2Fwww.bestbuy.com%2Fsite%2FSony%2B-%2B46%2522%2BClass%2B-%2BLED%2B-%2B1080p%2B-%2B120Hz%2B-%2BSmart%2B-%2BHDTV%2F4756745.p%3Fid%3D1218522130050%26skuId%3D4756745%26cmp%3DRMX%26ky%3D1u5pjbYbJlZWph3uiYOwJH0xohLPOZOP9%26ci_src%3D11138%26ci_sku%3D4756745%26nAID%3D11138%26ref%3D39%26loc%3D01&CJPID=5391124");
        browserHelper.load();
        assertFalse("The redirect url was good.", browserHelper.verifyURL());
    }

    //in stock things
    @Test
    @Ignore
    public void testInStock() throws Exception
    {
        String url = "http://www.walmart.com/ip/Sony-46-Class-LED-1080p-120Hz-HDTV-KDL-46EX640/20593826?wmlspartner=cVLKzmYcovo&sourceid=01855910470622781733&sourceid=0100000012230215302434&veh=aff";
        BrowserHelper browserHelper = new BrowserHelper(url);
        browserHelper.load();
        assertTrue("The page does not say the item from walmart is in stock.", browserHelper.verifyPageContains("in stock"));

        url = "http://www.amazon.com/Sony-BRAVIA-KDL46EX640-46-Inch-Internet/dp/B006U1VGHO?SubscriptionId=06SBPEW90EGFPQ05N702&tag=LelaID6-20&linkCode=xm2&camp=2025&creative=165953&creativeASIN=B006U1VGHO";
        browserHelper = new BrowserHelper(url);
        browserHelper.load();
        assertTrue("The page does not say the item from amazon is in stock.", browserHelper.verifyPageContains("in stock"));

        url = "http://www.newegg.com/Product/Product.aspx?Item=N82E16889252173&nm_mc=AFC-C8Junction&cm_mmc=AFC-C8Junction-_-LED%20TV-_-Sony-_-89252173&AID=10440897&PID=5391124&SID=0921121853-4f88f1dcff30fd0617012862-505cb7b44ab5f64092c5d879";
        browserHelper = new BrowserHelper(url);
        browserHelper.load();
        assertTrue("The page does not say the item from newegg in stock.", browserHelper.verifyPageContains("in stock"));
    }

    @Test
    @Ignore
    public void testOutOfStock() throws Exception
    {
        String url = "http://www.walmart.com/ip/Sony-46-Class-LED-1080p-120Hz-HDTV-KDL-46EX640/20593826?wmlspartner=cVLKzmYcovo&sourceid=01855910470622781733&sourceid=0100000012230215302434&veh=aff";
        BrowserHelper browserHelper = new BrowserHelper(url);
        browserHelper.load();

        assertTrue("The page does not say the item is out of stock.", browserHelper.verifyPageContains("not available at this time"));
    }

    @Test
    @Ignore
    public void testIsNotInStock() throws Exception
    {
        String url = "http://www.walmart.com/ip/Sony-46-Class-LED-1080p-120Hz-HDTV-KDL-46EX640/20593826?wmlspartner=cVLKzmYcovo&sourceid=01855910470622781733&sourceid=0100000012230215302434&veh=aff";
        BrowserHelper browserHelper = new BrowserHelper(url);
        browserHelper.load();

        List<String> contains = new ArrayList<String>();
        contains.add("out of stock");
        contains.add("not available at this time");
        assertFalse("The page does not say the item is out of stock.", browserHelper.verifyPageDoesNotContain(contains));
    }

    @Test
    public void testIsInStock() throws Exception
    {
        String url = "http://www.amazon.com/Sony-BRAVIA-KDL46EX640-46-Inch-Internet/dp/B006U1VGHO?SubscriptionId=06SBPEW90EGFPQ05N702&tag=LelaID6-20&linkCode=xm2&camp=2025&creative=165953&creativeASIN=B006U1VGHO";
        BrowserHelper browserHelper = new BrowserHelper(url);
        browserHelper.load();

        List<String> contains = new ArrayList<String>();
        contains.add("out of stock");
        contains.add("not available at this time");
        assertTrue("The page should be in stock.", browserHelper.verifyPageDoesNotContain(contains));
    }


    @Test
    public void testInStockUnknown() throws Exception
    {
        String url = "http://www.adorama.com/SOKDL46EX640.html?utm_term=Other&utm_medium=Affiliate&utm_campaign=Other&utm_source=cj_5391124";
        BrowserHelper browserHelper = new BrowserHelper(url);
        browserHelper.load();
        assertFalse("The page does not say the item in stock.", browserHelper.verifyPageContains("in stock"));
    }

}
