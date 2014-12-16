/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Bjorn Harvold
 * Date: 11/5/11
 * Time: 3:16 AM
 * Responsibility:
 */
//@Controller
public class ApiSavedListsController {
    private final static Logger log = LoggerFactory.getLogger(ApiSavedListsController.class);

    /*
    private static final String EXTERNAL_LIST_ENDPOINT = "/api/lists";
    private final EventService eventService;
    private final AnalyticsService analyticsService;
    private final UserService userService;
    private final ApplicationService apiService;

    @Autowired
    public ApiSavedListsController(EventService eventService, AnalyticsService analyticsService,
                                   UserService userService, ApplicationService apiService) {
        this.eventService = eventService;
        this.analyticsService = analyticsService;
        this.userService = userService;
        this.apiService = apiService;
    }

    @RequestMapping(value = EXTERNAL_LIST_ENDPOINT, method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ListsRpc getLists(Device device,
                            @RequestHeader(value = "deviceId", required = true) String deviceId,
                            @RequestHeader(value = "applicationId", required = true) String applicationId,
                            User devicePersistedUser,
                            HttpServletRequest request) throws Exception {
        ListsRpc result = null;

        // second, we need to figure out who this user is; either by the principal (in the case of oauth) or the deviceId
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        User user = null;

        if (principal != null && principal.getUser() != null) {
            // this means we have an oauth authenticated rest based request
            user = principal.getUser();
        } else if (devicePersistedUser != null) {
            // this means the user hasn't registered yet but has
            // saved a user from previous interactions
            user = devicePersistedUser;
        } else {
            // start from scratch
            user = new User();
        }

        // save call to analytics - this call should be asynchronous and not add to the overhead of this call
        analyticsService.saveRpcAnalytic(request, EXTERNAL_LIST_ENDPOINT, applicationId, deviceId, device, user);

        // first we need to check if the applicationId is a valid one
        Application application = apiService.findApplicationByUrlName(applicationId);

        if (application != null) {

            // now that we have the user we can determine what questions to return to her
//            result = populateQuizRpc(user);
        } else {
            log.error(String.format("Application Id: %s is not valid", applicationId));
        }

        return result;
    }
    */
}
