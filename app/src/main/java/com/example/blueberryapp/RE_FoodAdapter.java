package com.example.blueberryapp;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RE_FoodAdapter extends RecyclerView.Adapter<RE_FoodAdapter.ViewHolder> implements OnFoodItemClickListener {
    //implements OnFoodItemClickListener

    private static final String Tag = "RecyclerView";
    public static final String IMAGE_INFO = "ImageInfo : ";
    private Context mContext;
    private ArrayList<RE_Food> FoodList;
    private OnItemClickListener mListener;


    public RE_FoodAdapter(Context context, ArrayList<RE_Food> foodList) {
        this.mContext = context;
        this.FoodList = foodList;
    }

//    public RE_FoodAdapter(ArrayList<RE_Food> foodList) {
//        FoodList = foodList;
//    }


    @NonNull
    @Override
    public RE_FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        RE_FoodAdapter.ViewHolder viewHolder = new RE_FoodAdapter.ViewHolder(view);

        return new ViewHolder(view);

//        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RE_Food re_foodCurrent = FoodList.get(position);

        //TextView
        holder.상품이름.setText(re_foodCurrent.getFoodName());
        holder.상품가격.setText(re_foodCurrent.getFoodPrice()); //use for getting Number to adapter


        Glide.with(mContext)
                .asBitmap()
                .fitCenter().centerCrop()
                .load(FoodList.get(position).getImageUrl())
                .into(holder.상품사진);


        Log.d(IMAGE_INFO, "@@@@@@@1" + mContext);
        Log.d(IMAGE_INFO, "@@@@@@@2" + re_foodCurrent.getImageUrl());
        Log.d(IMAGE_INFO, "@@@@@@@3" + holder.상품사진);

    }

    @Override
    public int getItemCount() {
        //삼합 연산자.
        // 참이면 사이즈 실행 거짓이면 0 실행.
        return (null != FoodList ? FoodList.size() : 0);
    }


    public RE_Food getItem(int position) {
        return FoodList.get(position);
    }

    public void setItems(ArrayList<RE_Food> items) {
        this.FoodList = items;
    }

    public void addItem(RE_Food item) {
        FoodList.add(item);
    }

    public void setItem(int position, RE_Food item) {
        FoodList.set(position, item);
    }


    @Override
    public void onItemClick(View view, int position) {
        if (foodlistener != null) {
            foodlistener.onItemClick(view, position);
        }
    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {
        this.foodlistener = listener;

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        //Widgets

        public TextView 상품이름, 상품가격;
        public ImageView 상품사진;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            상품사진 = itemView.findViewById(R.id.food_item_image);
            상품이름 = itemView.findViewById(R.id.food_item_name);
            상품가격 = itemView.findViewById(R.id.food_item_price);

            itemView.setOnClickListener(this);
            //메뉴바 구현
            itemView.setOnCreateContextMenuListener(this);
        }

        //onClick부분에 어떻게 반응할지 구현.
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                //포지션을 어댑터 포지션과 일치 시킨다.
                int position = getAdapterPosition();
                //만약 포지션이 리사이클러뷰에 없다면, 리스너에서 포지션을 지정해준다.
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


            menu.setHeaderTitle("선택하세요");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "임시");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "지우기");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }


        //메뉴가 나왔을시 메뉴에서 입력하는 방식 구현
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                //포지션을 어댑터 포지션과 일치 시킨다.
                int position = getAdapterPosition();
                //만약 포지션이 리사이클러뷰에 없다면, 리스너에서 포지션을 지정해준다.
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;

                        case 2:
                            mListener.onDeletClick(position);
                            return true;
                    }


                }
            }
            return false;
        }
    }


    //아이템 클릭 리스너 구현
    static public OnFoodItemClickListener foodlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeletClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
