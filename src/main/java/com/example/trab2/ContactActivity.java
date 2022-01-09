package com.example.trab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class ContactActivity extends AppCompatActivity {
    public static int RESULT_ADD = 1;
    public static int RESULT_CANCEL = 2;
    public static int RESULT_EXCLUDE = 3;
    public int id;
    EditText edtTitle, edtDescription,edtId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtId = (EditText) findViewById(R.id.edtId);
        edtId.setEnabled(false);

        if (getIntent().getExtras() != null){

            id = getIntent().getExtras().getSerializable("id") == null ? -1 : (int) getIntent().getExtras().getSerializable("id");

            if (id > -1){
                edtId.setText(String.valueOf(id));
            }else{
                TaskModel task = (TaskModel) getIntent().getExtras().get("task");
                edtId.setText(String.valueOf(task.getId()));
                edtTitle.setText(task.getTitle());
                edtDescription.setText(task.getDescription());
            }

        }

    }
    public void adicionar(View view){
        Intent intent = new Intent();
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();

        TaskModel task = new TaskModel(edtId.getText().toString(),title,description);

        intent.putExtra("task",task);
        setResult(RESULT_ADD,intent);
        finish();
    }
    public void cancelar(View view){
        setResult(RESULT_CANCEL);
        finish();
    }
    public void excluir(View view){
        Intent intent = new Intent();
        intent.putExtra("taskId",edtId.getText().toString());
        setResult(RESULT_EXCLUDE,intent);
        finish();
    }

}