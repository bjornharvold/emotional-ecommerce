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

import com.polarrose.amazon.sqs.SqsException;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class SendMessageResponse extends AbstractResponse
{
    private String messageId;

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public SendMessageResponse()
    {
        super();

        getDigester().push(this);
        getDigester().addBeanPropertySetter("SendMessageResponse/SendMessageResult/MessageId", "messageId");
    }

    public void validate() throws SqsException
    {
        if (messageId == null) {
            throw new SqsException("Malformed response: MessageId is null");
        }
    }

    public String getTopElementName()
    {
        return "SendMessageResponse";
    }
}
