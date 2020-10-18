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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.grpc.Compressor;

public class B_AddItem extends AppCompatActivity implements View.OnClickListener {


    public static final String TAG = "InspiringQuote";
    static private ArrayList<RE_Food> mList = new ArrayList<>();
    static private RE_FoodAdapter mAdapter;
    Button 사진추가, 확인;
    ImageView 상품사진; //게시판 리사이클러뷰 구현한뒤에구현할 것.
    EditText edit_title, edit_price, edit_amount;
    private Uri mImageUri;

    String compressor;



//    FirebaseDatabase database;
//    DatabaseReference reference;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference foodStoreCRef = DB.collection("FoodStore");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");
    private DatabaseReference mDatabaseRef;

    private StorageTask uploadTask;


//    private DocumentReference foodStoreDRef = DB.document("Test");


    // 체크박스 체크여부
    public int TERMS_AGREE = 0; // 체크 안됬을시 0, 체크 됬을 경우 1

    AppCompatCheckBox check; // 첫번째 동의
    private ProgressBar progressBar;

//        EditText edit_title, edit_writing;
//        Button BT_edit;

    private String 실행 = "실행";

    //사진 설정
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_add_item);
        상품사진 = findViewById(R.id.IV_상품사진);
        확인 = findViewById(R.id.BT_확인);
        사진추가 = findViewById(R.id.BT_사진추가);
        edit_title = findViewById(R.id.ET_상품명입력);
        edit_price = findViewById(R.id.ET_상품가격입력);
        edit_amount = findViewById(R.id.ET_상품수량입력);
        progressBar = findViewById(R.id.ID_progressbar);

        확인.setOnClickListener(this);
        상품사진.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");

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
                    Toast.makeText(B_AddItem.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    private void uploadFile() {
        progressBar.setVisibility(View.VISIBLE);



        final String foodName = edit_title.getText().toString().trim();
        int foodPrice = Integer.parseInt(edit_price.getText().toString());


        if (foodName.isEmpty() || foodPrice==0) {
            return;
        } else {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(B_AddItem.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
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
                                RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString(),edit_amount.getText().toString().trim());
                                Toast.makeText(B_AddItem.this, "업로드 성공", Toast.LENGTH_SHORT).show();


                                foodStoreCRef.document(edit_title.getText().toString().trim()).set(re_food);

                                startActivity(new Intent(B_AddItem.this, B_ItemView.class));
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(B_AddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

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




