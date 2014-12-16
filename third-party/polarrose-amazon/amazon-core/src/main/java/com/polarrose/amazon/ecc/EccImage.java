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

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public class EccImage
{
    /**
     *
     */

    private String imageId;

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    /**
     *
     */

    private String imageLocation;

    public String getImageLocation()
    {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation)
    {
        this.imageLocation = imageLocation;
    }

    /**
     *
     */

    private String imageOwnerId;

    public String getImageOwnerId()
    {
        return imageOwnerId;
    }

    public void setImageOwnerId(String imageOwnerId)
    {
        this.imageOwnerId = imageOwnerId;
    }

    /**
     *
     */

    private EccImageState state;

    public EccImageState getState()
    {
        return state;
    }

    public void setState(EccImageState state)
    {
        this.state = state;
    }

    /**
     *
     */

    private boolean publik;

    public boolean isPublic()
    {
        return publik;
    }

    public void setPublic(boolean publik)
    {
        this.publik = publik;
    }
}
