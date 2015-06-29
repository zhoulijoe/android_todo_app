package com.zhou.todoapp.activity;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zhou.todoapp.R;
import com.zhou.todoapp.model.Task;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

   public TaskAdapter(Context context, List<Task> objects) {
      super(context, R.layout.task_view, R.id.taskTextView, objects);

   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);

      Button button = (Button)view.findViewById(R.id.doneButton);
      button.setTag(position);

      Task task = getItem(position);
      TextView summaryTextView = (TextView)view.findViewById(R.id.taskTextView);
      if (task.getComplete()) {
         summaryTextView.setPaintFlags(summaryTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      } else {
         summaryTextView.setPaintFlags(summaryTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      }

      return view;
   }
}
