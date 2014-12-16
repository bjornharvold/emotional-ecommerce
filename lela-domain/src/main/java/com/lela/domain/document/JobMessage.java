package com.lela.domain.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 1:53 PM
 */
@Document
public class JobMessage extends AbstractDocument {
    @Indexed
    private ObjectId jobExecutionId;
    private String message;
    private String exception;

    public ObjectId getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(ObjectId jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
