package com.lela.web.web.controller.affiliate;

import com.lela.commons.repository.ItemRepository;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.NavigationBarService;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.NavigationBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/16/12
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/affiliate")
public class AffiliateItemController {

    private final ItemRepository itemRepository;

    private final NavigationBarService navigationBarService;

    private final AffiliateService affiliateService;

    @Autowired
    public AffiliateItemController(ItemRepository itemRepository, AffiliateService affiliateService, NavigationBarService navigationBarService)
    {
        this.itemRepository = itemRepository;
        this.affiliateService = affiliateService;
        this.navigationBarService = navigationBarService;
    }

    @RequestMapping(value = "/{affiliateUrlName}/{itemId}")
    public String redirectToItem(@PathVariable("affiliateUrlName") String affiliateUrlName,@PathVariable("itemId") String itemId, HttpSession session)
    {
        //find the affiliate's navigation bar
        AffiliateAccount affiliate = affiliateService.findAffiliateAccountByUrlName(affiliateUrlName);
        NavigationBar navigationBar = navigationBarService.findNavigationBarByUrlName(affiliate.getDmnnvbr());

        List<String> categories = navigationBar.getCtgrrlnms();

        List<Item> items = itemRepository.findItemsInCategory("UPC", itemId, categories);
        Item item = null;

        //TODO There should only be one item with that UPC, but what if there isn't?
        if(items.size()>0)
        {
            item = items.get(0);
            return "redirect:/"+item.getSrlnm()+"/p?rlnm="+item.getRlnm();
        }

        throw new RuntimeException("Item not found");
        //redirect to rlnm name
    }
}
