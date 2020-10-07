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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    EditText edit_title, edit_price;
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
        progressBar = findViewById(R.id.ID_progressbar);

        확인.setOnClickListener(this);
        상품사진.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");


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

//    private void addItem(String title, String price) {
//
//
//        if (title.isEmpty() || price.isEmpty()) {
//            return;
//        } else {
//            if (uploadTask != null && uploadTask.isInProgress()) {
//                Toast.makeText(B_AddItem.this, "Upload in progress", Toast.LENGTH_SHORT).show();
//            } else {
//                //여기에서 foodImage폴더에 넣을수 있게 던져줌. 하위항목 이름 지어줄수 있는 곳.
//                //파일을 생성하는 fileRef를 만든다.
//                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
//
//                //하위 항목으로 이미지의 이름을 지어준다. child에서 현재 시간으로 이름을 지어준다. 뒤에부분은 .jpg로 확장자를 작명하는 곳 이다.
//                //getDownload는 Url을 다운로드 한다는 말이다.
//
//                uploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.setProgress(0);
//                            }
//                        }, 500);
//
//                        final String ImageURI;
//
//                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Log.d(TAG, "Image is : " + uri.toString());
//
//
//                                RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString());
//
//
//                                foodStoreCRef.document(edit_title.getText().toString().trim()).set(re_food).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(B_AddItem.this, "업로드 성공", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                                startActivity(new Intent(B_AddItem.this, B_ItemView.class));
//                                finish();
//
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(B_AddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    }


    private void uploadFile() {
        progressBar.setVisibility(View.VISIBLE);



        final String foodName = edit_title.getText().toString().trim();
        String foodPrice = edit_price.getText().toString().trim();

        if (foodName.isEmpty() || foodPrice.isEmpty()) {
            return;
        } else {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(B_AddItem.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                //여기에서 foodImage폴더에 넣을수 있게 던져줌. 하위항목 이름 지어줄수 있는 곳.
                //파일을 생성하는 fileRef를 선언.


                //하위 항목으로 이미지의 이름을 지어준다. child에서 현재 시간으로 이름을 지어준다. 뒤에부분은 .jpg로 확장자를 작명하는 곳 이다.
                //getDownload는 Url을 다운로드 한다는 말이다.
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
                                RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString());
                                Toast.makeText(B_AddItem.this, "업로드 성공", Toast.LENGTH_SHORT).show();


                                foodStoreCRef.document(edit_title.getText().toString().trim()).set(re_food);

                                startActivity(new Intent(B_AddItem.this, B_ItemView.class));
                                finish();
                            }
                        });




//                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Log.d(TAG, "Image is : " + uri.toString());
//
//                                RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString());
//                                Toast.makeText(B_AddItem.this, "업로드 성공", Toast.LENGTH_SHORT).show();
//
//
//                                foodStoreCRef.document(edit_title.getText().toString().trim()).set(re_food);
//
//                                startActivity(new Intent(B_AddItem.this, B_ItemView.class));
//                                finish();
//
//                            }
//                        });
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
    // error : StorageException has occurred.
    //    Object does not exist at location.

//    private void uploadFile() {
//        if (mImageUri != null) {
//            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
//
//            Log.d(TAG,"1"+fileReference);
//
//            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setProgress(0);
//                        }
//                    }, 500);
//
//                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(), edit_price.getText().toString().trim(), uri.toString());
//
//                            foodStoreCRef.document(edit_title.getText().toString().trim()).set(re_food);
//                            startActivity(new Intent(B_AddItem.this, B_ItemView.class));
//                            finish();
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(B_AddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        } else {
//            Toast.makeText(this, "파일이 선택 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//        }
//    }


//    private void uploadFile() {
//
//        if (imageUri != null) {
//            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
//                    + "." + getFileExtension(imageUri));
//
//            uploadTask = fileReferenece.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    RE_Food re_food = new RE_Food(edit_title.getText().toString().trim(),edit_price.getText().toString().trim(),taskSnapshot.)
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(B_AddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//        }
//
//    }

//    private void addFood(String foodName, String foodPrice, String foodImage) {
//
//
//        RE_Food re_food = new RE_Food(foodName, foodPrice, foodImage);
//
//        foodStoreCRef.document(foodName).set(re_food)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Log.d(TAG, "Document has been saved!");
//                        Toast.makeText(B_AddItem.this, "User saved!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(B_AddItem.this, B_ItemView.class));
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Document was not saved!", e);
//                Toast.makeText(B_AddItem.this, "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


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






    /*public void saveFoodData(){
        SharedPreferences sharedPreferences = getSharedPreferences("상품",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String FoodName = edit_title.getText().toString(); //입력칸에서 가져온 데이터를 ID에 넣고, ID,Password를 오브젝트 칸에 넘기고, 나머지 이름,이메일,폰넘버,주소를
        String FoodPrice = edit_price.getText().toString();


        RE_Food re_food  = new RE_Food(FoodName,FoodPrice);
        mList.add(re_food);
        Gson gson = new Gson();
        String json = gson.toJson(re_food);
        editor.putString(FoodName,json);
        editor.apply();
    }*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //    private DocumentReference documentReference = FirebaseFirestore.getInstance().collection("sampleData").document("inspiration");
//    private DocumentReference documentReference = FirebaseFirestore.getInstance().document("sampleData/inspiration");
    //EX) sampleData/inspiration/uesr/user_123/history/05182018 >> 수집/기록/수집/기록/수집/기록 (위와 같은 내용으로 작성할시 참고 해야함.)

//    public void addFood(View view) {
//        EditText editName = findViewById(R.id.ET_상품명입력);
//        EditText editPrice = findViewById(R.id.ET_상품가격입력);
//
//        String foodNameText = editName.getText().toString().trim();
//        String foodPriceText = editPrice.getText().toString().trim();
//
//        if (foodNameText.isEmpty() || foodPriceText.isEmpty()) {
//            return;
//        }
//
//
//        RE_Food re_food = new RE_Food(foodNameText, foodPriceText, null);
//
//        foodStoreCRef.document(foodNameText).set(re_food)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Document has been saved!");
//                        Toast.makeText(B_AddItem.this, "Food saved!", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Document was not saved!", e);
//                Toast.makeText(B_AddItem.this, "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
////        ItemAdder itemAdder = new ItemAdder(null, foodNameText, foodPriceText);
////        reference.child("Items").setValue(itemAdder); //참조되는 부분에 사용자가 입력하는 값을 참조이름으로 적는다. ID값으로 갖는 키값에 벨류로 유저헬퍼의 정보를 넣는다.
//        Intent intent = new Intent(B_AddItem.this, B_ItemView.class);
//        startActivity(intent);
//        finish();
//
//    }


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




