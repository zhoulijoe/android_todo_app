package com.zhou.todoapp.store;

import com.zhou.todoapp.model.AuthResult;

public class TokenStore {

   public final static TokenStore INSTANCE = new TokenStore();

   private TokenStore() {
   }

   private AuthResult authResult;

   public AuthResult getAuthResult() {
      return authResult;
   }

   public void setAuthResult(AuthResult authResult) {
      this.authResult = authResult;
   }
}
