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

import java.util.regex.Pattern;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class SdbUtils
{
    private final static Pattern domainNamePattern = Pattern.compile("[A-Za-z0-9-]{1,80}");

    public static void validateDomainName(String domainName)
    {
        if (domainName == null) {
            throw new IllegalArgumentException("Parameter 'domainName' cannot be null");
        }

        if (!domainNamePattern.matcher(domainName).matches()) {
            throw new IllegalArgumentException("Parameter 'domainName' is invalid: " + domainName);
        }
    }

    private final static Pattern itemNamePattern = Pattern.compile("[A-Za-z0-9-]{1,80}");

    public static void validateItemName(String itemName)
    {
        if (itemName == null) {
            throw new IllegalArgumentException("Parameter 'itemName' cannot be null");
        }

        if (!domainNamePattern.matcher(itemName).matches()) {
            throw new IllegalArgumentException("Parameter 'itemName' is invalid: " + itemName);
        }
    }

    public static void validateMaxNumberOfItems(int maxNumberOfItems)
    {
        // TODO
    }

    public static void validateQueryExpression(String queryExpression)
    {
        // TODO
    }

    public static void validateNextToken(String nextToken)
    {
        // TODO
    }

    public static void validateAttributeNames(Set<String> attributeNames)
    {
        // TODO
    }

    public static void validateAttributes(SdbAttributes attributes)
    {
        // TODO
    }

    public static Map<String, String> attributesToParameters(SdbAttributes attributes)
    {
        Map<String, String> parameters = new HashMap<String, String>();

        int i = 0;

        for (String attributeName : attributes.getAttributeNames()) {
            for (String attributeValue : attributes.getAttributes(attributeName)) {
                parameters.put("Attribute." + i + ".Name", attributeName);
                parameters.put("Attribute." + i + ".Value", attributeValue);
                i++;
            }
        }

        return parameters;
    }

    public static void validateSelectExpression(String selectExpression)
    {
        if (selectExpression == null) {
            throw new IllegalArgumentException("Parameter 'selectExpression' cannot be null");
        }
    }

    public static Map<String,String> attributeNamesToParameters(Set<String> attributeNames)
    {
        Map<String, String> parameters = new HashMap<String, String>();

        int i = 0;

        for (String attributeName : attributeNames) {
            parameters.put("AttributeName." + i, attributeName);
            i++;
        }

        return parameters;
    }
}
