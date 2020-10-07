package com.example.blueberryapp;

import android.view.View;

public interface OnFoodItemClickListener {
    void onItemClick( View view, int position);
}

// 메서드가 호출될 때 파라미터로 뷰홀더 객체와 뷰 객체 그리고 뷰의 position 정보가 전달되도록 합니다. position 정보는
// 몇 번째 아이템인지를 구분할 수 있는 인덱스 값입니다. 이 인터페이스를 사용하도록 ViewHolder 클래스를 수정합니다.