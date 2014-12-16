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

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class PeekMessageResponse extends AbstractResponse
{
    private SqsMessage message;

    public SqsMessage getMessage()
    {
        return message;
    }

    public void setMessage(SqsMessage message)
    {
        this.message = message;
    }

    public PeekMessageResponse()
    {
        super();
        getDigester().addObjectCreate("PeekMessageResponse/Message", SqsMessage.class);
        getDigester().addBeanPropertySetter("PeekMessageResponse/Message/MessageId", "id");
        getDigester().addBeanPropertySetter("PeekMessageResponse/Message/MessageBody", "body");
        getDigester().addSetNext("PeekMessageResponse/Message/MessageBody", "setMessage");
    }

    public void validate() throws SqsException
    {
        if (message == null) {
            throw new SqsException("Malformed response: message is null");
        }
    }

    public String getTopElementName()
    {
        return "PeekMessageResponse";
    }
}
