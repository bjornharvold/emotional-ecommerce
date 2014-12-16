/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.spring.security.oauth2;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.OAuthAccessToken;
import com.lela.domain.enums.OauthAccessTokenType;
import com.lela.commons.repository.OAuthAccessTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;


//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 9:40 AM
 * Responsibility:
 */
public class OAuth2ProviderTokenStore implements TokenStore {
    private final static Logger log = LoggerFactory.getLogger(OAuth2ProviderTokenStore.class);
    private final OAuthAccessTokenRepository repository;

    public OAuth2ProviderTokenStore(OAuthAccessTokenRepository repository) {
        this.repository = repository;
    }

    /**
     * Method description
     *
     * @param tokenValue tokenValue
     * @return Return value
     */
    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken result = null;

        OAuthAccessToken custom = repository.findByTokenIdAndType(tokenValue, OauthAccessTokenType.ACCESS_TOKEN);

        if (custom != null && custom.getTkn() != null) {
            result = SerializationUtils.deserialize(custom.getTkn());
        } else {
            log.warn("Couldn't retrieve oauth2 token with tokenValue: " + tokenValue);
        }

        return result;
    }

    /**
     * Method description
     *
     * @param token token
     * @return Return value
     */
    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication result = null;

        OAuthAccessToken custom = repository.findByTokenIdAndType(token.getValue(), OauthAccessTokenType.ACCESS_TOKEN);

        if (custom != null && custom.getThntctn() != null) {
            result = SerializationUtils.deserialize(custom.getThntctn());
        } else {
            log.warn("Couldn't retrieve oauth2 token with tokenValue: " + token.getValue());
        }

        return result;
    }

    /**
     * Method description
     *
     * @param token token
     * @return Return value
     */
    @Override
    public OAuth2Authentication readAuthentication(ExpiringOAuth2RefreshToken token) {
        OAuth2Authentication result = null;

        OAuthAccessToken custom = repository.findByTokenIdAndType(token.getValue(), OauthAccessTokenType.REFRESH_TOKEN);

        if (custom != null && custom.getThntctn() != null) {
            result = SerializationUtils.deserialize(custom.getThntctn());
        } else {
            log.warn("Couldn't retrieve oauth2 token with tokenValue: " + token.getValue());
        }

        return result;
    }

    /**
     * Method description
     *
     * @param tokenValue tokenValue
     * @return Return value
     */
    @Override
    public ExpiringOAuth2RefreshToken readRefreshToken(String tokenValue) {
        ExpiringOAuth2RefreshToken result = null;

        OAuthAccessToken custom = repository.findByTokenIdAndType(tokenValue, OauthAccessTokenType.REFRESH_TOKEN);

        if (custom != null && custom.getTkn() != null) {
            result = SerializationUtils.deserialize(custom.getTkn());
        } else {
            log.warn("Couldn't retrieve oauth2 refresh token with tokenValue: " + tokenValue);
        }

        return result;
    }

    /**
     * Method description
     *
     * @param tokenValue tokenValue
     */
    @Override
    public void removeAccessToken(String tokenValue) {
        OAuthAccessToken custom = repository.findByTokenIdAndType(tokenValue, OauthAccessTokenType.ACCESS_TOKEN);

        if (custom != null) {
            repository.delete(custom);
        } else {
            log.warn("Doesn't exist. Couldn't delete oauth2 token with tokenValue: " + tokenValue);
        }
    }

    /**
     * Method description
     *
     * @param refreshToken refreshToken
     */
    @Override
    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        OAuthAccessToken custom = repository.findByRefreshToken(refreshToken, OauthAccessTokenType.ACCESS_TOKEN);

        if (custom != null) {
            repository.delete(custom);
        } else {
            log.warn("Doesn't exist. Couldn't delete oauth2 token using refreshToken: " + refreshToken);
        }
    }

    /**
     * Method description
     *
     * @param tokenValue tokenValue
     */
    @Override
    public void removeRefreshToken(String tokenValue) {
        OAuthAccessToken custom = repository.findByTokenIdAndType(tokenValue, OauthAccessTokenType.REFRESH_TOKEN);

        if (custom != null) {
            repository.delete(custom);
        } else {
            log.warn("Doesn't exist. Couldn't delete oauth2 refresh token with tokenValue: " + tokenValue);
        }
    }

    /**
     * Method description
     *
     * @param token          token
     * @param authentication authentication
     */
    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }

        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
        oAuthAccessToken.setTknd(token.getValue());
        oAuthAccessToken.setTkn(SerializationUtils.serialize(token));
        oAuthAccessToken.setThntctn(SerializationUtils.serialize(authentication));
        oAuthAccessToken.setRfrshtkn(refreshToken);
        oAuthAccessToken.setTp(OauthAccessTokenType.ACCESS_TOKEN);

        repository.save(oAuthAccessToken);
    }

    /**
     * Method description
     *
     * @param refreshToken   refreshToken
     * @param authentication authentication
     */
    @Override
    public void storeRefreshToken(ExpiringOAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        OAuthAccessToken oAuthRefreshToken = new OAuthAccessToken();
        oAuthRefreshToken.setTknd(refreshToken.getValue());
        oAuthRefreshToken.setTkn(SerializationUtils.serialize(refreshToken));
        oAuthRefreshToken.setThntctn(SerializationUtils.serialize(authentication));
        oAuthRefreshToken.setTp(OauthAccessTokenType.REFRESH_TOKEN);

        repository.save(oAuthRefreshToken);
    }
}
