package com.dproject.alibi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Frag1 extends Fragment {

    private View view;
    private DatabaseReference databaseReference;
    private TextView tv_time_in, tv_time_out;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);

        ImageButton btn_back = (ImageButton) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        tv_time_in = (TextView) view.findViewById(R.id.tv_time_in);
        tv_time_out = (TextView) view.findViewById(R.id.tv_time_out);

        databaseReference = FirebaseDatabase.getInstance().getReference("Alibi");
        DatabaseReference timeinRef = databaseReference.child("TimeIn");
        DatabaseReference timeoutRef = databaseReference.child("TimeOut");
/*
        databaseReference.child("TimeIn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String time_in = dataSnapshot.getValue(String.class);
                tv_time_in.setText(time_in); // ERROR
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

 */

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String time_in = ds.child("time_in").getValue(String.class);
                    tv_time_in.setText(time_in); // ERROR: 가장 최근거 하나만 표시됨.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        timeinRef.addListenerForSingleValueEvent(eventListener);

        ValueEventListener eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String time_out = ds.child("time_out").getValue(String.class);
                    tv_time_out.setText(time_out); // ERROR: 가장 최근거 하나만 표시됨.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        timeoutRef.addListenerForSingleValueEvent(eventListener2);
        TextView whenDate = (TextView) view.findViewById(R.id.textView2);
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // todo
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                whenDate.setText(date); // 선택한 날짜로 설정

            }
        });

        return view;
    }
}
