package com.dproject.alibi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends  RecyclerView.Adapter<RecycleAdapter.ViewHolder> implements OnItemDataClickListener{

    // 동적배열 선언
    ArrayList<ItemData> itemData = new ArrayList<ItemData>();
    OnItemDataClickListener listener;

    // 각각의 뷰를 보관하는 Holder 객체인 ViewHolder를 재정의한다.
    @NonNull
    @Override
    //n onCreateViewHolder() : 뷰홀더가 만들어질 때 호출됨
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 뷰 변수인 itemview를 선언하고, item.xml을 인플레이트한다.
        View itemview = inflater.inflate(R.layout.item,parent,false);
        return new ViewHolder(itemview,this);
    }

    @Override
    // onBindViewHolder() : 뷰홀더가 재사용될 때 호출됨
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemData data =itemData.get(position);
        holder.setItem(data);
    }

    public void setOnItemClickListner (OnItemDataClickListener listener) {
        this.listener = listener;
    }



    @Override
    // 어댑터에서 관리하는 아이템 개수 반환
    public int getItemCount() {
        return itemData.size();
    }

    public void addItem(ItemData data) {
        itemData.add(data);
    }

    public ItemData getItem(int position) {
        return itemData.get(position);
    }

    public void setItemData(ArrayList<ItemData> itemData) {
        this.itemData = itemData;
    }

    public void setData(int position, ItemData data){
        itemData.set(position, data);
    }

    // OnItemDataClickListener에 담겨있는 OnItemClick을 재정의
    @Override
    public void OnItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.OnItemClick(holder,view,position);
        }
    }

    // RecyclerView.ViewHolder 상속받는 ViewHolder를 내부 클래스로 선언
    static class ViewHolder extends  RecyclerView.ViewHolder {

        ImageView itemPoster;
        TextView itemTitle, itemContent;

        public ViewHolder(@NonNull View itemView, final OnItemDataClickListener listener) {
            super(itemView);
            itemPoster = itemView.findViewById(R.id.itemPoster);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemContent = itemView.findViewById(R.id.itemContent);

            // itemView를 클릭한 경우
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    // NULL값이 아니라면
                    if(listener != null) {
                        listener.OnItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        // setItem메서드 정의
        public void setItem(ItemData data) {
            itemPoster.setImageResource(data.imgID);
            itemTitle.setText(data.title);
            itemContent.setText(data.content);
        }
    }

}
