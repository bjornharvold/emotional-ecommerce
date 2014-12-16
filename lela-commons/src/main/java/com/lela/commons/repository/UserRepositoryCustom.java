package com.lela.commons.repository;

import com.lela.domain.document.User;
import com.lela.domain.dto.user.UserSearchQuery;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface UserRepositoryCustom {
    List<User> findUsers(Integer page, Integer maxResults, List<String> fields);
    List<User> findByCodes(List<String> userCodes, List<String> fields);

    List<User> findUsersCreatedBetween(Date beginDate, Date endDate, Integer page, Integer maxResults, List<String> fields);
}
