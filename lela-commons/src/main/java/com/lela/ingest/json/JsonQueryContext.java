/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.json;

import com.amazonaws.util.json.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 7:09 PM
 */
public class JsonQueryContext {
    private static final Logger log = LoggerFactory.getLogger(JsonQueryContext.class);

    private final JsonQueryEngine engine;
    private final Map<String, Object> params;
    private boolean executedPreExecutionQueries = false;

    public JsonQueryContext(JsonQueryEngine engine, Map<String, Object> params) {
        this.engine = engine;
        this.params = params;
    }

    public String raw(String queryName) {
        checkPreExecution();
        String result = engine.query(queryName, String.class, params);
        return result != null ? result : "";
    }

    public String attribs(String queryName) {
        checkPreExecution();
        return attribs(queryName, null);
    }

    public String attribs(String queryName, Nest nest) {
        checkPreExecution();
        StringBuilder result = new StringBuilder();

        try {
            Map<String, Object> data = engine.queryForMap(queryName, params);
            if (data != null) {
                mapToJson(result, data, null, nest);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new JsonTemplateQueryException("No results for query: " + queryName + " with params: " + params, e);
        }

        return result.toString();
    }

    public String list(String queryName) {
        checkPreExecution();
        return list(queryName, null);
    }

    public String list(String queryName, Nest nest) {
        checkPreExecution();
        StringBuilder result = new StringBuilder();

        try {
            List<Map<String, Object>> list = engine.queryForList(queryName, params);
            if (list!= null && !list.isEmpty()) {

                listToJson(result, list, null, nest);
            }
        } catch (Exception e) {
            throw new JsonTemplateQueryException("Exception on query: " + queryName + " with params: " + params, e);
        }

        return result.toString();
    }

    public String attribs(String queryName, final GroupBy groupBy, Nest nest) {

        checkPreExecution();
        StringBuilder result = new StringBuilder();

        // Run the query as a list
        ListMultimap<Map<String, Object>, Map<String, Object>> grouped = queryAndGroup(queryName, groupBy);

        if (grouped != null && !grouped.isEmpty()) {

            // There should be only one key in the multimap... if there are more this is an error
            if (grouped.asMap().size() > 1) {
                throw new JsonTemplateQueryException(String.format("Attribs group query '%s' returned more than one group with params: " +params, queryName));
            }

            // Get the only key in the multimap
            Map<String, Object> attribData = null;
            for (Map<String, Object> key : grouped.keySet()) {
                attribData = key;
                break;
            }

            // Output the base attributes ignoring the nested data
            mapToJson(result, attribData, groupBy.names, null);

            // Output the nested list of data
            Set<String> nestedNames = new HashSet<String>(grouped.get(attribData).get(0).keySet());
            nestedNames.removeAll(groupBy.names);

            result.append(", ").append(quote(nest.attribute)).append(": ").append("[");
            listToJson(result, grouped.get(attribData), nestedNames, null);
            result.append("]");
        }

        return result.toString();
    }

    public String list(String queryName, final GroupBy groupBy, Nest nest) {

        checkPreExecution();
        StringBuilder result = new StringBuilder();

        // Run the query as a list
        ListMultimap<Map<String, Object>, Map<String, Object>> grouped = queryAndGroup(queryName, groupBy);

        if (grouped != null && !grouped.isEmpty()) {

            for (Map<String, Object> attribData : grouped.keySet()) {

                result.append("{ ");

                // Output the base attributes ignoring the nested data
                mapToJson(result, attribData, groupBy.names, null);

                // Output the nested list of data
                Set<String> nestedNames = nest.names;

                // No names were explicitly specified for the nesting, default to all non-grouped
                if (nestedNames == null || nestedNames.size() == 0) {
                    new HashSet<String>(grouped.get(attribData).get(0).keySet());
                    nestedNames.removeAll(groupBy.names);
                }

                result.append(", ").append(quote(nest.attribute)).append(": ").append("[");
                listToJson(result, grouped.get(attribData), nestedNames, null);
                result.append("]");

                result.append(" },\n");
            }

            if (result.length() > 0) {
                result.setLength(result.length() - 2);
            }
        }

        return result.toString();
    }

    private ListMultimap<Map<String, Object>, Map<String, Object>> queryAndGroup(String queryName, final GroupBy groupBy) {
        ListMultimap<Map<String, Object>, Map<String, Object>> grouped = null;

        List<Map<String, Object>> list = engine.queryForList(queryName, params);
        if (list != null && !list.isEmpty()) {
            // Build a multi-map based on the group by set
            grouped = Multimaps.index(list, new Function<Map<String, Object>, Map<String, Object>>() {
                @Override
                public Map<String, Object> apply(@Nullable Map<String, Object> data) {

                    // Key will be a map based on the columns named in the GroupBy
                    Map<String, Object> key = new HashMap<String, Object>();
                    for (String name : groupBy.names) {
                        key.put(name, data.get(name));
                    }

                    return key;
                }
            });
        }

        return grouped;
    }

    public GroupBy group(String ... names) {
        if (names == null || names.length == 0) {
            throw new IllegalArgumentException("Group must have at least on column");
        }

        Set<String> nameSet = new HashSet<String>();
        for (String name : names) {
            nameSet.add(name);
        }

        return new GroupBy(nameSet);
    }

    public Nest nest(String attribute, String ... fields) {
        return new Nest(attribute, fields);
    }

    private void mapToJson(StringBuilder result, Map<String, Object> data) {
        mapToJson(result, data, null, null);
    }

    private void mapToJson(StringBuilder result, Map<String, Object> data, Set<String> include, Nest nest) {

        if (nest != null && include == null) {
            include = new HashSet<String>(data.keySet());
            include.removeAll(nest.names);
        }

        for (String key : data.keySet()) {
            if (include == null || include.contains(key)) {
                result.append(quote(key)).append(":");

                Object value = data.get(key);
                if (value == null) {
                    result.append("null");
                } else if (value instanceof Number) {
                    result.append(value);
                } else {
                    result.append(quote(value));
                }

                result.append(",\n");
            }
        }

        if (nest != null) {
            result.append(quote(nest.attribute)).append(":").append("{");
            mapToJson(result, data, nest.names, null);
            result.append("},\n");
        }

        if (result.length() > 0) {
            result.setLength(result.length()-2);
        }
    }

    private void listToJson(StringBuilder result, List<Map<String, Object>> list) {
        listToJson(result, list, null, null);
    }

    private void listToJson(StringBuilder result, List<Map<String, Object>> list, Set<String> include, Nest nest) {

        for (Map<String, Object> data : list) {
            result.append("{ ");

            mapToJson(result, data, include, nest);

            result.append(" },\n");
        }

        if (result.length() > 0) {
            result.setLength(result.length() - 2);
        }
    }

    private String quote(Object value) {
        return value != null ? JSONObject.quote(value.toString()) : null;
    }

    private void checkPreExecution()
    {
        synchronized(this) {
            if (!executedPreExecutionQueries) {
                engine.executePreExecutionQueries();
                executedPreExecutionQueries = true;
            }
        }
    }

    private class GroupBy {
        private Set<String> names;

        public GroupBy(Set<String> names) {
            this.names = names;
        }
    }

    private class Nest {
        private String attribute;
        private Set<String> names;

        public Nest(String attribute, String ... nameArgs) {
            this.attribute = attribute;

            if (nameArgs != null && nameArgs.length > 0) {
                names = new HashSet<String>();
                for (String name : nameArgs) {
                    names.add(name);
                }
            }
        }
    }
}
