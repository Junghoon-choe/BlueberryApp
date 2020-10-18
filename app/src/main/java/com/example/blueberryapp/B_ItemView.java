package com.example.blueberryapp;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;


public class B_ItemView extends AppCompatActivity implements RE_FoodAdapter.OnItemClickListener {


    //Widgets
    RecyclerView recyclerView;

    //Firebase
    private DatabaseReference databaseReference;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference StoreCRef = DB.collection("FoodStore");

    // Variables
    private ArrayList<RE_Food> FoodList;
    private RE_FoodAdapter foodRecyclerAdapter;
    private Context mContext;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");


    private FirebaseFirestore firestore;
    private FirebaseStorage mStorage;

    public boolean B_ItemView = false;


    //    public Boolean checked;
//
//
//    private String 실행 = "실행";
//
//
//
//    private RE_FoodAdapter mAdapter;
//    private RecyclerView mRecyclerView;
//    private Context mContext;
//
    Button 추가; //바꿔야 하는 부분.
//    private int count;
//    private Thread thread;
//
//    // 체크박스 체크여부
//    public int TERMS_AGREE = 0; // 체크 안됬을시 0, 체크 됬을 경우 1


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        B_ItemView = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_item_view);

        추가 = findViewById(R.id.BT_추가);
        recyclerView = findViewById(R.id.RE_아이템관리);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // 부가 기능
        DividerItemDecoration FooddividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(FooddividerItemDecoration);

        //Firebase
//        databaseReference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");

        //TODO : 로그인 참고해서 아이템뷰 가져오기.


        추가.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(B_ItemView.this, B_AddItem.class); // 1번값 : 전환시킬 화면, 2번값 : 밑에서 나올 requestCode로 받을값.
                startActivity(intent);
                finish();
                //TODO 추가버튼을 누르면 다른 액티비티로 전환하고 전확했을때, 입력된 정보를 가져올수있게 한다.
            }
        });


        //클릭이벤트 //해당 인덱스값을 넘기고 인덱스 값을 받아서 수정하거나 삭제 시켜야함.

//        foodRecyclerAdapter.setOnItemClickListener(new OnFoodItemClickListener() {
//            @Override
//            public void onItemClick(RE_FoodAdapter.FoodViewHolder holder, View view, final int position) {
//
//
//                RE_Food item = foodRecyclerAdapter.getItem(position);
//
//
//                Toast.makeText(getApplicationContext(),
//                        "position : " + "[" + position + "]" + item.getName(), Toast.LENGTH_SHORT).show();
//
//                final Intent intent = new Intent(B_ItemView.this, B_ItemClick.class);
//
//                intent.putExtra("position", position);
//                intent.putExtra("title", item.getName());
//                intent.putExtra("price", item.getPrice());
//
//            }
//        });
    }


    //TODO : Store에서 가져올수 있게 변경하기. 참조 가능하게 만들기.
    //TODO : 아래 스냅샷에서 어떤역할을 하는지 정확히 알아보기.
    private void getDataFromFireStore() {

        foodRecyclerAdapter = new RE_FoodAdapter(B_ItemView.this, FoodList);
        recyclerView.setAdapter(foodRecyclerAdapter);

        //클릭 리스너 구현
        foodRecyclerAdapter.setOnItemClickListener(B_ItemView.this);

        StoreCRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                FoodList.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RE_Food re_food = documentSnapshot.toObject(RE_Food.class);
                    FoodList.add(re_food);
                }


                foodRecyclerAdapter.notifyDataSetChanged();

            }

        });
    }

    private void ClearAll() {
        if (FoodList != null) {
            FoodList.clear();
            if (foodRecyclerAdapter != null) {
                foodRecyclerAdapter.notifyDataSetChanged();
            }
        }

        FoodList = new ArrayList<>();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // ArrayList
        FoodList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        // Get Data Method
        getDataFromFireStore();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        B_ItemView = false;


    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    //아이템 클릭 리스너 구현 >> 어댑터에 정의한 내용을 implement해서 가져와서 연동시킴.
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "클릭 포지션 값 : " + position, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "이미 값 포지션 값 : " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletClick(int position) {
        Toast.makeText(this, "지우기 포지션 값 : " + position, Toast.LENGTH_SHORT).show();

        RE_Food selectedItem = FoodList.get(position);
        final String selectedTitle = selectedItem.getFoodName();

        StoreCRef.document(selectedTitle)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(B_ItemView.this, "아이템 삭제 완료.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Create a storage reference from our app
        StorageReference storageRef = mStorageRef;

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(selectedItem.FoodImageUrl);

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        });


    }


}
