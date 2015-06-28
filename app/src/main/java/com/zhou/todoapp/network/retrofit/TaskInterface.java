package com.zhou.todoapp.network.retrofit;

import com.zhou.todoapp.model.TaskList;

import retrofit.Callback;
import retrofit.http.GET;

public interface TaskInterface {

   @GET("/task")
   void getTasks(Callback<TaskList> cb);
}
