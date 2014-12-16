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

package com.polarrose.amazon.sqs;

import java.util.List;

/**
 * @author Stefan Arentz <stefan@polarrose.com>
 */

public interface SqsService
{
    /**
     * Return the request id of the last executed command.
     * @return a string containing the last request id.
     */

    String getLastRequestId();

    /**
     *
     * @param queueName
     * @return
     */

    SqsQueue lookupQueueByName(String queueName)
        throws SqsException;

    /**
     * The CreateQueue action creates a new queue. An optional queue name may be provided to associate
     * with the queue for future reference, but the queue name must be unique among the queues associated
     * with the Access Key ID provided in the request. All queues are assigned a queue ID, which is unique to
     * each AWS account signed up for SQS. The URL to the queue, the Queue URL, is used to specify the
     * queue on which to perform actions.
     *
     * @param queueName
     * @param visibilityTimeout
     * @return
     */

    SqsQueue createQueue(String queueName, int visibilityTimeout)
        throws SqsException;

    /**
     * The CreateQueue action creates a new queue. An optional queue name may be provided to associate
     * with the queue for future reference, but the queue name must be unique among the queues associated
     * with the Access Key ID provided in the request. All queues are assigned a queue ID, which is unique to
     * each AWS account signed up for SQS. The URL to the queue, the Queue URL, is used to specify the
     * queue on which to perform actions.
     *
     * @param queueName
     * @return
     */

    SqsQueue createQueue(String queueName)
        throws SqsException;

    /**
     * The ListQueues action returns a list of the queues associated with the AWS account represented by the
     * Access Key ID included in the request. A maximum of 10,000 queue URLs are returned.
     *
     * @return
     * @throws SqsException
     */

    List<SqsQueue> listQueues()
        throws SqsException;

    /**
     * The ListQueues action returns a list of the queues associated with the AWS account represented by the
     * Access Key ID included in the request. A maximum of 10,000 queue URLs are returned. If a value is
     * specified for the optional QueueNamePrefix parameter, only those queues with a queue name
     * beginning with the value specified are returned. The queue name is specified in the QueueName
     * parameter when a queue is created.
     *
     * @param queueNamePrefix
     * @return
     * @throws SqsException
     */

    List<SqsQueue> listQueues(String queueNamePrefix)
        throws SqsException;

    /**
     * Deletes the queue specified by the queue URL. A Queue is deleted only if it is does not contain any
     * messages. Use {@link #deleteMessage} to delete messages from the queue.
     *
     * @param queue
     */

    void deleteQueue(SqsQueue queue)
        throws SqsException;

    /**
     *
     * @param queue
     * @return
     */

    long getApproximateNumberOfMessages(SqsQueue queue)
        throws SqsException;

    /**
     * The SendMessage action delivers a message to the specified queue. A queue is represented by a queue
     * URL. The content of the message is specified in the MessageBody parameter, and can be any text.
     * However, the message body text must be URL-encoded.
     *
     * @param queue
     * @param body
     */

    SqsMessage sendMessage(SqsQueue queue, String body)
        throws SqsException;

    /**
     * Retrieves one message from the queue specified. The message returned by this action stays in the queue until
     * deleted. However, once a message is returned to a receiveMessage request, it will not be returned on subsequent
     * receiveMessage requests until the duration of the visibilityTimeout has passed.
     *
     * @param queue
     * @param visibilityTimeout
     */

    SqsMessage receiveMessage(SqsQueue queue, int visibilityTimeout)
        throws SqsException;

    /**
     * Retrieves one message from the queue specified. The message returned by this action stays in the queue until
     * deleted. However, once a message is returned to a receiveMessage request, it will not be returned on subsequent
     * receiveMessage requests until the duration of the visibilityTimeout has passed.
     *
     * @param queue the queue from which to receive a message
     */

    SqsMessage receiveMessage(SqsQueue queue)
        throws SqsException;

    /**
     * Retrieves one or more messages from the queue specified, returning the message body and message ID
     * of each message. Messages returned by this action stay in the queue until deleted. However, once a
     * message is returned to a ReceiveMessage request, it will not be returned on subsequent
     * ReceiveMessage requests until the duration of the VisibilityTimeout has passed.
     *
     * @param visibilityTimeout
     * @param queue the queue from which to receive a message
     * @return a list of messages received from the queue
     */

    List<SqsMessage> receiveMessages(SqsQueue queue, int visibilityTimeout, int numberOfMessages)
        throws SqsException;

