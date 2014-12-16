// EccServiceImpl.java

package com.polarrose.amazon.ecc.impl;

import java.util.Set;
import java.util.List;

import com.polarrose.amazon.ecc.EccService;
import com.polarrose.amazon.ecc.EccImage;
import com.polarrose.amazon.ecc.EccServiceException;
import com.polarrose.amazon.ecc.EccInstance;
import com.polarrose.amazon.ecc.EccInstanceRunStatus;
import com.polarrose.amazon.ecc.EccUserData;
import com.polarrose.amazon.ecc.EccInstanceRebootStatus;
import com.polarrose.amazon.ecc.EccInstanceTerminateStatus;
import com.polarrose.amazon.aws.AwsAccount;

public class EccServiceImpl implements EccService
{
    public Set<EccImage> describeAllImages(AwsAccount account) throws EccServiceException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<EccImage> describeImages(AwsAccount account, String... imageIds) throws EccServiceException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<EccInstance> describeInstances(AwsAccount account) throws EccServiceException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<EccInstanceRunStatus> runInstances(AwsAccount account, String imageId, int minCount, int maxCount, String keyName, Set<String> securityGroupNames, EccUserData userData) throws EccServiceException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<EccInstanceRebootStatus> rebootInstances(AwsAccount account, String... instanceIds) throws EccServiceException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<EccInstanceTerminateStatus> terminateInstances(AwsAccount account, String... instanceIds) throws EccServiceException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
