package com.lela.commons.service;

import com.lela.domain.document.Task;
import com.lela.domain.enums.TaskType;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/12
 * Time: 10:01 PM
 * Responsibility:
 */
public interface TaskService {
    Task saveTask(Task task);
    void removeTask(ObjectId id);
    Task findTask(ObjectId id);
    List<Task> findTasksByRecipient(String recipientId);
    void removeTasksOlderThan(TaskType taskType, Date date);
}
