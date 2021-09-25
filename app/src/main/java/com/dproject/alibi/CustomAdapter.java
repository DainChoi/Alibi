package com.dproject.alibi;

// MainActivity recyclerview adapter

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    public ImageButton btn_in, btn_out;
    private ArrayList<MyWork> arrayList;
    private TextView tv_time_in, tv_time_out;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_time_in;
    DatabaseReference databaseReference_time_out;

    final static MainActivity mainActivity = new MainActivity();
    //static Double beacon_distance = mainActivity.distance; // MainActivity > 변수 수령
    Double beacon_distance = 0.5; // 변수가 전달이 안되서 임시로 임의의 변수 집어 넣음
    int workable = 0;



    public CustomAdapter(ArrayList<MyWork> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Alibi").child("MyWork");
        tv_time_in = view.findViewById(R.id.time_in);
        tv_time_out = view.findViewById(R.id.time_out);
        databaseReference_time_in = FirebaseDatabase.getInstance().getReference("Alibi").child("TimeIn");
        databaseReference_time_out = FirebaseDatabase.getInstance().getReference("Alibi").child("TimeOut");

        btn_in = view.findViewById(R.id.btn_in);
        btn_out = view.findViewById(R.id.btn_out);

        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.work_title_txt.setText(arrayList.get(position).getTitle());
        holder.work_id_txt.setText(arrayList.get(position).getWorkid());
        holder.work_address_txt.setText(arrayList.get(position).getAddress());

        holder.btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 클릭 되었을때
                if (workable == 0) { // 디폴트 0
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                    String output = dateFormat.format(currentTime);
                    // output = holder.time_in.getText().toString();
                    // time_in.setText(output);
                    TimeIn time_in = new TimeIn(output);
                    databaseReference_time_in.child(time_in.getTime_in()).setValue(time_in);
                    tv_time_in.setText(output);

                    work_clicked();
                    //btn_in.setEnabled(false);
                    //btn_out.setEnabled(true);

                    workable = 1; // 출근 했다는 의미의 1로 workable 변수 변경
                }
            }
        });


        holder.btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workable == 1) { // 출근 한 상태인 1
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                    String output = dateFormat.format(currentTime);
                    TimeOut time_out = new TimeOut(output);
                    databaseReference_time_out.child(time_out.getTime_out()).setValue(time_out);
                    tv_time_out.setText(output);

                    work_clicked();
                    //btn_in.setEnabled(true);
                    //btn_out.setEnabled(false);

                    workable = 0; // 퇴근했으니 workable 변수 0으로 변경
                }
            }
        });


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, WorkActivity.class);
                //activity.startActivityForResult(intent, 1);
                context.startActivity(new Intent(context, WorkActivity.class));


            }
        });

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("근무지 삭제하기");
                builder.setMessage("해당 근무지를 삭제하시겠습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override // ERROR: 해당 항목 삭제 x -> 전체 삭제됨
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        databaseReference.removeValue(); // 데이터베이스에서 항목 제거 //Error: 전부 삭제
                        //  databaseReference.child(OwnerWork.workid).removeValue();
                        deleteItem(position); // 리사이클러뷰 다시 정렬/*


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                return true;
            }
        });


    }


    private void deleteItem(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void work_clicked() {
        if (beacon_distance <= 1) {
            switch (workable) {
                case 0:
                    btn_in.setEnabled(false);
                    btn_out.setEnabled(true);
                    //workable = 0;
                    break;

                case 1:
                    btn_in.setEnabled(true);
                    btn_out.setEnabled(false);
                    //workable = 1;
                    break;
            }
        }
        else {
            btn_in.setEnabled(false);
            btn_out.setEnabled(false);
        }

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView work_num_txt;
        TextView work_title_txt, work_id_txt, work_address_txt;
        LinearLayout mainLayout;
        ImageButton btn_in, btn_out;
        TextView tv_time_in, tv_time_out;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            work_num_txt = itemView.findViewById(R.id.work_num_txt);
            work_title_txt = itemView.findViewById(R.id.work_title_txt);
            work_id_txt = itemView.findViewById(R.id.work_id_txt);
            work_address_txt = itemView.findViewById(R.id.work_address_txt);
            btn_in = itemView.findViewById(R.id.btn_in);
            btn_out = itemView.findViewById(R.id.btn_out);
            tv_time_in = itemView.findViewById(R.id.time_in);
            tv_time_out = itemView.findViewById(R.id.time_out);

            mainLayout = itemView.findViewById(R.id.mainLayout);

            if(beacon_distance > 1){
                btn_in.setEnabled(false);
                btn_out.setEnabled(false);
            }




            //Animate Recyclerview
            //Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            //mainLayout.setAnimation(translate_anim);
        }


    }

}
