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

public class SdbItem implements Serializable
{
    public static final long serialVersionUID = 1;

    private final String name;

    private final SdbAttributes attributes;

    public SdbItem(String name, SdbAttributes attributes)
    {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName()
    {
        return name;
    }

    public SdbAttributes getAttributes()
    {
        return attributes;
    }
}
