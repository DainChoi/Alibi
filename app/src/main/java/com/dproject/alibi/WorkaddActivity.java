package com.dproject.alibi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class WorkaddActivity extends AppCompatActivity {

    EditText title_input, id_input, address_input;
    ImageButton btn_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workadd);

        title_input = findViewById(R.id.title_input);
        id_input = findViewById(R.id.id_input);
        address_input = findViewById(R.id.address_input);
        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(WorkaddActivity.this);
                myDB.addWork(title_input.getText().toString().trim(),
                        id_input.getText().toString().trim(),
                        address_input.getText().toString().trim());
                /*
                Intent intent = new Intent(WorkaddActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); */
            }
        });

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WorkaddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}