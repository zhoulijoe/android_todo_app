package com.zhou.todoapp.model;

import java.io.Serializable;

public class Task implements Serializable {

   private String id;

   private String userId;

   private String description;

   private Boolean complete;

   public Task() {
      this(null, null);
   }

   public Task(String description) {
      this(null, description);
   }

   public Task(String id, String description) {
      this.id = id;
      this.description = description;
      this.complete = false;
   }

   public String getId() {
      return id;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Task task = (Task) o;

      return !(id != null ? !id.equals(task.id) : task.id != null);

   }

   @Override
   public int hashCode() {
      return id != null ? id.hashCode() : 0;
   }

   @Override
   public String toString() {
      return this.description;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getUserId() {
      return userId;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Boolean getComplete() {
      return complete;
   }

   public void setComplete(Boolean complete) {
      this.complete = complete;
   }
}
