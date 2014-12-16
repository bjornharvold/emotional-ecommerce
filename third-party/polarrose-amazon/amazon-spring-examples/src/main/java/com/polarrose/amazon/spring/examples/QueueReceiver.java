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

import com.polarrose.amazon.sqs.SqsMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

public class QueueReceiver
{
    public static void main(String[] args)
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/polarrose/amazon/spring/examples/QueueReceiver.xml");
    }

    public static class MessageHandler implements com.polarrose.amazon.spring.sqs.MessageHandler
    {
        protected final Log logger = LogFactory.getLog(getClass());

        public void handle(SqsMessage message)
        {
            //logger.info("Received a message: body = " + message.getBody());
            simulateMessageProcessingTime();
        }

        private void simulateMessageProcessingTime()
        {
            Random random = new Random();

            try {
                Thread.sleep((random.nextInt(10) + 1) * 250L);
            } catch (InterruptedException e) {
                // Ignored
            }
        }
    }
}