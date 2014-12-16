/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.api;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.QuizStepFinishEvent;
import com.lela.commons.event.QuizFinishEvent;
import com.lela.commons.event.QuizStartEvent;
import com.lela.commons.service.CampaignService;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.StaticContent;
import com.lela.domain.document.User;
import com.lela.domain.dto.ApiValidationResponse;
import com.lela.domain.dto.quiz.QuizAnswersResponse;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.quiz.QuizDto;
import com.lela.domain.dto.quiz.QuizStaticContentRequest;
import com.lela.domain.dto.quiz.StaticContentDto;
import com.lela.domain.enums.ApplicationType;
import com.lela.domain.enums.InteractionType;
import com.lela.util.utilities.jackson.CustomObjectMapper;
import com.lela.util.utilities.pixel.TrackingPixel;
import com.lela.web.web.controller.AbstractApiController;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 11/5/11
 * Time: 3:16 AM
 * Responsibility:
 */
@Controller
public class ApiQuizController extends AbstractApiController {
    private final static Logger log = LoggerFactory.getLogger(ApiQuizController.class);
    private static final String INCORRECT_API_USAGE = "Affiliate: %s, is trying to call the quiz api for application: %s. That application is of type: %s";

    private final static ObjectMapper jsonMapper = new CustomObjectMapper();

    private final QuizService quizService;
    private final ProductEngineService productEngineService;
    private final Validator validator;
    private final Mapper mapper;
    private final UserTrackerService userTrackerService;

    @Autowired
    public ApiQuizController(UserTrackerService userTrackerService,
                             AffiliateService affiliateService,
                             ApplicationService applicationService,
                             CampaignService campaignService,
                             UserSessionTrackingHelper userSessionTrackingHelper,
                             QuizService quizService,
                             ProductEngineService productEngineService,
                             UserService userService,
                             @Qualifier("validator") Validator validator,
                             Mapper mapper) {
        super(userService, affiliateService, applicationService, campaignService, userTrackerService, userSessionTrackingHelper);
        this.quizService = quizService;
        this.productEngineService = productEngineService;
        this.validator = validator;
        this.mapper = mapper;
        this.userTrackerService = userTrackerService;
    }

