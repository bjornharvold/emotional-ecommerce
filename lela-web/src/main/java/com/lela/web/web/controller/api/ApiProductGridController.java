package com.lela.web.web.controller.api;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ProductGridService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Category;
import com.lela.domain.document.ProductGrid;
import com.lela.domain.document.ProductGridFilter;
import com.lela.domain.document.User;
import com.lela.domain.dto.ApiValidationResponse;
import com.lela.domain.dto.SimpleCategoryItemsQuery;
import com.lela.domain.dto.productgrid.EnrichProductGridQuery;
import com.lela.domain.dto.productgrid.EnrichedProductGrid;
import com.lela.domain.dto.productgrid.ProductGridDto;
import com.lela.domain.enums.ApplicationType;
import com.lela.web.web.controller.AbstractApiController;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/30/12
 * Time: 12:36 PM
 * Responsibility:
 */
@Controller
public class ApiProductGridController extends AbstractApiController {
    private final static Logger log = LoggerFactory.getLogger(ApiProductGridController.class);

    private final static String DEFAULT_SIZE = "4";
    private final static Integer MAX_SIZE = 20;

    private final ProductGridService productGridService;
    private final CategoryService categoryService;
    private final Mapper mapper;

    @Autowired
    public ApiProductGridController(UserService userService,
                                    AffiliateService affiliateService,
                                    ApplicationService applicationService,
                                    CampaignService campaignService,
                                    UserTrackerService userTrackerService,
                                    UserSessionTrackingHelper userSessionTrackingHelper,
                                    ProductGridService productGridService,
                                    CategoryService categoryService,
                                    Mapper mapper) {
        super(userService, affiliateService, applicationService, campaignService, userTrackerService, userSessionTrackingHelper);
        this.productGridService = productGridService;
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @RequestMapping(value = "/api/grid/{applicationId}", method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public ProductGridDto retrieveProductGrid(@PathVariable("applicationId") String applicationUrlName,
                                @RequestParam("affiliateId") String affiliateUrlName,
                                @RequestParam(value = "size", required = false, defaultValue = DEFAULT_SIZE) Integer size,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                HttpSession session) throws Exception {
        ProductGridDto result = null;

        // validate that the caller has the right to retrieve a quiz
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            // issue a warning in the log if the 3rd party developer is misusing the api
            if (validationResponse.getApplication() != null && !validationResponse.getApplication().getTp().equals(ApplicationType.PRODUCT_GRID)) {
                if (log.isWarnEnabled()) {
                    log.warn(String.format("Affiliate: %s, is trying to call the grid api for application: %s. That application is of type: %s",
                            affiliateUrlName, applicationUrlName,
                            validationResponse.getApplication().getTp().name()));
                }
            }

            ProductGrid pg = productGridService.findPublishedProductGridByUrlName(validationResponse.getApplication().getTrlnm());

            if (pg != null) {
                // api won't allow caller to retrieve an unlimited number of products
                if (size > MAX_SIZE) {
                    size = MAX_SIZE;
                }

                // If no email was provided, determine if there is a logged in user
                User user = retrieveUserFromPrincipalOrSession(session);

                // time to load up the data for this product grid based on the grid's configuration
                EnrichedProductGrid epg = new EnrichedProductGrid(pg);
                productGridService.enrichProductGrid(new EnrichProductGridQuery(user.getCd(), size, epg));

                // map to a dto that we want to send back to the user
                result = mapper.map(epg, ProductGridDto.class);

                // Set the google analytics flag
                result.setAnalyticsEnabled(getAnalyticsEnabled());

                // Set the user code on the response
                if (user != null) {
                    result.setSrcd(user.getCd());
                }

                result.setUrl(createProductUrl(request, result));
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return result;
    }

    @RequestMapping(value = "/api/grid/{applicationId}/seeall", method = RequestMethod.GET)
    public String seeAllUrl(@PathVariable("applicationId") String applicationUrlName,
                            @RequestParam("affiliateId") String affiliateUrlName,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            RedirectAttributes redirectAttributes) throws Exception {

        // validate that the caller has the right to retrieve a quiz
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        String redirect = "/";
        if (validationResponse.getValid()) {
            // issue a warning in the log if the 3rd party developer is misusing the api
            if (validationResponse.getApplication() != null && !validationResponse.getApplication().getTp().equals(ApplicationType.PRODUCT_GRID)) {
                if (log.isWarnEnabled()) {
                    log.warn(String.format("Affiliate: %s, is trying to call the grid api for application: %s. That application is of type: %s",
                            affiliateUrlName, applicationUrlName,
                            validationResponse.getApplication().getTp().name()));
                }
            }

            ProductGrid pg = productGridService.findPublishedProductGridByUrlName(applicationUrlName);

            if (pg != null) {
                Category category = categoryService.findCategoryByUrlName(pg.getCrlnm());
                if (category != null) {

                    redirect = "/" + category.getSrlnm() + "/c?rlnm=" + category.getRlnm();

                    if (pg.getFltrs() != null && !pg.getFltrs().isEmpty()) {
                        SimpleCategoryItemsQuery query = new SimpleCategoryItemsQuery();

                        query.setFilters(new HashMap<String, List<String>>());
                        for(ProductGridFilter f : pg.getFltrs()) {
                            query.getFilters().put(f.getKy(), f.getVl());
                        }

                        redirectAttributes.addFlashAttribute(WebConstants.CATEGORY_QUERY, query);
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return "redirect:" + redirect;
    }

    @RequestMapping(value = "/api/grid/documentation", method = RequestMethod.GET)
    public String gridTestHarness(@RequestParam("affiliateId") String affiliateUrlName,
                                  @RequestParam("applicationId") String applicationUrlName,
                                  Model model) throws Exception {

        model.addAttribute("affiliate", affiliateUrlName);
        model.addAttribute("application", applicationUrlName);

        return "api.product.grid.test";
    }

    private String createProductUrl(HttpServletRequest request, ProductGridDto result) {
        StringBuilder sb = new StringBuilder(createBaseUrl(request));

        sb.append("/").append(result.getSrlnm()).append("/c?rlnm=").append(result.getCrlnm());

        return sb.toString();
    }
}
