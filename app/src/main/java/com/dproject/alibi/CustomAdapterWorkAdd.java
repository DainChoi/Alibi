package com.dproject.alibi;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class CustomAdapterWorkAdd extends RecyclerView.Adapter<CustomAdapterWorkAdd.CustomViewHolder> {

    private ArrayList<Work> arrayList;
    private ArrayList<Work> filteredUserDataList;

    private Context context;
    DatabaseReference databaseReference;


    public CustomAdapterWorkAdd(ArrayList<Work> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.filteredUserDataList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_work_add, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Alibi").child("MyWork");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final Work work = arrayList.get(position);
        //  recyclerview로 나타나게
        holder.work_title_txt.setText(String.valueOf(arrayList.get(position).getTitle()));
        holder.work_address_txt.setText(arrayList.get(position).getAddress());
        holder.work_id_txt.setText(arrayList.get(position).getWorkid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("근무지 추가하기");
                builder.setMessage("해당 근무지를 추가하시겠습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                        String title = holder.work_title_txt.getText().toString();
                        String address = holder.work_address_txt.getText().toString();
                        String workid = holder.work_id_txt.getText().toString();
                        MyWork mywork = new MyWork(title, workid, address);
                        // databaseReference.push().setValue(mywork);
                        databaseReference.child(mywork.getWorkid()).setValue(mywork);


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return filteredUserDataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView work_title_txt;
        TextView work_address_txt;
        TextView work_id_txt;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.work_title_txt = itemView.findViewById(R.id.work_title_txt);
            this.work_address_txt = itemView.findViewById(R.id.work_address_txt);
            this.work_id_txt = itemView.findViewById(R.id.work_id_txt);
        }

    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if (Key.isEmpty()) {
                    filteredUserDataList = arrayList;
                } else {
                    ArrayList<Work> listFiltered = new ArrayList<>();
                    for (Work row : arrayList) {
                        if (row.getWorkid().toLowerCase().contains(Key.toLowerCase())) {
                            listFiltered.add(row);
                        }
                    }
                    filteredUserDataList = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUserDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filteredUserDataList = (ArrayList<Work>) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }
}
