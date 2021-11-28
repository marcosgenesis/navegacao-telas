package com.example.trab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_ADD = 1;
    public static int REQUEST_EDIT = 2;
    List<TaskModel> tasks =  new ArrayList<TaskModel>();
    ArrayAdapter<TaskModel> adapter;
    ListView listTasks;
    Button edtButton;

    TaskModel taskEdited = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtButton = findViewById(R.id.edtBtn);
        edtButton.setEnabled(false);

        listTasks = (ListView) findViewById(R.id.tasks);
        adapter = new ArrayAdapter<TaskModel>(this, android.R.layout.simple_list_item_1,tasks);
        listTasks.setAdapter(adapter);

        listTasks.setSelector(android.R.color.system_accent1_300);

        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               taskEdited = tasks.get(i);
               edtButton.setEnabled(true);
            }
        });
    }

    public void clicarAdicionar(View view){
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("id", tasks.size() + 1);
        startActivityForResult(intent,REQUEST_ADD);
    }

    public void clicarEditar(View view){
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("task", taskEdited);
        startActivityForResult(intent,REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TaskModel task = null;
        if (requestCode == REQUEST_ADD && resultCode == ContactActivity.RESULT_ADD) {
            if (data.getExtras() != null) {
                task = (TaskModel) data.getExtras().get("task");
                tasks.add(task);
                adapter.notifyDataSetChanged();
            }

        }else if (requestCode == REQUEST_EDIT && resultCode == ContactActivity.RESULT_ADD){
            if (data.getExtras() != null) {
                task = (TaskModel) data.getExtras().get("task");
                for(TaskModel taskItem : tasks){
                    if (taskItem.getId() == task.getId()){
                        taskItem.setTitle(task.getTitle());
                        taskItem.setDescription(task.getDescription());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

    }
}