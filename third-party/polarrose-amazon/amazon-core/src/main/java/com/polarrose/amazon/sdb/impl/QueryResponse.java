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

import com.polarrose.amazon.sdb.SdbItem;

import java.util.ArrayList;
import java.util.List;

public class QueryResponse extends AbstractResponse
{
    private final List<SdbItem> items = new ArrayList<SdbItem>();

    public List<SdbItem> getItems()
    {
        return items;
    }

    public void addItem(String name)
    {
        items.add(new SdbItem(name, null));
    }

    private String nextToken;

    public String getNextToken()
    {
        return nextToken;
    }

    public void setNextToken(String nextToken)
    {
        this.nextToken = nextToken;
    }

    protected String getTopElementName()
    {
        return "QueryResponse";
    }

    public QueryResponse()
    {
        getDigester().push(this);
        getDigester().addCallMethod("QueryResponse/QueryResult/ItemName", "addItem", 0);
        getDigester().addCallMethod("QueryResponse/QueryResult/NextToken", "setNextToken", 0);
    }
}