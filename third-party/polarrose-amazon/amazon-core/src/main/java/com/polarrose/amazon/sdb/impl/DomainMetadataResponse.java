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

public class DomainMetadataResponse extends AbstractResponse
{
    protected String getTopElementName()
    {
        return "DomainMetadataResponse";
    }

    private SdbDomainMetaDataImpl domainMetaData;

    public SdbDomainMetaDataImpl getDomainMetaData()
    {
        return domainMetaData;
    }

    public void setDomainMetaData(SdbDomainMetaDataImpl domainMetaData)
    {
        this.domainMetaData = domainMetaData;
    }

    public DomainMetadataResponse()
    {
        getDigester().push(this);

        getDigester().addObjectCreate("DomainMetadataResponse/DomainMetadataResult", SdbDomainMetaDataImpl.class);

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/TimeStamp",
            "setTimeStamp", 0, new Class[] { long.class });

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/ItemCount",
            "setItemCount", 0, new Class[] { double.class });

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/AttributeNamesSizeBytes",
            "setAttributeNamesSize", 0, new Class[] { double.class });

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/AttributeValueCount",
            "setAttributeValueCount", 0, new Class[] { double.class });

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/AttributeNameCount",
            "setAttributeNameCount", 0, new Class[] { double.class });

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/ItemNamesSizeBytes",
            "setItemNamesSize", 0, new Class[] { double.class });

        getDigester().addCallMethod("DomainMetadataResponse/DomainMetadataResult/AttributeValuesSizeBytes",
            "setAttributeValuesSize", 0, new Class[] { double.class });

        getDigester().addSetNext("DomainMetadataResponse/DomainMetadataResult", "setDomainMetaData");
    }
}