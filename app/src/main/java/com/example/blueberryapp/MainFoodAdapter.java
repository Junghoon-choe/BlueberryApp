package com.example.blueberryapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainFoodAdapter extends RecyclerView.Adapter <MainFoodAdapter.ViewHolder> implements OnMainFoodItemClickListener {


    private ArrayList<MainData> mainDataArrayList;
    OnMainFoodItemClickListener foodItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView 품, 품가격;
        private ImageView 품사진;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.품사진 = itemView.findViewById(R.id.food_item_image);
            this.품 = itemView.findViewById(R.id.food_item_name);
            this.품가격 = itemView.findViewById(R.id.food_item_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (foodItemClickListener != null){
                        foodItemClickListener.onMainItemClick(ViewHolder.this, itemView,position);
                    }
                }
            });
        }
    }

    public MainFoodAdapter(ArrayList<MainData> list) {
        this.mainDataArrayList = list; //컨스트럭쳐로 만듬
    }

    @NonNull
    @Override
    //뷰홀더객체가 생성될때
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setOnMainItemClickListener(OnMainFoodItemClickListener onMainFoodItemClickListener) {
        this.foodItemClickListener = onMainFoodItemClickListener;
    }

    @Override
    public void onMainItemClick(ViewHolder viewHolder, View itemView, int position) {
        if (foodItemClickListener !=null){
            foodItemClickListener.onMainItemClick(viewHolder,itemView,position);
        }
    }




    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        holder.품.setText(mainDataArrayList.get(position).getFood_item_name());
        Log.v(mainDataArrayList.get(position).getFood_item_name()+"상품이름","실행");
        holder.품가격.setText(mainDataArrayList.get(position).getFood_item_price());
        Log.v(mainDataArrayList.get(position).getFood_item_price()+"상품가격","실행");




        /*holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.품.getText().toString();
                String curPrice = holder.품가격.getText().toString();
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return (null != mainDataArrayList ? mainDataArrayList.size() : 0);
    }



    public MainData getItem(int position) {
        return mainDataArrayList.get(position);
    }

    public void setItems(ArrayList<MainData> items) {
        this.mainDataArrayList = items;
    }

    public void addItem(MainData item) {
        mainDataArrayList.add(item);
    }

    public void setItem(int position, MainData item) {
        mainDataArrayList.set(position, item);
    }



}


