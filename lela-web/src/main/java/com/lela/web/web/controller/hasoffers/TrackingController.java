package com.lela.web.web.controller.hasoffers;

import com.lela.util.utilities.pixel.TrackingPixel;
import com.lela.web.web.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 10/5/12
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/tracking")

public class TrackingController extends AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(TrackingController.class);

    @RequestMapping(value="/pixel.gif")
    public @ResponseBody
    void servePixel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("Serving HasOffers Tracking Pixel...");

        //Capture info with user code
        for(Object key:request.getParameterMap().keySet())
        {
            LOG.debug(key + " - " + request.getParameter(String.valueOf(key)));
        }

        new TrackingPixel().writeToResponse(response);
    }
}
