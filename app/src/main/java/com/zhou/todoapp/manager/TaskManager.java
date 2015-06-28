package com.zhou.todoapp.manager;

import com.zhou.todoapp.model.Task;
import com.zhou.todoapp.model.TaskList;
import com.zhou.todoapp.network.mock.TaskServiceImpl;
import com.zhou.todoapp.network.service.TaskService;

import retrofit.Callback;

public class TaskManager {

   public final static TaskManager INSTANCE = new TaskManager();

   TaskService taskService;

   private TaskManager() {
      taskService = new TaskServiceImpl();
   }

   public void getTasks(Callback<TaskList> cb) {
      taskService.getTasks(cb);
   }

   public void addTask(Task task, Callback<Task> cb) {
      taskService.addTask(task, cb);
   }

   public void deleteTask(Task task, Callback cb) {
      taskService.deleteTask(task, cb);
   }

   public void updateTask(Task task, Callback<Task> cb) {
      taskService.updateTask(task, cb);
   }
}
