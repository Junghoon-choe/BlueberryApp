package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


public class B_ItemClick extends AppCompatActivity {


    public Boolean 아이템수정;


    private String 실행 = "실행";
    public ArrayList<RE_Food> mList;
    public RE_FoodAdapter mAdapter;
    private Button BT_수정, BT_삭제;

    public EditText 상품명변경;
    public EditText 상품가격변경;

    // 체크박스 체크여부
    public int TERMS_AGREE = 0; // 체크 안됬을시 0, 체크 됬을 경우 1

    private Thread thread;
    AppCompatCheckBox check; // 첫번째 동의

    public ImageView Edit_IV_상품사진;
    public Button BT_사진변경;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_item_click);
        loadData();

        BT_수정 = findViewById(R.id.B_BT_수정);
        BT_삭제 = findViewById(R.id.B_BT_삭제);
        Edit_IV_상품사진 = findViewById(R.id.Edit_IV_상품사진);
        BT_사진변경 = findViewById(R.id.BT_사진변경);
        상품명변경 = findViewById(R.id.B_ET_상품명변경);
        상품가격변경 = findViewById(R.id.B_ET_상품가격변경);


        //리사이클러뷰 어댑터 연동.


        Intent intent = getIntent();

//        int position = intent.getExtras().getInt("title");

        String title = intent.getExtras().getString("title");
        Log.v(title, "title2");
        상품명변경.setText(title);


        String price = intent.getExtras().getString("price");
        Log.v(price, "price2");
        상품가격변경.setText(price);

        //--텍스트 값 가져오는 부분,




        /*intent.putExtra("position",item.getClass());
        String position = extra.getClass()*/


        BT_삭제.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = getIntent();


                int position = intent.getExtras().getInt("position");
                String price = intent.getExtras().getString("price");
                String title = intent.getExtras().getString("title");

                intent.putExtra("position",position);

                Log.v(price, "price");


                SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove(title);
                editor.remove(price);


                Log.v(title, "title");
                Log.v(price, "price");

                editor.apply();


                setResult(RESULT_OK, intent);
                finish();
            }
        });


        //--텍스트 값 가져오는 부분,


    }

    public void BT_CLICK(View view) {

       /* if(check.isChecked()) {
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        TERMS_AGREE = 1;
                    } else {
                        TERMS_AGREE = 0;
                    }
                }
            });
        }*/


        if (view.getId() == R.id.B_BT_수정) {
            //지우고 다시 새로 추가하기.
//            deleteData();
//            mList.remove(0);
//            mAdapter.notifyDataSetChanged();
//            saveData();


            //추가하는 부분
//            Intent intent = new Intent();
//            Log.v("Intent", 실행);
//            intent.putExtra("상품명", 상품명변경.getText().toString());
//            intent.putExtra("상품가격", 상품가격변경.getText().toString());


//            String 상품명
//            String 상품가격;
//
//            상품명.getExtras().getString("상품명");
//            상품가격.getExtras().getString("상품가격");
//
//            Log.v(상품명+"상품명",실행);
//            Log.v(상품가격+"상품가격",실행);
//
//            getIntent().getStringExtra("상품이름");
//
//            RE_Food re_food = new RE_Food(상품명, 상품가격 );
//            mList.add(re_food);
//            saveData();
//
//
//            finish();


        } else if (view.getId() == R.id.B_BT_삭제) {
            Intent intent = getIntent();


            Log.v("Intent", 실행);

//            deleteData();
            //--텍스트 값 가져오는 부분,

            Bundle extras = getIntent().getExtras();



        /*intent.putExtra("position",item.getClass());
        String position = extra.getClass()*/

            String title = extras.getString("title");
            String price = extras.getString("price");

            Log.v(title, " : title");
            Log.v(price, " : price");


            SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.remove(title);
            editor.remove(price);

            Log.v(title, "remove - title");
            Log.v(price, "remove - price");

            editor.apply();

//                      saveData();


            /*saveFoodData();*/
            //String 과일이름 = edit_title.getText().toString();

            //intent.putExtra("과일이름",과일이름);
            //intent.putExtra(과일이름.getText().toString());

            //가져올때
            //Intent intent = getIntent();
            //String 과일이름 = intent.getExtras().getString("과일이름");
            /*intent.putExtra("title",edit_title.getText().toString());*/
            //체크박스도 넣을수 있음.

//            intent.putExtra("상품명",edit_title.getText().toString());
//
//            intent.putExtra("상품가격",edit_price.getText().toString());


            setResult(RESULT_OK, intent);
            Log.v("RESULT_OK", 실행);

            finish();


        }
    }

    //연구


    /*private void doJsonParser(){
        SharedPreferences 상품 = getSharedPreferences("상품",MODE_PRIVATE);
        StringBuffer stringBuffer = new StringBuffer();

        try {
            JSONArray jsonArray = new JSONArray(상품);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String FoodName = jsonObject.getString("FoodName");
                String FoodPrice = jsonObject.getString("FoodPrice");

                stringBuffer.append(
                        "과일명 : "+FoodName+"과일가격 : "+FoodPrice+"");
            }
            stringBuffer.toString();
            Toast.makeText(this.getApplicationContext(),stringBuffer,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

    //연구


    private void saveData() { //이 메소드가 shardpreferences에 저장해준다.
        SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);

        editor.putString("상품목록", json);
        editor.apply();
    }

    public void updateData() {
        SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Bundle extra = getIntent().getExtras();
        String title = extra.getString("상품명");
        String price = extra.getString("상품가격");

        EditText 상품명 = findViewById(R.id.B_ET_상품명변경);
        EditText 상품가격 = findViewById(R.id.B_ET_상품명변경);

        String a = 상품명.getText().toString();
        String b = 상품가격.getText().toString();
        editor.putString(title, a);
        editor.putString(price, b);

        mAdapter.notifyDataSetChanged();
        editor.apply();


    }

    public void deleteData() {

        loadData();

        SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Bundle extras = getIntent().getExtras();



        /*intent.putExtra("position",item.getClass());
        String position = extra.getClass()*/

        String title = extras.getString("title");
        String price = extras.getString("price");

        Log.v(title, " : title");
        Log.v(price, " : price");


        sharedPreferences.getString(title, null);
        sharedPreferences.getString(price, null);

        Log.v(title, "title포지션1");
        Log.v(price, "price포지션1");

        editor.remove(title);
        editor.remove(price);

        Log.v(title, "title포지션2");
        Log.v(price, "price포지션2");

        Log.v(title, "실행");
        Log.v(price, "실행");


        Toast.makeText(getApplicationContext(), "deleteData 실행됨", Toast.LENGTH_SHORT).show();

        editor.apply();


    }


    private void loadData() { //읽어오는 메소드
        SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
        String json = sharedPreferences.getString("상품목록", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RE_Food>>() {
        }.getType();
        mList = gson.fromJson(json, type);


        if (mList == null) {
            mList = new ArrayList<>();
        }
    }

}
