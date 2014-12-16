/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.json;

import com.lela.commons.jobs.IngestParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 7:09 PM
 */
public class JsonQueryEngine implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(JsonQueryEngine.class);

    private final DataSource etlDataSource;
    private List<String> preExecutionQueries;
    private List<JsonQuery> queries;

    private Map<String, JsonQuery> queryByName = new HashMap<String, JsonQuery>();
    private JdbcTemplate jdbcTemplate;
    private IngestParserContext context;
    private SpelExpressionParser parser;

    public JsonQueryEngine(DataSource etlDataSource) {
        this.etlDataSource = etlDataSource;
        jdbcTemplate = new JdbcTemplate(etlDataSource);

        this.context = new IngestParserContext();
        this.parser = new SpelExpressionParser();

    }

    public JsonQueryContext getQueryContext(Map<String,Object> params) {
        return new JsonQueryContext(this, params);
    }

    public <T> T query(String queryName, Class<T> requiredType, Map<String, Object> params) {
        String sql = getSql(queryName, params);
        log.warn("JSON raw sql: " + sql);

        // Perform the query
        T result = jdbcTemplate.queryForObject(sql, requiredType);
        return result;
    }

    public Map<String, Object> queryForMap(String queryName, Map<String, Object> params) {
        String sql = getSql(queryName, params);
        log.warn("JSON attribs sql: " + sql);

        // Perform the query
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return result;
    }

    public List<Map<String, Object>> queryForList(String queryName, Map<String,Object> params) {
        String sql = getSql(queryName, params);
        log.warn("JSON list sql: " + sql);

        // Perform the query
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result;
    }

    private String getSql(String queryName, Map<String, Object> params) {
        JsonQuery query = queryByName.get(queryName);
        if (query == null) {
            throw new IllegalArgumentException("No query defined for name: " + queryName);
        }

        Expression expression = parser.parseExpression(query.getSql(), context);

        return (String)expression.getValue(new SpelParamsAdapter(params));
    }

    public List<JsonQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<JsonQuery> queries) {
        this.queries = queries;
    }

    public List<String> getPreExecutionQueries() {
        return preExecutionQueries;
    }

    public void setPreExecutionQueries(List<String> preExecutionQueries) {
        this.preExecutionQueries = preExecutionQueries;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (queries != null) {
            queryByName.clear();
            for (JsonQuery query : queries) {
                queryByName.put(query.getName(), query);
            }
        }
    }

    public void executePreExecutionQueries() {
        if (preExecutionQueries != null && !preExecutionQueries.isEmpty()) {
            for (String query : preExecutionQueries) {
                jdbcTemplate.execute(query);
            }
        }
    }

    private class SpelParamsAdapter {
        private Map params;

        private SpelParamsAdapter(Map params) {
            this.params = params;
        }

        public Map getParams() {
            return params;
        }

        public void setParams(Map params) {
            this.params = params;
        }
    }
}
