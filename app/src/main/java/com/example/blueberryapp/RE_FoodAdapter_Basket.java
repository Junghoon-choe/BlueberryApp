package com.example.blueberryapp;

import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RE_FoodAdapter_Basket extends RecyclerView.Adapter<RE_FoodAdapter_Basket.ViewHolder> implements OnFoodItemClickListener {


    public static final String IMAGE_INFO = "Basket : ";

    private Context mContext;
    private ArrayList<RE_Food> FoodList;
    private RE_FoodAdapter_Basket.OnItemClickListener mListener;


    //파이어 베이스
    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference StoreCRef = DB.collection("Users");

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");
    private DocumentReference basket;
    private int resultPrice;


    public RE_FoodAdapter_Basket(Context mContext, ArrayList<RE_Food> foodList) {
        this.mContext = mContext;
        this.FoodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RE_FoodAdapter_Basket.ViewHolder holder, final int position) {

        final A_basket_page a_basket_page = new A_basket_page();

        final RE_Food re_foodCurrent = FoodList.get(position);
        //TextView
        holder.상품이름.setText(re_foodCurrent.getFoodName());
        holder.상품가격.setText(re_foodCurrent.getFoodPrice()); //use for getting Number to adapter
        holder.상품수량.setText(re_foodCurrent.getFoodAmount());

        //계산 예시
//        resultPrice = (Integer.parseInt(storePrice) / Integer.parseInt(storeNumber)) * Integer.parseInt(number);
//        String Price = String.valueOf(resultPrice);

        int itemPrice = Integer.parseInt(re_foodCurrent.getFoodPrice());
        int totalPrice = itemPrice;

//        MyApplication.결제금액 += String.valueOf(totalPrice);

        //TODO : 결제금액을int 로 바꾸고 전역변수에서String 으로 다시 바꿔서 합 구하기.
        Log.d("전체1", "결제금액 : " + MyApplication.결제금액);

        Glide.with(mContext)
                .asBitmap()
                .fitCenter().centerCrop()
                .load(FoodList.get(position).getImageUrl())
                .into(holder.상품사진);


        holder.BT_countUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String storeNumber = FoodList.get(position).FoodAmount;
                    int result = Integer.parseInt(storeNumber) + 1;
                    String number = String.valueOf(result);


                    String storePrice = FoodList.get(position).FoodPrice;


                    resultPrice = (Integer.parseInt(storePrice) / Integer.parseInt(storeNumber)) * Integer.parseInt(number);
                    String Price = String.valueOf(resultPrice);
                    Log.d("Price : ", Price);


                    RE_Food selectedItem = FoodList.get(position);
                    final String selectedTitle = selectedItem.getFoodName();


                    final Map<String, Object> counter = new HashMap<>();
                    counter.put("foodAmount", number);
                    counter.put("foodName", FoodList.get(position).FoodName);
                    counter.put("foodPrice", Price);
                    counter.put("imageUrl", FoodList.get(position).FoodImageUrl);


                    StoreCRef.document(MyApplication.회원Email).collection("Basket").document(selectedTitle)
                            .set(counter)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(mContext, "수량이 증가 되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });


        holder.BT_countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String storeNumber = FoodList.get(position).FoodAmount;
                    int result = Integer.parseInt(storeNumber) - 1;
                    String number = String.valueOf(result);

                    String storePrice = FoodList.get(position).FoodPrice;
                    resultPrice = (Integer.parseInt(storePrice) / Integer.parseInt(storeNumber)) * Integer.parseInt(number);

                    String Price = String.valueOf(resultPrice);


                    MyApplication.결제금액 += resultPrice;
                    Log.d("더하기 금액", "" + resultPrice);


                    RE_Food selectedItem = FoodList.get(position);
                    String selectedTitle = selectedItem.getFoodName();

                    Map<String, Object> counter = new HashMap<>();
                    counter.put("foodAmount", number);
                    counter.put("foodName", FoodList.get(position).FoodName);
                    counter.put("foodPrice", Price);
                    counter.put("imageUrl", FoodList.get(position).FoodImageUrl);


                    StoreCRef.document(MyApplication.회원Email).collection("Basket").document(selectedTitle)
                            .set(counter)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(mContext, "수량이 감소 되었습니다.", Toast.LENGTH_SHORT).show();


                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        holder.BT_deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    RE_Food selectedItem = FoodList.get(position);
                                    String selectedTitle = selectedItem.getFoodName();


                                    StoreCRef.document(MyApplication.회원Email).collection("Basket").document(selectedTitle)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(mContext, "아이템 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    FoodList.remove(position);
                                                    notifyItemChanged(position);
                                                    notifyItemRangeChanged(position, FoodList.size());

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




    }


    @Override
    public int getItemCount() {

        return (null != FoodList ? FoodList.size() : 0);

    }

    @Override
    public void onItemClick(View view, int position) {
        this.notifyDataSetChanged();
        if (foodListener != null) {
            foodListener.onItemClick(view, position);
        }
    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {
        this.notifyDataSetChanged();
        this.foodListener = listener;

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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView 상품이름, 상품가격;
        public TextView 상품수량;
        public ImageView 상품사진;


        //장바구니 연산 객체
        public Button BT_countUp, BT_countDown, BT_deleteItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            상품사진 = itemView.findViewById(R.id.basketItemImage);
            상품이름 = itemView.findViewById(R.id.basketItemTitle);
            상품가격 = itemView.findViewById(R.id.basketItemPrice);
            상품수량 = itemView.findViewById(R.id.basketItemAmount);

            //장바구니 연산 객체

            BT_countUp = itemView.findViewById(R.id.BT_countUp);
            BT_countDown = itemView.findViewById(R.id.BT_countDown);
            BT_deleteItem = itemView.findViewById(R.id.BT_deleteItem);

            itemView.setOnClickListener(this);
            //메뉴바 구현
        }

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
    }


    //아이템 클릭 리스너 구현
    static public OnFoodItemClickListener foodListener;

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

    public void setOnBasketItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }

}
