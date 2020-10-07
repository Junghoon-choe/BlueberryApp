package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
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

//        UserEmail = Objects.requireNonNull(auth.getCurrentUser()).getUid();
//        UserEmail = auth.getCurrentUser().getUid();




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


        // 이전에 로그인 정보를 저장시킨 기록이 있다면















       /* SharedPreferences sharedPreferences = getSharedPreferences(shared,0);  //string 값을 받아옴.
        String value = sharedPreferences.getString("User_ID","");//꺼내오는 것이니까 빈값으로 만들어줌.
        String value2 = sharedPreferences.getString("User_Password","");
        ET_ID.setText(value);
        ET_Password.setText(value2);// onDestroy로 파괴될때 저장된 String텍스트 값을 셋해줌. 즉, 죽기전에 저장된 데이터를 만들어지면서 써지게 만들어줌. 불러오는 구문임.
*/


        /*ID_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                } else {

                }
            }
        });*/


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

//                String textEmail = ET_Email.getText().toString();
//                String textPassword = ET_Password.getText().toString();
//
//                loginUser(textEmail,textPassword);

//                    isUser();

//                Intent intentManager = new Intent(Login_Page.this, B_Manager_Page.class);
//                Intent intent = new Intent(Login_Page.this, A_main_page.class);
//
////                SharedPreferences pref = getSharedPreferences("회원정보", MODE_PRIVATE);
////                String 입력아이디 = ET_ID.getText().toString(); //입력칸에서 가져온 데이터를 ID에 넣고, ID,Password를 오브젝트 칸에 넘기고, 나머지 이름,이메일,폰넘버,주소를
////                String 입력비밀번호 = ET_Password.getText().toString();
////                String 오류; // defValue에 넣을 입력값 넣기.
////
////                String 입력정보 = pref.getString(입력아이디, "");
////
////                /*String 회원아이디 = pref.getString(회원정보.substring(0,10),""); // 불러오고 substring을 써야 수정됨.*/
////                String 회원ID = null; // 불러오고
////                String 회원PW = null;
////                String 회원Name = null;
////                String 회원Email = null;
////                String 회원PhoneNum = null;
////                String 회원Address = null;
////
////                JSONObject jsonObject;
////
////                try {
////                    jsonObject = new JSONObject(입력정보);
////
////                    회원ID = jsonObject.getString("회원아이디");
////                    회원PW = jsonObject.getString("회원비밀번호");
////                    회원Name = jsonObject.getString("회원이름");
////                    회원Email = jsonObject.getString("회원이메일");
////                    회원PhoneNum = jsonObject.getString("회원번호");
////                    회원Address = jsonObject.getString("회원주소");
////
////
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                } //회원정보 안에 있는 키값의 정보를 불러와줌.
//
//                String 입력아이디 = ET_ID.getText().toString();
//                String 입력비밀번호 = ET_Password.getText().toString();
//
//                String ID = ET_ID.getText().toString();
//                String PW = ET_Password.getText().toString();
//
//                isUser();
//
//                Log.v("실행3", 입력아이디); //112
//                Log.v("실행4", 입력비밀번호); //112
//
//
//                databaseReference.child(ID).setValue(userHelper);
//
//
//
//                if (입력아이디.equals(회원ID) && 입력비밀번호.equals(회원PW)) {
//
//                    Toast.makeText(Login_Page.this, ET_ID.getText().toString() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
//
//                    MyApplication.회원ID = 회원ID;
//                    MyApplication.회원PW = 회원PW;
//                    MyApplication.회원Name = 회원Name;
//                    MyApplication.회원Email = 회원Email;
//                    MyApplication.회원PhoneNum = 회원PhoneNum;
//                    MyApplication.회원Address = 회원Address;
//                    startActivity(intent);
//                    finish();
//                } else if (입력아이디.equals("manager") && 입력비밀번호.equals("1234")) {
//                    Toast.makeText(Login_Page.this, "관리자님 환영합니다.", Toast.LENGTH_SHORT).show();
//                    startActivity(intentManager);
//                    finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
//                    return;
//
//                }

                /*//Key에 해당한 value를 불러온다. 두번째 매개변수는, key에 해당하는 value값이 없을 때에는 이 값으로 대체한다.
                String ID = pref.getString("회원아이디","");
                String PW = pref.getString("회원비밀번호","");*/


                // 이곳에 다양한 키값 가져오는법 변수로 가져오면됨


                //===========================

                /*String 회원정보PW = 입력비밀번호;*/
                //1. 파일안에 있는지 확인해야함.
                //===========================
                //Gson으로 바꿔보기.




                /*JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse();
                jsonElement.
                String ID = jsonElement*/


                //defValue는 입력된아이디가 없을때 돌려주는 인터페이스이다.
                // 위에 넣을 String 변수를 만들어서 넣을것. Toast활용해서 문구 띄워주기.













               /* JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(입력정보);
                jsonElement.getAsJsonObject().get("회원아이디");*/





               /* jsonElement.
//                String ID = jsonElement


                //나눠서 해야 좋다. 알아보기도 쉽다. 에러찾는데 무서워 하지 말아라.

                Log.v("실행1",회원아이디); //{"회원번호":"1","회원비밀번호":"1","회원아이디":"wjdgns","회원이름":"1","회원이메일":"1","회원주소":"1"}
                //>> 다른걸 다 잘라내고 회원아이디만 뽑아낼수 있어야함.

                Log.v("실행2",회원비밀번호); //{"회원번호":"1","회원비밀번호":"1","회원아이디":"wjdgns","회원이름":"1","회원이메일":"1","회원주소":"1"}
                //>> 다른걸 다 잘라내고 회원비밀번호만 뽑아낼수 있어야한다.*/

                //기찬파트장님 : 지슨으로 가져오기면 하면 섭 스트링 안써도된다.


                //리스펀스 리스너








               /* final SharedPreferences signupSf = getSharedPreferences("signupFile",MODE_PRIVATE);
                //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
                // getString의 첫 번째 인자는 저장될 키, 두번째 인자는 값이다.
                // 처음엔 값이 없으므로 키 값은 원하는 것으로 하고 값을 null로 준다.

                String LoginId = signupSf.getString("ET_ID",null);
                String LoginPwd = signupSf.getString("ET_Password",null);

                //Login으로 들어왔을때 ET_ID와 ET_Password를 가져와서 null이 아니면 값을 가져와 Id가 지정이름이고, pwd가 지정번호면 자동적으로 액티비티 이동함.
                if (ET_ID !=null && ET_Password !=null){
                    if (ET_ID.equals("ID") && ET_Password.equals("PW")){
                        Toast.makeText(Login_Page.this,LoginId+"님 자동 로그인 입니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Page.this, A_main_page.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else if(ET_ID==null&&ET_Password==null){
                    BT_로그인박스.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ET_ID.getText().toString().equals("ID")&&ET_Password.getText().toString().equals("PW")){
                                SharedPreferences signpSf = getSharedPreferences("signupFile",Activity.MODE_PRIVATE);
                                //회워가입할때 입력된아이디와 비번이 일치할 경루 에디터를 통해 아이디와 비번에 값을 저장해준다.
                                //아이디가 wjdgns0312이고, 비번이 1234일 경우 SharedPreferences.Editor을 통해 auto의 아이디와 비번에 값을 저장해 준다.
                                SharedPreferences.Editor autoLogin = signupSf.edit();
                                autoLogin.putString("ET_ID",ET_ID.getText().toString());
                                autoLogin.putString("ET_Password",ET_Password.getText().toString());
                                autoLogin.commit();
                                Toast.makeText(Login_Page.this,ET_ID.getText().toString()+"님 환영합니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_Page.this,A_main_page.class);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }

//--------------

                String email = ET_ID.getText().toString();
                String password = ET_Password.getText().toString(); // 사용자가 입력한 값을 가져오기.

                Intent intent = new Intent(Login_Page.this, A_main_page.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);*/

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

        if (Email.equals("Manager")&& PW.equals("1234")) {
            startActivity(new Intent(this , B_Manager_Page.class));
        } else{
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

//    private void loginUser(String Email, String Password) {
//        auth.signInWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Toast.makeText(Login_Page.this,"Login Successful",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Login_Page.this,A_main_page.class));
//                finish();
//            }
//        });
//    }

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

//    private void isUser() {
//        final String userEnteredUserID = ET_ID.getText().toString().trim(); //trim : 앞뒤 공백 제거 메서드.
//        final String userEnteredUserPassword = ET_Password.getText().toString().trim();
//
////        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//
//
//
//        //TODO : 아래 부분 해석하기.
//        Query checkUser = UsersCRef.("id").equalTo(userEnteredUserID);//checkUser이름 으로 문의한다. 주문 child값인 id를 찾아서 참조한다 같은지 유저가 입력한 아이디가 같은지.
//
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) { //dataSnapshot = 데이터 정보
//
//                if (snapshot.exists()) {
//                    ET_ID.setError(null);
////                    ET_ID.setEnabled(false);
//
//                    String passwordFromDB = snapshot.child(userEnteredUserID).child("pw").getValue(String.class);
//
//                    if (passwordFromDB.equals(userEnteredUserPassword)) {
//
//                        ET_ID.setError(null);
////                        ET_ID.setEnabled(false);
//
//                        String idFromDB = snapshot.child(userEnteredUserID).child("id").getValue(String.class);
//                        String nameFromDB = snapshot.child(userEnteredUserID).child("name").getValue(String.class);
//                        String phoneNumFromDB = snapshot.child(userEnteredUserID).child("phoneNum").getValue(String.class);
//                        String emailFromDB = snapshot.child(userEnteredUserID).child("email").getValue(String.class);
//
//                        Intent intent = new Intent(getApplicationContext(), A_main_page.class);
//                        Intent managerPageIntent = new Intent(getApplicationContext(), B_Manager_Page.class);
//
////                        intent.putExtra("id",idFromDB);
////                        intent.putExtra("name",nameFromDB);
////                        intent.putExtra("phoneNum",phoneNumFromDB);
////                        intent.putExtra("email",emailFromDB);
////                        intent.putExtra("password",passwordFromDB);
//
//
//
//                        MyApplication.회원ID = idFromDB;
//                        MyApplication.회원PW = passwordFromDB;
//                        MyApplication.회원Name = nameFromDB;
//                        MyApplication.회원Email = emailFromDB;
//                        MyApplication.회원PhoneNum = phoneNumFromDB;
//
//                        Toast.makeText(Login_Page.this,ET_ID.getText().toString() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
//
//                        if (idFromDB.equals("manager") && passwordFromDB.equals("1234")) {
//
//                            startActivity(managerPageIntent);
//
//                        } else {
//                            startActivity(intent);
//                        }
//
//
//                    } else {
//                        ET_ID.setError("아이디 또는 비밀번호가 잘못되었습니다.");
//                        ET_Password.setError("아이디 또는 비밀번호가 잘못되었습니다.");
//                        ET_ID.requestFocus();
//                        ET_Password.requestFocus();
//
//                    }
//                } else {
//                    ET_ID.setError("아이디 또는 비밀번호가 잘못되었습니다.");
//                    ET_Password.setError("아이디 또는 비밀번호가 잘못되었습니다.");
//                    ET_ID.requestFocus();
//                    ET_Password.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }


    // OnSharedPreferecnesChangeListener == 쉐어드를 사용하여 값을 유지 변경사항 을 수신할게 싸용.


//    public void loadUserData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("회원정보", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("PW", "");
//        Type type = new TypeToken<ArrayList<UserInfo>>() {
//        }.getType();
//        UserList = gson.fromJson(json, type);
//
//        if (UserList == null) {
//            UserList = new ArrayList<>();
//        }
//    }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Login_onDestroy", 실행);

        /*SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //에디터랑 쉐프랑 연결.
        String value = ET_ID.getText().toString(); // String 형태로 value에 저장하겠다.
        editor.putString("User_ID",value); //저장하는 실제 구문 2가지 인자.
        editor.commit(); // 저장함.
        ID_checkBox.setChecked(true);*/


        /*ID_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                } else {
                }
            }
        });*/


    }


}

//--

