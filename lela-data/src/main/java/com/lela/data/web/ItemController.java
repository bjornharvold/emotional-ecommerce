package com.lela.data.web;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.lela.data.domain.entity.*;
import com.lela.util.utilities.storage.FileStorage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RequestMapping("/crud/items")
@Controller
@RooWebScaffold(path = "crud/items", formBackingObject = Item.class)
@RooWebFinder
public class ItemController implements ApplicationContextAware {

    private static final String MANUAL = "MANUAL";
    private static final int PAGE_SIZE = 20;
    private static final String DEFAULT_ITEM_SEARCH = "itemSearch";

    @Autowired
    @Qualifier("itemImageFileStorage")
    private FileStorage fileStorage;

    @Autowired
    @Resource(name="imageAngleMap")
    private Map imageAngleMap;

    @Autowired
    @Resource(name="manualSourceKeyMap")
    private Map manualSourceKeyMap;

    @Autowired
    private ApplicationContext ctx;

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Item item, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, item);
            return "crud/items/create";
        }
        uiModel.asMap().clear();
        item.persist();
        return "redirect:/crud/items/" + encodeUrlPathSegment(item.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Item());
        return "crud/items/create";
    }


    @RequestMapping(value = "/category/{id}/attributes", produces = "text/html")
    public String listAttributeByMotivatorAndCategory(@PathVariable("id") Long id, Model uiModel) {
        Category category = Category.findCategory(id);
        uiModel.addAttribute("items", Item.findAllItemsByCategory(category));
        List<AttributeTypeMotivator> attributeTypeMotivators = AttributeTypeMotivator.findAllAttributeTypeMotivatorByCategory(category);
        uiModel.addAttribute("categoryId", id);

        //find the attribute types for each motivator and group them
        Multimap<Integer, AttributeType> motivators = ArrayListMultimap.create();

        for(AttributeTypeMotivator attributeTypeMotivator:attributeTypeMotivators)
        {
            motivators.put(attributeTypeMotivator.getMotivator(), attributeTypeMotivator.getAttributeType());
        }
        uiModel.addAttribute("motivators", motivators.asMap());

        addDateTimeFormatPatterns(uiModel);
        return "crud/items/attributes";
    }

    @RequestMapping(value = "/category/{id}/details", produces = "text/html")
    public String listByCategory(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        handleItemsByCategory(id, page, size, uiModel);
        return "crud/items/list";
    }

    private void handleItemsByCategory(Long id, Integer page, Integer size, Model uiModel) {
        if( id == null )
        {
            throw new RuntimeException("Category ID is required");
        }
        Category category = Category.findCategory(id);
        if (page != null || size != null) {

            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("items", Item.findItemsByCategory(category, firstResult, sizeNo));
            float nrOfPages = (float) Item.countItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("items", Item.findAllItemsByCategory(category));
        }
        addDateTimeFormatPatterns(uiModel);
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("item", Item.findItem(id));
        uiModel.addAttribute("itemId", id);
        return "crud/items/show";
    }


    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("items", Item.findItemEntries(firstResult, sizeNo));
            float nrOfPages = (float) Item.countItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("items", Item.findAllItems());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/items/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Item item, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, item);
            return "crud/items/update";
        }
        uiModel.asMap().clear();
        item.merge();
        return "redirect:/crud/items/" + encodeUrlPathSegment(item.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Item.findItem(id));
        return "crud/items/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Item item = Item.findItem(id);
        item.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/items";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("item_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("item_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }

    void populateEditForm(Model uiModel, Item item) {
        uiModel.addAttribute("item", item);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("brands", Brand.findAllBrands());
        uiModel.addAttribute("categorys", Category.findAllCategorys());
        uiModel.addAttribute("conditiontypes", ConditionType.findAllConditionTypes());
        //uiModel.addAttribute("itemcolors", ItemColor.findAllItemColors());
        //uiModel.addAttribute("itemcolormerchants", ItemColorMerchant.findAllItemColorMerchants());
        //uiModel.addAttribute("itemidentifiers", ItemIdentifier.findAllItemIdentifiers());
        //uiModel.addAttribute("itemrecalls", ItemRecall.findAllItemRecalls());
        //uiModel.addAttribute("itemvideos", ItemVideo.findAllItemVideos());
        //uiModel.addAttribute("productimageitems", ProductImageItem.findAllProductImageItems());
        //uiModel.addAttribute("merchantoffers", MerchantOffer.findAllMerchantOffers());
        uiModel.addAttribute("reviewStatuses", ReviewStatus.findAllReviewStatuses());
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

    @RequestMapping(params = { "find=ByLelaUrl", "form" }, method = RequestMethod.GET)
    public String findItemsByLelaUrlForm(Model uiModel) {
        return "crud/items/findItemsByLelaUrl";
    }

    @RequestMapping(params = "find=ByLelaUrl", method = RequestMethod.GET)
    public String findItemsByLelaUrl(@RequestParam("lelaUrl") String lelaUrl, Model uiModel) {
        uiModel.addAttribute("items", Item.findItemsByLelaUrl(lelaUrl));
        return "crud/items/list";
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody String handleUncaughtException(Exception ex, WebRequest request, HttpServletResponse response) throws Exception {
        if (request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            response.setHeader("Content-Type", "application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "Unknown error occurred: " + ex.getMessage();
        } else {
            throw ex;
        }
    }
}
