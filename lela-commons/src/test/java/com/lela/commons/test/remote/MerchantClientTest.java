package com.lela.commons.test.remote;

import com.lela.commons.remote.impl.AmazonMerchantClient;
import com.lela.commons.remote.impl.ParameterizedMerchantClient;
import com.lela.commons.remote.impl.PopShopsMerchantClient;
import com.lela.domain.document.Item;
import com.lela.domain.document.Redirect;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bjorn Harvold
 * Date: 5/23/12
 * Time: 2:03 PM
 * Responsibility:
 */
public class MerchantClientTest {

    private static SimpleDateFormat format = new SimpleDateFormat("MMddyyHHmm");
    private static String objId = "012345678901234567891234";
    private static String affiliateId = "AFFILIATEID";
    private static String affiliateRedirectUrl = "http://yada.zzz?a=%1$s&b=%2$s&c=%3$s";

    @Test
    public void testParameterizedMerchantClient()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMddyyHHmm");
        String objId = "012345678901234567891234";

        Redirect redirect = new Redirect();
        Date date = new Date();
        redirect.setRdrctdt(date);
        redirect.setId(new ObjectId(objId));//24 char length

        Item item = new Item();
        item.setId(new ObjectId(objId));

        ParameterizedMerchantClient client = new ParameterizedMerchantClient("ABC");

        String result = client.constructTrackableUrl("BASEURL?A=B", null, redirect, item);

        String expectedResult = "BASEURL"+"?"+"ABC"+"="+format.format(date)+"-"+objId+"-"+objId + "&A=B";

        assertEquals("Parameterize url built incorrectly", expectedResult, result);
    }

    @Test
    public void testParameterizedMerchantClientWithoutExistingParams()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMddyyHHmm");
        String objId = "012345678901234567891234";

        Redirect redirect = new Redirect();
        Date date = new Date();
        redirect.setRdrctdt(date);
        redirect.setId(new ObjectId(objId));//24 char length

        Item item = new Item();
        item.setId(new ObjectId(objId));

        ParameterizedMerchantClient client = new ParameterizedMerchantClient("ABC");

        String result = client.constructTrackableUrl("BASEURL", null, redirect, item);

        String expectedResult = "BASEURL"+"?"+"ABC"+"="+format.format(date)+"-"+objId+"-"+objId;

        assertEquals("Parameterize url built incorrectly", expectedResult, result);
    }

    @Test
    public void testPopShopsMerchantClientClient()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMddyyHHmm");
        String objId = "012345678901234567891234";

        Redirect redirect = new Redirect();
        Date date = new Date();
        redirect.setRdrctdt(date);
        redirect.setId(new ObjectId(objId));//24 char length

        Item item = new Item();
        item.setId(new ObjectId(objId));

        PopShopsMerchantClient client = new PopShopsMerchantClient();

        String result = client.constructTrackableUrl("BASEURL", null, redirect, item);

        String expectedResult = "BASEURL"+"/"+format.format(date)+"-"+objId+"-"+objId;

        assertEquals("Parameterize url built incorrectly", expectedResult, result);
    }

    @Test
    public void testAmazonMerchantClient()
    {
        Date date = new Date();
        Redirect redirect = buildRedirect(objId, date);

        Item item = new Item();
        item.setId(new ObjectId(objId));

        ObjectId userId = new ObjectId(objId);

        AmazonMerchantClient client = new AmazonMerchantClient(affiliateId, affiliateRedirectUrl, true, false);

        String result = client.constructTrackableUrl("BASEURL", userId, redirect, item);

        String expectedResult = String.format(affiliateRedirectUrl, format.format(date)+"-"+objId+"-"+objId, Boolean.TRUE.toString(), "BASEURL");

        assertEquals("Amazon url built incorrectly", expectedResult, result);
    }

    @Test
    public void testAmazonMerchantClientForGuest()
    {
        Date date = new Date();
        Redirect redirect = buildRedirect(objId, date);

        Item item = new Item();
        redirect.setId(new ObjectId(objId));

        AmazonMerchantClient client = new AmazonMerchantClient(affiliateId, affiliateRedirectUrl, true, false);

        String result = client.constructTrackableUrl("BASEURL"+"%26tag%3Dws%26", null, redirect, item);

        String expectedResult = "BASEURL"+"%26tag%3D" + affiliateId + "%26";

        assertEquals("Parameterize url built incorrectly", expectedResult, result);

    }

    @Test
    public void testAmazonMerchantClientDisabled()
    {
        Date date = new Date();
        Redirect redirect = buildRedirect(objId, date);

        Item item = new Item();
        redirect.setId(new ObjectId(objId));

        AmazonMerchantClient client = new AmazonMerchantClient(affiliateId, affiliateRedirectUrl, false, false);

        String result = client.constructTrackableUrl("BASEURL"+"%26tag%3Dws%26", null, redirect, item);

        String expectedResult = "BASEURL"+"%26tag%3D" + affiliateId + "%26";

        assertEquals("Parameterize url built incorrectly", expectedResult, result);

    }

    @Test
    public void testAmazonMerchantClientAllUsers()
    {
        Date date = new Date();
        Redirect redirect = buildRedirect(objId, date);

        Item item = new Item();
        item.setId(new ObjectId(objId));

        ObjectId userId = new ObjectId(objId);

        AmazonMerchantClient client = new AmazonMerchantClient(affiliateId, affiliateRedirectUrl, true, true);

        String result = client.constructTrackableUrl("BASEURL", userId, redirect, item);

        String expectedResult = String.format(affiliateRedirectUrl, format.format(date)+"-"+objId+"-"+objId, Boolean.TRUE.toString(), "BASEURL");

        assertEquals("Amazon url built incorrectly", expectedResult, result);
    }

    private Redirect buildRedirect(String objId, Date date) {
        Redirect redirect = new Redirect();
        redirect.setRdrctdt(date);
        redirect.setId(new ObjectId(objId));//24 char length
        return redirect;
    }
}
