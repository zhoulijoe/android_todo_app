package com.zhou.todoapp.network.service;

import android.util.Base64;

import com.zhou.todoapp.config.GlobalConfig;
import com.zhou.todoapp.model.AuthResult;
import com.zhou.todoapp.network.retrofit.AuthInterface;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class AuthServiceImpl implements AuthService {

   @Override
   public void auth(String username, String password, Callback<AuthResult> cb) {
      RestAdapter restAdapter = new RestAdapter.Builder()
         .setEndpoint(GlobalConfig.API_SERVER)
         .setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
               request.addHeader("Authorization", basic("todo-client", "secret"));
               request.addHeader("Accept", "*/*");
            }

            public String basic(String userName, String password) {
               String usernameAndPassword = userName + ":" + password;
               String encoded = Base64.encodeToString(usernameAndPassword.getBytes(), Base64.DEFAULT);
               return "Basic " + encoded;
            }
         })
         .build();

      AuthInterface authInterface = restAdapter.create(AuthInterface.class);

      authInterface.auth(username, password, cb);
   }
}