    @RequestMapping(value = "/api/quiz/{applicationId}", method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public QuizDto retrieveQuiz(@PathVariable("applicationId") String applicationUrlName,
                             @RequestParam("affiliateId") String affiliateUrlName,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "campaignId", required = false) String campaignId,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             HttpSession session) throws Exception {
        QuizDto result = null;

        // validate that the caller has the right to retrieve a quiz
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            // track analytics
            handleAnalyticsCapture(request, response, affiliateUrlName, applicationUrlName, campaignId);

            // issue a warning in the log if the 3rd party developer is misusing the api
            if (validationResponse.getApplication() != null && !validationResponse.getApplication().getTp().equals(ApplicationType.QUIZ)) {
                if (log.isWarnEnabled()) {
                    log.warn(String.format(INCORRECT_API_USAGE, affiliateUrlName, applicationUrlName,
                            validationResponse.getApplication().getTp().name()));
                }
            }

            Quiz quiz = quizService.findPublishedQuizByUrlName(validationResponse.getApplication().getTrlnm());

            if (quiz != null) {
                result = mapper.map(quiz, QuizDto.class);

                // Set the google analytics flag
                result.setAnalyticsEnabled(getAnalyticsEnabled());
                result.setKmEnabled(getKmEnabled());
                result.setKmKey(getKmKey());

                // Set the user code on the response
                User user = null;
                if (email != null) {
                    user = userService.findUserByEmail(email);
                    if (user != null) {
                        result.setSrcd(user.getCd());
                    }
                }

                if (result.getSrcd() == null) {
                    user = retrieveUserFromPrincipalOrSession(session);
                    if (user != null) {
                        result.setSrcd(user.getCd());
                    }
                }

                EventHelper.post(new QuizStartEvent(
                        user,
                        validationResponse.getAffiliateAccount(),
                        validationResponse.getApplication())
                );
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return result;
    }

    @RequestMapping(value = "/api/quiz", method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public QuizDto retrieveDefaultQuiz(@RequestParam("affiliateId") String affiliateUrlName,
                                    @RequestParam("applicationId") String applicationUrlName,
                                    @RequestParam(value = "campaignId", required = false) String campaignId,
                                    @RequestParam(value = "lang", required = false, defaultValue = "en") String language,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuizDto result = null;

        // validate that the caller has the right to retrieve a quiz
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            // track analytics
            handleAnalyticsCapture(request, response, affiliateUrlName, applicationUrlName, campaignId);

            // issue a warning in the log if the 3rd party developer is misusing the api
            if (validationResponse.getApplication() != null && !validationResponse.getApplication().getTp().equals(ApplicationType.QUIZ)) {
                if (log.isWarnEnabled()) {
                    log.warn(String.format(INCORRECT_API_USAGE, affiliateUrlName, applicationUrlName,
                            validationResponse.getApplication().getTp().name()));
                }
            }
            Quiz quiz = quizService.findPublishedDefaultQuiz(language);

            if (quiz != null) {
                result = mapper.map(quiz, QuizDto.class);
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return result;
    }

    @RequestMapping(value = "/api/quiz",
            method = RequestMethod.POST,
            headers = "content-type=text/plain")
    public void answerQuizIECors(InputStream input,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session) throws Exception {


        String content = IOUtils.toString(input, "UTF-8");
        QuizAnswers dto = jsonMapper.readValue(content, QuizAnswers.class);

        QuizAnswersResponse result = answerQuiz(dto, request, response, session);

        response.setContentType("text/plain");
        ServletOutputStream out = response.getOutputStream();

        jsonMapper.writeValue(out, result);
        response.flushBuffer();
    }

    /**
     * Called when a user answers a question in the UI
     *
     * @param dto quizAnswer
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/api/quiz", method = RequestMethod.POST,
            consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public QuizAnswersResponse answerQuiz(@RequestBody QuizAnswers dto,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          HttpSession session) throws Exception {
        QuizAnswersResponse result = new QuizAnswersResponse();

        // validate on annotations
        Set<ConstraintViolation<QuizAnswers>> failures = validator.validate(dto);

        if (!failures.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.setMessage(ApplicationConstants.APPLICATION_MISSING_DATA);
        } else {
            // validate that the caller has the right to retrieve a quiz
            ApiValidationResponse validationResponse = validateRequest(dto.getAffiliateId(), dto.getApplicationId());

            if (validationResponse.getValid()) {
                // track analytics
                handleAnalyticsCapture(request, response, dto.getAffiliateId(), dto.getApplicationId(), dto.getCampaignId());

                // issue a warning in the log if the 3rd party developer is misusing the api
                if (validationResponse.getApplication() != null && !validationResponse.getApplication().getTp().equals(ApplicationType.QUIZ)) {
                    if (log.isWarnEnabled()) {
                        log.warn(String.format(INCORRECT_API_USAGE, dto.getAffiliateId(), dto.getApplicationId(),
                                validationResponse.getApplication().getTp().name()));
                    }
                }

                // Check for user as defined by email address from the Quiz API call
                // Otherwise use the logged in principal or transient user to save the quiz results
                User user = null;
                if (StringUtils.isNotEmpty(dto.getEmail())) {
                    user = userService.findUserByEmail(dto.getEmail());

                    // Override the user tracking code if not logged in
                    // So that the quiz registration will be tracked correctly
                    if (user != null) {
                        if (SpringSecurityHelper.getSecurityContextPrincipal() == null) {
                            session.setAttribute(WebConstants.SESSION_USER_CODE, user.getCd());
                        }
                    } else {
                        // User could not be found for the email address... this is an API error
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        result.setMessage(ApplicationConstants.APPLICATION_MISSING_USER);
                    }
                } else {
                    user = retrieveUserFromPrincipalOrSession(session);
                }

                if (user != null && dto.getList() != null && !dto.getList().isEmpty()) {
                    // answer the questions
                    productEngineService.answerQuestions(user.getCd(), dto);

                    // log analytics
                    userTrackerService.trackQuizComplete((String)session.getAttribute(WebConstants.SESSION_USER_CODE), dto.getQuizUrlName(), dto.getApplicationId(), dto.getAffiliateId(), InteractionType.API);
                    EventHelper.post(new QuizFinishEvent(
                            user,
                            validationResponse.getAffiliateAccount(),
                            validationResponse.getApplication())
                    );
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.setMessage(ApplicationConstants.APPLICATION_NOT_AUTHENTICATED);
            }

            result.setMessage(WebConstants.SUCCESS);
        }

        return result;
    }

    @RequestMapping( value="/api/quiz/content/{staticContentUrlName}", method = RequestMethod.GET,
                     produces = "application/json")
    @ResponseBody
    public StaticContentDto renderStaticContent(@PathVariable("staticContentUrlName") String staticContentUrlName,
                                                @RequestParam("affiliateId") String affiliateUrlName,
                                                @RequestParam("applicationId") String applicationUrlName,
                                                @RequestParam(value = "campaignId", required = false) String campaignId,
                                                @RequestParam("returned") boolean returned,
                                                HttpServletRequest request,
                                                HttpServletResponse response,
                                                 HttpSession session) {

        StaticContentDto result = null;

        // validate that the caller has the right to retrieve a quiz
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            // track analytics
            handleAnalyticsCapture(request, response, affiliateUrlName, applicationUrlName, campaignId);

            QuizStaticContentRequest quizStaticContentRequest = new QuizStaticContentRequest();
            quizStaticContentRequest.setUser(retrieveUserFromPrincipalOrSession(session));
            quizStaticContentRequest.setAffiliate(validationResponse.getAffiliateAccount());
            quizStaticContentRequest.setApplication(validationResponse.getApplication());
            quizStaticContentRequest.setReturned(returned);
            quizStaticContentRequest.setRlnm(staticContentUrlName);

            StaticContent sc = quizService.renderStaticContent(quizStaticContentRequest);
            if (sc == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                result = mapper.map(sc, StaticContentDto.class);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return result;
    }

    @RequestMapping(value = "/api/quiz/test", method = RequestMethod.GET)
    public String quizTestHarness(@RequestParam("affiliateId") String affiliateUrlName,
                                  @RequestParam("applicationId") String applicationUrlName,
                                  Model model) throws Exception {
        model.addAttribute("affiliate", affiliateUrlName);
        model.addAttribute("application", applicationUrlName);

        return "api.quiz.test";
    }

    @RequestMapping(value = "/api/quiz/metrics/step.gif", method = RequestMethod.GET)
    public void trackAnswer(@RequestParam("_lelacd") String userCode,
                            @RequestParam("affiliateId") String affiliateUrlName,
                            @RequestParam("applicationId") String applicationUrlName,
                            @RequestParam(value = "campaignId", required = false) String campaignId,
                            @RequestParam("stepId") String stepUrlName,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session) throws Exception {

        // validate that the caller has the right to retrieve a quiz
        User user = userService.findUserByCode(userCode);
        if (user == null) {
            user = retrieveUserFromPrincipalOrSession(session);
        }

        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);
        if (validationResponse.getValid()) {

            // Answer Event
            EventHelper.post(new QuizStepFinishEvent(
                    user,
                    validationResponse.getAffiliateAccount(),
                    validationResponse.getApplication(),
                    stepUrlName)
            );

            new TrackingPixel().writeToResponse(response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
