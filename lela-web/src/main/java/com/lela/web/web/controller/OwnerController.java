package com.lela.web.web.controller;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.UserService;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Category;
import com.lela.domain.document.Owner;
import com.lela.domain.document.User;
import com.lela.domain.dto.RelevantItemsForOwnerSearchQuery;
import com.lela.domain.dto.owner.OwnerAggregateQuery;
import com.lela.domain.dto.search.AbstractSearchResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class OwnerController extends AbstractSearchController {
    /**
     * Logger
     */
    private final static Logger log = LoggerFactory.getLogger(OwnerController.class);
    private static final Integer MAX_RESULT_SIZE = 12;
    private final OwnerService ownerService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public OwnerController(EventService eventService,
                           ProductEngineService productEngineService,
                           OwnerService ownerService,
                           MessageSource messageSource,
                           CategoryService categoryService,
                           UserService userService) {
        super(eventService, productEngineService, messageSource);
        this.ownerService = ownerService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    /**
     * This url mapping shows an overview of the products this owner carries
     *
     * @param urlName urlName
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/{seoUrlName}/b", method = RequestMethod.GET)
    public String showOwnerSEO(@PathVariable("seoUrlName") String seoUrlName,
                               @RequestParam(value = "rlnm", required = true) String urlName,
                               HttpSession session, Model model, Locale locale)
            throws Exception {
        String view = "owner.index";
        RelevantItemsForOwnerSearchQuery query = new RelevantItemsForOwnerSearchQuery();
        query.setUrlName(urlName);
        query.setPage(0);
        query.setSize(4);

        doCommonShowOwner(query, session, model, locale);

        Owner owner = (Owner) model.asMap().get(WebConstants.OWNER);

        if (owner == null || StringUtils.isBlank(owner.getSrlnm())) {
            view = "resourceNotFound";
        }

        return view;
    }

    /**
     * This url mapping shows an overview of the products this owner carries
     *
     * @param urlName urlName
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     * @deprecated This is for search engine support while they haven't crawled the new url
     */
    @RequestMapping(value = "/brand/{urlName}", method = RequestMethod.GET)
    public ModelAndView showOwner(@PathVariable("urlName") String urlName, HttpSession session, Model model, Locale locale)
            throws Exception {
        ModelAndView view = new ModelAndView("owner.index");

        RelevantItemsForOwnerSearchQuery query = new RelevantItemsForOwnerSearchQuery();
        query.setUrlName(urlName);
        query.setPage(0);
        query.setSize(4);

        // load up this brand
        Owner owner = ownerService.findOwnerByUrlName(query.getUrlName());

        if (owner == null) {
            view.setViewName("resourceNotFound");
        } else {
            if (StringUtils.isNotBlank(owner.getSrlnm())) {
                // redirect to the url mapping above
                StringBuilder sb = new StringBuilder("/");
                sb.append(owner.getSrlnm()).append("/b?rlnm=");
                sb.append(owner.getRlnm());
                RedirectView rView = new RedirectView(sb.toString());
                rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                view = new ModelAndView(rView);
            } else {
                doCommonShowOwner(query, session, model, locale);
            }
        }

        return view;
    }

    /**
     * This method drills down to the category level of the brand
     *
     * @param urlName            urlName
     * @param seoCategoryUrlName categoryUrlName
     * @param page               page
     * @param session            session
     * @param model              model
     * @param locale             locale
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/{seoUrlName}/{seoCategoryUrlName}/b", method = RequestMethod.GET)
    public String showOwnerCategorySEO(@PathVariable("seoUrlName") String seoUrlName,
                                       @PathVariable("seoCategoryUrlName") String seoCategoryUrlName,
                                       @RequestParam(value = "rlnm", required = true) String urlName,
                                       @RequestParam(value = "crlnm", required = true) String categoryUrlName,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       HttpSession session, Model model, Locale locale)
            throws Exception {
        String view = "owner.category";
        RelevantItemsForOwnerSearchQuery query = new RelevantItemsForOwnerSearchQuery();
        query.setUrlName(urlName);
        query.setCat(categoryUrlName);
        query.setPage(page);
        query.setSize(12);

        doCommonShowOwner(query, session, model, locale);

        Owner owner = (Owner) model.asMap().get(WebConstants.OWNER);

        if (owner == null || StringUtils.isBlank(owner.getSrlnm())) {
            view = "resourceNotFound";
        } else {
            model.addAttribute(WebConstants.URL_NAME, urlName);
            model.addAttribute(WebConstants.CATEGORY_URL_NAME, categoryUrlName);
        }

        return view;
    }

    /**
     * This method drills down to the category level of the brand
     *
     * @param urlName         urlName
     * @param categoryUrlName categoryUrlName
     * @param page            page
     * @param session         session
     * @param model           model
     * @param locale          locale
     * @return Tile def
     * @throws Exception ex
     * @deprecated This is for search engine support while they haven't crawled the new url
     */
    @RequestMapping(value = "/brand/{urlName}/{categoryUrlName}", method = RequestMethod.GET)
    public ModelAndView showOwnerCategory(@PathVariable("urlName") String urlName,
                                          @PathVariable("categoryUrlName") String categoryUrlName,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          HttpSession session, Model model, Locale locale)
            throws Exception {
        ModelAndView view = new ModelAndView("owner.category");
        RelevantItemsForOwnerSearchQuery query = new RelevantItemsForOwnerSearchQuery();
        query.setUrlName(urlName);
        query.setCat(categoryUrlName);
        query.setPage(page);
        query.setSize(12);

        // load up this brand
        Owner owner = ownerService.findOwnerByUrlName(query.getUrlName());

        if (owner == null) {
            view.setViewName("resourceNotFound");
        } else {
            if (StringUtils.isNotBlank(owner.getSrlnm())) {
                Category category = categoryService.findCategoryByUrlName(categoryUrlName);
                // redirect to the url mapping above
                StringBuilder sb = new StringBuilder("/");
                sb.append(owner.getSrlnm());
                sb.append("/").append(category.getSrlnm()).append("/b?rlnm=");
                sb.append(owner.getRlnm()).append("&crlnm=").append(category.getRlnm());
                RedirectView rView = new RedirectView(sb.toString());
                rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                view = new ModelAndView(rView);
            } else {
                doCommonShowOwner(query, session, model, locale);
            }
        }

        return view;
    }

    /**
     * Returns a page with brands
     *
     * @param page  page
     * @param model model
     * @return Tile def
     */
    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public String showBrands(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                             Model model) {
        String view = "owners.index";

        OwnerAggregateQuery query = new OwnerAggregateQuery();
        model.addAttribute(WebConstants.OWNERS, ownerService.findOwnerAggregateDetails(query));

        return view;
    }

    /**
     * Returns a page with brands that start with the first letter
     *
     * @param page  page
     * @param model model
     * @return Tile def
     */
    @RequestMapping(value = "/brands/{letter}", method = RequestMethod.GET)
    public String showBrandsByName(@PathVariable("letter") String letter,
                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                   Model model) {
        String view = "owners.index";

        model.addAttribute(WebConstants.SELECTED_LETTER, letter);

        OwnerAggregateQuery query = new OwnerAggregateQuery();
        query.setFilterOnLetter(letter);
        model.addAttribute(WebConstants.OWNERS, ownerService.findOwnerAggregateDetails(query));

        return view;
    }

    private void doCommonShowOwner(RelevantItemsForOwnerSearchQuery query, HttpSession session, Model model, Locale locale) {
        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            // add user to the model
            query.setUserCode(user.getCd());

            // load up this brand
            Owner owner = ownerService.findOwnerByUrlName(query.getUrlName());

            if (owner != null) {
                model.addAttribute(WebConstants.OWNER, owner);
                AbstractSearchResult searchResults;

                try {

                    if (userService.shouldUserSeeRecommendedProducts(user.getCd())) {
                        // if a category is specified, we'll return a SearchResult object
                        if (StringUtils.isNotBlank(query.getCat())) {
                            searchResults = productEngineService.searchForRelevantCategoryItemsForOwnerUsingSolr(query);
                        } else {
                            searchResults = productEngineService.searchForRelevantItemsForOwnerUsingSolr(query);
                        }
                    } else {
                        // if a category is not specified, we'll return a GroupedSearchResult object
                        if (StringUtils.isNotBlank(query.getCat())) {
                            searchResults = productEngineService.searchForCategoryItemsForOwnerUsingSolr(query);
                        } else {
                            searchResults = productEngineService.searchForItemsForOwnerUsingSolr(query);
                        }
                    }

                    if (searchResults != null && searchResults.getCategories() != null) {
                        sortCategoriesFromSearchResult(searchResults.getCategories(), locale);
                        model.addAttribute(WebConstants.SEARCH_RESULTS, searchResults);
                    }
                } catch (SearchException ex) {
                    if (log.isErrorEnabled()) {
                        log.error(ex.getMessage());
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn(String.format("Owner: %s does not exist or is not available", query.getUrlName()));
                }
            }
        }
    }
}
