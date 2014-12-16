package com.lela.data.web.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lela.commons.service.TriggerService;
import com.lela.domain.document.CronTrigger;


@Component
public class TriggerValidator implements Validator {

	private TriggerService triggerService;
	
    @Autowired
    public TriggerValidator(TriggerService triggerService) {
        this.triggerService = triggerService;
    }
	@Override
	public boolean supports(Class<?> clazz) {
		return CronTrigger.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
        CronTrigger trigger = (CronTrigger) target;

        if (!errors.hasErrors()) {
            // check for email availability
            CronTrigger t = triggerService.findTriggerByUrlName(trigger.getRlnm());

            if (t != null) {
                if (StringUtils.equals(t.getRlnm(), trigger.getRlnm()) && !t.getId().equals(trigger.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.trigger.url.name", null, "Url name is already taken");
                }
            }
        }
		
	}


}
