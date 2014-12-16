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
import com.polarrose.amazon.sqs.SqsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Written at 12500 feet between København and Amsterdam.
 *
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class ReceiveMessagesResponse extends AbstractResponse
{
    private List<SqsMessage> messages = new ArrayList<SqsMessage>();

    public List<SqsMessage> getMessages()
    {
        return messages;
    }

    public void setMessages(List<SqsMessage> messages)
    {
        this.messages = messages;
    }

    public void addMessage(SqsMessage message)
    {
        messages.add(message);
    }

    public ReceiveMessagesResponse()
    {
        super();

        getDigester().push(this);
        getDigester().addObjectCreate("ReceiveMessageResponse/ReceiveMessageResult/Message", SqsMessage.class);
        getDigester().addBeanPropertySetter("ReceiveMessageResponse/ReceiveMessageResult/Message/MessageId", "id");
        getDigester().addBeanPropertySetter("ReceiveMessageResponse/ReceiveMessageResult/Message/Body", "body");
        getDigester().addBeanPropertySetter("ReceiveMessageResponse/ReceiveMessageResult/Message/ReceiptHandle", "receiptHandle");
        getDigester().addSetNext("ReceiveMessageResponse/ReceiveMessageResult/Message/Body", "addMessage");
    }

    public void validate() throws SqsException
    {
    }

    public String getTopElementName()
    {
        return "ReceiveMessageResponse";
    }
}
