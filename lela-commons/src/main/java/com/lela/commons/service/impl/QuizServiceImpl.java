/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.lela.commons.LelaException;
import com.lela.commons.comparator.AnswerComparator;
import com.lela.commons.comparator.QuizStepComparator;
import com.lela.commons.comparator.QuizStepEntryComparator;
import com.lela.commons.repository.QuestionRepository;
import com.lela.commons.repository.QuizRepository;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Answer;
import com.lela.domain.document.Application;
import com.lela.domain.document.QQuiz;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.QuizStep;
import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.quiz.AnswerEntry;
import com.lela.domain.dto.quiz.QuizStaticContentRequest;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:31 PM
 * Responsibility:
 */
@Service("quizService")
public class QuizServiceImpl extends AbstractCacheableService implements QuizService {
    private final static Logger log = LoggerFactory.getLogger(QuizServiceImpl.class);

    private final UserService userService;
    private final StaticContentService staticContentService;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final FileStorage fileStorage;
    private final AffiliateService affiliateService;
    private final ApplicationService applicationService;

    @Autowired
    public QuizServiceImpl(CacheService cacheService,
                           UserService userService,
                           StaticContentService staticContentService,
                           QuestionRepository questionRepository,
                           QuizRepository quizRepository,
                           @Qualifier("quizImageFileStorage") FileStorage fileStorage,
                           AffiliateService affiliateService,
                           ApplicationService applicationService) {
        super(cacheService);
        this.userService = userService;
        this.staticContentService = staticContentService;
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.fileStorage = fileStorage;
        this.affiliateService = affiliateService;
        this.applicationService = applicationService;
    }

    @Override
    public Question findQuestionByUrlName(String urlName) {
        Question result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.QUESTION_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Question) {
            result = (Question) wrapper.get();
        } else {
            result = questionRepository.findByUrlName(urlName);

            if (result != null) {

                // sort answers
                if (result.getNswrs() != null && !result.getNswrs().isEmpty()) {
                    Collections.sort(result.getNswrs(), new AnswerComparator());
                }

                putInCache(ApplicationConstants.QUESTION_CACHE, urlName, result);
            }
        }

