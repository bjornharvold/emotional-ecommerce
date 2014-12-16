/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.etl;

import com.lela.commons.jobs.IngestParserContext;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.ingest.json.JsonTemplateEngine;
import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: Chris Tallent
 * Date: 5/24/12
 * Time: 2:07 PM
 */
public class ConvertAndSaveUtil {

    private static final Logger log = LoggerFactory.getLogger(ConvertAndSaveUtil.class);

    // Special characters to escape in JSON
    private final Pattern CHARACTERS_TO_REMOVE_PATTERN;
    private final Pattern ESCAPE_CARRIER_RETURN_PATTERN;

    private final ExecutionContext context;

    private final Object ingestService;
    private final String ingestMethodName;
    private final Class jsonType;
    private final Class simpleType;

    private final ObjectMapper ingestObjectMapper;
    private final ConversionService conversionService;
    private final JsonTemplateEngine jsonTemplateEngine;
    private final String jsonTemplate;

    private final IngestParserContext parserContext;
    private final ExpressionParser parser;

    private Method ingestMethod;
    private boolean isListMethod = false;
    private boolean methodReturnsVoid = false;

    private Class queryType = null;
    private Class methodType = null;
    private boolean convertJson = false;
    private boolean executeJsonTemplate = false;

    private JdbcTemplate jdbcTemplate;
    private String etlUpdateQuery;
    private String ingestLogProperty;

    public ConvertAndSaveUtil(ExecutionContext context,
                              ObjectMapper ingestObjectMapper,
                              ConversionService conversionService,
                              JsonTemplateEngine jsonTemplateEngine,
                              JdbcTemplate jdbcTemplate,
                              ConvertAndSaveArgs args) {

        this(   context,
                ingestObjectMapper,
                conversionService,
                jsonTemplateEngine,
                jdbcTemplate,
                args.getIngestService(),
                args.getIngestMethod(),
                args.getJsonTemplate(),
                args.getJsonType(),
                args.getSimpleType(),
                args.getEtlUpdateQuery(),
                args.getIngestLogProperty());
    }

    public ConvertAndSaveUtil(ExecutionContext context,
                              ObjectMapper ingestObjectMapper,
                              ConversionService conversionService,
                              JsonTemplateEngine jsonTemplateEngine,
                              JdbcTemplate jdbcTemplate,
                              Object ingestService,
                              String ingestMethodName,
                              String jsonTemplate,
                              Class jsonType,
                              Class simpleType,
                              String etlUpdateQuery,
                              String ingestLogProperty
                              ) {

        this.context = context;

        // Use detail defined target ingestService or default
        this.ingestService = ingestService;

        this.ingestMethodName = ingestMethodName;
        this.jsonType = jsonType;
        this.simpleType = simpleType;

        this.ingestObjectMapper = ingestObjectMapper;
        this.conversionService = conversionService;
        this.jsonTemplateEngine = jsonTemplateEngine;
        this.jsonTemplate = jsonTemplate;

        // Special character patterns to escape
        ESCAPE_CARRIER_RETURN_PATTERN = Pattern.compile("(\r)");

        // Control characters to remove
        CHARACTERS_TO_REMOVE_PATTERN = Pattern.compile("[\\x00-\\x0C\\x0E-\\x1F\\x7F\u0000-\u000C\u000E-\u001F\u007F]");

        this.parserContext = new IngestParserContext();
        this.parser = new SpelExpressionParser();

        this.jdbcTemplate = jdbcTemplate;
        this.etlUpdateQuery = etlUpdateQuery;
        this.ingestLogProperty = ingestLogProperty;

        // Determine the object type being queried
        if (jsonType != null) {
            if (jsonTemplate != null) {
                queryType = Map.class;
                executeJsonTemplate = true;
            } else {
                queryType = String.class;
                executeJsonTemplate = false;
            }
            methodType = jsonType;
            convertJson = true;
        } else {
            queryType = simpleType;
            methodType = queryType;
            convertJson = false;
            executeJsonTemplate = false;
        }

        // Find the save method
        findSaveMethod();
    }

    public Class getQueryType() {
        return queryType;
    }

    public boolean isListMethod() {
        return isListMethod;
    }

    public boolean isExecuteJsonTemplate() {
        return executeJsonTemplate;
    }

