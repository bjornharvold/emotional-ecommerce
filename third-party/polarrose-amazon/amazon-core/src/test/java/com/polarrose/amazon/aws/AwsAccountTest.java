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

package com.polarrose.amazon.aws;

import static org.junit.Assert.*;
import org.junit.Test;

public class AwsAccountTest
{
    @Test
    public void testConstructor()
    {
        AwsAccount account = new AwsAccount("AccessKeyId", "SecretAccessKey");
        assertNotNull(account);
        assertEquals("AccessKeyId", account.getAccessKeyId());
        assertEquals("SecretAccessKey", account.getSecretAccessKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParameters1()
    {
        new AwsAccount(null, "SecretAccessKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParameters2()
    {
        new AwsAccount("AccessKeyId", null);
    }

    @Test
    public void testEquals()
    {
        AwsAccount account1 = new AwsAccount("AccessKey", "SecretAccessKey");
        AwsAccount account2 = new AwsAccount("AccessKey", "SecretAccessKey");
        assertTrue(account1.equals(account2));
    }

    @Test
    public void testNotEquals()
    {
        AwsAccount account1 = new AwsAccount("AccessKey1", "SecretAccessKey1");
        AwsAccount account2 = new AwsAccount("AccessKey2", "SecretAccessKey2");
        assertFalse(account1.equals(account2));
    }

    @Test
    public void testHashCodes()
    {
        AwsAccount account1 = new AwsAccount("AccessKey", "SecretAccessKey");
        AwsAccount account2 = new AwsAccount("AccessKey", "SecretAccessKey");
        assertTrue(account1.hashCode() == account2.hashCode());
    }

    @Test
    public void testDifferentHashCodes()
    {
        AwsAccount account1 = new AwsAccount("AccessKey1", "SecretAccessKey1");
        AwsAccount account2 = new AwsAccount("AccessKey2", "SecretAccessKey2");
        assertTrue(account1.hashCode() != account2.hashCode());
    }
}
