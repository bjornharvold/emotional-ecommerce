package com.lela.data.web.controller.administration.job;

import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.TriggerService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.TriggerValidator;
import com.lela.domain.document.CronTrigger;

@Controller("triggerController")
public class TriggerController {

	private final  TriggerService triggerService;
	private final TriggerValidator triggerValidator;
	private final MessageSource messageSource;
    @Autowired
    public TriggerController(TriggerService triggerService, TriggerValidator triggerValidator, MessageSource messageSource){
    	this.triggerService = triggerService;
    	this.triggerValidator = triggerValidator;
    	this.messageSource = messageSource;
    }
    
    @RequestMapping(value = "/administration/job/trigger/list", method = RequestMethod.GET)
    public String showTriggers(@RequestParam(required = false, defaultValue = "0") Integer page,
                             @RequestParam(required = false, defaultValue = "10") Integer size,
                             Model model) throws Exception {
        model.addAttribute(WebConstants.TRIGGERS, triggerService.findTriggers(page, size));

        return "trigger.list";
    }
    
    @RequestMapping(value = "/administration/job/trigger", method = RequestMethod.GET)
    public String showTrigger(@RequestParam(value = "urlName", required = false) String urlName,
                            Model model) throws Exception {
        if (StringUtils.isNotBlank(urlName)) {
            CronTrigger t = triggerService.findTriggerByUrlName(urlName);
            if (t != null) {
                model.addAttribute(WebConstants.TRIGGER, t);
            } else {
                t = new CronTrigger();
                t.setRlnm(urlName);
                model.addAttribute(WebConstants.TRIGGER, t);
            }
        } else {
        	 model.addAttribute(WebConstants.TRIGGER, new CronTrigger());
        }
        return "trigger.form";
    }

    @RequestMapping(value = "/administration/job/trigger", method = RequestMethod.POST)
    public String saveTrigger(@Valid  CronTrigger trigger, BindingResult errors,
                            Model model, RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view; 
        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.TRIGGER, trigger);
            view = "trigger.form";
        } else {
            trigger = triggerService.saveTrigger(trigger);
            String message = messageSource.getMessage("trigger.saved.successfully", new String[]{trigger.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
            
            view = "redirect:/administration/job/trigger?urlName=" + trigger.getRlnm();
        }


        return view;
    }

    @RequestMapping(value = "/administration/job/trigger/{urlName}", method = RequestMethod.DELETE)
    public String deleteTrigger(@PathVariable(value = "urlName") String urlName) throws Exception {
        CronTrigger trigger = triggerService.findTriggerByUrlName(urlName);

        if (trigger != null) {
        	triggerService.removeTrigger(trigger.getRlnm());
        }
        return "redirect:/administration/job/trigger/list";
    }
    
//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.setValidator(triggerValidator);
//    }
}
