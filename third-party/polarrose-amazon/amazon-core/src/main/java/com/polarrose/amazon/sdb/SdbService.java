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

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface SdbService
{
    String getLastRequestId();

    BigDecimal getLastBoxUsage();

    BigDecimal getTotalBoxUsage();

    //

    void createDomain(String domainName)
        throws SdbException;

    void deleteDomain(String domainName)
        throws SdbException;

    List<String> listDomains()
        throws SdbException;

    SdbDomainMetaData getDomainMetaData(String domainName)
        throws SdbException;

    //

    void putAttributes(String domainName, String itemName, SdbAttributes attributes)
        throws SdbException;

    SdbAttributes getAttributes(String domainName, String itemName)
        throws SdbException;

    SdbAttributes getAttributes(String domainName, String itemName, Set<String> attributeNames)
        throws SdbException;

    //

    SdbResultSet query(String domainName, int maxNumberOfItems, String expression)
        throws SdbException;

    SdbResultSet query(String domainName, int maxNumberOfItems, String expression, String nextToken)
        throws SdbException;

    SdbResultSet queryWithAttributes(String domainName, int maxNumberOfItems, String expression, Set<String> attributeNames)
        throws SdbException;

    SdbResultSet queryWithAttributes(String domainName, int maxNumberOfItems, String expression, Set<String> attributeNames, String nextToken)
        throws SdbException;

    //


    SdbResultSet select(String selectExpression)
            throws SdbException;

    SdbResultSet select(String selectExpression, String nextToken)
            throws SdbException;
}