    /**
     * Retrieves one or more messages from the queue specified, returning the message body and message ID
     * of each message. Messages returned by this action stay in the queue until deleted. However, once a
     * message is returned to a ReceiveMessage request, it will not be returned on subsequent
     * ReceiveMessage requests until the duration of the VisibilityTimeout has passed.
     *
     * @param queue the queue from which to receive a message
     * @param maxNumberOfMessages the maximum amount of messages to receive from the queue
     * @return a list of messages received from the queue
     *
     * @throws SqsException when an error occurs in the SQS service
     */

    List<SqsMessage> receiveMessages(SqsQueue queue, int maxNumberOfMessages)
        throws SqsException;

// TODO Not sure if I actually want this in the API.

//    /**
//     *
//     * @param queue the queue to which to send the object
//     * @param object the object to send
//     * @throws SqsException when an error occurs in the SQS service
//     */
//
//    void sendObject(SqsQueue queue, Serializable object)
//            throws SqsException;
//
//    /**
//     *
//     * @param queue
//     * @param clazz
//     * @param <T>
//     * @return
//     * @throws SqsException
//     */
//
//    <T extends Serializable> T receiveObject(Class<T> clazz, SqsQueue queue)
//            throws SqsException;
    
    /**
     * The DeleteMessage action removes the specified message from the queue. Messages stay in the queue
     * until they are deleted with a DeleteMessage request.
     *
     * @param queue
     * @param messageId
     */

    void deleteMessageById(SqsQueue queue, String messageId)
        throws SqsException;

    /**
     * The DeleteMessage action removes the specified message from the queue. Messages stay in the queue
     * until they are deleted with a DeleteMessage request.
     *
     * @param queue
     * @param message
     */

    void deleteMessage(SqsQueue queue, SqsMessage message)
        throws SqsException;

    /**
     * The PeekMessage action returns a preview of the message specified in the MessageId parameter. The
     * message is returned regardless of the VisibilityTimeout state on the queue. The visibility state is
     * not modified when PeekMessage is used, thereby not affecting which messages get returned from a
     * subsequent ReceiveMessage request.
     *
     * @param messageId
     * @return
     */

    SqsMessage peekMessage(SqsQueue queue, String messageId);

    /**
     *
     * @param message
     * @param visibilityTimeout
     */

    void changeMessageVisibility(SqsQueue queue, SqsMessage message, int visibilityTimeout);

    /**
     *
     * @param messageId
     * @param visibilityTimeout
     */

    void changeMessageVisibility(SqsQueue queue, String messageId, int visibilityTimeout);

    /**
     * Sets the amount of time, VisibilityTimeout, messages are hidden from subsequent read requests
     * after being retrieved by a ReceiveMessage request. The value is set in seconds, and measured from the
     * time of the request.
     *
     * The VisibilityTimeout value is set for all messages in the queue. It cannot be set for a specific
     * message.
     *
     * The visibility timeout for a message indicates whether to return the message to a request. If a message is
     * returned, it will not be returned on subsequent ReceiveMessage requests for the duration of the
     * VisibilityTimeout period. Once that duration has passed, the message will again be returned to a
     * ReceiveMessage request unless it is deleted from the queue.
     *
     * @param queue
     * @param visibilityTimeout
     * @throws SqsException
     */

    void setVisibilityTimeout(SqsQueue queue, int visibilityTimeout)
        throws SqsException;

    /**
     * The GetVisibilityTimeout action gets the VisibilityTimeout value set on the queue specified.
     *
     * The VisibilityTimeout is the amount of time a message in a queue is not returned to
     * ReceiveMessage requests. For example, message A is in a queue and has a visibility timeout set at 30
     * seconds. A ReceiveMessage request is made on that queue, and Message A is returned. A subsequent
     * ReceiveMessage reqeust is made in 10 seconds (within duration of VisibilityTimeout of 30
     * seconds). Message A is not returned to that request. Another request is made 20 seconds after the first
     * request. Message A is again not returned. Any request made after 30 seconds has passed will return
     * Message A until it is deleted from the queue.
     *
     * Messages that are meant to be received only once should be deleted, using DeleteMessage within the
     * duration of the VisibilityTimeout.
     *
     * @param queue
     * @return
     * @throws SqsException
     */

    int getVisibilityTimeout(SqsQueue queue)
        throws SqsException;
}
