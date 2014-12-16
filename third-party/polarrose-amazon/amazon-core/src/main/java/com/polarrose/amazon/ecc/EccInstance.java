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

package com.polarrose.amazon.ecc;

import java.util.Set;
import java.util.HashSet;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class EccInstance
{
    private String reservationId;

    public String getReservationId()
    {
        return reservationId;
    }

    public void setReservationId(String reservationId)
    {
        this.reservationId = reservationId;
    }

    private String ownerId;

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    private Set<String> groups = new HashSet<String>();

    public Set<String> getGroups()
    {
        return groups;
    }

    public void setGroups(Set<String> groups)
    {
        this.groups = groups;
    }

    private String instanceId;

    public String getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    private String imageId;

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    private EccInstanceState state;

    public EccInstanceState getState()
    {
        return state;
    }

    public void setState(EccInstanceState state)
    {
        this.state = state;
    }

    private String hostName;

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    private String keyName;

    public String getKeyName()
    {
        return keyName;
    }

    public void setKeyName(String keyName)
    {
        this.keyName = keyName;
    }
}
