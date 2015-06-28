package com.zhou.todoapp.network.mock;

import com.zhou.todoapp.model.Task;
import com.zhou.todoapp.model.TaskList;
import com.zhou.todoapp.network.service.TaskService;

import java.util.ArrayList;

import retrofit.Callback;

public class TaskServiceImpl implements TaskService {

   private ArrayList<Task> tasks;

   public TaskServiceImpl() {
      tasks = new ArrayList<Task>();
      int currentId = 0;

      tasks.add(new Task(String.valueOf(currentId++), "Learn Android"));
      tasks.add(new Task(String.valueOf(currentId++), "Buy milk"));
      tasks.add(new Task(String.valueOf(currentId++), "Feed cat"));

      for (int i = 0; i < 20; i++) {
         tasks.add(new Task(String.valueOf(currentId++), "Task " + currentId));
      }
   }

   @Override
   public void getTasks(Callback<TaskList> cb) {
      cb.success(new TaskList(tasks), null);
   }

   @Override
   public void addTask(Task task, Callback<Task> cb) {
      task.setId(String.valueOf(getNextId()));

      tasks.add(task);

      cb.success(task, null);
   }

   @Override
   public void deleteTask(Task task, Callback cb) {
      tasks.remove(task);

      cb.success(null, null);
   }

   @Override
   public void updateTask(Task task, Callback<Task> cb) {
      Task currentTask = tasks.get(tasks.indexOf(task));
      currentTask.setDescription(task.getDescription());
      currentTask.setComplete(task.getComplete());

      cb.success(task, null);
   }

   private int getNextId() {
      int maxId = 1;

      for (Task task : tasks) {
         int idInt = Integer.valueOf(task.getId());
         if (idInt > maxId) {
            maxId = idInt;
         }
      }

      return maxId;
   }
}
