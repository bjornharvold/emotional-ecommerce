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

package com.polarrose.amazon.sdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SdbAttributes implements Serializable
{
    public static final long serialVersionUID = 1;

    private final Map<String, List<String>> attributes = new HashMap<String, List<String>>();

    public boolean containsAttribute(String name)
    {
        return attributes.containsKey(name);
    }

    public Set<String> getAttributeNames()
    {
        return attributes.keySet();
    }

    public String getAttribute(String name)
    {
        List<String> values = attributes.get(name);
        if (values != null && values.size() != 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    public Collection<String> getAttributes(String name)
    {
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        } else {
            return new ArrayList<String>();
        }
    }

    public void setAttribute(String name, String value)
    {
        List<String> values = new ArrayList<String>();
        values.add(value);
        attributes.put(name, values);
    }

    public void addAttribute(String name, String value)
    {
        if (attributes.containsKey(name)) {
            attributes.get(name).add(value);
        } else {
            setAttribute(name, value);
        }
    }
}
