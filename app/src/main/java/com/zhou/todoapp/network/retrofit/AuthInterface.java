package com.zhou.todoapp.network.retrofit;

import com.zhou.todoapp.model.AuthResult;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

public interface AuthInterface {

   @POST("/oauth/token?grant_type=password&scope=read")
   public void auth(@Query("username")String username, @Query("password")String password, Callback<AuthResult> cb);
}
