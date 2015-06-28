package com.zhou.todoapp.model;

import com.google.gson.annotations.SerializedName;

public class AuthResult {

   @SerializedName("access_token")
   private String accessToken;

   public AuthResult() {
   }

   public AuthResult(String accessToken) {
      this.accessToken = accessToken;
   }

   public String getAccessToken() {
      return accessToken;
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("AuthResult{");
      sb.append("accessToken='").append(accessToken).append('\'');
      sb.append('}');
      return sb.toString();
   }
}
