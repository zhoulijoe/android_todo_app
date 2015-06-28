package com.zhou.todoapp.network.service;

import com.zhou.todoapp.config.GlobalConfig;
import com.zhou.todoapp.model.TaskList;
import com.zhou.todoapp.network.retrofit.TaskInterface;
import com.zhou.todoapp.store.TokenStore;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class TaskServiceImpl {

   public void getTasks(Callback<TaskList> cb) {
      RestAdapter restAdapter = (new RestAdapter.Builder())
         .setLogLevel(RestAdapter.LogLevel.FULL)
         .setEndpoint(GlobalConfig.API_SERVER)
         .setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
               request.addHeader("Authorization", "Bearer " + TokenStore.INSTANCE.getAuthResult().getAccessToken());
               request.addHeader("Accept", "*/*");
            }
         })
         .build();
      TaskInterface taskInterface = restAdapter.create(TaskInterface.class);

      taskInterface.getTasks(cb);
   }
}
