package com.dproject.alibi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ImageButton btn_in, btn_out;
    private ArrayList<MyWork> arrayList;
    private TextView time_in, time_out;
    DatabaseReference databaseReference;


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
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.work_title_txt.setText(arrayList.get(position).getTitle());
        holder.work_id_txt.setText(arrayList.get(position).getWorkid());
        holder.work_address_txt.setText(arrayList.get(position).getAddress());

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

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView work_num_txt;
        TextView  work_title_txt, work_id_txt, work_address_txt;
        LinearLayout mainLayout;
        ImageButton btn_in, btn_out;
        TextView time_in, time_out;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            work_num_txt = itemView.findViewById(R.id.work_num_txt);
            work_title_txt = itemView.findViewById(R.id.work_title_txt);
            work_id_txt = itemView.findViewById(R.id.work_id_txt);
            work_address_txt = itemView.findViewById(R.id.work_address_txt);
            btn_in = itemView.findViewById(R.id.btn_in);
            btn_out = itemView.findViewById(R.id.btn_out);
            time_in = itemView.findViewById(R.id.time_in);
            time_out = itemView.findViewById(R.id.time_out);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            //Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            //mainLayout.setAnimation(translate_anim);
        }

    }

}