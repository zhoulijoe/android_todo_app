package com.zhou.todoapp.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.zhou.todoapp.R;
import com.zhou.todoapp.manager.TaskManager;
import com.zhou.todoapp.model.Task;
import com.zhou.todoapp.model.TaskList;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TasksActivity extends ListActivity {

   private static final String TAG = "TasksActivity";

   private List<Task> tasks;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_tasks);

      queryTasks();
   }

   @Override
   protected void onResume() {
      super.onResume();

      queryTasks();
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
         Intent editTaskIntent = new Intent(TasksActivity.this, EditTaskActivity.class);
         TasksActivity.this.startActivity(editTaskIntent);

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
      TaskManager.INSTANCE.getTasks(new Callback<TaskList>() {

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
