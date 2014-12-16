package com.lela.web.web.controller.affiliate;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.PurchaseEvent;
import com.lela.commons.service.AffiliateSaleService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.domain.document.AffiliateSale;
import com.lela.domain.document.Sale;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import com.lela.util.utilities.pixel.TrackingPixel;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/12/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/affiliate/accounting")
public class SalesPixelController {
    private static final Logger LOG = LoggerFactory.getLogger(SalesPixelController.class);

    private AffiliateSaleService affiliateSaleService;

    private UserService userService;

    private UserTrackerService userTrackerService;

    private final String network = "lela";

    @Autowired
    public SalesPixelController(AffiliateSaleService affiliateSaleService,
                                UserService userService,
                                UserTrackerService userTrackerService) {
        this.affiliateSaleService = affiliateSaleService;
        this.userService = userService;
        this.userTrackerService = userTrackerService;
    }

    @RequestMapping(value = "/sales/pixel.gif")
    public
    @ResponseBody
    void servePixel(@RequestParam(value = "affiliate", required = true) String affiliate,
                    @RequestParam(value = "subId", required = true) String subId,
                    @RequestParam(value = "productId", required = true) String productId,
                    @RequestParam(value = "productCategory", required = true) String productCategory,
                    @RequestParam(value = "productName", required = true) String productName,
                    @RequestParam(value = "totalPrice", required = true) Double totalPrice,
                    @RequestParam(value = "commission", required = true) Double commission,
                    @RequestParam(value = "quantity", required = true) Integer quantity,
                    @RequestParam(value = "orderId", required = true) String orderId,
                    HttpServletResponse response) throws Exception {

        LOG.info("Serving Accounting Tracking Pixel...");

        captureInformation(affiliate, subId, productId, productCategory, productName, totalPrice, commission, quantity, orderId);

        new TrackingPixel().writeToResponse(response);
    }

    @Async
    private void captureInformation(String affiliate, String subId, String productId, String productCategory, String productName, Double totalPrice, Double commission, Integer quantity, String orderId) {

        //First save the sale info in mongo
        AffiliateSale affiliateSale = new AffiliateSale();
        affiliateSale.setId(ObjectId.get());
        affiliateSale.setCdt(new Date());
        affiliateSale.setCmmssn(commission);
        String redirectId = parseSubIdForRedirectId(subId);
        affiliateSale.setRdrctd(redirectId);
        affiliateSale.setSbd(subId);
        affiliateSale.setRlnm(affiliate);
        affiliateSale.setPrdctcd(productId);
        affiliateSale.setTtlPrce(totalPrice);
        affiliateSale.setOrdrd(orderId);
        affiliateSaleService.save(affiliateSale);

        //Post a sale event
        Sale sale = new Sale();
        sale.setAdvrtsrNm(affiliate);
        sale.setCmmssnmnt(commission);
        sale.setPrcssdt(new Date());
        sale.setQntty(quantity);
        sale.setRdrd(subId);
        sale.setNtwrk(network);
        sale.setSlmnt(totalPrice);
        sale.setPrdctNm(productName);
        sale.setPrdctCtgry(productCategory);
        sale.setPrdctd(productId);


        //Attempt to get the user information
        UserTracker userTracker = userTrackerService.findByRedirectId(redirectId);
        User user = null;
        if(userTracker != null)
        {
            user = userService.findUserByCode(userTracker.getSrcd());
        }

        EventHelper.post(new PurchaseEvent(user, userTracker, sale));
    }

    private String parseSubIdForRedirectId(String subId) {
        if (StringUtils.isNotBlank(subId)) {
            String[] splitId = StringUtils.split(subId, "-");
            if (splitId.length == 3) {
                String redirectId = splitId[2];
                return redirectId;
            }
        }
        return "";
    }
}
