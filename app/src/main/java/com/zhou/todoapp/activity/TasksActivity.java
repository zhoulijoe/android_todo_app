package com.zhou.todoapp.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

import com.zhou.todoapp.R;
import com.zhou.todoapp.config.GlobalConfig;
import com.zhou.todoapp.model.Task;
import com.zhou.todoapp.model.TaskList;
import com.zhou.todoapp.service.TaskService;
import com.zhou.todoapp.store.TokenStore;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TasksActivity extends ListActivity {

   private static final String TAG = "TasksActivity";

   private List<Task> tasks;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_tasks);

      this.queryTasks();
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_tasks, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_add_task) {
         Log.d(TAG, "Add a new task");
         return true;
      }

      return super.onOptionsItemSelected(item);
   }

   public void onDoneButtonClick(View view) {

   }

   private void updateUI() {
      ArrayAdapter<Task> listAdapter = new ArrayAdapter<>(
         this,
         R.layout.task_view,
         R.id.taskTextView,
         this.tasks
      );
      this.setListAdapter(listAdapter);
   }

   private void queryTasks() {
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
      TaskService taskService = restAdapter.create(TaskService.class);

      taskService.getTasks(new Callback<TaskList>() {

         @Override
         public void success(TaskList tasks, Response response) {
            Log.d(TAG, "Got tasks: " + tasks.getTasks());

            TasksActivity.this.tasks = tasks.getTasks();

            TasksActivity.this.updateUI();
         }

         @Override
         public void failure(RetrofitError error) {
            Log.e(TAG, "Error: " + error);
         }
      });
   }
}
