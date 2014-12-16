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

package com.polarrose.amazon.sqs;

import com.polarrose.amazon.aws.AwsAccount;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class SqsUtils
{
    public static void validateAwsAccount(AwsAccount account)
    {
        if (account == null) {
            throw new IllegalArgumentException("Parameter 'account' cannot be null");
        }
    }

    public static void validateVisibilityTimeout(int visibilityTimeout)
    {
        if (visibilityTimeout < 0 || visibilityTimeout > 86400) {
            throw new IllegalArgumentException("Parameter 'visibilityTimeout' is invalid: " + visibilityTimeout);
        }
    }

    private final static Pattern queueNamePattern = Pattern.compile("[A-Za-z0-9]{1,80}");

    public static void validateQueueName(String queueName)
    {
        if (queueName == null) {
            throw new IllegalArgumentException("Parameter 'queueName' cannot be null");
        }

        if (!queueNamePattern.matcher(queueName).matches()) {
            throw new IllegalArgumentException("Parameter 'queueName' is invalid: " + queueName);
        }
    }

    private final static Pattern queueNamePrefixPattern = Pattern.compile("[A-Za-z0-9]{1,79}");

    public static void validateQueueNamePrefix(String queueNamePrefix)
    {
        if (queueNamePrefix == null) {
            throw new IllegalArgumentException("Parameter 'queueNamePrefix' cannot be null");
        }

        if (!queueNamePrefixPattern.matcher(queueNamePrefix).matches()) {
            throw new IllegalArgumentException("Parameter 'queueNamePrefix' is invalid: " + queueNamePrefix);
        }
    }

    public static void validateQueueUrl(String queueUrl)

    {
        if (queueUrl == null) {
            throw new IllegalArgumentException("Parameter 'queueUrl' cannot be null");
        }

        try {
            new URL(queueUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Parameter 'queueUrl' is invalid: " + queueUrl);
        }
    }

    public static void validateBody(String body)
    {
        if (body == null) {
            throw new IllegalArgumentException("Parameter 'body' cannot be null");
        }

        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if ((c < 32 || c > 126) && c != '\n') {
                throw new IllegalArgumentException("Parameter 'body' has invalid non-printable characters");
            }
        }

        if (body.length() == 0 || body.length() > 8192) {
            throw new IllegalArgumentException("Parameter 'body' has an invalid length: " + body.length());
        }
    }
    
    public static void validateMessageId(String messageId)
    {
        if (messageId == null) {
            throw new IllegalArgumentException("Parameter 'messageId' cannot be null");
        }
    }

    public static void validateQueue(SqsQueue queue)
    {
        if (queue == null) {
            throw new IllegalArgumentException("Parameter 'queue' cannot be null");
        }

        if (queue.getUrl() == null) {
            throw new IllegalArgumentException("Parameter 'queue' is invalid; it's url is null");
        }
    }

    public static void validateMessage(SqsMessage message)
    {
        if (message == null) {
            throw new IllegalArgumentException("Parameter 'message' cannot be null");
        }
    }

    public static void validateNumberOfMessages(int numberOfMessages)
    {
        if (numberOfMessages < 1 || numberOfMessages > 256) {
            throw new IllegalArgumentException("Parameter 'numberOfMessages' is invalid: " + numberOfMessages);
        }
    }

    public static void validateGranteeEmailAddress(String granteeEmailAddress)
    {
        if (granteeEmailAddress == null) {
            throw new IllegalArgumentException("Parameter 'granteeEmailAddress' cannot be null");
        }
    }

    public static void validatePermission(SqsPermission permission)
    {
        if (permission == null) {
            throw new IllegalArgumentException("Parameter 'permission' cannot be null");
        }
    }

    public static void validateGranteeId(String granteeId)
    {
        if (granteeId == null) {
            throw new IllegalArgumentException("Parameter 'granteeId' cannot be null");
        }
    }
}
