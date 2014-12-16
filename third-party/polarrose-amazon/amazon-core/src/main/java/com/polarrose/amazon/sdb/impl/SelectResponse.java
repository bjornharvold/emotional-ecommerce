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
import com.polarrose.amazon.sdb.SdbAttributes;

import java.util.ArrayList;
import java.util.List;

public class SelectResponse extends AbstractResponse
{
    private final List<SdbItem> items = new ArrayList<SdbItem>();

    public List<SdbItem> getItems()
    {
        return items;
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
        return "SelectResponse";
    }


    private String itemName;

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    private SdbAttributes itemAttributes = new SdbAttributes();

    public void setItemAttributes(SdbAttributes itemAttributes)
    {
        this.itemAttributes = itemAttributes;
    }

    public void setItemAttribute(String name, String value)
    {
        itemAttributes.addAttribute(name, value);
    }

    public void addItem()
    {
        items.add(new SdbItem(itemName, itemAttributes));
        itemName = null;
        itemAttributes = new SdbAttributes();
    }


    public SelectResponse()
    {
        getDigester().push(this);

        getDigester().addCallMethod("SelectResponse/SelectResult/Item/Name", "setItemName", 0);
        getDigester().addCallMethod("SelectResponse/SelectResult/Item/Attribute", "setItemAttribute", 2);
        getDigester().addCallParam("SelectResponse/SelectResult/Item/Attribute/Name", 0);
        getDigester().addCallParam("SelectResponse/SelectResult/Item/Attribute/Value", 1);
        getDigester().addCallMethod("SelectResponse/SelectResult/Item", "addItem");
        getDigester().addCallMethod("SelectResponse/SelectResult/NextToken", "setNextToken", 0);
    }
}