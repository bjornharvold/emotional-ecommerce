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

package com.polarrose.amazon.spring.sqs;

import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;

public interface QueueListener
{
    public static final int DEFAULT_POLL_INTERVAL = 5;
    public static final int DEFAULT_THREAD_POOL_SIZE = 8;

    void setService(SqsService service);
    SqsService getService();

    void setQueue(SqsQueue queue);
    SqsQueue getQueue();

    void setMessageHandler(MessageHandler messageHandler);
    MessageHandler getMessageHandler();

    int getThreadPoolSize();
    void setThreadPoolSize(int threadPoolSize);

    void setPollInterval(int pollInterval);
    int getPollInterval();
}
