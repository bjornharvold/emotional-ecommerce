/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.repository.TaskRepository;
import com.lela.commons.service.TaskService;
import com.lela.commons.service.impl.TaskServiceImpl;
import com.lela.domain.document.Task;
import com.lela.domain.enums.TaskType;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/12
 * Time: 10:02 PM
 * Responsibility:
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskServiceUnitTest {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceUnitTest.class);
    private static final String RCPNT = "TaskServiceUnitTest";

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    private Task task;

    @Test
    public void testTaskService() {
        log.info("First we create a task");
        ObjectId id = new ObjectId();
        ObjectId id2 = new ObjectId();
        task = new Task();
        task.setId(id);
        task.setTp(TaskType.FACEBOOK_DATA_AGGREGATION);
        task.setStps(10);
        task.setCstp(1);
        task.setMsg("Task is initializing...");
        task.setRcpnt(RCPNT);
        List<Task> tasks = new ArrayList<Task>(1);
        tasks.add(task);

        when(taskService.saveTask(task)).thenReturn(task);
        task = taskService.saveTask(task);
        assertNotNull("Task is null", task);

        verify(taskRepository, times(1)).save(task);

        log.info("Task was saved successfully");

        log.info("Time to query the service for tasks");

        when(taskService.findTask(id)).thenReturn(task);
        when(taskService.findTask(id2)).thenReturn(null);

        task = taskService.findTask(id);
        assertNotNull("Task is null", task);

        Task nullTask = taskService.findTask(id2);
        assertNull("This task should exist", nullTask);
        verify(taskRepository, times(1)).findOne(id);
        verify(taskRepository, times(1)).findOne(id2);
        verify(taskRepository, times(2)).findOne(any(ObjectId.class));

        when(taskService.findTasksByRecipient(RCPNT)).thenReturn(tasks);
        tasks = taskService.findTasksByRecipient(RCPNT);
        assertNotNull("Recipient tasks are null", tasks);
        verify(taskRepository, times(1)).findTasksByRecipient(anyString());

        log.info("Task service queried successfully");

        log.info("Remove the task again");

        doNothing().when(taskRepository).delete(id);

        taskService.removeTask(id);

        verify(taskRepository, times(1)).delete(id);
    }
}
