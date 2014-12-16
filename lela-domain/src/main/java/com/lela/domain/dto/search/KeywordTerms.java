/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bjorn Harvold
 * Date: 2/24/12
 * Time: 1:28 AM
 * Responsibility:
 */
public class KeywordTerms {
    private static final Pattern ESCAPE_CHARACTERS_PATTERN = Pattern.compile("[*?]");

    private static final String RELAXED_PHRASE_TARGET_FIELD = "text";
    private static final String STRICT_PHRASE_TARGET_FIELD = "text_english_splitting_tight";
    private String query;
    private List<String> terms = new ArrayList<String>();

    public KeywordTerms() {
    }

    public KeywordTerms(String keywords) {
        this(keywords, true);
    }
    
    public KeywordTerms(String keywords, boolean parseTerms) {

        if (parseTerms) {

            if (StringUtils.isNotBlank(keywords)) {

                // remove whitespace on each side
                keywords = keywords.trim();

                // Escape special characters
                Matcher m = ESCAPE_CHARACTERS_PATTERN.matcher(keywords);
                if (m.find()) {
                    keywords = m.replaceAll("\\\\$0");
                }

                this.query = keywords;

                // Check for a single, non-quoted word
                if (!StringUtils.contains(keywords, ' ') && !keywords.contains("\"")) {
                    // add keyword to terms list
                    addTerm(keywords);
                } else {
                    // Remove all versions of 'OR' in the keyword string as the query is by default an 'OR' query
                    keywords = keywords.replaceAll(" or ", " ");
                    keywords = keywords.replaceAll(" OR ", " ");
                    keywords = keywords.replaceAll(" and ", " ");
                    keywords = keywords.replaceAll(" AND ", " ");

                    // Break up the keywords string on spaces while retaining double quoted tokens
                    m = Pattern.compile("(\"[^\"]*?\"|\\S)+").matcher(keywords);
                    while (m.find()) {
                        String term = m.group();

                        // add term to terms list
                        addTerm(term);
                    }
                }
            }
        }
    }

    public String getQuery() {
        return query;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void addTerm(String term) {
        this.terms.add(term);
    }

    /**
     * @param textField textField
     * @param values    values
     * @param condition condition
     * @param wildcard  wildcard
     */
    public void addSearchCondition(String textField, List<String> values, SearchConditionType condition, boolean wildcard) {
        StringBuilder sb;

        if (StringUtils.isBlank(query)) {
            sb = new StringBuilder();
        } else {
            sb = new StringBuilder(query);

            sb.append(" ");

            switch (condition) {
                case AND:
                    sb.append("AND ");
                    break;
                case OR:
                    sb.append("OR ");
                    break;
            }
        }

        if (values != null && !values.isEmpty()) {

            sb.append(textField).append(":");

            sb.append("(");

            boolean first = true;
            for (String value : values) {

                // Test if value has a space in it and needs to be quoted
                boolean quoted = value.contains(" ");

                if (!first) {
                    sb.append(" OR ");
                }

                if (wildcard) {
                    sb.append(value).append("*");
                } else {
                    if (quoted) {
                        sb.append("\"");
                    }
                    sb.append(value);
                    if (quoted) {
                        sb.append("\"");
                    }
                }

                first = false;
            }
            sb.append(")");
        }

        query = sb.toString();
    }

    public static void main(String[] args) {

        KeywordTerms kt = new KeywordTerms("G-Luxe");
        System.out.println(kt.getQuery());

        kt = new KeywordTerms("G-Luxe Triumph*");
        System.out.println(kt.getQuery());

        kt = new KeywordTerms("G-Luxe OR Triumph");
        System.out.println(kt.getQuery());

        kt = new KeywordTerms("\"G-Luxe Stroller\" Triumph");
        System.out.println(kt.getQuery());

        kt = new KeywordTerms("G-Luxe or Triumph");
        System.out.println(kt.getQuery());

        List<String> categories = new ArrayList<String>();
        categories.add("stroller");

        kt.addSearchCondition("ctgry", categories, SearchConditionType.AND, false);
        System.out.println(kt.getQuery());

        kt = new KeywordTerms("twin stroller", false);
        kt.addSearchCondition("ctgry", categories, SearchConditionType.NONE, false);
        System.out.println(kt.getQuery());
    }
}
