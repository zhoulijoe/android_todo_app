package com.zhou.todoapp.network.service;

import com.zhou.todoapp.model.AuthResult;

import retrofit.Callback;

public interface AuthService {

   public void auth(String username, String password, Callback<AuthResult> cb);
}
