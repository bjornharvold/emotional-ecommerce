/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.repository.TaskRepository;
import com.lela.commons.service.TaskService;
import com.lela.domain.document.Task;
import com.lela.domain.enums.TaskType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/12
 * Time: 9:46 PM
 * Responsibility: Task service is for handling internal processes that have more than one step,
 *  take time and cannot be handled in real-time.
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void removeTask(ObjectId id) {
        taskRepository.delete(id);
    }

    @Override
    public Task findTask(ObjectId id) {
        return taskRepository.findOne(id);
    }

    @Override
    public List<Task> findTasksByRecipient(String recipientId) {
        return taskRepository.findTasksByRecipient(recipientId);
    }

    @Override
    public void removeTasksOlderThan(TaskType taskType, Date date) {
        taskRepository.removeTasksOlderThan(taskType, date);
    }
}
