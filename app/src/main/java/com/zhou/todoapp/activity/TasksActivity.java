package com.zhou.todoapp.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

   public final static String BUNDLE_TASK_KEY = "com.zhou.todoapp.activity.TasksActivity.taskKey";

   private final static String TAG = "TasksActivity";

   private List<Task> tasks;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_tasks);

      queryTasks();

      setupClickListener();
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
      Log.d(TAG, "done button tag: " + view.getTag());
      final View finalView = view;

      finalView.setEnabled(false);

      Task task = tasks.get((int)finalView.getTag());
      task.setComplete(!task.getComplete());

      TaskManager.INSTANCE.updateTask(task, new Callback<Task>() {
         @Override
         public void success(Task task, Response response) {
            finalView.setEnabled(true);

            TaskAdapter arrayAdapter = (TaskAdapter)TasksActivity.this.getListAdapter();
            arrayAdapter.notifyDataSetChanged();
         }

         @Override
         public void failure(RetrofitError error) {
            finalView.setEnabled(true);
         }
      });
   }

   private void setupClickListener() {
      getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Task task = tasks.get(position);

            Intent editTaskIntent = new Intent(TasksActivity.this, EditTaskActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_TASK_KEY, task);
            editTaskIntent.putExtras(bundle);

            TasksActivity.this.startActivity(editTaskIntent);
         }
      });

      getListView().setOnItemLongClickListener(
         new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               Log.d(TAG, "Long click detected");

               final Task task = tasks.get(position);

               TaskManager.INSTANCE.deleteTask(task, new Callback() {
                  @Override
                  public void success(Object o, Response response) {
                     TasksActivity.this.tasks.remove(task);

                     ArrayAdapter<Task> arrayAdapter = (ArrayAdapter<Task>)TasksActivity.this.getListAdapter();
                     arrayAdapter.notifyDataSetChanged();
                  }

                  @Override
                  public void failure(RetrofitError error) {

                  }
               });

               return true;
            }
         }
      );
   }

   private void updateUI() {
      TaskAdapter listAdapter = new TaskAdapter(
         this,
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
