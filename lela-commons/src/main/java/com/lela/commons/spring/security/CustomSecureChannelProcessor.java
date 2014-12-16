package com.lela.commons.spring.security;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.channel.SecureChannelProcessor;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/17/12
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomSecureChannelProcessor extends SecureChannelProcessor implements ApplicationContextAware {

    ApplicationContext ctx;

    AffiliateService affiliateService;

    public void decide(FilterInvocation invocation, Collection<ConfigAttribute> config) throws IOException, ServletException {
        Assert.isTrue((invocation != null) && (config != null), "Nulls cannot be provided");

        String serverName = invocation.getHttpRequest().getServerName();

        /**
         * If you're looking at this code you should also be looking at CustomInsecureChannelProcessor.
         *
         * If you don't do things right you will get a infinite redirect loop from https -> http
         */

        AffiliateAccount domain = getAffiliateService().findAffiliateAccountByDomain(serverName);

        for (ConfigAttribute attribute : config) {
            if (supports(attribute)) {
                if (!invocation.getHttpRequest().isSecure() && (domain == null || ( domain != null && domain.getSsl() == Boolean.TRUE ))) {
                    getEntryPoint().commence(invocation.getRequest(), invocation.getResponse());
                }
            }
        }
    }

    private AffiliateService getAffiliateService()
    {
        if(this.affiliateService == null)
        {
            this.affiliateService = (AffiliateService)ctx.getBean(ChannelPostProcessor.AFFILIATE_SERVICE);
        }
        return this.affiliateService;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
