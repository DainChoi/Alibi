package com.dproject.alibi;

import android.app.Activity;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ImageButton btn_modify2;
    private ArrayList<MyWork> arrayList;


    public CustomAdapter(ArrayList<MyWork> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
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


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView work_num_txt;
        TextView  work_title_txt, work_id_txt, work_address_txt;
        LinearLayout mainLayout;
        ImageButton btn_modify2;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            work_num_txt = itemView.findViewById(R.id.work_num_txt);
            work_title_txt = itemView.findViewById(R.id.work_title_txt);
            work_id_txt = itemView.findViewById(R.id.work_id_txt);
            work_address_txt = itemView.findViewById(R.id.work_address_txt);
           // btn_modify2 = itemView.findViewById(R.id.btn_modify2);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            //Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            //mainLayout.setAnimation(translate_anim);
        }

    }

}