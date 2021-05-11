package com.dproject.alibi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private RecyclerView rv;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerView recycle1;
    RecycleAdapter adapter;

    int imgID[] = {R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo};

    String title[] = {"logo1", "logo2", "logo3", "logo4", "logo5", "logo6", "logo7", "logo8", "logo9", "logo10"};

    String content[] = {"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9", "test10"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycle1 = findViewById(R.id.recycle1);

        adapter = new RecycleAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle1.setLayoutManager(layoutManager);

        for (int i = 0; i < imgID.length; i++){
            adapter.addItem(new ItemData(imgID[i],title[i],content[i]));
        }

        recycle1.setAdapter(adapter);

        adapter.setOnItemClickListner(new OnItemDataClickListener() {

            public void OnItemClick(RecycleAdapter.ViewHolder holder, View view, int position) {
                ItemData data = adapter.getItem(position);
                Toast.makeText(getApplicationContext(),data.title+"가 선택됨",Toast.LENGTH_SHORT).show();

            }
        });


        ImageButton btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn_move = findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WorkActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 탈퇴 처리
        // mFirebaseAuth.getCurrentUser().delete();
    }
}
