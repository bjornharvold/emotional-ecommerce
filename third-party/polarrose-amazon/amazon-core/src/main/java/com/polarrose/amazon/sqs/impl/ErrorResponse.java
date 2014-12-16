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
import com.polarrose.amazon.sqs.SqsError;
import com.polarrose.amazon.sqs.SqsException;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class ErrorResponse
{
    private Digester digester;

    //

    private List<SqsError> errors = new ArrayList<SqsError>();

    public List<SqsError> getErrors()
    {
        return errors;
    }

    public void setErrors(List<SqsError> errors)
    {
        this.errors = errors;
    }

    public void addError(SqsError error)
    {
        errors.add(error);
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

    public ErrorResponse()
    {
        digester = new Digester();
        digester.setValidating(false);

        digester.push(this);
        digester.addBeanPropertySetter("ErrorResponse/RequestID", "requestId");
        digester.addObjectCreate("ErrorResponse/Errors/Error", SqsError.class);
        digester.addBeanPropertySetter("ErrorResponse/Errors/Error/Type", "type");
        digester.addBeanPropertySetter("ErrorResponse/Errors/Error/Code", "code");
        digester.addBeanPropertySetter("ErrorResponse/Errors/Error/Message", "message");
        digester.addSetNext("ErrorResponse/Errors/Error", "addError");
    }

    public void parse(InputStream responseStream)
        throws AwsException
    {
        try {
            digester.parse(responseStream);

            // Validate our part

            if (getRequestId() == null) {
                throw new SqsException("Malformed response: requestId is missing");
            }
        }

        catch (IOException e) {
            throw new SqsException("Malformed response exception: " + e.getMessage());
        }

        catch (SAXException e) {
            throw new SqsException("Malformed response exception: " + e.getMessage());
        }
    }
}
