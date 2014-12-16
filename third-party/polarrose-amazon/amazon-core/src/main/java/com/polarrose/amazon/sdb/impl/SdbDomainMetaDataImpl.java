/*
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

import com.polarrose.amazon.sdb.SdbDomainMetaData;

import java.util.Date;

public class SdbDomainMetaDataImpl implements SdbDomainMetaData
{
    /** The date and time the metadata was last updated. */

    private Date timeStamp; // TODO I don't like this name. Change it to lastReportDate?

    /** The number of all items in the domain. */

    private long itemCount;

    /** The number of all attribute name/value pairs in the domain. */

    private long attributeValueCount;

    /** The number of unique attribute names in the domain. */

    private long attributeNameCount;

    /** The total size of all item names in the domain, in bytes. */

    private long itemNamesSize;

    /** The total size of all attribute values, in bytes. */

    private long attributeValuesSize;

    /** The total size of all unique attribute names, in bytes. */

    private long attributeNamesSize;


    public Date getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = new Date(timeStamp * 1000L);
    }

    public long getItemCount()
    {
        return itemCount;
    }

    public void setItemCount(double itemCount)
    {
        this.itemCount = (long) itemCount;
    }

    public long getAttributeValueCount()
    {
        return attributeValueCount;
    }

    public void setAttributeValueCount(double attributeValueCount)
    {
        this.attributeValueCount = (long) attributeValueCount;
    }

    public long getAttributeNameCount()
    {
        return attributeNameCount;
    }

    public void setAttributeNameCount(double attributeNameCount)
    {
        this.attributeNameCount = (long) attributeNameCount;
    }

    public long getItemNamesSize()
    {
        return itemNamesSize;
    }

    public void setItemNamesSize(double itemNamesSize)
    {
        this.itemNamesSize = (long) itemNamesSize;
    }

    public long getAttributeValuesSize()
    {
        return attributeValuesSize;
    }

    public void setAttributeValuesSize(double attributeValuesSize)
    {
        this.attributeValuesSize = (long) attributeValuesSize;
    }

    public long getAttributeNamesSize()
    {
        return attributeNamesSize;
    }

    public void setAttributeNamesSize(double attributeNamesSize)
    {
        this.attributeNamesSize = (long) attributeNamesSize;
    }
}
