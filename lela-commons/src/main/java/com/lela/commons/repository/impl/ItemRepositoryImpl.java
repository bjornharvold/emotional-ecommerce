/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.commons.repository.ItemRepositoryCustom;
import com.lela.domain.dto.FunctionalFilters;
import com.lela.util.utilities.number.NumberUtils;
import com.mongodb.WriteResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 1/6/12
 * Time: 2:47 AM
 * Responsibility:
 */
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateAvailableStoresOnItem(Item item) {
        WriteResult result = mongoTemplate.updateFirst(query(where("id").is(item.getId())),
                new Update().set("strs", item.getStrs()), Item.class);
    }

    @Override
    public List<Item> findItemsByCategoryUrlName(String categoryUrlName, int page, int maxResults, List<String> extraFields) {
        Query query = query(where("ctgry.rlnm").is(categoryUrlName));

        // minimum fields
        query.fields().include("nm").include("rlnm").include("srlnm");

        if (extraFields != null && !extraFields.isEmpty()) {
            for (String extraField : extraFields) {
                query.fields().include(extraField);
            }
        }

        query.skip(page * maxResults);
        query.limit(maxResults);

        return mongoTemplate.find(query, Item.class);
    }

    @Override
    public List<Item> findItemsByUrlName(List<String> itemUrlNames) {
        return mongoTemplate.find(query(where("rlnm").in(itemUrlNames)), Item.class);
    }

    @Override
    public List<Item> findByCategoryUrlName(String categoryUrlName) {
        // return those categories that match the url name and that are available for purchase
        Query query = query(where("ctgry.rlnm").is(categoryUrlName).and("vlbl").ne(false));

        return mongoTemplate.find(query, Item.class);
    }

    @Override
    public Long findCountByCategoriesAndFilters(String categoryUrlName, List<FunctionalFilter> functionalFilters, Map<String, Map<String, String>> filters) {
        Query query = query(where("vlbl").ne(false).and("ctgry.rlnm").is(categoryUrlName));

        if (filters != null && !filters.isEmpty()) {

            List<Criteria> criterion = new ArrayList<Criteria>();

            for (Map.Entry<String, Map<String, String>> entry : filters.entrySet()) {
                String filterKey = entry.getKey();

                if (functionalFilters != null && !functionalFilters.isEmpty()) {
                    for (FunctionalFilter ff : functionalFilters) {
                        if (StringUtils.equals(filterKey, ff.getKy())) {
                            if (!entry.getValue().isEmpty()) {
                                switch (ff.getTp()) {
                                    case DYNAMIC_RANGE:
                                        criterion.addAll(filterOnDynamicRange(ff, entry.getValue()));
                                        break;
                                    case MULTIPLE_CHOICE_SINGLE_ANSWER:
                                    case MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND:
                                        criterion.addAll(filterOnMultipleChoiceMultipleAnswerAND(ff, entry.getValue()));
                                        break;
                                    case MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR:
                                        criterion.add(filterOnMultipleChoiceMultipleAnswerOR(ff, entry.getValue()));
                                        break;
                                    case BRAND:
                                        query.addCriteria(filterOnBrand(ff, entry.getValue()));
                                        break;
                                    case STORE:
                                        query.addCriteria(filterOnStore(ff, entry.getValue()));
                                }
                            }
                        }
                    }
                }
            }

            if (!criterion.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criterion.toArray(new Criteria[criterion.size()])));
            }

        }

        return mongoTemplate.count(query, Item.class);
    }

    private List<Criteria> filterOnMultipleChoiceMultipleAnswerAND(FunctionalFilter ff, Map<String, String> options) {
        List<Criteria> criterion = new ArrayList<Criteria>(options.size());

        for (String key : options.keySet()) {
            criterion.add(new Criteria("attrs").elemMatch(where("ky").is(key).and("vl").is(options.get(key))));
        }

        return criterion;
    }

    private Criteria filterOnMultipleChoiceMultipleAnswerOR(FunctionalFilter ff, Map<String, String> options) {
        List<Criteria> criterion = new ArrayList<Criteria>(options.size());

        for (String key : options.keySet()) {
            criterion.add(new Criteria("attrs").elemMatch(where("ky").is(key).and("vl").is(options.get(key))));
        }

        return new Criteria().orOperator(criterion.toArray(new Criteria[criterion.size()]));
    }

    /**
     * Creates a criteria object that creates a range
     *
     * @param ff      ff
     * @param options options
     * @return criteria
     */
    private List<Criteria> filterOnDynamicRange(FunctionalFilter ff, Map<String, String> options) {
        List<Criteria> result = new ArrayList<Criteria>(2);
        Double low = NumberUtils.safeDouble(options.get(ApplicationConstants.DYNAMIC_RANGE_LOW));
        Double high = NumberUtils.safeDouble(options.get(ApplicationConstants.DYNAMIC_RANGE_HIGH));

        Criteria highC = new Criteria("attrs").elemMatch(where("ky").is(ff.getDtky()).and("vl").lte(high));
        Criteria lowC = new Criteria("attrs").elemMatch(where("ky").is(ff.getDtky()).and("vl").gte(low));

        result.add(highC);
        result.add(lowC);

        return result;
    }

    /**
     * Create a criteria object to match on a specific owner
     *
     * @param ff      ff
     * @param options options
     * @return criteria
     */
    private Criteria filterOnBrand(FunctionalFilter ff, Map<String, String> options) {
        return where("wnr.rlnm").in(options.keySet());
    }

    /**
     * Create a criteria object to match on a specific store
     *
     * @param ff      ff
     * @param options options
     * @return criteria
     */
    private Criteria filterOnStore(FunctionalFilter ff, Map<String, String> options) {
        return where("strs").elemMatch(where("rlnm").in(options.keySet()));
    }
}
