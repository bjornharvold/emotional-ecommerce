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

package com.polarrose.amazon.sqs.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.polarrose.amazon.aws.AwsAccount;
import com.polarrose.amazon.aws.AwsException;
import com.polarrose.amazon.aws.AwsUtils;
import com.polarrose.amazon.sqs.SqsError;
import com.polarrose.amazon.sqs.SqsException;
import com.polarrose.amazon.sqs.SqsMessage;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;
import com.polarrose.amazon.sqs.SqsUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class SqsServiceImpl implements SqsService
{
    private final static String SQS_API_VERSION = "2008-01-01";
    private final static String SQS_SERVICE_HOST = "queue.amazonaws.com";

    private final Map<String,HttpClient> clients = new HashMap<String,HttpClient>();

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
     * The id of the last command that executed succesfully. Were succesfully means that it returned a valid
     * SQS response with a RequestId element.
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
     * @see com.polarrose.amazon.sqs.SqsService#getLastRequestId()
     */

    public String getLastRequestId()
    {
        return lastRequestId.get();
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#lookupQueueByName(String)
     */

    public SqsQueue lookupQueueByName(String queueName)
        throws SqsException
    {
        // Validate the parameters

        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueueName(queueName);

        // Request queues that match the name, then find an exact match

        List<SqsQueue> queues = listQueues(queueName);
        for (SqsQueue queue : queues) {
            if (queue.getName().equals(queueName)) {
                return queue;
            }
        }

        return null;
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#createQueue(String,int)
     */

    public SqsQueue createQueue(String queueName, int visibilityTimeout)
        throws SqsException
    {
        // Validate the parameters

        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueueName(queueName);
        SqsUtils.validateVisibilityTimeout(visibilityTimeout);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("QueueName", queueName);
        parameters.put("VisibilityTimeout", Integer.toString(visibilityTimeout));

        CreateQueueResponse response = executeServiceRequest(
            CreateQueueResponse.class,
            "CreateQueue",
            parameters
        );

        // Return a Queue object

        return new SqsQueue(response.getQueueUrl());
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#createQueue(String)
     */

    public SqsQueue createQueue(String queueName)
        throws AwsException
    {
        // Validate the parameters

        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueueName(queueName);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("QueueName", queueName);

        CreateQueueResponse response = executeServiceRequest(
            CreateQueueResponse.class,
            "CreateQueue",
            parameters
        );

        // Return a Queue object

        return new SqsQueue(response.getQueueUrl());
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#listQueues()
     */

    public List<SqsQueue> listQueues()
        throws SqsException
    {
        // Validate the parameters

        SqsUtils.validateAwsAccount(account);

        // Execute the request

        ListQueuesResponse response = executeServiceRequest(
            ListQueuesResponse.class,
            "ListQueues",
            null
        );

        // Return the Queues

        return response.getQueues();
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#listQueues(String)
     */

    public List<SqsQueue> listQueues(String queueNamePrefix)
        throws SqsException
    {
        // Validate the parameters

        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueueNamePrefix(queueNamePrefix);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("QueueNamePrefix", queueNamePrefix);

        ListQueuesResponse response = executeServiceRequest(
            ListQueuesResponse.class,
            "ListQueues",
            parameters
        );

        // Return the Queues

        return response.getQueues();
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#deleteQueue(SqsQueue)
     */

    public void deleteQueue(SqsQueue queue)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);

        executeQueueRequest(
            DeleteQueueResponse.class,
            queue,
            "DeleteQueue",
            null
        );
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#getApproximateNumberOfMessages(com.polarrose.amazon.sqs.SqsQueue)
     */

    public long getApproximateNumberOfMessages(SqsQueue queue)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);

        GetQueueAttributesResponse response = executeQueueRequest(
            GetQueueAttributesResponse.class,
            queue,
            "GetQueueAttributes",
            null
        );

        return Long.parseLong(response.getAttribute("ApproximateNumberOfMessages"));
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#sendMessage(SqsQueue, String)
     */

    public SqsMessage sendMessage(SqsQueue queue, String body)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);
        SqsUtils.validateBody(body);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MessageBody", body);

        SendMessageResponse response = executeQueueRequest(
            SendMessageResponse.class,
            queue,
            "SendMessage",
            parameters
        );

        // Return a message object

        return new SqsMessage(response.getMessageId(), body);
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#receiveMessage(SqsQueue, int)
     */

    public SqsMessage receiveMessage(SqsQueue queue, int visibilityTimeout)
        throws SqsException
    {
        List<SqsMessage> messages = receiveMessages(queue, visibilityTimeout, 1);
        if (messages.size() != 0) {
            return messages.get(0);
        } else {
            return null;
        }
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#receiveMessage(SqsQueue)
     */

    public SqsMessage receiveMessage(SqsQueue queue)
        throws SqsException
    {
        List<SqsMessage> messages = receiveMessages(queue, 1);
        if (messages.size() != 0) {
            return messages.get(0);
        } else {
            return null;
        }
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#receiveMessages(SqsQueue, int, int)
     */

    public List<SqsMessage> receiveMessages(SqsQueue queue, int visibilityTimeout, int numberOfMessages)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);
        SqsUtils.validateVisibilityTimeout(visibilityTimeout);
        SqsUtils.validateNumberOfMessages(numberOfMessages);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MaxNumberOfMessages", Integer.toString(numberOfMessages));
        parameters.put("VisibilityTimeout", Integer.toString(visibilityTimeout));

        ReceiveMessagesResponse response = executeQueueRequest(
            ReceiveMessagesResponse.class,
            queue,
            "ReceiveMessage",
            parameters
        );

        // Return the messages

        return response.getMessages();
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#receiveMessages(SqsQueue, int)
     */

    public List<SqsMessage> receiveMessages(SqsQueue queue, int numberOfMessages)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);
        SqsUtils.validateNumberOfMessages(numberOfMessages);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MaxNumberOfMessages", Integer.toString(numberOfMessages));

        ReceiveMessagesResponse response = executeQueueRequest(
            ReceiveMessagesResponse.class,
            queue,
            "ReceiveMessage",
            parameters
        );

        // Return the messages

        return response.getMessages();
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#deleteMessageById(SqsQueue, String)
     */

    public void deleteMessageById(SqsQueue queue, String messageId)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);
        SqsUtils.validateMessageId(messageId);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MessageId", messageId);

        executeQueueRequest(
            DeleteMessageResponse.class,
            queue,
            "DeleteMessage",
            parameters
        );
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#deleteMessage(SqsQueue,com.polarrose.amazon.sqs.SqsMessage)
     */

    public void deleteMessage(SqsQueue queue, SqsMessage message)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateMessage(message);

        if (message.getReceiptHandle() == null) {
            throw new IllegalArgumentException("The message does not contain a recipeHandle.");
        }

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MessageId", message.getId());
        parameters.put("ReceiptHandle", message.getReceiptHandle());

        executeQueueRequest(
            DeleteMessageResponse.class,
            queue,
            "DeleteMessage",
            parameters
        );
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#peekMessage(com.polarrose.amazon.sqs.SqsQueue, String)
     */

    public SqsMessage peekMessage(SqsQueue queue, String messageId)
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateMessageId(messageId);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MessageId", messageId);

        PeekMessageResponse response = executeQueueRequest(
            PeekMessageResponse.class,
            queue,
            "DeleteMessage",
            parameters
        );

        return response.getMessage();
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#changeMessageVisibility(com.polarrose.amazon.sqs.SqsQueue, com.polarrose.amazon.sqs.SqsMessage, int)
     */

    public void changeMessageVisibility(SqsQueue queue, SqsMessage message, int visibilityTimeout)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateMessage(message);
        SqsUtils.validateVisibilityTimeout(visibilityTimeout);

        changeMessageVisibility(queue, message.getId(), visibilityTimeout);
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#changeMessageVisibility(com.polarrose.amazon.sqs.SqsQueue, String, int)
     */

    public void changeMessageVisibility(SqsQueue queue, String messageId, int visibilityTimeout)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateMessageId(messageId);
        SqsUtils.validateVisibilityTimeout(visibilityTimeout);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("MessageId", messageId);
        parameters.put("VisibilityTimeout", Integer.toString(visibilityTimeout));

        executeQueueRequest(
            ChangeMessageVisibilityResponse.class,
            queue,
            "ChangeMessageVisibility",
            parameters
        );
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#setVisibilityTimeout(com.polarrose.amazon.sqs.SqsQueue, int)
     */

    public void setVisibilityTimeout(SqsQueue queue, int visibilityTimeout)
        throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);
        SqsUtils.validateVisibilityTimeout(visibilityTimeout);

        // Execute the request

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("Attribute", "VisibilityTimeout");
        parameters.put("Value", Integer.toString(visibilityTimeout));

        executeQueueRequest(
            SetQueueAttributesResponse.class,
            queue,
            "SetQueueAttributes",
            parameters
        );
    }

    /**
     * @see com.polarrose.amazon.sqs.SqsService#getVisibilityTimeout(com.polarrose.amazon.sqs.SqsQueue)
     */

    public int getVisibilityTimeout(SqsQueue queue) throws SqsException
    {
        SqsUtils.validateAwsAccount(account);
        SqsUtils.validateQueue(queue);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("AttributeName", "VisibilityTimeout");

        GetQueueAttributesResponse response = executeQueueRequest(
            GetQueueAttributesResponse.class,
            queue,
            "GetQueueAttributes",
            parameters
        );

        return Integer.parseInt(response.getAttribute("VisibilityTimeout"));
    }

    //

    private <T extends AbstractResponse> T executeQueueRequest(Class<T> responseParserClass, SqsQueue queue, String action, Map<String,String> parameters)
        throws SqsException
    {
        return executeRequest(responseParserClass, queue.getUrl(), action, parameters);
    }

    private <T extends AbstractResponse> T executeServiceRequest(Class<T> responseParserClass, String action, Map<String, String> parameters)
        throws SqsException
    {
        try {
            URL serviceUrl = new URL("http://" + SQS_SERVICE_HOST);
            return executeRequest(responseParserClass, serviceUrl, action, parameters);
        } catch (MalformedURLException e) {
            throw new SqsException("Cannot construct service url: " + e.getMessage());
        }
    }

    private <T extends AbstractResponse> T executeRequest(Class<T> responseParserClass, URL serviceEndpoint, String action, Map<String,String> parameters)
        throws SqsException
    {
        GetMethod method = null;

        try
        {
            // Create a method

            method = new GetMethod(serviceEndpoint.getPath());

            // Build the parameter list, add the required ones

            Map<String,String> queryParameters = new HashMap<String,String>();
            if (parameters != null) {
                queryParameters.putAll(parameters);
            }

            queryParameters.put("Action", action);
            queryParameters.put("Version", SQS_API_VERSION);
            queryParameters.put("AWSAccessKeyId", account.getAccessKeyId());
            queryParameters.put("Timestamp", AwsUtils.createRequestTimestamp());

            // Finally add the signature parameters

            AwsUtils.createSignatureV2Parameters(
                account,
                method.getName(),
                SQS_SERVICE_HOST,
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

            // Create a new client for this endpoint

            HttpClient client;

            synchronized(clients)
            {
                client = clients.get(serviceEndpoint.getHost());
                if (client == null) {
                    client = new HttpClient(new MultiThreadedHttpConnectionManager());
                    client.getHostConfiguration().setHost(serviceEndpoint.getHost());
                    clients.put(serviceEndpoint.getHost(), client);
                }
            }

            // Execute the request

            int responseCode = client.executeMethod(method);

            switch (responseCode)
            {
                case HttpStatus.SC_OK:
                {
                    T responseParser = responseParserClass.newInstance();
                    responseParser.parse(method.getResponseBodyAsStream());
                    lastRequestId.set(responseParser.getRequestId());
                    return responseParser;
                }

                case HttpStatus.SC_BAD_REQUEST:
                {
                    ErrorResponse response = new ErrorResponse();
                    response.parse(method.getResponseBodyAsStream());
                    lastRequestId.set(response.getRequestId());

                    String errors = "";
                    for (SqsError error : response.getErrors()) {
                        if (errors.length() != 0) {
                            errors += ",";
                        }
                        errors += error.getCode();
                    }

                    throw new SqsException("SQS Call Failed: " + errors, response.getErrors());
                }

                default:
                {
                    throw new SqsException("SQS Service returned unknown response: code=" + responseCode + " message=" + method.getStatusText());
                }
            }
        }

        catch (Exception e)
        {
            throw new SqsException("Cannot execute command: " + e.getMessage(), e);
        }

        finally
        {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
}
