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

import java.util.ArrayList;
import java.util.List;

import com.polarrose.amazon.aws.AwsException;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class SqsException extends AwsException
{
    private List<SqsError> errors = new ArrayList<SqsError>();

    public List<SqsError> getErrors()
    {
        return errors;
    }

    public void setErrors(List<SqsError> errors)
    {
        this.errors = errors;
    }

    public SqsException(String message)
    {
        super(message);
    }

    public SqsException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SqsException(String message, List<SqsError> errors)
    {
        super(message);
        this.errors = errors;
    }
}
