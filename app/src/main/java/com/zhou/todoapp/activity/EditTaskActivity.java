package com.zhou.todoapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhou.todoapp.R;
import com.zhou.todoapp.manager.TaskManager;
import com.zhou.todoapp.model.Task;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditTaskActivity extends Activity {

   private Task taskToEdit;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_edit_task);

      taskToEdit = (Task)getIntent().getSerializableExtra(TasksActivity.BUNDLE_TASK_KEY);

      final EditText summaryEditText = (EditText)findViewById(R.id.task_summary);

      if (taskToEdit != null) {
         summaryEditText.setText(taskToEdit.getDescription());
      }

      Button saveButton = (Button)findViewById(R.id.edit_task_button);
      saveButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String summary = summaryEditText.getText().toString();

            if (EditTaskActivity.this.taskToEdit == null) {
               Task task = new Task(summary);
               TaskManager.INSTANCE.addTask(task, new Callback<Task>() {
                  @Override
                  public void success(Task task, Response response) {
                     finish();
                  }

                  @Override
                  public void failure(RetrofitError error) {
                     finish();
                  }
               });
            } else {
               EditTaskActivity.this.taskToEdit.setDescription(summary);
               TaskManager.INSTANCE.updateTask(EditTaskActivity.this.taskToEdit, new Callback<Task>() {
                  @Override
                  public void success(Task task, Response response) {
                     finish();
                  }

                  @Override
                  public void failure(RetrofitError error) {
                     finish();
                  }
               });
            }
         }
      });
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      return false;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_settings) {
         return true;
      }

      return super.onOptionsItemSelected(item);
   }
}
