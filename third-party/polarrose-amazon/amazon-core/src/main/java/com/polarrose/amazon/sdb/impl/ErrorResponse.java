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

import com.polarrose.amazon.sdb.SdbError;
import com.polarrose.amazon.sqs.SqsException;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse
{
    private Digester digester;

    //

    private List<SdbError> errors = new ArrayList<SdbError>();

    public List<SdbError> getErrors()
    {
        return errors;
    }

    public void setErrors(List<SdbError> errors)
    {
        this.errors = errors;
    }

    public void addError(SdbError error)
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
        digester.addBeanPropertySetter("Response/RequestID", "requestId");
        digester.addObjectCreate("Response/Errors/Error", SdbError.class);
        digester.addBeanPropertySetter("Response/Errors/Error/Code", "code");
        digester.addBeanPropertySetter("Response/Errors/Error/Message", "message");
        digester.addSetNext("Response/Errors/Error", "addError");
    }

    public void parse(InputStream responseStream)
        throws SqsException
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
