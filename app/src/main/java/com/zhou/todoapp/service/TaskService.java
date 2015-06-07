package com.zhou.todoapp.service;

import com.zhou.todoapp.model.TaskList;

import retrofit.Callback;
import retrofit.http.GET;

public interface TaskService {

   @GET("/task")
   void getTasks(Callback<TaskList> cb);
}
