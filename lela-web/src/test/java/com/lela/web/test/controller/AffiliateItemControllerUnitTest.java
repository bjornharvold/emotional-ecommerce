package com.lela.web.test.controller;

import com.lela.commons.event.EventHelper;
import com.lela.commons.repository.ItemRepository;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.NavigationBarService;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.NavigationBar;
import com.lela.web.web.controller.AffiliateController;
import com.lela.web.web.controller.affiliate.AffiliateItemController;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/17/12
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class AffiliateItemControllerUnitTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private NavigationBarService navigationBarService;

    @Mock
    private AffiliateService affiliateService;

    @InjectMocks
    private AffiliateItemController affiliateItemController;

    @Before
    public void setup()
    {

    }

    @Test
    public void testRedirectToItem()
    {
        String affiliateUrlName = "test";
        String itemId = "000";
        String navbarUrlName = "navbar";
        HttpSession session = new MockHttpSession();

        List<String> categories = new ArrayList<String>();
        categories.add("categoryUrlName");

        AffiliateAccount affiliateAccount = new AffiliateAccount();
        affiliateAccount.setRlnm(affiliateUrlName);
        affiliateAccount.setDmnnvbr(navbarUrlName);

        NavigationBar navigationBar = new NavigationBar();
        navigationBar.setCtgrrlnms(categories);

        Item item = new Item();
        item.setSrlnm("Item Name");
        item.setRlnm("itemurlname");

        List<Item> items = new ArrayList<Item>();
        items.add(item);

        when(affiliateService.findAffiliateAccountByUrlName(affiliateUrlName)).thenReturn(affiliateAccount);
        when(navigationBarService.findNavigationBarByUrlName(navbarUrlName)).thenReturn(navigationBar);
        when(itemRepository.findItemsInCategory("UPC", itemId, categories)).thenReturn(items);

        String result = affiliateItemController.redirectToItem(affiliateUrlName, itemId, session);
        assertTrue("Result did not redirect", result.startsWith("redirect:"));
        assertEquals("Does not redirect to correct item", StringUtils.substringAfter(result, "redirect:"), "/Item Name/p?rlnm=itemurlname");
    }

    @Test(expected = RuntimeException.class)
    public void testRedirectToItemNotFound()
    {
        String affiliateUrlName = "test";
        String itemId = "000";
        String navbarUrlName = "navbar";
        HttpSession session = new MockHttpSession();

        List<String> categories = new ArrayList<String>();
        categories.add("categoryUrlName");

        AffiliateAccount affiliateAccount = new AffiliateAccount();
        affiliateAccount.setRlnm(affiliateUrlName);
        affiliateAccount.setDmnnvbr(navbarUrlName);

        NavigationBar navigationBar = new NavigationBar();
        navigationBar.setCtgrrlnms(categories);

        List<Item> items = new ArrayList<Item>();

        when(affiliateService.findAffiliateAccountByUrlName(affiliateUrlName)).thenReturn(affiliateAccount);
        when(navigationBarService.findNavigationBarByUrlName(navbarUrlName)).thenReturn(navigationBar);
        when(itemRepository.findItemsInCategory("UPC", itemId, categories)).thenReturn(items);

        String result = affiliateItemController.redirectToItem(affiliateUrlName, itemId, session);
    }

}
