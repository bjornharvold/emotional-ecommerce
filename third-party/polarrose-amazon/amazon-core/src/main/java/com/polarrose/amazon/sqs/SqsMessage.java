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

import java.io.Serializable;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class SqsMessage implements Serializable
{
    private static final long serialVersionUID = 4707000238543950504L;

    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        if (id == null) {
            throw new IllegalArgumentException("Parameter 'id' cannot be null");
        }

        this.id = id;
    }

    private String body;

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        if (body == null) {
            throw new IllegalArgumentException("Parameter 'body' cannot be null");
        }

        this.body = body;
    }

    private String receiptHandle;

    public String getReceiptHandle()
    {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle)
    {
        this.receiptHandle = receiptHandle;
    }

    public SqsMessage()
    {
    }

    public SqsMessage(String id, String body)
    {
        setId(id);
        setBody(body);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqsMessage that = (SqsMessage) o;

        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    public int hashCode()
    {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