    private void findSaveMethod() {
        // Try to find a List based method
        ingestMethod = ReflectionUtils.findMethod(ingestService.getClass(),
                ingestMethodName,
                List.class);

        if (ingestMethod != null) {
            isListMethod = true;
        } else {
            // Try to find a method accepting the actual simple class type
            ingestMethod = ReflectionUtils.findMethod(ingestService.getClass(),
                    ingestMethodName,
                    methodType);
        }

        // There was no single argument method with the name and the simple type, check
        // for a method with only one argument with a converstion between the simple type and the arg type
        if (ingestMethod == null) {
            for (Method method : ReflectionUtils.getAllDeclaredMethods(ingestService.getClass())) {
                if (method.getName().equals(ingestMethodName) &&
                        method.getParameterTypes().length == 1) {

                    // Is the argument type assignable from the simple type?
                    if (method.getParameterTypes()[0].isAssignableFrom(queryType)) {
                        ingestMethod = method;
                        methodType = method.getParameterTypes()[0];
                        break;
                    }

                    // Is there a conversion between the simple type and the method arg type?
                    if (conversionService.canConvert(queryType, method.getParameterTypes()[0])) {
                        ingestMethod = method;
                        methodType = method.getParameterTypes()[0];
                        break;
                    }
                }
            }
        }

        if (ingestMethod == null) {
            log.error(context.message("Could not find ingest method accepting object type or List parameter: " + ingestMethodName));
            throw new IllegalArgumentException(context.message("Could not find ingest method accepting object type or List parameter: " + ingestMethodName));
        }

        // Check the return type
        methodReturnsVoid = ingestMethod.getReturnType().equals(Void.TYPE);
    }

    public Object convert(Object row)
            throws IOException {

        // Pass the row straight through unless need to convert from JSON
        Object result = null;

        // Execute a Json Template if defined
        if (executeJsonTemplate) {
            row = jsonTemplateEngine.generate(jsonTemplate, (Map)row);
        }

        // Convert the Json String to Object or handle a simple type
        if (convertJson) {

            // This job expects the SQL query to return JSON strings to convert to Objects
            String jsonString = (String)row;
            if (StringUtils.hasText(jsonString)) {

                // Escape any special characters
                row = CHARACTERS_TO_REMOVE_PATTERN.matcher(jsonString).replaceAll("");
                row = ESCAPE_CARRIER_RETURN_PATTERN.matcher(jsonString).replaceAll("");

                // Create an domain object from the query JSON
                try {
                    result = ingestObjectMapper.readValue(jsonString, jsonType);
                } catch (JsonProcessingException e) {
                    log.error(context.message("Jackson could not process the JSON: " + row), e);
                    throw e;
                }
            } else {
                log.info(context.message("JSON row null or empty.... skipping"));
                result = null;
            }
        } else if (methodType.isAssignableFrom(queryType)) {

            // IF the method type is assignable from the query type, pass the value through
            result = row;
        } else if (conversionService.canConvert(queryType, methodType)) {

            // Spring has a convertor from the query type to the method type, convert now
            result = conversionService.convert(row, methodType);
        } else {
            result = row;
        }

        return result;
    }

    public List<Object> ingest(List<Object> objects) {

        log.debug(context.message("Processing batch of size: " + objects.size()));

        List<Object> processed = new ArrayList<Object>();

        // If the ingest method takes a list parameter, the argument will be the full object list
        // otherwise the argument will be the first (and only) item in the object list
        Object argument = isListMethod ? objects : objects.get(0);

        // Save the domain object using the specified EventService method
        Object result = ReflectionUtils.invokeMethod(ingestMethod, ingestService, argument);

        // If the ingest method returned a value, the return value is passed to the update SQL
        // expression evaluation.  If the ingest method returns void, the current object row is passed
        List ingestResults = null;
        if (methodReturnsVoid) {
            ingestResults = objects;
        } else if (result != null) {
            ingestResults = isListMethod ? (List)result : Arrays.asList(result);
        }

        // Process the update SQL using Spring EL template
        if (ingestResults != null && StringUtils.hasText(etlUpdateQuery)) {
            for (Object ingestResult : ingestResults) {
                Expression expression = parser.parseExpression(etlUpdateQuery, parserContext);

                String updateSql = (String)expression.getValue(ingestResult);

                // Execute the update sql for the row
                // Allow any exceptions from this to terminate the loop since it implies
                // a broader query issue that is not associated with a single row
                jdbcTemplate.update(updateSql);
            }
        } else {
            log.debug(context.message("No results from Ingest... not performing Update sql"));
        }

        // Add to processed list
        if (!methodReturnsVoid) {
            processed.addAll(ingestResults);
        } else {
            if (isListMethod) {
                processed.addAll(objects);
            } else {
                processed.add(objects.get(0));
            }
        }

        // Log the processed
        if (StringUtils.hasText(ingestLogProperty)) {
            for (Object o : processed) {
                try {
                context.message("Processed " + ingestLogProperty + ": " + PropertyUtils.getProperty(o, ingestLogProperty));
                } catch (Exception e) {
                    log.error(context.message("Could not access ingestLogProperty " + ingestLogProperty));
                }
            }
        }

        return processed;
    }

}
