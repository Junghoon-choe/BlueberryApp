package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class A_Writing_Page extends AppCompatActivity {


    //TODO :1. 파이어베이스에 자료 추가하기.
    //TODO :2. 파이어베이스의 자료를 payment페이지의 리사이클러뷰로 불러오기.

    //파이어베이스 qna 또는 Review 다큐에 넣을 값 > set
    private ImageView IV_상품사진;
    private Button BT_사진추가, BT_확인;
    private AppCompatCheckBox CB_QNA, CB_REVIEW;
    private TextInputLayout mTitle, mWriting;
    private int comment = 0;
    private int good = 0;


    EditText editText_Title, editText_Writing;

    //파이어베이스의 다큐먼트로 쓸 인텐트값
    private String FoodName,FoodPrice,FoodImage,FoodAmount;

    //사진 설정
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private Uri mImageUri;

    //파이어 베이스 설정
    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    //Store
    private CollectionReference foodStoreCRef = DB.collection("FoodStore");
    private CollectionReference QNACRef = DB.collection("QNA");
    private CollectionReference REVIEWCRef = DB.collection("REVIEW");
    //Storage
    private StorageReference QNAStorageRef = FirebaseStorage.getInstance().getReference("QNAImages");
    private StorageReference REVIEWStorageRef = FirebaseStorage.getInstance().getReference("REVIEWImages");

    private DatabaseReference mDatabaseRef;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__writing__page);

        IV_상품사진 = findViewById(R.id.IV_상품사진);
        BT_사진추가 = findViewById(R.id.BT_사진추가);
        BT_확인 = findViewById(R.id.BT_확인);
        CB_QNA = findViewById(R.id.CB_QNA);
        CB_REVIEW = findViewById(R.id.CB_REVIEW);
        mTitle = findViewById(R.id.Title);
        mWriting = findViewById(R.id.Writing);

        Intent intent = getIntent();
        FoodName = intent.getExtras().getString("FoodNameToWriting");
        FoodPrice = intent.getExtras().getString("FoodPriceToWriting");
        FoodImage = intent.getExtras().getString("FoodImageToWriting");
        FoodAmount = intent.getExtras().getString("FoodAmountToWriting");


        //TODO : 아래 로직 구현 하기.
        //확인 누르면 해당 사진+글제목+글이 해당 상품안에 서브컬랙션으로 들어감.
        // 예) 다큐# : 사과 >       컬렉션@ : 리뷰와 질문 > 다큐# : 사과+리뷰 >      컬렉션@ : 사과 + 댓글 > 다큐# :
        //서브컬렉션으로 들어간 내용은 리사이클러뷰로 구현됨. 또한, 그

        editText_Title = mTitle.getEditText();
        editText_Writing = mWriting.getEditText();

        final TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력난에 변화가 있을 시 조치
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 끝났을 때 조치
            }

            public void afterTextChanged(Editable s) {
                // 입력하기 전에 조치
            }
        };

        assert editText_Title != null;
        assert editText_Writing != null;

        editText_Title.addTextChangedListener(textWatcher);
        editText_Writing.addTextChangedListener(textWatcher);

        CB_QNA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CB_REVIEW.setChecked(false);
            }
        });
        CB_REVIEW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CB_QNA.setChecked(false);
            }
        });


        BT_확인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CB_QNA.isChecked()) {
                    addQnA();
                } else if (CB_REVIEW.isChecked()) {
                    addReview();
                } else if(!CB_QNA.isChecked()&&!CB_REVIEW.isChecked()) {
                    Toast.makeText(A_Writing_Page.this,"게시판을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        BT_사진추가.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
        }

        BT_확인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(A_Writing_Page.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    if (CB_QNA.isChecked()) {
                        CB_REVIEW.setChecked(false);
                        addQnA();

                    } else if (CB_REVIEW.isChecked()) {
                        CB_QNA.setChecked(false);
                        addReview();
                    }
                }
            }
        });
    }

    private void addQnA() {

        String userName = MyApplication.회원Name.trim();
        final String title = mTitle.getEditText().toString().trim();
        final String writing = mWriting.getEditText().toString().trim();
        final int countGood = good;
        final int countComment = comment;

        if (title.isEmpty() || writing.isEmpty()) {
            return;
        } else {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(A_Writing_Page.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                //여기에서 foodImage폴더에 넣을수 있게 던져줌. 하위항목 이름 지어줄수 있는 곳.
                //파일을 생성하는 fileRef를 선언.

                //하위 항목으로 이미지의 이름을 지어준다. child에서 현재 시간으로 이름을 지어준다. 뒤에부분은 .jpg로 확장자를 작명하는 곳 이다.
                final StorageReference fileRef = QNAStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

                uploadTask = fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                qna_review_item qnaReviewItem = new qna_review_item(uri.toString(), MyApplication.회원Name, title.toString().trim(), writing, countComment, countGood);

                                Toast.makeText(A_Writing_Page.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                                QNACRef.document(System.currentTimeMillis()+FoodName).set(qnaReviewItem);

                                Intent intent = new Intent(A_Writing_Page.this, A_main_page.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(A_Writing_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }


    private void addReview() {
    }


//    private void uploadFile() {
//
//        String userName = MyApplication.회원Name.trim();
//        final String title = mTitle.getEditText().toString().trim();
//        String writing = mWriting.getEditText().toString().trim();
//        int countGood = good;
//        int countComment = comment;
//
//
//        if (title.isEmpty() || writing.isEmpty()) {
//            return;
//        } else {
//            if (uploadTask != null && uploadTask.isInProgress()) {
//                Toast.makeText(A_Writing_Page.this, "Upload in progress", Toast.LENGTH_SHORT).show();
//            } else {
//                //여기에서 foodImage폴더에 넣을수 있게 던져줌. 하위항목 이름 지어줄수 있는 곳.
//                //파일을 생성하는 fileRef를 선언.
//
//                //하위 항목으로 이미지의 이름을 지어준다. child에서 현재 시간으로 이름을 지어준다. 뒤에부분은 .jpg로 확장자를 작명하는 곳 이다.
//                final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
//
//                uploadTask = fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        Handler handler = new Handler();
//
//
//                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString(), edit_amount.getText().toString().trim());
//                                Toast.makeText(B_AddItem.this, "업로드 성공", Toast.LENGTH_SHORT).show();
//
//
//                                foodStoreCRef.document(edit_title.getText().toString().trim()).set(re_food);
//
//                                //혹시 에러나면 그냥 지금 페이지로 다시 이동.
//                                startActivity(new Intent(A_Writing_Page.this, A_payment_page.class));
//                                finish();
//                            }
//                        });
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(A_Writing_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    }


    // TODO : 아래 메서드 이해하기.
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(IV_상품사진);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
