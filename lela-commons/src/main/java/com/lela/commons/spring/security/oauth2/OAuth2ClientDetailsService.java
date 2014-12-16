/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.spring.security.oauth2;

import com.lela.commons.service.OAuthService;
import com.lela.domain.document.OAuthClientDetail;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.BaseClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/11
 * Time: 2:44 PM
 * Responsibility:
 */
public class OAuth2ClientDetailsService implements ClientDetailsService {
    private final OAuthService oAuthService;

    public OAuth2ClientDetailsService(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws OAuth2Exception {
        OAuthClientDetail details = oAuthService.findOAuthClientDetailsByClientId(clientId);

        if (details == null) {
            throw new InvalidClientException("Client not found: " + clientId);
        }

        BaseClientDetails result = new BaseClientDetails();
        result.setClientId(details.getClientId());
        result.setClientSecret(details.getClientSecret());
        result.setAuthorizedGrantTypes(details.getAuthorizedGrantTypes());
        result.setResourceIds(details.getResourceIds());
        result.setScope(details.getScope());
        result.setAuthorities(AuthorityUtils.createAuthorityList(details.getAuthorities().toArray(new String[details.getAuthorities().size()])));

        return result;
    }
}
