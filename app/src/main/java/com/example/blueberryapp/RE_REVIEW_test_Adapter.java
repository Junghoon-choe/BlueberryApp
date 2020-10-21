package com.example.blueberryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RE_REVIEW_test_Adapter extends RecyclerView.Adapter<RE_REVIEW_test_Adapter.ViewHolder> implements OnFoodItemClickListener {
    //implements OnFoodItemClickListener

    private static final String Tag = "RecyclerView";
    public static final String IMAGE_INFO = "ImageInfo : ";
    private Context mContext;
    private ArrayList<RE_REVIEW_test> List;
    private OnItemClickListener mListener;


    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference ReviewCRef = DB.collection("REVIEW");


    public RE_REVIEW_test_Adapter(Context context, ArrayList<RE_REVIEW_test> foodList) {
        this.mContext = context;
        this.List = foodList;
    }

//    public RE_FoodAdapter(ArrayList<RE_Food> foodList) {
//        FoodList = foodList;
//    }


    @NonNull
    @Override
    public RE_REVIEW_test_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);
        RE_REVIEW_test_Adapter.ViewHolder viewHolder = new RE_REVIEW_test_Adapter.ViewHolder(view);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        RE_REVIEW_test re_review_test = List.get(position);

        //TextView
        holder.상품이름.setText(re_review_test.getTitle_Review());
        holder.상품가격.setText(re_review_test.getWritingReview()); //use for getting Number to adapter
        holder.작성자이름.setText(re_review_test.getUserName());

        Glide.with(mContext)
                .asBitmap()
                .fitCenter().centerCrop()
                .load(List.get(position).getImageUrl())
                .into(holder.상품사진);

        RE_REVIEW_test selectedItem = List.get(position);
        String selectedTitle = selectedItem.getDocuName();
        if (!selectedItem.get작성자이메일().equals(MyApplication.회원Email)) {
            holder.mDeleteImage.setVisibility(View.INVISIBLE);
        }


        holder.mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "삭제클릭", Toast.LENGTH_SHORT).show();


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("삭제");
                builder.setMessage("해당 항목을 삭제하시겠습니까?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    RE_REVIEW_test selectedItem = List.get(position);
                                    String selectedTitle = selectedItem.getDocuName();



                                    ReviewCRef.document(selectedTitle)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(mContext, "아이템 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    List.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, List.size());

                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                builder.show();


            }
        });


        Log.d(IMAGE_INFO, "@@@@@@@1" + mContext);
        Log.d(IMAGE_INFO, "@@@@@@@2" + re_review_test.getImageUrl());
        Log.d(IMAGE_INFO, "@@@@@@@3" + holder.상품사진);

    }

    @Override
    public int getItemCount() {
        //삼합 연산자.
        // 참이면 사이즈 실행 거짓이면 0 실행.
        return (null != List ? List.size() : 0);
    }

    public RE_REVIEW_test getItem(int position) {
        return List.get(position);
    }

    public void setItems(ArrayList<RE_REVIEW_test> items) {
        this.List = items;
    }

    public void addItem(RE_REVIEW_test item) {
        List.add(item);
    }

    public void setItem(int position, RE_REVIEW_test item) {
        List.set(position, item);
    }


    @Override
    public void onItemClick(View view, int position) {
        if (foodListener != null) {
            foodListener.onItemClick(view, position);
        }
    }

    @Override
    public void onItemClick(int position) {
        //이부분에서 삭제 구현.
        List.remove(position);
        notifyItemChanged(position);
    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {
        this.foodListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        //Widgets

        public TextView 상품이름, 상품가격;
        public TextView 작성자이름;
        public ImageView 상품사진;
        public Button BT_수정, BT_삭제;
        public ImageView mDeleteImage;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);


           /* ReviewCRef.whereEqualTo("작성자이메일", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (DocumentSnapshot doc : task.getResult()) {
                                RE_REVIEW_test model = new RE_REVIEW_test(doc.getString("작성자이메일")
                                        , doc.getString("docuName")
                                        , doc.getString("title_Review")
                                        , doc.getString("writingReview")
                                        , doc.getString("imageUrl")
                                        , doc.getString("userName")
                                );

                            }

                        }
                    });*/
//            String 작성자이메일 = ;
            상품사진 = itemView.findViewById(R.id.food_item_image);
            상품이름 = itemView.findViewById(R.id.food_item_name);
            상품가격 = itemView.findViewById(R.id.food_item_price);
            작성자이름 = itemView.findViewById(R.id.TV_userName);

            BT_수정 = itemView.findViewById(R.id.BT_수정);
//            BT_삭제 = itemView.findViewById(R.id.BT_삭제);
            mDeleteImage = itemView.findViewById(R.id.IV_삭제);

//            if (MyApplication.회원Email ==) {
//                BT_수정.setVisibility(View.VISIBLE);
//                BT_삭제.setVisibility(View.VISIBLE);
//            }


            itemView.setOnClickListener(this);
            //메뉴바 구현
            itemView.setOnCreateContextMenuListener(this);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (foodListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            foodListener.onItemClick(position);
                        }
                    }
                }
            });

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
    static public OnFoodItemClickListener foodListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeletClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
