/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller.administration;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: DashboardController</p>
 * <p>Description: Admin homepage.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("administrationDashboardController")
public class DashboardController {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(DashboardController.class);

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/administration/dashboard", method = RequestMethod.GET)
    public String show(Model model) throws Exception {
        return "administration.dashboard";
    }

    @RequestMapping(value = "/administration/force/error", method = RequestMethod.GET)
    public String forceError() throws Exception {
        log.error("Logging an Error message: " + new java.util.Date());
        return "administration.dashboard";
    }

    @RequestMapping(value = "/administration/force/fatal", method = RequestMethod.GET)
    public String forceFatal() throws Exception {
        log.error("Logging a Fatal message: " + new java.util.Date());
        return "administration.dashboard";
    }

    @RequestMapping(value = "/administration/force/exception", method = RequestMethod.GET)
    public String forceException() throws Exception {
        throw new RuntimeException("Forcing an Exception");
    }

}
