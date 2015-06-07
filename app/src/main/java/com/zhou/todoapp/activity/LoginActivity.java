package com.zhou.todoapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhou.todoapp.R;
import com.zhou.todoapp.config.GlobalConfig;
import com.zhou.todoapp.model.AuthResult;
import com.zhou.todoapp.service.AuthService;
import com.zhou.todoapp.store.TokenStore;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

   private static final String TAG = "LoginActivity";

   // UI references.
   private AutoCompleteTextView mEmailView;
   private EditText mPasswordView;
   private View mProgressView;
   private View mLoginFormView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      // Set up the login form.
      mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
      populateAutoComplete();

      mPasswordView = (EditText) findViewById(R.id.password);
      mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
         @Override
         public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
               attemptLogin();
               return true;
            }
            return false;
         }
      });

      Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
      mEmailSignInButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View view) {
            attemptLogin();
         }
      });

      mLoginFormView = findViewById(R.id.login_form);
      mProgressView = findViewById(R.id.login_progress);
   }

   private void populateAutoComplete() {
      getLoaderManager().initLoader(0, null, this);
   }


   /**
    * Attempts to sign in or register the account specified by the login form.
    * If there are form errors (invalid email, missing fields, etc.), the
    * errors are presented and no actual login attempt is made.
    */
   public void attemptLogin() {
      // Reset errors.
      mEmailView.setError(null);
      mPasswordView.setError(null);

      // Store values at the time of the login attempt.
      String email = mEmailView.getText().toString();
      String password = mPasswordView.getText().toString();

      boolean cancel = false;
      View focusView = null;


      // Check for a valid password, if the user entered one.
      if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
         mPasswordView.setError(getString(R.string.error_invalid_password));
         focusView = mPasswordView;
         cancel = true;
      }

      // Check for a valid email address.
      if (TextUtils.isEmpty(email)) {
         mEmailView.setError(getString(R.string.error_field_required));
         focusView = mEmailView;
         cancel = true;
      } else if (!isEmailValid(email)) {
         mEmailView.setError(getString(R.string.error_invalid_email));
         focusView = mEmailView;
         cancel = true;
      }

      if (cancel) {
         // There was an error; don't attempt login and focus the first
         // form field with an error.
         focusView.requestFocus();
      } else {
         // Show a progress spinner, and kick off a background task to
         // perform the user login attempt.
         showProgress(true);
         auth(email, password);
      }
   }

   private void auth(String username, String password) {
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

      AuthService authService = restAdapter.create(AuthService.class);

      authService.auth(username, password, new Callback<AuthResult>() {
         @Override
         public void success(AuthResult authResult, Response response) {
            showProgress(false);

            Log.d(TAG, "Got response for token: " + authResult);

            TokenStore.INSTANCE.setAuthResult(authResult);

            // Start tasks activity
            Intent tasksIntent = new Intent(LoginActivity.this, TasksActivity.class);
            LoginActivity.this.startActivity(tasksIntent);
         }

         @Override
         public void failure(RetrofitError error) {
            showProgress(false);

            Log.e(TAG, "error: " + error);

            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
         }
      });
   }

   private boolean isEmailValid(String email) {
      return true;
   }

   private boolean isPasswordValid(String password) {
      return password.length() > 4;
   }

   /**
    * Shows the progress UI and hides the login form.
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
   public void showProgress(final boolean show) {
      // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
      // for very easy animations. If available, use these APIs to fade-in
      // the progress spinner.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
         int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

         mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
         mLoginFormView.animate().setDuration(shortAnimTime).alpha(
            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
               mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
         });

         mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
         mProgressView.animate().setDuration(shortAnimTime).alpha(
            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
               mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
         });
      } else {
         // The ViewPropertyAnimator APIs are not available, so simply show
         // and hide the relevant UI components.
         mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
         mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
   }

   @Override
   public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
      return new CursorLoader(this,
         // Retrieve data rows for the device user's 'profile' contact.
         Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
            ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

         // Select only email addresses.
         ContactsContract.Contacts.Data.MIMETYPE +
            " = ?", new String[]{ContactsContract.CommonDataKinds.Email
         .CONTENT_ITEM_TYPE},

         // Show primary email addresses first. Note that there won't be
         // a primary email address if the user hasn't specified one.
         ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
   }

   @Override
   public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
      List<String> emails = new ArrayList<String>();
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
         emails.add(cursor.getString(ProfileQuery.ADDRESS));
         cursor.moveToNext();
      }

      addEmailsToAutoComplete(emails);
   }

   @Override
   public void onLoaderReset(Loader<Cursor> cursorLoader) {

   }

   private interface ProfileQuery {
      String[] PROJECTION = {
         ContactsContract.CommonDataKinds.Email.ADDRESS,
         ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
      };

      int ADDRESS = 0;
      int IS_PRIMARY = 1;
   }


   private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
      //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
      ArrayAdapter<String> adapter =
         new ArrayAdapter<String>(LoginActivity.this,
            android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

      mEmailView.setAdapter(adapter);
   }
}



