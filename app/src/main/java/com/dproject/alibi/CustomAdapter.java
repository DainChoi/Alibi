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
    private ArrayList work_num, work_title, work_id, work_address;

    CustomAdapter(Activity activity, Context context, ArrayList work_num, ArrayList work_title, ArrayList work_id,
                  ArrayList work_address){
        this.activity = activity;
        this.context = context;
        this.work_num = work_num;
        this.work_title = work_title;
        this.work_id = work_id;
        this.work_address = work_address;
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
        //holder.work_num_txt.setText(String.valueOf(work_num.get(position)));
        holder.work_title_txt.setText(String.valueOf(work_title.get(position)));
        holder.work_id_txt.setText(String.valueOf(work_id.get(position)));
        holder.work_address_txt.setText(String.valueOf(work_address.get(position)));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("num", String.valueOf(work_num.get(position)));
                intent.putExtra("title", String.valueOf(work_title.get(position)));
                intent.putExtra("id", String.valueOf(work_id.get(position)));
                intent.putExtra("address", String.valueOf(work_address.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return work_num.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView work_num_txt;
        TextView  work_title_txt, work_id_txt, work_address_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            work_num_txt = itemView.findViewById(R.id.work_num_txt);
            work_title_txt = itemView.findViewById(R.id.work_title_txt);
            work_id_txt = itemView.findViewById(R.id.work_id_txt);
            work_address_txt = itemView.findViewById(R.id.work_address_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}