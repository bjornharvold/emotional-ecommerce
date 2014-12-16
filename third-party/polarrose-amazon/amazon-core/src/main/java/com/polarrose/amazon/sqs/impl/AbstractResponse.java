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

import com.polarrose.amazon.aws.AwsException;
import com.polarrose.amazon.sqs.SqsException;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public abstract class AbstractResponse
{
    private Digester digester;

    public Digester getDigester()
    {
        return digester;
    }

    //

    private String requestId;

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    //

    protected AbstractResponse()
    {
        digester = new Digester();
        digester.setValidating(false);

        digester.push(this);
        getDigester().addBeanPropertySetter(getTopElementName() + "/ResponseMetadata/RequestId", "requestId");
    }

    public void parse(InputStream responseStream) throws AwsException
    {
        try
        {
            digester.parse(responseStream);

            // Validate our part

            if (getRequestId() == null) {
                throw new SqsException("Malformed response: requestId is missing");
            }

            // Let the specific response validate the rest

            validate();
        }

        catch (IOException e)
        {
            throw new SqsException("Malformed response exception: " + e.getMessage());
        }

        catch (SAXException e)
        {
            throw new SqsException("Malformed response exception: " + e.getMessage());
        }
    }

    protected void validate() throws SqsException
    {

    }

    protected abstract String getTopElementName();
}
