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

package com.polarrose.amazon.spring.sqs.impl;

import com.polarrose.amazon.spring.sqs.QueueCreator;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;
import com.polarrose.amazon.sqs.SqsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.List;

public class QueueCreatorImpl implements QueueCreator, InitializingBean
{
    protected final Log logger = LogFactory.getLog(getClass());

    private SqsService service;

    public void setService(SqsService service)
    {
        this.service = service;
    }

    public SqsService getService()
    {
        return service;
    }

    private List<SqsQueue> queues = Collections.emptyList();

    public void setQueues(List<SqsQueue> queues)
    {
        this.queues = queues;
    }

    public List<SqsQueue> getQueues()
    {
        return queues;
    }

    public void afterPropertiesSet() throws Exception
    {
        if (service == null) {
            throw new IllegalArgumentException("Property 'service' is required");
        }

        // Create all queues that do not already exist

        List<SqsQueue> availableQueues = service.listQueues();

        for (SqsQueue queue : queues)
        {
            if (!availableQueues.contains(queue))
            {
                if (logger.isDebugEnabled()) {
                    logger.debug("Queue [" + queue.getUrl() + "] does not exist and will be created.");
                }

                try {
                    service.createQueue(queue.getName());
                } catch (SqsException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Failed to create queue [" + queue.getUrl() + "]", e);
                    }
                }

                // TODO Should we wait until the queue is created?
            }
        }
    }
}
