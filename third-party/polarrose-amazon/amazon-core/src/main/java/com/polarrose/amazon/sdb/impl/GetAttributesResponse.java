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

import com.polarrose.amazon.sdb.SdbAttributes;

public class GetAttributesResponse extends AbstractResponse
{
    private final SdbAttributes attributes = new SdbAttributes();

    public SdbAttributes getAttributes()
    {
        return attributes;
    }

    public void setAttribute(String name, String value)
    {
        attributes.addAttribute(name, value);
    }

    protected String getTopElementName()
    {
        return "GetAttributesResponse";
    }

    public GetAttributesResponse()
    {
        getDigester().push(this);
        getDigester().addCallMethod("GetAttributesResponse/GetAttributesResult/Attribute", "setAttribute", 2);
        getDigester().addCallParam("GetAttributesResponse/GetAttributesResult/Attribute/Name", 0);
        getDigester().addCallParam("GetAttributesResponse/GetAttributesResult/Attribute/Value", 1);
    }
}