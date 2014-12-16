/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.TaskService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Task;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Bjorn Harvold
 * Date: 8/20/12
 * Time: 10:57 AM
 * Responsibility: Tells the user the status of a task
 */
@Controller
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Task showTask(@PathVariable("id") ObjectId taskId, HttpServletResponse response) {
        Task result = taskService.findTask(taskId);

        if (result == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        return result;
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.DELETE,
            produces = "application/json")
    @ResponseBody
    public String deleteTask(@PathVariable("id") ObjectId taskId, HttpServletResponse response) {
        Task task = taskService.findTask(taskId);
        String result;

        if (task == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            result = WebConstants.FAILURE;
        } else {
            taskService.removeTask(taskId);
            result = WebConstants.SUCCESS;
        }

        return result;
    }
}
