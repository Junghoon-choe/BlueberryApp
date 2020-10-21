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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reply_Adapter extends RecyclerView.Adapter<Reply_Adapter.ViewHolder> implements OnFoodItemClickListener {


    public static final String IMAGE_INFO = "Basket : ";

    private Context mContext;
    private ArrayList<Reply> List;
    private Reply_Adapter.OnItemClickListener mListener;


    //파이어 베이스
    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference ReplyCRef = DB.collection("REVIEW");

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");
    private DocumentReference basket;
    private int resultPrice;


    public Reply_Adapter(Context mContext, ArrayList<Reply> List) {
        this.mContext = mContext;
        this.List = List;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Reply_Adapter.ViewHolder holder, final int position) {

        final A_basket_page a_basket_page = new A_basket_page();

        final Reply ReplyCurrent = List.get(position);
        //TextView
        holder.TV_userName.setText(ReplyCurrent.getUserName());
        holder.TV_reply.setText(ReplyCurrent.getWriting());

        Reply selectedItem = List.get(position);
        String selectedTitle = selectedItem.getUserEmail();

        String CheckEmail = MyApplication.회원Email;

        if (!CheckEmail.equals(selectedTitle)) {
            holder.IV_댓글삭제.setVisibility(View.INVISIBLE);
        }

        holder.IV_댓글삭제.setOnClickListener(new View.OnClickListener() {
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
                                    Reply selectedItem = List.get(position);
                                    String selectedTitle = selectedItem.getDocuName();
                                    final String selectedDocu = MyApplication.ReplyDocuName;
                                    Log.d("12312312312", "12312312" + selectedDocu);


                                    ReplyCRef.document(selectedDocu).collection("댓글").document(selectedTitle)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(mContext, "아이템 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    List.remove(position);
                                                    notifyItemChanged(position);
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


    }


    @Override
    public int getItemCount() {

        return (null != List ? List.size() : 0);

    }

    @Override
    public void onItemClick(View view, int position) {
        this.notifyDataSetChanged();
        if (foodListener != null) {
            foodListener.onItemClick(view, position);
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {
        this.notifyDataSetChanged();
        this.foodListener = listener;

    }

    public Reply getItem(int position) {
        return List.get(position);
    }

    public void setItems(ArrayList<Reply> items) {
        this.List = items;
    }

    public void addItem(Reply item) {
        List.add(item);
    }

    public void setItem(int position, Reply item) {
        List.set(position, item);

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView TV_userName, TV_reply;
        public ImageView IV_댓글삭제;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TV_userName = itemView.findViewById(R.id.TV_userName);
            TV_reply = itemView.findViewById(R.id.TV_reply);
            IV_댓글삭제 = itemView.findViewById(R.id.IV_댓글삭제);

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
