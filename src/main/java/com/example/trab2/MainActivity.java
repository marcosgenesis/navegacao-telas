package com.example.trab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_ADD = 1;
    public static int REQUEST_EDIT = 2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        showTasksList();

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

    public void showTasksList(){
        tasks.clear();
        db.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaskModel taskmodel = new TaskModel();
                                taskmodel.setId((String) document.get("id"));
                                taskmodel.setTitle((String) document.get("title"));
                                taskmodel.setDescription((String) document.get("description"));
                                tasks.add(taskmodel);
                            }
                            System.out.println("tasks ta aqui" + tasks);
                            adapter = new ArrayAdapter<TaskModel>(MainActivity.this, android.R.layout.simple_list_item_1,tasks);
                            listTasks.setAdapter(adapter);
                        } else {
                            System.out.println("Fail to list tasks");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TaskModel task = null;
        if (requestCode == REQUEST_ADD && resultCode == ContactActivity.RESULT_ADD) {
            if (data.getExtras() != null) {
                task = (TaskModel) data.getExtras().get("task");

                db.collection("tasks").add(task);
                showTasksList();
                adapter.notifyDataSetChanged();
            }
        }else if (requestCode == REQUEST_EDIT && resultCode == ContactActivity.RESULT_ADD){
            if (data.getExtras() != null) {
                task = (TaskModel) data.getExtras().get("task");
                final TaskModel taskResult = task;

                for(TaskModel taskItem : tasks){
                    if (taskItem.getId() == task.getId()){
                        db.collection("tasks")
                                .whereEqualTo("id", task.getId())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                                db.collection("tasks").document(document.getId())
                                                        .update("title", taskResult.getTitle(), "description", taskResult.getDescription());
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                    }
                }
            }
            showTasksList();
        }else if (requestCode == REQUEST_EDIT && resultCode == ContactActivity.RESULT_EXCLUDE){
            if (data.getExtras() != null) {
                task = (TaskModel) data.getExtras().get("task");
                db.collection("tasks").document(task.getId()).delete();
            }
            showTasksList();
        }
    }
}