package com.zhou.todoapp.manager;

import com.zhou.todoapp.model.AuthResult;
import com.zhou.todoapp.network.mock.AuthServiceImpl;
import com.zhou.todoapp.network.service.AuthService;

import retrofit.Callback;

public class AuthManager {

   public final static AuthManager INSTANCE = new AuthManager();

   private AuthService authService;

   private AuthManager() {
      authService = new AuthServiceImpl();
   }

   public void auth(String username, String password, Callback<AuthResult> cb) {
      authService.auth(username, password, cb);
   }
}
