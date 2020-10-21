package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class A_review_writing_page extends AppCompatActivity implements View.OnClickListener {


    private String userName = MyApplication.회원Name;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    private TextInputLayout ET_Title, ET_Writing;

    private String 실행 = "실행";


    //--
    public static final String TAG = "InspiringQuote";
    static private ArrayList<RE_Food> mList = new ArrayList<>();
    static private RE_FoodAdapter mAdapter;
    Button 사진추가, 확인;
    ImageView 상품사진; //게시판 리사이클러뷰 구현한뒤에구현할 것.
    EditText edit_title, edit_price, edit_amount;
    private Uri mImageUri = null;
    private ProgressBar progressBar;
    String compressor;
    String 다큐제목;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference foodStoreCRef = DB.collection("REVIEW");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("REVIEW_images");
    private DatabaseReference mDatabaseRef;

    private StorageTask uploadTask;

    //사진 설정
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private boolean clicked = false;
    //--


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_review_writing_page);

        상품사진 = findViewById(R.id.IV_상품사진);
        확인 = findViewById(R.id.BT_확인);
        사진추가 = findViewById(R.id.BT_사진추가);
        edit_title = findViewById(R.id.ET_상품명입력);
        edit_price = findViewById(R.id.ET_상품가격입력);
        edit_amount = findViewById(R.id.ET_상품수량입력);
        progressBar = findViewById(R.id.ID_progressbar);

        확인.setOnClickListener(this);
        상품사진.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("REVIEW_images");

        if (edit_amount.getText() == null) {
            edit_amount.setText("1");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        사진추가.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                clicked = true;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
        }

        확인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(A_review_writing_page.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();


                }
            }
        });
    }


    private void uploadFile() {
        progressBar.setVisibility(View.VISIBLE);

        final String ReviewTitle = edit_title.getText().toString().trim();
        final String ReviewWriting = edit_price.getText().toString().trim();
        final String 다큐제목 = MyApplication.회원Email+System.currentTimeMillis();
        final String 작성자이름  = MyApplication.회원Name.trim();
        final String 작성자이메일 = MyApplication.회원Email.trim();

        if (ReviewTitle.isEmpty() || ReviewWriting.isEmpty()) {
            return;
        } else if (clicked) {

            //여기에서 foodImage폴더에 넣을수 있게 던져줌. 하위항목 이름 지어줄수 있는 곳.
            //파일을 생성하는 fileRef를 선언.

            //하위 항목으로 이미지의 이름을 지어준다. child에서 현재 시간으로 이름을 지어준다. 뒤에부분은 .jpg로 확장자를 작명하는 곳 이다.
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            uploadTask = fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            RE_REVIEW_test re_review_test = new RE_REVIEW_test(작성자이메일,다큐제목,edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString(),작성자이름);
                            Toast.makeText(A_review_writing_page.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                            foodStoreCRef.document(다큐제목).set(re_review_test);

                            Intent intent = new Intent(A_review_writing_page.this, A_review_page.class);
                            intent.putExtra("리뷰제목",다큐제목);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(A_review_writing_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            RE_REVIEW_test re_review_test = new RE_REVIEW_test(작성자이메일,다큐제목,edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), null,작성자이름);
            Toast.makeText(A_review_writing_page.this, "업로드 성공", Toast.LENGTH_SHORT).show();

            foodStoreCRef.document(다큐제목).set(re_review_test);

            Intent intent = new Intent(A_review_writing_page.this, A_review_page.class);
            intent.putExtra("리뷰제목",다큐제목);
            startActivity(intent);
            finish();
        }
    }

    //    // TODO : 아래 메서드 이해하기.
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

            Picasso.with(this).load(mImageUri).into(상품사진);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.BT_사진추가:

                openFileChooser();

                break;

            case R.id.BT_확인:

                break;
        }

    }


}


