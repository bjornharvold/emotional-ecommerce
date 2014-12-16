/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.ingest.web.controller.ingest;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.jobs.AvailableJobs;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.IngestJobDetail;
import com.lela.commons.jobs.JobListing;
import com.lela.commons.jobs.JobParameter;
import com.lela.commons.jobs.JobSummary;
import com.lela.domain.document.JobKey;
import com.lela.commons.service.IngestJobService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.dto.job.JobRunParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 10:40 AM
 * Responsibility: This controller is responsible for ingesting content that is coming from Talend feeds
 */
@Controller
public class IngestionJobController {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(IngestionJobController.class);

    //~--- fields -------------------------------------------------------------

    private final IngestJobService ingestJobService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    @Autowired
    public IngestionJobController(IngestJobService ingestJobService) {
        this.ingestJobService = ingestJobService;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String getJobList(Model model) {

        AvailableJobs jobs = ingestJobService.getAvailableJobs();
        model.addAttribute(WebConstants.INGEST_JOB_LIST, jobs);

        return "ingest.job.list";
    }

    @RequestMapping(value="/ingest/{type}/job/{jobName}/parameters", method = RequestMethod.POST)
    public String parameters(@PathVariable("type") String type,
                             @PathVariable("jobName") String jobName,
                             Model model) {

        JobListing listing = ingestJobService.getJobListing(new JobKey(jobName));
        model.addAttribute("job", listing);

        return "ingest.job.parameters";
    }

    @RequestMapping(value="/ingest/{type}/job/{jobName}/run", method = RequestMethod.POST)
    public String runJob(@PathVariable("type") String type,
                         @PathVariable("jobName") String jobName,
                         JobRunParameters parameters,
                         Model model,
                         BindingResult errors,
                         RedirectAttributes redirectAttributes) {

        // Get the job and check for required parameters
        JobKey jobKey = new JobKey(jobName);
        JobListing listing = ingestJobService.getJobListing(jobKey);
        if (listing.getParameters() != null) {
            for (JobParameter parameter : listing.getParameters()) {
                if (parameter.isRequired() && (!parameters.getParams().containsKey(parameter.getName()) || !StringUtils.hasText(parameters.getParams().get(parameter.getName())) )) {
                    errors.addError(new FieldError("parameters", parameter.getName(), "ingest.error.parameter.required"));
                }
            }
        }

        if (errors.hasErrors()) {
            model.addAttribute("job", listing);
            return "ingest.job.parameters";
        }

        if (ingestJobService.executeJob(jobKey, parameters.getParams())) {
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, "Job started: " + jobName);
            return "redirect:/ingest/" + type + "/job/" + jobName + "/context";
        } else {
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, "Job already running: " + jobName);
            return "redirect:/";
        }
    }

    @RequestMapping(value="/ingest/{type}/job/{jobName}/detail", method = RequestMethod.GET)
    public String jobDetails(@PathVariable("type") String type,
                             @PathVariable("jobName") String jobName,
                             Model model) {

        IngestJobDetail job = ingestJobService.getJob(new JobKey(jobName));
        model.addAttribute("job", job);

        model.addAttribute("jobName", jobName);

        return type + ".job.details";
    }

    @RequestMapping(value="/ingest/{type}/job/{jobName}/context", method = RequestMethod.GET)
    public String jobContext(@PathVariable("type") String type,
                             @PathVariable("jobName") String jobName,
                             Model model) {

        JobSummary summary = ingestJobService.getSummary(new JobKey(jobName));
        model.addAttribute("context", summary);

        model.addAttribute("jobName", jobName);

        model.addAttribute("refreshDate", new java.util.Date());

        return type + ".job.context";
    }
}
