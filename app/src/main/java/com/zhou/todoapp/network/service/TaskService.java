package com.zhou.todoapp.network.service;

import com.zhou.todoapp.model.Task;
import com.zhou.todoapp.model.TaskList;

import retrofit.Callback;

public interface TaskService {

   void getTasks(Callback<TaskList> cb);

   void addTask(Task task, Callback<Task> cb);

   void deleteTask(Task task, Callback cb);

   void updateTask(Task task, Callback<Task> cb);
}
