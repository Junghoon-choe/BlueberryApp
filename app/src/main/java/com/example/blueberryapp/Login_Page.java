package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.media.MediaSession2;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.se.omapi.Session;
import android.service.textservice.SpellCheckerService;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.blueberryapp.R.id.ID_checkBox;
import static com.example.blueberryapp.R.id.Login;
import static com.example.blueberryapp.R.id.bottom;
import static com.example.blueberryapp.R.id.checked;
import static com.example.blueberryapp.R.id.start;
import static com.example.blueberryapp.R.id.text;

public class Login_Page extends AppCompatActivity {


    public static final String TAG = "Email";
    ArrayList<UserInfo> UserList;

    private EditText ET_Email, ET_Password;
    private Button BT_이용후기, BT_매거진, BT_질문답변, BT_아패찾기, BT_회원가입, BT_로그인박스;
    private TextView TV_로그인, TV_회원가입, TV_장바구니;
    private ImageView IV_사진, IV_전화기;
    private String 실행 = "실행";
    private String shared = "file";
    private SharedPreferences appData;
    private ProgressBar progressBar;


    private String userEmail, userPw, userName, userPhoneNum;


    public int TERMS_AGREE_1 = 0; // 체크 안됬을시 0, 체크 됬을 경우 1
    public int TERMS_AGREE_2 = 0;


    AppCompatCheckBox ID_checkBox; // 아이디 저장
    AppCompatCheckBox Login_checkBox; // 아이디 저장
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    private FirebaseAuth auth;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference UsersCRef = DB.collection("Users");
    private String UserEmail;



    //카카오 로그인버튼 구현
    //TODO : 1. 로그인되면 바로 회원가입 처리되어서 fireStore에 추가되고 추가된 아이디로 로그인 될수 있도록 만들기.
    //TODO : 2. 로그인된 메인창으로 이동후 로그아웃버튼을 누르면, 카카오 로그아웃 될수 있도록 만들기.

    public boolean Kakao_Login = false;


    private ISessionCallback sessionCallback = new ISessionCallback() {
        @Override
        public void onSessionOpened() {

            Log.i("KAKAO_SESSION", "로그인성공");
            sessionCallback.onSessionOpened();
            Kakao_Login = true;
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.i("KAKAO_SESSION", "로그인실패", exception);
        }
    };



    Session session;


    //kakaoLogin을 위한 해쉬 키 값.
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
//        loadUserData();

        getAppKeyHash();


        ET_Email = findViewById(R.id.ET_Email);
        ET_Password = findViewById(R.id.ET_Password);
        ID_checkBox = findViewById(R.id.ID_checkBox);
        BT_로그인박스 = findViewById(R.id.BT_로그인박스);
        imageSwitcher = findViewById(R.id.로그인광고창);
        progressBar = findViewById(R.id.progressbarId);

        auth = FirebaseAuth.getInstance();

//카카오 로그인 구현

        //세션 콜백 등록

        sessionCallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(sessionCallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();


        if (Kakao_Login = true){
            startActivity(new Intent(Login_Page.this, A_main_page.class));
        } else if (Kakao_Login = false){
            return;
        }


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


        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        BT_아패찾기 = findViewById(R.id.BT_아패찾기);
        BT_회원가입 = findViewById(R.id.BT_회원가입);
        TV_회원가입 = findViewById(R.id.TV_회원가입);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그인 = findViewById(R.id.TV_로그인);
        Log.v("Login_onCreat", 실행);


        IV_전화기 = findViewById(R.id.IV_전화기);
        IV_전화기.setClickable(true);
        IV_전화기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-8885-3426"));
                startActivity(intent);
            }
        });


        BT_회원가입.setClickable(true);
        BT_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, signup_page1.class);
                startActivity(intent);
            }
        });

        BT_아패찾기.setClickable(true);
        BT_아패찾기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, forgot_page.class);
                startActivity(intent);
            }
        });

        IV_사진.setClickable(true);
        IV_사진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그인.setClickable(true);
        TV_로그인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_회원가입.setClickable(true);
        TV_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, signup_page1.class);
                startActivity(intent);
                finish();
            }
        });


        BT_로그인박스.setClickable(true);
        BT_로그인박스.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();
            }
        });

        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, basket_page.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void userLogin() {

        //사용자가 입력한 값을 Email, PW변수에 저장함.
        String Email = ET_Email.getText().toString().trim();
        String PW = ET_Password.getText().toString().trim();

        if (Email.equals("Manager") && PW.equals("1234")) {
            startActivity(new Intent(this, B_Manager_Page.class));
        } else {
            if (Email.isEmpty()) {
                ET_Email.setError("Enter an email address");
                ET_Email.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                ET_Email.setError("Enter a valid email address");
                ET_Email.requestFocus();
                return;
            }

            if (PW.isEmpty()) {
                ET_Password.setError("Enter a password");
                ET_Password.requestFocus();
                return;
            }

            if (PW.length() < 6) {
                ET_Password.setError("Minimum length of a password should be 6");
                ET_Password.requestFocus();
                return;
            }
        }


        progressBar.setVisibility(View.VISIBLE);

        //        DocumentReference documentReference = DB.collection("Users").document("email");


        DocumentReference documentReference = UsersCRef.document(Email);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                userEmail = value.getString("email");
                userPw = value.getString("pw");
                userName = value.getString("name");
                userPhoneNum = value.getString("phoneNum");

                String Email = userEmail;
                String Pw = userPw;
                String Name = userName;
                String PhoneNum = userPhoneNum;

                Log.d(TAG, "Email :" + Email);

                MyApplication.회원Email = userEmail;
                MyApplication.회원PW = userPw;
                MyApplication.회원Name = userName;
                MyApplication.회원PhoneNum = userPhoneNum;

                Log.d(TAG, "회원Email :" + MyApplication.회원Email);
            }
        });

        //auth에 로그인을 하기위해서 이메일과 패스워드를 입력 받는 메서드를 사용해서 Task를 통해 로그인 여부를 확인한다.
        auth.signInWithEmailAndPassword(Email, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), A_main_page.class);
                    //TODO : 어떤 메서드인지 정확하게 파악할것.
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateUserEmail() {
        String val = ET_Email.getText().toString();
        if (val.isEmpty()) {
            ET_Email.setError("아이디가 입력 안되었습니다.");
            return false;
        } else {
            ET_Email.setError(null);
            ET_Email.setEnabled(false);
            return true;
        }
    }

    private Boolean validateUserPassword() {
        String val = ET_Password.getText().toString();

        if (val.isEmpty()) {
            ET_Password.setError("비밀번호를 확인해주세요.");
            return false;
        } else {
            ET_Password.setError(null);
            ET_Password.setEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        //validate Login Info
        if (!validateUserEmail() | !validateUserPassword()) {
            return;
        } else {
//            isUser();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Login_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Login_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Login_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Login_onStop", 실행);

    }

    //카카오 로그인 구현
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Login_onDestroy", 실행);
        //세션 콜백 삭제
        com.kakao.auth.Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (com.kakao.auth.Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

