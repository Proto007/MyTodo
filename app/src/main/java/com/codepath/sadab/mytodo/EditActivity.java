package com.codepath.sadab.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText editTask;
    Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTask=findViewById(R.id.editSingleTask);
        btnEdit=findViewById(R.id.btnEdit);

        getSupportActionBar().setTitle("Edit Task");
        editTask.setText(getIntent().getStringExtra(MainActivity.KEY_TASK_TEXT));
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.putExtra(MainActivity.KEY_TASK_TEXT,editTask.getText().toString());
                intent.putExtra(MainActivity.KEY_TASK_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_TASK_POSITION));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}