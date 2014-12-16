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

package com.polarrose.amazon.sqs.impl;

import com.polarrose.amazon.sqs.SqsException;
import com.polarrose.amazon.sqs.SqsMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;

public class ResponseParserTest
{
    // CreateQueueResponse Tests

    @Test
    public void testCreateQueueResponseParser()
    {
        String xml = "<CreateQueueResponse xmlns=\"http://queue.amazonaws.com/doc/2006-04-01/\"><QueueUrl>http://foo/bar</QueueUrl><ResponseStatus><RequestId>1234567890</RequestId></ResponseStatus></CreateQueueResponse>";
        CreateQueueResponse response = new CreateQueueResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));

        assertEquals("1234567890", response.getRequestId());

        URL queueUrl = response.getQueueUrl();
        assertNotNull(queueUrl);
        assertEquals("http://foo/bar", queueUrl.toExternalForm());
    }

    @Test
    public void testCreateQueueResponseValidatorRequestId()
    {
        String xml = "<CreateQueueResponse xmlns=\"http://queue.amazonaws.com/doc/2006-04-01/\"><QueueUrl>http://foo/bar</QueueUrl><ResponseStatus><RequestId>1234567890</RequestId></ResponseStatus></CreateQueueResponse>";
        CreateQueueResponse response = new CreateQueueResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));

        String requestId = response.getRequestId();
        assertNotNull(requestId);
        assertEquals("1234567890", requestId);
    }

    @Test(expected = SqsException.class)
    public void testCreateQueueResponseValidator()
    {
        String xml = "<CreateQueueResponse></CreateQueueResponse>";
        CreateQueueResponse response = new CreateQueueResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));
    }

    //

    @Test
    public void testReceiveMessageResponseWithSingleMessage()
    {
        String xml = "<ReceiveMessageResponse xmlns=\"http://queue.amazonaws.com/doc/2006-04-01/\"><Message><MessageId>ID1</MessageId><MessageBody>BODY1</MessageBody></Message><ResponseStatus><StatusCode>Success</StatusCode><RequestId>2d9c3dcc-b4d4-496b-9292-086a97f0b500</RequestId></ResponseStatus></ReceiveMessageResponse>";
        ReceiveMessagesResponse response = new ReceiveMessagesResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));

        List<SqsMessage> messages = response.getMessages();
        assertNotNull(messages);
        assertEquals(1, messages.size());

        SqsMessage message = messages.get(0);
        assertNotNull(message);
        assertEquals("ID1", message.getId());
        assertEquals("BODY1", message.getBody());
    }

    @Test
    public void testReceiveMessageResponseWithMultipleMessages()
    {
        String xml = "<ReceiveMessageResponse xmlns=\"http://queue.amazonaws.com/doc/2006-04-01/\"><Message><MessageId>ID1</MessageId><MessageBody>BODY1</MessageBody></Message><Message><MessageId>ID2</MessageId><MessageBody>BODY2</MessageBody></Message><ResponseStatus><StatusCode>Success</StatusCode><RequestId>1234567890</RequestId></ResponseStatus></ReceiveMessageResponse>";
        ReceiveMessagesResponse response = new ReceiveMessagesResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));

        assertEquals("1234567890", response.getRequestId());

        List<SqsMessage> messages = response.getMessages();
        assertNotNull(messages);
        assertEquals(2, messages.size());

        SqsMessage message1 = messages.get(0);
        assertNotNull(message1);
        assertEquals("ID1", message1.getId());
        assertEquals("BODY1", message1.getBody());

        SqsMessage message2 = messages.get(1);
        assertNotNull(message2);
        assertEquals("ID2", message2.getId());
        assertEquals("BODY2", message2.getBody());
    }

    @Test
    public void testPeekMessageResponse()
    {
        String xml = "<PeekMessageResponse xmlns=\"http://queue.amazonaws.com/doc/2006-04-01/\"><Message><MessageId>MessageId</MessageId><MessageBody>MessageBody</MessageBody></Message><ResponseStatus><StatusCode>Success</StatusCode><RequestId>1234567890</RequestId></ResponseStatus></PeekMessageResponse>";
        PeekMessageResponse response = new PeekMessageResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));

        assertEquals("1234567890", response.getRequestId());

        SqsMessage message = response.getMessage();
        assertNotNull(message);
        assertEquals("MessageId", message.getId());
        assertEquals("MessageBody", message.getBody());
    }

    @Test
    public void testGetQueueAttributesResponse()
    {
        String xml = "<GetQueueAttributesResponse>\n" +
                "\t<AttributedValue>\n" +
                "        <Attribute>ApproximateNumberOfMessages</Attribute>\n" +
                "        <Value>2900</Value>\n" +
                "     </AttributedValue>\n" +
                "     <AttributedValue>\n" +
                "        <Attribute>VisibilityTimeout</Attribute>\n" +
                "        <Value>35</Value>\n" +
                "     </AttributedValue>\n" +
                "     <ResponseStatus>\n" +
                "      <StatusCode>Success</StatusCode>\n" +
                "      <RequestId>cb919c0a-9bce-4afe-9b48-9bdf2412bb67</RequestId>\n" +
                "    </ResponseStatus>\n" +
                "</GetQueueAttributesResponse>";

        GetQueueAttributesResponse response = new GetQueueAttributesResponse();
        response.parse(new ByteArrayInputStream(xml.getBytes()));

        assertEquals("cb919c0a-9bce-4afe-9b48-9bdf2412bb67", response.getRequestId());

        assertEquals("2900", response.getAttribute("ApproximateNumberOfMessages"));
        assertEquals("35", response.getAttribute("VisibilityTimeout"));
    }
}
