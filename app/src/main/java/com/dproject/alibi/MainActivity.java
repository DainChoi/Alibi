package com.dproject.alibi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
//import android.graphics.Region;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter customAdapter;
    RecyclerView recyclerView;
    ImageButton btn_workadd;
    ImageButton btn_setting;
    ImageView empty_imageview;
    TextView no_data;
    private ArrayList<MyWork> arrayList;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_time_in;

    //비콘
    private BeaconManager beaconManager;
    private Region region;
    private TextView tvId;
    private boolean isConnected;
    //비콘


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //비콘
        tvId = (TextView) findViewById(R.id.tvId);
        beaconManager = new BeaconManager(this);
        //비콘


        recyclerView = findViewById(R.id.recyclerView);
        btn_workadd = findViewById(R.id.btn_workadd);
        btn_setting = findViewById(R.id.btn_setting);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);




        customAdapter = new CustomAdapter(arrayList, MainActivity.this );
        recyclerView.setAdapter(customAdapter); // 리사이클러뷰에 어댑터 연결

        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // UserAccount 객체를 담을 어레이리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Alibi").child("MyWork"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    MyWork mywork = snapshot.getValue(MyWork.class); // UserAccount 객체에 데이터 담음
                    arrayList.add(mywork); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                // adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생 시
                Log.e("MyWork Error", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        //비콘
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(com.estimote.sdk.Region region, List<Beacon> list) {
                if(!list.isEmpty()){
                    Beacon nearestBeacon = list.get(0);
                    Log.d("Airport", "Nearest places : " + nearestBeacon.getRssi());

                    tvId.setText(nearestBeacon.getRssi() + "");

                    if(!isConnected && nearestBeacon.getRssi() > -70){
                        isConnected = true;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("알림").setMessage("비콘연결")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                    }
                                }).create().show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "연결종료", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 40010, 55026);
        //비콘


        customAdapter = new CustomAdapter(arrayList, MainActivity.this);
        recyclerView.setAdapter(customAdapter); // 리사이클러뷰에 어댑터 연결


        ImageButton btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });



        ImageButton btn_workadd = findViewById(R.id.btn_workadd);
        btn_workadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WorkaddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 탈퇴 처리
        // mFirebaseAuth.getCurrentUser().delete();


    }
    //비콘
    @Override
    protected void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager.connect((new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        }));
    }


    //비콘




    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    */




}
