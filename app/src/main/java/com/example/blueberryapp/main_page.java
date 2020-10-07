package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class main_page extends AppCompatActivity implements RE_FoodAdapter.OnItemClickListener {
    static final int REQUEST = 2;

    //Thread
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;

    private ProgressBar progressBar;

    // 네비게이션 바

    private Button BT_이용후기, BT_매거진, BT_질문답변;
    private TextView TV_로그인, TV_회원가입, TV_장바구니;
    private ImageView IV_사진, IV_전화기;

    private long backKeyPressedTime = 0; // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장.
    private Toast toast; // 첫 번째 뒤로가기 버튼을 누를때 표시.
    private String 실행 = "실행";

    private ArrayList<RE_Food> FoodList;
    private Context mContext;
    private RE_FoodAdapter foodRecyclerAdapter;

    private ImageSwitcher imageSwitcher;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference StoreCRef = DB.collection("FoodStore");

    private StorageReference storageRef;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        //BT_아이템설정 = findViewById(R.id.BT_아이템추가); 이부분은 >> Admin페이지로 관리할것.
        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        TV_회원가입 = findViewById(R.id.TV_회원가입);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그인 = findViewById(R.id.TV_로그인);
        Log.v("Main_onCreate", 실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
        imageSwitcher = findViewById(R.id.광고창);

        StoreCRef = FirebaseFirestore.getInstance().collection("FoodStore");

        //Thread
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setBackgroundColor(Color.WHITE);
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                view.setLayoutParams(
                        new ImageSwitcher.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                );
                return view;
            }
        });
        imageSwitcher.setInAnimation(this, android.R.anim.fade_out);
        imageSwitcher.setInAnimation(this, android.R.anim.fade_in);

        class ImageThread extends java.lang.Thread {
            int duration = 2000;
            final int images[] = {
                    R.drawable.a1,
                    R.drawable.a2,
            };

            int cur = 0;

            public void run() {
                aBoolean = true;
                while (aBoolean) {
                    synchronized (this) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageSwitcher.setImageResource(images[cur]);
                            }
                        });
                    }
                    cur++;

                    if (cur == images.length) {
                        cur = 0;
                    }
                    try {
                        Thread.sleep(duration);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        imageSwitcher.setVisibility(View.VISIBLE);
        thread = new ImageThread();
        thread.start();

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 갯수
        recyclerView.setLayoutManager(layoutManager);

//        re_foodAdapter = new RE_FoodAdapter(mContext, foodList);
//        mRecyclerView.setAdapter(re_foodAdapter);

        //TODO : 왜 매니저 창에는 뜨는데 Main에는 데이터값이 안뜨는 지 이유를 알아보고 정리하기.  (메인과 로드했을때의 메인에 해당 값 띄우기. + 게시판 만들기.)

        DividerItemDecoration FooddividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(FooddividerItemDecoration);

        //Firebase
        reference = FirebaseDatabase.getInstance().getReference();

        storageRef = FirebaseStorage.getInstance().getReference("foodImages");


        // ArrayList
        FoodList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        // Get Data Method
        getDataFromFireStore();


        //TODO : 바로 구현하기.
        //클릭이벤트


        IV_전화기.setClickable(true);
        IV_전화기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-8885-3426"));
                startActivity(intent);
            }
        });

        IV_사진.setClickable(true);
        IV_사진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, review_page.class);
                startActivity(intent);
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, magazine_page.class);
                startActivity(intent);
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, qna_page.class);
                startActivity(intent);
            }
        });
        TV_로그인.setClickable(true);
        TV_로그인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                Intent intent = new Intent(main_page.this, Login_Page.class);
                startActivity(intent);

            }
        });
        TV_회원가입.setClickable(true);
        TV_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, signup_page1.class);
                startActivity(intent);
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, basket_page.class);
                startActivity(intent);
            }
        });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    //TODO : Store에서 가져올수 있게 변경하기. 참조 가능하게 만들기.
    //TODO : 아래 스냅샷에서 어떤역할을 하는지 정확히 알아보기.
    private void getDataFromFireStore() {

        foodRecyclerAdapter = new RE_FoodAdapter(main_page.this, FoodList);
        //connect Adapter with RecyclerView
        recyclerView.setAdapter(foodRecyclerAdapter);

        //클릭 리스너 구현
        foodRecyclerAdapter.setOnItemClickListener(main_page.this);

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
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Main_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Main_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.v("Main_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Main_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Main_onDestroy", 실행);
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(main_page.this, "클릭 포지션 값 : " + position, Toast.LENGTH_SHORT).show();


        RE_Food selectedItem = FoodList.get(position);
        final String selectedTitle = selectedItem.getFoodName();

        String FoodName =FoodList.get(position).FoodName;
        String FoodPrice = FoodList.get(position).FoodPrice;
        String FoodImage = FoodList.get(position).FoodImageUrl;

        Log.d("Image",FoodImage);

        Intent intent = new Intent(main_page.this,Payment_Page.class);
        intent.putExtra("FoodName",FoodName);
        intent.putExtra("FoodPrice",FoodPrice);
        intent.putExtra("FoodImage",FoodImage);
        startActivity(intent);



        //해당 아이템의 정보를 넘겨야한다.

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeletClick(int position) {

    }
}
