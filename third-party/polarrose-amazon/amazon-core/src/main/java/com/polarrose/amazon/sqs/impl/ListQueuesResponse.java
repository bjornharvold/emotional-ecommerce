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

import com.polarrose.amazon.sqs.SqsQueue;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Written at 12500 feet between K�benhavn and Amsterdam.
 *
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class ListQueuesResponse extends AbstractResponse
{
    // TODO Not sure if this is the right thing to do

    static
    {
        if (ConvertUtils.lookup(URL.class) == null) {
            ConvertUtils.register(new UrlConverter(), URL.class);
        }
    }

    private List<SqsQueue> queues = new ArrayList<SqsQueue>();

    public List<SqsQueue> getQueues()
    {
        return queues;
    }

    public void setQueues(List<SqsQueue> queues)
    {
        this.queues = queues;
    }

    public void addQueue(SqsQueue queue)
    {
        queues.add(queue);
    }

    public ListQueuesResponse()
    {
        getDigester().push(this);
        getDigester().addObjectCreate("ListQueuesResponse/ListQueuesResult/QueueUrl", SqsQueue.class);
        getDigester().addBeanPropertySetter("ListQueuesResponse/ListQueuesResult/QueueUrl", "url");
        getDigester().addSetNext("ListQueuesResponse/ListQueuesResult/QueueUrl", "addQueue");
    }

    public String getTopElementName()
    {
        return "ListQueuesResponse";
    }

    static class UrlConverter implements Converter
    {
        public Object convert(Class type, Object value)
        {
            if (value == null) {
                throw new ConversionException("Input value not specified");
            }

            if (value instanceof URL) {
                return value;
            }

            if (!(value instanceof String)) {
                throw new ConversionException("Input value not of correct type");
            }

            try {
                return new URL((String) value);
            } catch (MalformedURLException e) {
                throw new ConversionException("Malformed URL: " + value.toString());
            }
        }
    }
}
