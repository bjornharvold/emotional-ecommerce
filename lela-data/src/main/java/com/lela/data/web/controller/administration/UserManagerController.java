/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.data.web.controller.administration;

//~--- non-JDK imports --------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.FacebookUserService;
import com.lela.commons.service.FavoritesListService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.RoleService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.FacebookSnapshot;
import com.lela.domain.document.Role;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserTracker;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.dto.user.DisableUser;
import com.lela.domain.dto.user.UserSearchQuery;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: UserController</p>
 * <p>Description: User homepage.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("administrationUserController")
@SessionAttributes(types = {UserSearchQuery.class})
public class UserManagerController {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(UserManagerController.class);

    /**
     * Field description
     */
    private final UserService userService;
    private final ProfileService profileService;
    private final RoleService roleService;
    private final FavoritesListService favoritesListService;
    private final UserTrackerService userTrackerService;

    /**
     * Field description
     */
    private final FacebookUserService facebookUserService;

    private final ObjectMapper mapper;

    @Autowired
    public UserManagerController(UserService userService,
                                 ProfileService profileService, RoleService roleService,
                                 FavoritesListService favoritesListService, FacebookUserService facebookUserReport,
                                 @Qualifier("socialObjectMapper") ObjectMapper mapper,
                                 UserTrackerService userTrackerService) {
        this.userService = userService;
        this.profileService = profileService;
        this.roleService = roleService;
        this.favoritesListService = favoritesListService;
        this.userTrackerService = userTrackerService;
        this.facebookUserService = facebookUserReport;
        this.mapper = mapper;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/administration/user/list", method = RequestMethod.GET)
    public String showUsers(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    						@RequestParam(value = "size", required = false, defaultValue = "10") String size,
                            Model model) throws Exception {

        List<String> fieldsOnUserList = determineFieldsOnUser();
        List<String> fieldsOnUserSupplementList = determineFieldsOnUserSupplement();

        model.addAttribute(WebConstants.USER_SEARCH_QUERY, new UserSearchQuery());

        int pageSize = 10;
        if (size.equals("ALL")){
        	pageSize = Integer.MAX_VALUE;
        } else {
        	try {
        		pageSize = Integer.parseInt(size);
        	} catch (Exception e) {
        		pageSize = 10; //suppress errors
        	}
        }
        List<UserUserSupplementEntry> uuseList = userService.findUsers(page, pageSize, fieldsOnUserList, fieldsOnUserSupplementList);
        Long userCount = userService.findUserCount();
        CustomPage<UserUserSupplementEntry> result = new CustomPage<UserUserSupplementEntry>(uuseList, page, pageSize, userCount);
        model.addAttribute(WebConstants.USERS, result);
        model.addAttribute(WebConstants.FACEBOOK_USER_COUNT, userService.findFacebookUserCount());
        
        model.addAttribute(WebConstants.USER_COUNT, userCount);
        model.addAttribute(WebConstants.PAGE_SIZE, size);
        return "admin.user.list";
    }

    @RequestMapping(value = "/administration/user/list", method = RequestMethod.POST)
    public String searchForUserByQuery(UserSearchQuery query, Model model) throws Exception {

        if (query != null) {
            List<String> fields = determineFieldsOnUser();
            query.setFields(fields);
            List<UserUserSupplementEntry> uuseList = userService.findUsersByQuery(query);
            Long userCount = uuseList == null?0L:uuseList.size();
            CustomPage<UserUserSupplementEntry> result = null;
            if (uuseList != null && uuseList.size() > 0){
            	result = new CustomPage<UserUserSupplementEntry>(uuseList, 0, Integer.MAX_VALUE, userCount); //No pagination for search
            }
            model.addAttribute(WebConstants.USERS, result);
            model.addAttribute(WebConstants.FACEBOOK_USER_COUNT, userService.findFacebookUserCount(query));
            model.addAttribute(WebConstants.USER_COUNT, userCount);
            model.addAttribute(WebConstants.PAGE_SIZE, "ALL");
        }

        return "admin.user.list";
    }

    @RequestMapping(value = "/administration/user/facebook", method = RequestMethod.GET)
    public String showFacebookDataPage(Model model) throws Exception {

        model.addAttribute(WebConstants.PAGE, "show");

        return "admin.user.facebook.data";
    }

    @RequestMapping(value = "/administration/user/facebook/data", method = RequestMethod.GET)
    public String showFacebookData(@RequestParam(value = "userEmail", required = true) String userEmail,
                                   Model model) throws Exception {

        User user = userService.findUserByEmail(userEmail);

        if (user != null) {

            // Create a JSON facebook report
            FacebookSnapshot snapshot = facebookUserService.findOrGenerateFacebookSnapshot(user);
            facebookSnapshotCommon(model, user, snapshot);
        }

        model.addAttribute("userEmail", userEmail);
        model.addAttribute(WebConstants.PAGE, "results");

        return "admin.user.facebook.data";
    }

    @RequestMapping(value = "/administration/user/facebook/reload", method = RequestMethod.GET)
    public String reloadFacebookData(@RequestParam(value = "userEmail", required = true) String userEmail,
                                     Model model) throws Exception {

        User user = userService.findUserByEmail(userEmail);

        if (user != null) {

            // Create a JSON facebook report
            FacebookSnapshot snapshot = facebookUserService.generateSnapshot(user);
            facebookSnapshotCommon(model, user, snapshot);
        }

        model.addAttribute("userEmail", userEmail);
        model.addAttribute(WebConstants.PAGE, "results");

        return "admin.user.facebook.data";
    }

    /**
     * Generates an excel file for download
     *
     * @param response response
     * @throws Exception
     */
    @RequestMapping(value = "/administration/user/report/motivator", method = RequestMethod.GET)
    public void generateUserMotivatorReport(HttpServletResponse response) throws Exception {
        Workbook workbook = userService.generateUserMotivatorReport();

        if (workbook != null) {
            response.setHeader("Content-Disposition", "attachment; filename=user-motivator-report.xlsx");
            response.setContentType("application/vnd.ms-excel.12");

            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);

            response.flushBuffer();
        } else {
            PrintWriter pw = response.getWriter();
            pw.write("Problem generating report. Workbook is null.");
        }
    }

