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
import java.net.URL;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class SqsQueue implements Serializable
{
    private static final long serialVersionUID = -7097199504957341226L;

    private URL url;

    public URL getUrl()
    {
        return url;
    }

    public void setUrl(URL url)
    {
        if (url == null) {
            throw new IllegalArgumentException("Parameter 'url' cannot be null");
        }

        this.url = url;
    }

    public String getName()
    {
        if (url == null) {
            return null;
        }

        String path = url.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public SqsQueue()
    {
    }

    public SqsQueue(URL url)
    {
        setUrl(url);
    }

    //

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqsQueue sqsQueue = (SqsQueue) o;

        return !(url != null ? !url.equals(sqsQueue.url) : sqsQueue.url != null);

    }

    public int hashCode()
    {
        return (url != null ? url.hashCode() : 0);
    }
}
