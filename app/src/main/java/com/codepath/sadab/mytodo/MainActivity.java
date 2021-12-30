package com.codepath.sadab.mytodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_TASK_TEXT="task_text";
    public static final String KEY_TASK_POSITION="task_position";
    public static final int EDIT_TEXT_CODE=10;

    List<String> tasks;

    Button btnAdd;
    EditText editItem;
    RecyclerView rvItems;
    TasksAdapter tasksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        editItem=findViewById(R.id.editItem);
        rvItems=findViewById(R.id.rvItems);

        tasks=new ArrayList<>();
        loadTasks();
        TasksAdapter.OnLongClickListener onLongClickListener=new TasksAdapter.OnLongClickListener() {
            @Override
            public void onTaskLongClicked(int position) {
               tasks.remove(position);
               tasksAdapter.notifyItemRemoved(position);
               Toast.makeText(getApplicationContext(),"Task removed",Toast.LENGTH_SHORT).show();
               saveTasks();
            }
        };
        TasksAdapter.OnClickListener onClickListener=new TasksAdapter.OnClickListener() {
            @Override
            public void onTaskClicked(int position) {
                Intent i=new Intent(MainActivity.this,EditActivity.class);
                i.putExtra(KEY_TASK_TEXT,tasks.get(position));
                i.putExtra(KEY_TASK_POSITION,position);
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };
        tasksAdapter= new TasksAdapter(tasks, onLongClickListener,onClickListener);
        rvItems.setAdapter(tasksAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem=editItem.getText().toString();
                tasks.add(todoItem);
                tasksAdapter.notifyItemInserted(tasks.size()-1);
                editItem.setText("");
                Toast.makeText(getApplicationContext(),"Task added!",Toast.LENGTH_SHORT).show();
                saveTasks();
            }
        });
    }
    private File getDataFile(){
        return new File(getFilesDir(),"tasks.txt");
    }
    private void loadTasks(){
        try{
            tasks=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch(IOException e){
            Log.e("MainActivity","Error reading items",e);
            tasks=new ArrayList<>();
        }
    }
    private void saveTasks(){
        try{
            FileUtils.writeLines(getDataFile(),tasks);
        }catch(IOException e){
            Log.e("MainActivity","Error writing items",e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String taskText = data.getStringExtra(KEY_TASK_TEXT);
            int position = data.getExtras().getInt(KEY_TASK_POSITION);
            tasks.set(position, taskText);
            tasksAdapter.notifyItemChanged(position);
            saveTasks();
            Toast.makeText(getApplicationContext(), "Task Updated!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }
}