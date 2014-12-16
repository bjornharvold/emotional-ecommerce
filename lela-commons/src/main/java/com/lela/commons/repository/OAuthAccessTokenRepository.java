/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.OAuthAccessToken;
import com.lela.domain.enums.OauthAccessTokenType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 9:56 AM
 * Responsibility:
 */
public interface OAuthAccessTokenRepository extends CrudRepository<OAuthAccessToken, ObjectId> {
    @Query("{ 'tknd' : ?0, 'tp' : ?1}")
    OAuthAccessToken findByTokenIdAndType(String tokenValue, OauthAccessTokenType type);

    @Query("{ 'rfrshtkn' : ?0, 'tp' : ?1}")
    OAuthAccessToken findByRefreshToken(String refreshToken, OauthAccessTokenType type);
}
