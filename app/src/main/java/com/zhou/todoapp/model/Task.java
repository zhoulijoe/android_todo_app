package com.zhou.todoapp.model;

public class Task {

   private String id;

   private String userId;

   private String description;

   private Boolean complete;

   public String getId() {
      return id;
   }

   public String getUserId() {
      return userId;
   }

   public String getDescription() {
      return description;
   }

   public Boolean getComplete() {
      return complete;
   }

   @Override
   public String toString() {
      return this.description;
   }
}
