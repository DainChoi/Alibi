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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity  implements BeaconConsumer, AutoPermissionsListener {

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

    TextView textView;
    private BeaconManager beaconManager;
    String beaconUUID="E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"; // beacon -uuid
    private String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.tv_message);
        AutoPermissions.Companion.loadAllPermissions(this,101); // AutoPermissions
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

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
                Log.e("Frag4", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

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

    @Override
    public void onBeaconServiceConnect() { beaconManager.removeAllMonitorNotifiers();

        class ThreadTest implements Runnable {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        beaconManager.setRangeNotifier(new RangeNotifier()
                        {
                            @Override
                            public void didRangeBeaconsInRegion(Collection beacons, Region region)
                            {
                                if (beacons.size() > 0)
                                {
                                    Log.i(TAG, "The first beacon I see is about "+((Beacon)beacons.iterator().next()).getDistance()+" meters away.");
                                }
                            }
                        });

                        beaconManager.addMonitorNotifier(new MonitorNotifier() {
                            @Override
                            public void didEnterRegion(Region region) {
                                Log.i(TAG, "I just saw an beacon for the first time!");
                                Toast.makeText(MainActivity.this, "didEnterRegion - 비콘 연결됨", Toast.LENGTH_SHORT).show();
                                textView.setText("Beacon connected");

                            }

                            @Override
                            public void didExitRegion(Region region) {
                                Log.i(TAG, "I no longer see an beacon");
                                Toast.makeText(MainActivity.this, "didExitRegion - 비콘 연결 끊김", Toast.LENGTH_SHORT).show();
                                textView.setText("Beacon disconnected");
                            }

                            @Override
                            public void didDetermineStateForRegion(int state, Region region) {
                                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
                            }

                        });
                        try {
                            beaconManager.startMonitoringBeaconsInRegion(new Region("beacon", Identifier.parse(beaconUUID), null, null));
                        } catch (RemoteException e) {    }
                        try
                        {
                            beaconManager.startRangingBeaconsInRegion(new Region("beacon", Identifier.parse(beaconUUID), null, null));
                        }
                        catch (RemoteException e)
                        {
                        }

                    }

                }, 0);
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }






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
