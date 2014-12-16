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

package com.polarrose.amazon.spring.examples;

import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.Random;

public class QueueSender
{
    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/polarrose/amazon/spring/examples/QueueSender.xml");
        SqsService service = (SqsService) context.getBean("queueService", SqsService.class);

        SqsQueue queue = new SqsQueue(new URL("http://queue.amazonaws.com/TestQueue"));

        Random random = new Random();

        while (true)
        {
            int n = random.nextInt(10);

            for (int i = 1; i <= n; i++) {
                service.sendMessage(
                    queue,
                    "Hello, world. This is message " + i + " in a batch of " + n
                );
            }

            Thread.sleep((random.nextInt(13) + 3) * 100L);
        }
    }
}
