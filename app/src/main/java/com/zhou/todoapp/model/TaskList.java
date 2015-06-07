package com.zhou.todoapp.model;

import java.util.List;

public class TaskList {

   private List<Task> tasks;

   public List<Task> getTasks() {
      return tasks;
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("TaskList{");
      sb.append("tasks=").append(tasks);
      sb.append('}');
      return sb.toString();
   }
}
