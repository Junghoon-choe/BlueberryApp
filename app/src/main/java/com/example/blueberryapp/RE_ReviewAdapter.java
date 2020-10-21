package com.example.blueberryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RE_ReviewAdapter extends RecyclerView.Adapter<RE_ReviewAdapter.ReviewHolder> implements OnReviewItemClickListener {

    private ArrayList<RE_Review>reviewList;
    OnReviewItemClickListener onReviewItemClickListener;

    private static final String Tag = "RecyclerView";
    private Context mContext;

    private String userID = MyApplication.회원ID;


    //1. 컨텍스트 메뉴를 사용하려면 RecyclerView.ViewHolder를 상속받은 클래스에서
    //OnCreateContextMenuListener리스너를 구현해야 한다.



    public class ReviewHolder extends RecyclerView.ViewHolder {



        private TextView review_id;
        private TextView Title;
        private TextView Writing;

        public ReviewHolder(@NonNull final View itemView) {
            super(itemView);

//            this.review_id = itemView.findViewById(R.id.review_id);
//            this.Title = itemView.findViewById(R.id.review_textview);
//            this.Writing = itemView.findViewById(R.id.review_textview2);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        int position = getAdapterPosition();

                        if (onReviewItemClickListener != null){
                            onReviewItemClickListener.onReviewItemClick(ReviewHolder.this,itemView,position);
                        }



                    }
                }
            });

        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
//            //3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를
//            //선택했는지 리스너에서 구분하게 된다.
//
//            MenuItem Edit = menu.add(Menu.NONE, 1001,1,"수정");
//            MenuItem Delete = menu.add(Menu.NONE, 1002,2,"삭제");
//            Edit.setOnMenuItemClickListener(onEditMenu);
//            Delete.setOnMenuItemClickListener(onEditMenu);
//
//        }


//        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener(){
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//
//
//                switch (item.getItemId()){
//                    case 1001: //5. 편집 항목을 선택시
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(reviewContext);
//                        //다이얼로그를 보여주기 위해 edit_box파일을 사용합니다.
//                        View view = LayoutInflater.from(reviewContext).inflate(R.layout.review_editbox,null,false);
//                        builder.setView(view);
//                        final Button ButtonSubmit = view.findViewById(R.id.BT_edit);
//
//                        final EditText editTexttitle = view.findViewById(R.id.review_edit_title);
//                        final EditText editTextwriting = view.findViewById(R.id.review_edit_writing);
//
//                        //6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줍니다.
//                        Log.v("실행",String.valueOf(reviewlist));
//
//                        editTexttitle.setText(reviewlist.get(getAdapterPosition()).getTitle());
//                        editTextwriting.setText(reviewlist.get(getAdapterPosition()).getWriting());
//
//                        final AlertDialog dialog = builder.create();
//                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
//
//                            //7. 수정 버튼을 클릭하면 현재 ui에 입력되어 있는 내용으로~
//                            @Override
//                            public void onClick(View v) {
//
//                                String strid = userID;
//                                String strTitle = editTexttitle.getText().toString();
//                                String strWriting = editTextwriting.getText().toString();
//
//                                RE_Review re_review = new RE_Review(strid,strTitle,strWriting);
//
//                                //8. ListArray에 있는 데이터를 변경하고,
//                                reviewlist.set(getAdapterPosition(),re_review);
//
//                                //9. 어댑터에서 RecyclerView에 반영하도록 합니다.
//                                notifyItemChanged(getAdapterPosition());
//                                dialog.dismiss();
//
//
//                            }
//                        });
//
//                        dialog.show();
//                        break;
//
//                    case 1002:
//
//
//                        reviewlist.remove(getAdapterPosition());
//                        notifyDataSetChanged();
//
//
////                        notifyItemChanged(getAdapterPosition());
////                        notifyItemRangeChanged(getAdapterPosition(),reviewlist.size());
//                        break;
//
//
//                    default:
//
//                }
//                return true;
//
//
//            }
//        };


    }



    public RE_ReviewAdapter(Context mContext, ArrayList<RE_Review> reviewList){
        this.mContext = mContext;
        this.reviewList = reviewList;
    }


    @NonNull
    @Override
    //뷰 홀더 객체가 생성될때
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_list,viewGroup,false);

        ReviewHolder reviewHolder = new ReviewHolder(view);

        return reviewHolder;
    }

    public void setOnReviewItemClickListener (OnReviewItemClickListener onReviewItemClickListener){
        this.onReviewItemClickListener = onReviewItemClickListener;
    } //외부에서 리스너를 설정할 수 있도록 메서드 추가.


    @Override
    public void onReviewItemClick(ReviewHolder reviewHolder, View view, int position) {
        if (onReviewItemClickListener != null){
            onReviewItemClickListener.onReviewItemClick(reviewHolder, view,position);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int position) {


        reviewHolder.review_id.setText(userID);
        reviewHolder.Title.setText(reviewList.get(position).getReViewTitle());
        reviewHolder.Writing.setText(reviewList.get(position).getReViewWriting());



    }



    @Override
    public int getItemCount() {
        return (null != reviewList ? reviewList.size() : 0);
    }

    public void addItem (RE_Review item) {
        reviewList.add(item);
    }

    public void setItems (ArrayList<RE_Review> items){
        this.reviewList = items;
    }

    public RE_Review getItem (int position){
        return reviewList.get(position);
    }

    public void setItem(int position, RE_Review item){
        reviewList.set(position,item);
    }









}
