/*
 * Copyright 2006 Polar Rose <http://www.polarrose.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.polarrose.amazon.sdb.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.polarrose.amazon.aws.AwsAccount;
import com.polarrose.amazon.aws.AwsUtils;
import com.polarrose.amazon.sdb.SdbAttributes;
import com.polarrose.amazon.sdb.SdbDomainMetaData;
import com.polarrose.amazon.sdb.SdbError;
import com.polarrose.amazon.sdb.SdbResultSet;
import com.polarrose.amazon.sdb.SdbService;
import com.polarrose.amazon.sdb.SdbException;
import com.polarrose.amazon.sdb.SdbUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

public class SdbServiceImpl implements SdbService
{
    private final static String SDB_API_VERSION = "2007-11-07";
    private final static String SDB_SERVICE_HOST = "sdb.amazonaws.com";

    private final HttpClient client;

    /**
     *
     */

    private AwsAccount account;

    public AwsAccount getAccount()
    {
        return account;
    }

    public void setAccount(AwsAccount account)
    {
        this.account = account;
    }

    /**
     *
     */

    private BigDecimal totalBoxUsage = new BigDecimal(0);

    public BigDecimal getTotalBoxUsage()
    {
        return totalBoxUsage;
    }

    /**
     * The id of the last command that executed succesfully. Were succesfully means that it returned a valid
     * SDB response with a RequestId element.
     */

    private static final ThreadLocal<String> lastRequestId = new ThreadLocal<String>()
    {
        @Override
        protected String initialValue()
        {
            return null;
        }
    };

    /**
     * The id of the last command that executed succesfully. Were succesfully means that it returned a valid
     * SDB response with a RequestId element.
     */

    private static final ThreadLocal<BigDecimal> lastBoxUsage = new ThreadLocal<BigDecimal>()
    {
        @Override
        protected BigDecimal initialValue()
        {
            return new BigDecimal(0);
        }
    };

    /**
     * @see SdbService#getLastRequestId()
     */

    public String getLastRequestId()
    {
        return lastRequestId.get();
    }

    public BigDecimal getLastBoxUsage()
    {
        return lastBoxUsage.get();
    }

    public SdbServiceImpl()
    {
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        client.getHostConfiguration().setHost(SDB_SERVICE_HOST);
    }

    /**
     * @see SdbService#createDomain(String)
     */

    public void createDomain(String domainName) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);

        executeRequest(
            CreateDomainResponse.class,
            "CreateDomain",
            parameters
        );
    }

    public void deleteDomain(String domainName) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);

        executeRequest(
            DeleteDomainResponse.class,
            "DeleteDomain",
            parameters
        );
    }

    /**
     * @see SdbService#listDomains()
     */

    public List<String> listDomains() throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();

        ListDomainsResponse response = executeRequest(
            ListDomainsResponse.class,
            "ListDomains",
            parameters
        );

        return response.getDomains();
    }

    /**
     * @see com.polarrose.amazon.sdb.SdbService#getDomainMetaData(String)
     */

    public SdbDomainMetaData getDomainMetaData(String domainName) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);

        DomainMetadataResponse response = executeRequest(
            DomainMetadataResponse.class,
            "DomainMetadata",
            parameters
        );

        return response.getDomainMetaData();
    }

    /**
     * @see com.polarrose.amazon.sdb.SdbService#putAttributes(String, String, SdbAttributes)
     */

    public void putAttributes(String domainName, String itemName, SdbAttributes attributes) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);
        SdbUtils.validateItemName(itemName);
        SdbUtils.validateAttributes(attributes);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);
        parameters.put("ItemName", itemName);

        parameters.putAll(SdbUtils.attributesToParameters(attributes));

        executeRequest(
            PutAttributesResponse.class,
            "PutAttributes",
            parameters
        );
    }

    /**
     * @see com.polarrose.amazon.sdb.SdbService#getAttributes(String, String)
     */

    public SdbAttributes getAttributes(String domainName, String itemName) throws SdbException
    {
        Set<String> attributeNames= Collections.emptySet();
        return getAttributes(domainName, itemName, attributeNames);
    }

    /**
     * @see com.polarrose.amazon.sdb.SdbService#getAttributes(String, String, java.util.Set)
     */

    public SdbAttributes getAttributes(String domainName, String itemName, Set<String> attributeNames) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);
        SdbUtils.validateItemName(itemName);
        SdbUtils.validateAttributeNames(attributeNames);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);
        parameters.put("ItemName", itemName);

        int i = 0;
        for (String attributeName : attributeNames) {
            parameters.put("AttributeName." + i++, attributeName);
        }

        GetAttributesResponse response = executeRequest(
            GetAttributesResponse.class,
            "GetAttributes",
            parameters
        );

        return response.getAttributes();
    }

    //

    public SdbResultSet query(String domainName, int maxNumberOfItems, String queryExpression) throws SdbException
    {
        return query(domainName, maxNumberOfItems, queryExpression, null);
    }

    public SdbResultSet query(String domainName, int maxNumberOfItems, String queryExpression, String nextToken) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);
        SdbUtils.validateMaxNumberOfItems(maxNumberOfItems);
        SdbUtils.validateQueryExpression(queryExpression);

        if (nextToken != null) {
            SdbUtils.validateNextToken(nextToken);
        }

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);
        parameters.put("MaxNumberOfItems", Integer.toString(maxNumberOfItems));
        parameters.put("QueryExpression", queryExpression);

        if (nextToken != null) {
            parameters.put("NextToken", nextToken);
        }

        QueryResponse response = executeRequest(
            QueryResponse.class,
            "Query",
            parameters
        );

        return new SdbResultSet(response.getItems(), response.getNextToken());
    }

    public SdbResultSet queryWithAttributes(String domainName, int maxNumberOfItems, String queryExpression, Set<String> attributeNames) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);
        SdbUtils.validateMaxNumberOfItems(maxNumberOfItems);
        SdbUtils.validateQueryExpression(queryExpression);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);
        parameters.put("MaxNumberOfItems", Integer.toString(maxNumberOfItems));
        parameters.put("QueryExpression", queryExpression);

        parameters.putAll(SdbUtils.attributeNamesToParameters(attributeNames));

        QueryWithAttributesResponse response = executeRequest(
            QueryWithAttributesResponse.class,
            "QueryWithAttributes",
            parameters
        );

        return new SdbResultSet(response.getItems(), response.getNextToken());
    }

    public SdbResultSet queryWithAttributes(String domainName, int maxNumberOfItems, String queryExpression, Set<String> attributeNames, String nextToken) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateDomainName(domainName);
        SdbUtils.validateMaxNumberOfItems(maxNumberOfItems);
        SdbUtils.validateQueryExpression(queryExpression);

        if (nextToken != null) {
            SdbUtils.validateNextToken(nextToken);
        }

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("DomainName", domainName);
        parameters.put("MaxNumberOfItems", Integer.toString(maxNumberOfItems));
        parameters.put("QueryExpression", queryExpression);

        parameters.putAll(SdbUtils.attributeNamesToParameters(attributeNames));

        if (nextToken != null) {
            parameters.put("NextToken", nextToken);
        }

        QueryWithAttributesResponse response = executeRequest(
            QueryWithAttributesResponse.class,
            "QueryWithAttributes",
            parameters
        );

        return new SdbResultSet(response.getItems(), response.getNextToken());
    }

    //

    public SdbResultSet select(String selectExpression) throws SdbException
    {
        return select(selectExpression,  null);
    }

    public SdbResultSet select(String selectExpression, String nextToken) throws SdbException
    {
        // Validate the parameters

        AwsUtils.validateAwsAccount(account);
        SdbUtils.validateSelectExpression(selectExpression);

        if (nextToken != null) {
            SdbUtils.validateNextToken(nextToken);
        }

        // Build the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("SelectExpression", selectExpression);

        if (nextToken != null) {
            parameters.put("NextToken", nextToken);
        }

        // Execute the request

        SelectResponse response = executeRequest(
            SelectResponse.class,
            "Select",
            parameters
        );

        return new SdbResultSet(response.getItems(), response.getNextToken());
    }

    //

    private <T extends AbstractResponse> T executeRequest(Class<T> responseParserClass, String action, Map<String, String> parameters)
        throws SdbException
    {
        GetMethod method = null;

        try
        {
            // Create a method

            method = new GetMethod("/");

            // Build the parameter list, add the required ones

            Map<String,String> queryParameters = new HashMap<String,String>();
            if (parameters != null) {
                queryParameters.putAll(parameters);
            }

            queryParameters.put("Action", action);
            queryParameters.put("Version", SDB_API_VERSION);
            queryParameters.put("AWSAccessKeyId", account.getAccessKeyId());
            queryParameters.put("Timestamp", AwsUtils.createRequestTimestamp());

            // Finally add the signature parameters

            AwsUtils.createSignatureV2Parameters(
                account,
                method.getName(),
                SDB_SERVICE_HOST,
                method.getPath(),
                queryParameters
            );

            // Convert it to what the HTTP client wants

            NameValuePair[] nameValuePairs = new NameValuePair[queryParameters.size()];

            int i = 0;
            for (String name : queryParameters.keySet()) {
                nameValuePairs[i] = new NameValuePair(name, queryParameters.get(name));
                i++;
            }

            method.setQueryString(nameValuePairs);

            // Execute the request

            int responseCode = client.executeMethod(method);
            if (responseCode == HttpStatus.SC_OK)
            {
                T responseParser = responseParserClass.newInstance();
                responseParser.parse(method.getResponseBodyAsStream());

                lastRequestId.set(responseParser.getRequestId());
                lastBoxUsage.set(responseParser.getBoxUsage());

                synchronized (this) {
                    totalBoxUsage = totalBoxUsage.add(lastBoxUsage.get());
                }

                return responseParser;
            }
            else if (responseCode == HttpStatus.SC_BAD_REQUEST)
            {
                ErrorResponse response = new ErrorResponse();
                response.parse(method.getResponseBodyAsStream());
                lastRequestId.set(response.getRequestId());

                String errors = "";
                for (SdbError error : response.getErrors()) {
                    if (errors.length() != 0) {
                        errors += ",";
                    }
                    errors += error.getCode();
                }

                throw new SdbException("SQS Call Failed: " + errors, response.getErrors());
            }
            else
            {
                throw new SdbException("SQS Service returned unknown response: code=" + responseCode + " message=" + method.getStatusText());
            }
        }

        catch (Exception e)
        {
            throw new SdbException("Cannot execute command: " + e.getMessage(), e);
        }

        finally
        {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
}