    /**
     * Displays a user profile
     *
     * @throws Exception
     */
    @RequestMapping(value = "/administration/user/{id}", method = RequestMethod.GET)
    public String showUser(@PathVariable("id") ObjectId userId, Model model) throws Exception {
        String view = "admin.user.data";

        User user = userService.findUser(userId);
        UserSupplement us = userService.findUserSupplement(user.getCd());
        List<Role> roles = roleService.findRoles();
        List<Role> toRemove = new ArrayList<Role>();

        // remove roles the user already has from the list
        if (user.getRrlnms() != null && !user.getRrlnms().isEmpty() &&
                roles != null && !roles.isEmpty()) {
            for (String roleUrlName : user.getRrlnms()) {
                for (Role role : roles) {
                    if (StringUtils.equals(roleUrlName, role.getRlnm())) {
                        toRemove.add(role);
                    }
                }
            }

            if (!toRemove.isEmpty()) {
                roles.removeAll(toRemove);
            }
        }

        UserTracker userTracker = userTrackerService.findByUserCode(user.getCd());
        model.addAttribute(WebConstants.USER_TRACKER, userTracker);
        model.addAttribute(WebConstants.ROLES, roles);
        model.addAttribute(WebConstants.USER, user);
        model.addAttribute(WebConstants.USER_SUPPLEMENT, us);

        return view;
    }

    /**
     * Add a role to a user
     *
     * @throws Exception
     */
    @RequestMapping(value = "/administration/user/{id}/role/{roleUrlName}/add", method = RequestMethod.GET)
    public String addUserRole(@PathVariable("id") ObjectId userId,
                              @PathVariable("roleUrlName") String roleUrlName) throws Exception {
        String view = "redirect:/administration/user/" + userId.toString();

        User user = userService.findUser(userId);

        if (user != null) {
            user.addRole(roleUrlName);
            userService.saveUser(user);
        }

        return view;
    }

    /**
     * Remove a role from a user
     *
     * @throws Exception
     */
    @RequestMapping(value = "/administration/user/{id}/role/{roleUrlName}/remove", method = RequestMethod.GET)
    public String removeUserRole(@PathVariable("id") ObjectId userId,
                                 @PathVariable("roleUrlName") String roleUrlName) throws Exception {
        String view = "redirect:/administration/user/" + userId.toString();

        User user = userService.findUser(userId);

        if (user != null) {
            user.removeRole(roleUrlName);
            userService.saveUser(user);
        }

        return view;
    }

    @RequestMapping(value = "/administration/user/{id}/deactivate", method = RequestMethod.GET)
    public String showUserDeactivateForm(@PathVariable("id") ObjectId userId,
                                         Model model) throws Exception {

        User user = userService.findUser(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("deactivateUser", user);
        model.addAttribute("deactivateUserSupplement", userService.findUserSupplement(user.getCd()));
        return "admin.user.deactivate";
    }

    @RequestMapping(value = "/administration/user/{id}", method = RequestMethod.DELETE)
    public String deactivateUser(@PathVariable("id") ObjectId userId,
                                 DisableUser du) throws Exception {

        profileService.removeUserProfile(du, userId);
        return "redirect:/administration/user/list";
    }

    @RequestMapping(value = "/administration/user/{id}/alerts/send")
    public String sendPriceAlerts(@PathVariable("id") ObjectId userId,
                                  RedirectAttributes redirectAttributes) {
        User user = userService.findUser(userId);
        favoritesListService.sendValidPriceAlerts(user.getCd());

        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, "Price alerts emailed if they met the alert criteria");

        return "redirect:/administration/user/" + userId;
    }

    private void facebookSnapshotCommon(Model model, User user, FacebookSnapshot snapshot) throws IOException {
        // Calculate the motivators
        if (snapshot.getError() == null) {
            Map<String, Integer> motivators = facebookUserService.calculateMotivators(snapshot);
            model.addAttribute("motivators", motivators);
        }

        StringWriter writer = new StringWriter();

        // TODO deprecated
        mapper.defaultPrettyPrintingWriter().writeValue(writer, snapshot);

        model.addAttribute("user", user);
        model.addAttribute("snapshot", snapshot);
        model.addAttribute("snapshotJson", writer.toString());

        if (StringUtils.isNotBlank(snapshot.getError())) {
            model.addAttribute(WebConstants.ERROR, snapshot.getError());
        }
    }

    private List<String> determineFieldsOnUser() {
        List<String> fields = new ArrayList<String>(6);
        fields.add("cd");
        fields.add("fnm");
        fields.add("lnm");
        fields.add("ml");
        fields.add("cdt");
        fields.add("lgndt");
        return fields;
    }
    private List<String> determineFieldsOnUserSupplement() {
        List<String> fields = new ArrayList<String>();
        fields.add("cd");
        fields.add("fnm");
        fields.add("lnm");
        fields.add("ml");
        return fields;
    }
}
