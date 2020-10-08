package com.example.blueberryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void onBindViewHolder(@NonNull RE_FoodAdapter_Basket.ViewHolder holder, final int position) {

        RE_Food re_foodCurrent = FoodList.get(position);
        //TextView
        holder.상품이름.setText(re_foodCurrent.getFoodName());
        holder.상품가격.setText(re_foodCurrent.getFoodPrice()); //use for getting Number to adapter
        holder.상품수량.setText(re_foodCurrent.getFoodAmount());

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


                    int resultPrice = (Integer.parseInt(storePrice) / Integer.parseInt(storeNumber)) * Integer.parseInt(number);
                    String Price = String.valueOf(resultPrice);
                    Log.d("Price : ", Price);

//                    FoodList.get(position).FoodPrice = MyApplication.결제금액;


                    RE_Food selectedItem = FoodList.get(position);
                    final String selectedTitle = selectedItem.getFoodName();

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
                                    Toast.makeText(mContext, "수량이 증가 되었습니다.", Toast.LENGTH_SHORT).show();
//                                    notifyItemChanged(FoodList.indexOf(number));
//                                    notifyDataSetChanged();
//                                    notifyItemRangeChanged(position, FoodList.size());
//                                    FoodList.remove(position);
//
//                                    notifyItemRemoved(position);
//                                    notifyItemRangeChanged(position, FoodList.size());
//                                    notifyDataSetChanged();
                                    notifyItemChanged(position);
                                    notifyItemRangeChanged(position, FoodList.size());

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
                    int resultPrice = (Integer.parseInt(storePrice) / Integer.parseInt(storeNumber)) * Integer.parseInt(number);

                    String Price = String.valueOf(resultPrice);


//                    FoodList.get(position).FoodPrice = MyApplication.결제금액;


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
                                    notifyItemChanged(position);
                                    notifyItemRangeChanged(position, FoodList.size());

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
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.setPositiveButton("예",
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

                                    // Create a storage reference from our app
                                    StorageReference storageRef = mStorageRef;

                                    // Create a reference to the file to delete
                                    StorageReference desertRef = storageRef.child(selectedItem.getImageUrl());

                                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
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
        if (foodListener != null) {
            foodListener.onItemClick(view, position);
        }
    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

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
            itemView.setOnCreateContextMenuListener(this);

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


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("선택하세요");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "임시");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "지우기");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

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

    public void setOnBasketItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
