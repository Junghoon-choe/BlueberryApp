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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.se.omapi.Session;
import android.service.textservice.SpellCheckerService;
import android.text.format.Formatter;
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
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
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
import static com.example.blueberryapp.R.id.fill;
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

    Session session;
    private boolean Logined = false;

    String myIp;


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

//    public boolean Kakao_Login = false;

    public boolean clickedBT_Login = false;
    public boolean clickedBT_kakaoLogin = false;

    private ISessionCallback sessionCallback = new ISessionCallback() {
        @Override
        public void onSessionOpened() {

            Log.i("KAKAO_SESSION", "로그인성공");

            sessionCallback.onSessionOpened();


//            Kakao_Login = true;
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.i("KAKAO_SESSION", "로그인실패", exception);
        }
    };


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

        //쉐어드 아이디 저장 구현
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        myIp = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
        Log.d("IP", "" + myIp);


//카카오 로그인 구현

        //세션 콜백 등록

        sessionCallback = new SessionCallback();

        com.kakao.auth.Session.getCurrentSession().addCallback(sessionCallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();


//        if (Kakao_Login = true){
//            startActivity(new Intent(Login_Page.this, A_main_page.class));
//            SessionCallback sessionCallback = new SessionCallback();
//            String email = sessionCallback.Email;
//            String name = sessionCallback.Name;
//            String pw = sessionCallback.PW;
//            String phoneNum = sessionCallback.PhoneNum;
//
//            MyApplication.회원Email = email;
//            MyApplication.회원Name = name;
//            MyApplication.회원PW = pw;
//            MyApplication.회원PhoneNum = phoneNum;
//            finish();
//
//        } else if (Kakao_Login = false){
//            startActivity(new Intent(Login_Page.this, Login_Page.class));
//
//        }


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
        final String Email = ET_Email.getText().toString().trim();
        String PW = ET_Password.getText().toString().trim();


        if (Email.equals("Manager") && PW.equals("1234")) {
            startActivity(new Intent(this, B_Manager_Page.class));
            clickedBT_Login = false;
            clickedBT_kakaoLogin = false;
            finish();
        } else {
            clickedBT_Login = true;
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

            progressBar.setVisibility(View.VISIBLE);
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


                    Log.d(TAG, "회원Email :" + MyApplication.회원Email);
                }
            });
        }
        if (clickedBT_Login) {
//auth에 로그인을 하기위해서 이메일과 패스워드를 입력 받는 메서드를 사용해서 Task를 통해 로그인 여부를 확인한다.
            auth.signInWithEmailAndPassword(Email, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), A_main_page.class);
                        startActivity(intent);
                        MyApplication.회원Email = userEmail;
                        MyApplication.회원Name = userName;
                        MyApplication.회원PhoneNum = userPhoneNum;
                        Logined = true;
                        finish();


//                        // 문자열 불러오기
//                        String loadSharedName = ""; // 가져올 SharedPreferences 이름 지정
//                        String loadKey = ""; // 가져올 데이터의 Key값 지정
//                        String loadValue = ""; // 가져올 데이터가 담기는 변수
//                        String defaultValue = ""; // 가져오는것에 실패 했을 경우 기본 지정 텍스트.
//
//                        SharedPreferences loadShared = getSharedPreferences(loadSharedName,MODE_PRIVATE);
//                        loadValue = loadShared.getString(loadKey,defaultValue);

