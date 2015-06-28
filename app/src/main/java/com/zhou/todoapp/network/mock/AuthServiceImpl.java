package com.zhou.todoapp.network.mock;

import com.zhou.todoapp.model.AuthResult;
import com.zhou.todoapp.network.service.AuthService;

import retrofit.Callback;

public class AuthServiceImpl implements AuthService {

   @Override
   public void auth(String username, String password, Callback<AuthResult> cb) {
      cb.success(new AuthResult("mockToken"), null);
   }
}
