package com.lela.commons.service;

import com.lela.domain.document.CategoryInitiatorQuery;
import com.lela.domain.document.CategoryInitiatorResult;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryInitiatorService {

    public List<CategoryInitiatorQuery> findAllQuerys();

    public List<CategoryInitiatorQuery> findQuerysForUser(String srid);

    public void save(CategoryInitiatorQuery categoryInitiatorQuery);

    public void save(CategoryInitiatorResult categoryInitiatorResult);

    public CategoryInitiatorQuery getQuery(String id);

    public CategoryInitiatorResult getResult(String id);

    public void deleteQuery(String id);

    public void deleteResult(String id);

    public void run(CategoryInitiatorQuery categoryInitiatorQuery);

    public List<CategoryInitiatorResult> findResults(CategoryInitiatorQuery categoryInitiatorQuery);
}