//                        // 문자열 데이터 삭제하기
//                        String delSharedName = ""; // 저장된 SharedPreferences 이름 지정.
//                        String delKey = ""; // 삭제할 데이터의  Key값 지정.
//
//                        SharedPreferences pref = getSharedPreferences(delSharedName, MODE_PRIVATE);
//                        SharedPreferences.Editor dleEditor = pref.edit();
//
//                        dleEditor.remove(delKey);
//                        dleEditor.commit();


                    } else {
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

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


        // 문자열 불러오기
        String loadSharedName = myIp; // 가져올 SharedPreferences 이름 지정
        String loadKey = ""; // 가져올 데이터의 Key값 지정
        String loadValue = ""; // 가져올 데이터가 담기는 변수
        boolean loadValueCheckBox = Boolean.parseBoolean(""); // 가져올 데이터가 담기는 변수
        AppCompatCheckBox checkBox = null;
        String defaultValue = ""; // 가져오는것에 실패 했을 경우 기본 지정 텍스트.

        SharedPreferences loadShared = getSharedPreferences(loadSharedName, MODE_PRIVATE);
        loadValueCheckBox = loadShared.getBoolean("아이디체크박스", false);
        loadValue = loadShared.getString("회원아이디정보", defaultValue);

        ET_Email.setText(loadValue);
        ID_checkBox.setChecked(loadValueCheckBox);

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
        if (!ID_checkBox.isChecked()) {
            String saveSharedName = myIp; // 저장할 SharedPreferences 이름 지정.
//                            String saveKey = ""; // 저장할 데이터의 Key값 지정.
//                            String saveValue = ""; //저장할 데이터의 Content 지정.

            SharedPreferences saveShared = getSharedPreferences(saveSharedName, MODE_PRIVATE);
            SharedPreferences.Editor sharedEditor = saveShared.edit();

            sharedEditor.putBoolean("아이디체크박스", false);
            sharedEditor.putString("회원아이디정보", "");
            sharedEditor.apply();
            sharedEditor.commit();
            Toast.makeText(Login_Page.this, "버튼 x 쉐어드 저장됨", Toast.LENGTH_SHORT).show();
        }
    }

    //카카오 로그인 구현
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Login_onDestroy", 실행);
        //세션 콜백 삭제
        com.kakao.auth.Session.getCurrentSession().removeCallback(sessionCallback);

        if (Logined) {
            if (ID_checkBox.isChecked()) {
                // 문자열 저장하기
                String saveSharedName = myIp; // 저장할 SharedPreferences 이름 지정.
//                            String saveKey = ""; // 저장할 데이터의 Key값 지정.
//                            String saveValue = ""; //저장할 데이터의 Content 지정.

                SharedPreferences saveShared = getSharedPreferences(saveSharedName, MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = saveShared.edit();

                sharedEditor.putBoolean("아이디체크박스", true);
                sharedEditor.putString("회원아이디정보", userEmail);
                sharedEditor.apply();
                sharedEditor.commit();
                Toast.makeText(Login_Page.this, "버튼 o 쉐어드 저장됨", Toast.LENGTH_SHORT).show();
            } else if (!ID_checkBox.isChecked()) {
                String saveSharedName = myIp; // 저장할 SharedPreferences 이름 지정.
//                            String saveKey = ""; // 저장할 데이터의 Key값 지정.
//                            String saveValue = ""; //저장할 데이터의 Content 지정.

                SharedPreferences saveShared = getSharedPreferences(saveSharedName, MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = saveShared.edit();

                sharedEditor.putBoolean("아이디체크박스", false);
                sharedEditor.putString("회원아이디정보", "");
                sharedEditor.apply();
                sharedEditor.commit();
                Toast.makeText(Login_Page.this, "버튼 x 쉐어드 저장됨", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (com.kakao.auth.Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public class SessionCallback implements ISessionCallback {


        public static final String TAG = "kakao";
        private FirebaseFirestore DB = FirebaseFirestore.getInstance();


        String Email = null;
        String Name = null;
        String PW = "X";
        String PhoneNum = "X";

        //로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        //로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Log.i("KAKAO_API", "사용자 아이디: " + result.getId());


                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {

                                // 이메일
                                String email = kakaoAccount.getEmail();

                                if (email != null) {
                                    Log.i("KAKAO_API", "email: " + email);
                                    Email = email.trim();

                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                                } else {
                                    // 이메일 획득 불가
                                }

                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if (profile != null) {
                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                    Name = profile.getNickname().trim();


                                    Intent intent = new Intent(getApplicationContext(), A_main_page.class);
//                                    intent.putExtra("kakaoName", Name);
//                                    intent.putExtra("kakaoEmail", Email);
//                                    intent.putExtra("kakaoPhoneNum", "X");

                                    MyApplication.회원Email = Email;
                                    MyApplication.회원Name = Name;
                                    MyApplication.회원PhoneNum = PhoneNum;

                                    Log.d("PUT kakaoEmail : ", Email);
                                    UsersCRef.document(Email).collection("Basket");

                                    startActivity(intent);
                                    finish();


                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능

                                } else {
                                    // 프로필 획득 불가
                                }
                            }


                        }
                    });
        }

        private void addUser(String email, String name, String phoneNum) {

            UserHelper userHelper = new UserHelper(email, name, phoneNum);

            UsersCRef.document(email).set(userHelper)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Document has been saved!");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Document was not saved!", e);
                }
            });
        }

    }


}

