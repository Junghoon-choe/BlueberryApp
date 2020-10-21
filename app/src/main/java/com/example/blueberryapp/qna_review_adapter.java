package com.example.blueberryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
/*
public class qna_review_adapter extends RecyclerView.Adapter<qna_review_adapter.ViewHolder> implements OnQnaNReviewItemClickListener {

    private Context mContext;
    private ArrayList<qna_review_item> QnRList;
    private OnItemClickListener mListener;

    public qna_review_adapter(Context context, ArrayList<qna_review_item> qnRList) {
        this.mContext = context;
        this.QnRList = qnRList;
    }

    @NonNull
    @Override
    public qna_review_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_review_item, parent, false);
        qna_review_adapter.ViewHolder viewHolder = new qna_review_adapter.ViewHolder(view);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull qna_review_adapter.ViewHolder holder, int position) {

        {
            qna_review_item QnRCurrent = QnRList.get(position);

            //TextView
            holder.회원이름.setText(QnRCurrent.getUserName());
            holder.글제목.setText(QnRCurrent.getTitle());
            holder.글내용.setText(QnRCurrent.getWriting());
            holder.좋아요수.setText(QnRCurrent.getCountGood());
            holder.댓글수.setText(QnRCurrent.getCountComment()); //use for getting Number to adapter

            Glide.with(mContext)
                    .asBitmap()
                    .fitCenter().centerCrop()
                    .load(QnRList.get(position).getUserImageUrl())
                    .into(holder.회원사진);
        }
    }

    @Override
    public int getItemCount() {
        return (null != QnRList ? QnRList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView 회원사진;
        public TextView 회원이름, 글제목, 글내용, 좋아요수, 댓글수;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            회원사진 = itemView.findViewById(R.id.profileImage);
            회원이름 = itemView.findViewById(R.id.TV_UserName);
            글제목 = itemView.findViewById(R.id.TV_글제목);
            글내용 = itemView.findViewById(R.id.TV_글내용);
            좋아요수 = itemView.findViewById(R.id.TV_좋아요수);
            댓글수 = itemView.findViewById(R.id.TV_댓글수);

            itemView.setOnClickListener(this);

        }
    }


    //아이템 클릭 리스너
    @Override
    public void onItemClick(View view, int position) {
        if () {
        }
    }

    public void setOnItemClickListener(OnQnaNReviewItemClickListener listener){
        this.
    }
    *//*

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
        if (foodListener != null) {
            foodListener.onItemClick(view, position);
        }
    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {
        this.foodListener = listener;
    }


    //아이템 클릭 리스너 구현
    static public OnFoodItemClickListener foodListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeletClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

     *//*
    //아이템 클릭 리스너 구현
    static public OnQnaNReviewItemClickListener QnRListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}
*/
