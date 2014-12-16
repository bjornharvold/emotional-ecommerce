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

import java.util.Map;
import java.util.HashMap;

public class GetQueueAttributesResponse extends AbstractResponse
{
    private Map<String,String> attributes = new HashMap<String, String>();

    @Override
    protected String getTopElementName()
    {
        return "GetQueueAttributesResponse";
    }

    public String getAttribute(String name)
    {
        return attributes.get(name);
    }

    public void addAttribute(String name, String value)
    {
        attributes.put(name, value);
    }

    public GetQueueAttributesResponse()
    {
        getDigester().push(this);
        getDigester().addCallMethod("GetQueueAttributesResponse/GetQueueAttributesResult/Attribute", "addAttribute", 2);
        getDigester().addCallParam("GetQueueAttributesResponse/GetQueueAttributesResult/Attribute/Name", 0);
        getDigester().addCallParam("GetQueueAttributesResponse/GetQueueAttributesResult/Attribute/Value", 1);
    }
}
