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

package com.polarrose.amazon.aws;

import java.io.Serializable;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class AwsAccount implements Serializable
{
    private static final long serialVersionUID = -2295615821980120336L;

    private String accessKeyId;

    public String getAccessKeyId()
    {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId)
    {
        if (accessKeyId == null) {
            throw new IllegalArgumentException("Parameter 'accessKeyId' cannot be null");
        }

        this.accessKeyId = accessKeyId;
    }

    private String secretAccessKey;

    public String getSecretAccessKey()
    {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey)
    {
        if (secretAccessKey == null) {
            throw new IllegalArgumentException("Parameter 'secretAccessKey' cannot be null");
        }

        this.secretAccessKey = secretAccessKey;
    }

    public AwsAccount()
    {
    }

    public AwsAccount(String accessKeyId, String secretAccessKey)
    {
        setAccessKeyId(accessKeyId);
        setSecretAccessKey(secretAccessKey);
    }

    //

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwsAccount that = (AwsAccount) o;

        if (accessKeyId != null ? !accessKeyId.equals(that.accessKeyId) : that.accessKeyId != null) return false;
        return !(secretAccessKey != null ? !secretAccessKey.equals(that.secretAccessKey) : that.secretAccessKey != null);

    }

    public int hashCode()
    {
        int result;
        result = (accessKeyId != null ? accessKeyId.hashCode() : 0);
        result = 31 * result + (secretAccessKey != null ? secretAccessKey.hashCode() : 0);
        return result;
    }
}
