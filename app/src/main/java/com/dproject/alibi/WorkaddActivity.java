package com.dproject.alibi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkaddActivity extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView recyclerView;
    private CustomAdapterWorkAdd customAdapterWorkAdd;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Work> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private CharSequence search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workadd);

        searchBar = (EditText) findViewById(R.id.search_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // UserAccount 객체를 담을 어레이리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Alibi").child("Work"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    Work work = snapshot.getValue(Work.class); // UserAccount 객체에 데이터 담음
                    arrayList.add(work); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                // adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                customAdapterWorkAdd.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생 시
                Log.e("WorkAddActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


        customAdapterWorkAdd = new CustomAdapterWorkAdd(arrayList, this);
        recyclerView.setAdapter(customAdapterWorkAdd); // 리사이클러뷰에 어댑터 연결

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i3) {

                customAdapterWorkAdd.getFilter().filter(charSequence);
                search = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

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