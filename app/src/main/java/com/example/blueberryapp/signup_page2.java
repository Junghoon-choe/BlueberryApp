package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

public class signup_page2 extends AppCompatActivity {


    ArrayList<UserInfo> UserList = new ArrayList<>();
    public static final String TAG = "InspiringQuote";


    private EditText ET_Password, ET_Email, ET_Name, ET_PhoneNum, ET_PasswordCheck;
    private Button BT_이용후기, BT_매거진, BT_질문답변, BT_가입하기, BT_중복체크, BT_주소검색;
    private TextView TV_로그인, TV_회원가입, TV_장바구니, PW_Check_Message, TV_Address;
    private ProgressBar progressBar;


    private ImageView IV_사진, IV_전화기;
    private String 실행 = "실행";
    private Object StringBuffer;

    private Handler handler = new Handler();
    private Handler handler1;
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    private Boolean CheckID = false;
    private Boolean 비밀번호확인 = false;


    private String Red = "#FF0000";
    private String Blue = "#0000CD";
    private String Black = "#000000";

    private WebView webView;

//    FirebaseDatabase database;
//    DatabaseReference databaseReference;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference UsersCRef = firestore.collection("Users");

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page2);

        auth = FirebaseAuth.getInstance();
        ///


//        loadUserData();

//이름 주소 이메일 전화번호 입력여부 체크

        ET_Email = findViewById(R.id.ET_Email);
        progressBar = findViewById(R.id.progressbarId);
        ET_Name = findViewById(R.id.ET_Name);
        ET_PhoneNum = findViewById(R.id.ET_PhoneNum);
        BT_가입하기 = findViewById(R.id.BT_가입하기);
        BT_중복체크 = findViewById(R.id.BT_중복체크);
        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        TV_회원가입 = findViewById(R.id.TV_회원가입);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그인 = findViewById(R.id.TV_로그인);
        Log.v("signup2_onCreate", 실행);
        imageSwitcher = findViewById(R.id.회원가입2광고창);

        ET_Password = findViewById(R.id.ET_Password);
        ET_PasswordCheck = findViewById(R.id.ET_PasswordCheck);
        PW_Check_Message = findViewById(R.id.PW_Check_Message);


//         비밀번호 검사
        ET_Password.addTextChangedListener(pwTextWatcher);
        ET_PasswordCheck.addTextChangedListener(pwTextWatcher);


//        ET_Name.addTextChangedListener(NameChecked);
//        ET_PhoneNum.addTextChangedListener(PhoneNumChecked);


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
        class ImageThread extends Thread {
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






        //아이디 중복 검사
        BT_중복체크.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //중복 체크시 메시지를 아이디가 없으면 띄워서 사용가능 아이디입니다 또는, 아이디가 있으면 이미 등록된 아이디입니다. 라고 띄울것.
                // 그리고 아이디가 사용가능 할시 확인을 누르면 회색부분으로 건들지 못하게 만들것, 아니오를 누르면 다시 진행 되도록 할것.
                //TODO : 아래 핸들러부분 수정할것.
                onClickHandler();
            }
        });

        IV_전화기 = findViewById(R.id.IV_전화기);
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
                Intent intent = new Intent(signup_page2.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page2.this, review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page2.this, magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page2.this, qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그인.setClickable(true);
        TV_로그인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page2.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_회원가입.setClickable(true);
        TV_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page2.this, signup_page1.class);
                startActivity(intent);
                finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page2.this, basket_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_가입하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = ET_Email.getText().toString().trim();
                String PW = ET_Password.getText().toString().trim();
                String Name = ET_Name.getText().toString().trim();
                String PhoneNum = ET_PhoneNum.getText().toString().trim();

                if (CheckID.equals(true)) {
                    UserRegister();
                    addUser(Email, PW, Name, PhoneNum);
                } else if (CheckID.equals(false)) {
                    Toast.makeText(signup_page2.this, "중복 체크를 눌러서                                       이메일 사용여부를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Store에 추가함.
    private void addUser(String email, String pw, String name, String phoneNum) {


        UserHelper userHelper = new UserHelper(email, pw, name, phoneNum);

        UsersCRef.document(email).set(userHelper)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "Document has been saved!");
                        Toast.makeText(signup_page2.this, "User saved!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Document was not saved!", e);
                Toast.makeText(signup_page2.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Authentication에 추가함.
    private void UserRegister() {

        String Email = ET_Email.getText().toString().trim();
        String PW = ET_Password.getText().toString().trim();
        String Name = ET_Name.getText().toString().trim();
        String PhoneNum = ET_PhoneNum.getText().toString().trim();


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
        auth.createUserWithEmailAndPassword(Email, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(), "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signup_page2.this, signup_page3.class));
                    finish();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "이미 가입이 되어있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // If sign in fails, display a message to the user.

                }
            }
        });
    }

    private TextWatcher pwTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String PW = ET_Password.getText().toString(); // 트림은 앞뒤 공백을 없애줌, 가운데 없애는 건 replace이다.
            String PWChecked = ET_PasswordCheck.getText().toString();


            if (PW.equals("") && PWChecked.equals("")) {
                PW_Check_Message.setTextColor(Color.parseColor(Black));
                PW_Check_Message.setText("비밀번호를 입력해주세요.");
                비밀번호확인 = false;
            } else if (PW.equals(PWChecked)) {
                PW_Check_Message.setTextColor(Color.parseColor(Blue));
                PW_Check_Message.setText("비밀번호가 일치합니다.");
                비밀번호확인 = true;
            } else {
                PW_Check_Message.setTextColor(Color.parseColor(Red));
                PW_Check_Message.setText("비밀번호를 확인해주세요.");
                비밀번호확인 = false;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void onClickHandler() {

        String Email = ET_Email.getText().toString();

        if (Email.isEmpty()) {
            ET_Email.setError("이메일을 입력해주세요.");
            ET_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            ET_Email.setError("이메일 주소 형식에 맞게 입력해주세요.");
            ET_Email.requestFocus();
            return;
        } else {
            DocumentReference CheckEmail = firestore.collection("Users").document(Email);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            CheckEmail.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!");
                            //중복된 아이디일경우, >> 확인만 나오게.
                            builder.setTitle("이메일 중복 확인").setMessage("이미 등록된 이메일입니다.");

                            builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            Log.d(TAG, "Document does not exist!");
                            //중복된 아이디가 아닐경우, 확인 또는 아니오가 나오게. 확인 누를경우 한번더 확인 할수 있게 만들기.
                            builder.setTitle("이메일 중복 확인").setMessage("사용가능한 이메일입니다.");

                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();
                                    ET_Email.setEnabled(false);
                                    CheckID = true;
                                }
                            });

                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("signup2_onStart", 실행);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("signup2_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("signup2_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("signup2_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("signup2_onDestroy", 실행);
    }


}