        return result;
    }

    /**
     * Retrieves all questions and only the fields that were specified
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public List<Question> findAllQuestions() {
        List<String> fields = new ArrayList<String>(3);
        fields.add("rlnm");
        fields.add("nm");
        fields.add("tp");

        return questionRepository.findAll(fields);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public Page<Quiz> findQuizzes(Integer page, Integer maxResults) {
        return quizRepository.findAll(new PageRequest(page, maxResults));
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public List<Quiz> findQuizzes(List<String> fields) {
        return quizRepository.findAll(fields);
    }

    @Override
    public Quiz findPublishedQuizByUrlName(String urlName) {
        Quiz result = findQuizByUrlName(urlName);

        if (result != null && result.getPblshd()) {
            return result;
        } else {
            if (log.isWarnEnabled()) {
                log.warn(String.format("Quiz: %s either doesn't exist or it is not yet published", urlName));
            }
        }

        return null;
    }

    @Override
    public Quiz findQuizByUrlName(String urlName) {
        Quiz result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.QUIZ_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Quiz) {
            result = (Quiz) wrapper.get();
        } else {
            result = quizRepository.findByUrlName(urlName);

            if (result != null) {
                // sort answers
                processQuizSteps(result, null, null);

                putInCache(ApplicationConstants.QUIZ_CACHE, urlName, result);
            }
        }

        return result;
    }
    
    /**
     * Use this method when the quiz is associated to a static content that is a velocity template
     * @param urlName
     * @param affiliateAccountUrl
     * @param applicationUrl
     * @return
     */
    @Override
    public Quiz findQuizByUrlName(String urlName, String affiliateAccountUrl, String applicationUrl) {
        Quiz result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.QUIZ_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Quiz) {
            result = (Quiz) wrapper.get();
        } else {
            result = quizRepository.findByUrlName(urlName);

            if (result != null) {
                // sort answers
                processQuizSteps(result, affiliateAccountUrl, applicationUrl);

                putInCache(ApplicationConstants.QUIZ_CACHE, urlName, result);
            }
        }

        return result;
    }   

    @Override
    public Quiz findPublishedDefaultQuiz(String language) {
        Quiz result = findDefaultQuiz(language);

        if (result != null && result.getPblshd()) {
            return result;
        }

        return null;
    }

    @Override
    public Quiz findDefaultQuiz(String language) {
        Quiz result = null;

        if (StringUtils.isNotBlank(language)) {
            Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.QUIZ_CACHE, ApplicationConstants.DEFAULT_QUIZ + "_" + language);

            if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Quiz) {
                result = (Quiz) wrapper.get();
            } else {
                result = quizRepository.findOne(QQuiz.quiz.dflt.eq(true).and(QQuiz.quiz.lng.eq(language)));

                if (result != null) {
                    // sort answers
                    processQuizSteps(result, null, null);

                    putInCache(ApplicationConstants.QUIZ_CACHE, ApplicationConstants.DEFAULT_QUIZ + "_" + language, result);
                }
            }
        }

        return result;
    }

    @Override
    public StaticContent renderStaticContent(QuizStaticContentRequest request) {

        // Set up additional parameters for the velocity template
        if (request.getUser() != null) {
            request.setUs(userService.findUserSupplement(request.getUser().getCd()));
        }

        StaticContent result = staticContentService.findStaticContentByUrlName(request.getRlnm(), request);
        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_QUIZ')")
    @Override
    public Quiz saveQuiz(Quiz quiz) {
        // if the default flag is set to true, we have to set all other default flags to false
        if (quiz.getDflt()) {
            // set all quiz default flag to false
            quizRepository.setDefaultFlagOnCollection(quiz.getLng(), false);

            // evict the default quiz that might already be in the cache
            removeFromCache(CacheType.QUIZ, ApplicationConstants.DEFAULT_QUIZ + "_" + quiz.getLng());
        }

        Quiz result = quizRepository.save(quiz);

        // Remove from cache
        removeFromCache(CacheType.QUIZ, result.getRlnm());

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_QUIZ')")
    @Override
    public void removeQuiz(String urlName) {
        Quiz quiz = findQuizByUrlName(urlName);

        if (quiz != null) {
            quizRepository.delete(quiz);

            // Remove from cache
            removeFromCache(CacheType.QUIZ, quiz.getRlnm());

            if (quiz.getDflt()) {
                // evict the default quiz that might already be in the cache
                removeFromCache(CacheType.QUIZ, ApplicationConstants.DEFAULT_QUIZ + "_" + quiz.getLng());
            }
        }
    }

    /**
     * Method description
     *
     * @param rlnm question
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Question removeQuestion(String rlnm) {
        Question question = questionRepository.findByUrlName(rlnm);
        if (question != null) {
            questionRepository.delete(question);

            // Remove from cache
            removeFromCache(CacheType.QUESTION, question.getRlnm());
        }

        return question;
    }

    /**
     * Method description
     *
     * @param question question
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Question saveQuestion(Question question) {
        Question result = questionRepository.save(question);

        // Remove from cache
        removeFromCache(CacheType.QUESTION, question.getRlnm());

        return result;
    }

    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Question> saveQuestions(List<Question> list) {
        List<Question> result = (List<Question>) questionRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (Question item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.QUESTION, cacheKeys);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_QUIZ')")
    @Override
    public void saveQuizStep(QuizStep step) {
        Quiz quiz = findQuizByUrlName(step.getQrlnm());

        if (quiz != null) {

            boolean saved = quiz.addQuizStep(step);

            if (saved) {
                saveQuiz(quiz);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_QUIZ')")
    @Override
    public void saveQuizStepQuestion(QuizStepEntry question) {
        Quiz quiz = findQuizByUrlName(question.getQrlnm());

        if (quiz != null) {

            boolean saved = quiz.addQuizStepEntry(question);

            if (saved) {
                saveQuiz(quiz);
            }
        }

    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public Page<Question> findQuestions(Integer page, Integer maxResults) {
        return questionRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "ldt"));
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public QuizStep findQuizStep(String quizUrlName, String quizStepId) {
        Quiz quiz = findQuizByUrlName(quizUrlName);
        QuizStep result = null;

        if (quiz != null) {
            result = quiz.findQuizStepById(quizStepId);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public QuizStepEntry findQuizStepQuestion(String quizUrlName, String quizStepId, String questionId) {
        Quiz quiz = findQuizByUrlName(quizUrlName);
        QuizStepEntry result = null;

        if (quiz != null) {
            result = quiz.findQuizStepEntry(quizStepId, questionId);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_QUIZ')")
    @Override
    public void removeQuizStep(String quizUrlName, String quizStepId) {
        Quiz quiz = findQuizByUrlName(quizUrlName);

        if (quiz != null) {
            boolean removed = quiz.removeQuizStep(quizStepId);

            if (removed) {
                saveQuiz(quiz);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_QUIZ')")
    @Override
    public void removeQuizStepQuestion(String quizUrlName, String quizStepId, String questionId) {
        Quiz quiz = findQuizByUrlName(quizUrlName);

        if (quiz != null) {
            boolean removed = quiz.removeQuizStepEntry(quizStepId, questionId);

            if (removed) {
                saveQuiz(quiz);
            }

        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_QUIZ')")
    @Override
    public Answer findAnswer(String questionUrlName, String answerId) {
        Question question = findQuestionByUrlName(questionUrlName);
        Answer result = null;

        if (question != null) {
            result = question.findAnswer(answerId);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_QUIZ')")
    @Override
    public void saveAnswerEntry(AnswerEntry ae) {
        Question question = findQuestionByUrlName(ae.getQrlnm());

        if (question != null) {

            try {
                // if there is a multipart file on the blog it should get uploaded to S3
                if (ae.getMultipartFile() != null && !ae.getMultipartFile().isEmpty()) {
                    MultipartFile file = ae.getMultipartFile();
                    FileData data = new FileData(file.getOriginalFilename(), file.getBytes(), file.getContentType());
                    String url = fileStorage.storeFile(data);
                    ae.setMg(url);
                }
            } catch (IOException e) {
                throw new LelaException(e.getMessage(), e);
            }

            // clean up mtvtrs
            cleanMotivators(ae);

            boolean saved = question.addAnswer(new Answer(ae));

            if (saved) {
                saveQuestion(question);
            }
        }
    }

    private void cleanMotivators(AnswerEntry ae) {
        // we want to clean up everything that has empty or 0 values
        List<String> toRemove = null;

        if (ae.getMtvtrs() != null && !ae.getMtvtrs().isEmpty()) {
            for (String key : ae.getMtvtrs().keySet()) {
                if (ae.getMtvtrs().get(key) == 0) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<String>();
                    }
                    toRemove.add(key);
                }
            }

            if (toRemove != null && !toRemove.isEmpty()) {
                for (String key : toRemove) {
                    ae.getMtvtrs().remove(key);
                }
            }
        }

    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_QUIZ')")
    @Override
    public void removeAnswer(String questionUrlName, String answerId) {
        Question question = findQuestionByUrlName(questionUrlName);

        if (question != null) {
            boolean removed = question.removeAnswer(answerId);

            if (removed) {
                saveQuestion(question);
            }
        }
    }
    
   

    private void processQuizSteps(Quiz quiz, String affiliateAccountUrl, String applicationUrl) {
        if (quiz != null) {
            // sort collections
            if (quiz.getStps() != null && !quiz.getStps().isEmpty()) {

                Collections.sort(quiz.getStps(), new QuizStepComparator());

                for (QuizStep step : quiz.getStps()) {
                    if (step.getNtrs() != null && !step.getNtrs().isEmpty()) {
                        Collections.sort(step.getNtrs(), new QuizStepEntryComparator());

                        switch (step.getTp()) {
                            case QUESTION:
                                // load up questions into the quiz so we have a complete data set
                                loadQuestion(step);
                                break;
                            case SPLASH:
                                // load up static content by url name
                                loadStaticContent(step, affiliateAccountUrl, applicationUrl);
                                break;
                        }
                    }
                }
            }
        }
    }

    private void loadStaticContent(QuizStep step, String affiliateAccountUrl, String applicationUrl) {
        for (QuizStepEntry entry : step.getNtrs()) {
            StaticContent q = staticContentService.findStaticContentByUrlName(entry.getRlnm());
            if (q != null){
	            if (q.isVlctytmplt()){
	            	Assert.notNull(affiliateAccountUrl, "An affiliateAccountUrl must be specified for a velocified static content");
	            	Assert.notNull(applicationUrl, "An applicationUrl must be specified for a velocified static content");
	            	
	                QuizStaticContentRequest quizStaticContentRequest = new QuizStaticContentRequest();
	                if (SpringSecurityHelper.getSecurityContextPrincipal() != null){ //Don't care about transient users?
	                	quizStaticContentRequest.setUser(SpringSecurityHelper.getSecurityContextPrincipal().getUser());
	                }
	                
	                AffiliateAccount aa = affiliateService.findActiveAffiliateAccountByUrlName(affiliateAccountUrl);
	                quizStaticContentRequest.setAffiliate(aa);
	                Application app = applicationService.findApplicationByUrlName(applicationUrl);
	                quizStaticContentRequest.setApplication(app);
	                quizStaticContentRequest.setReturned(true); //Confirm the value of this
	                quizStaticContentRequest.setRlnm(q.getRlnm());
	                StaticContent velocifiedStaticContent = renderStaticContent(quizStaticContentRequest);
	                entry.setStaticContent(velocifiedStaticContent);
	            } else {
	            	entry.setStaticContent(q);
	            }
            }
        }
    }

    private void loadQuestion(QuizStep step) {
        for (QuizStepEntry entry : step.getNtrs()) {
            Question q = findQuestionByUrlName(entry.getRlnm());

            if (q != null) {
                entry.setQuestion(q);
            }
        }
    }
}
