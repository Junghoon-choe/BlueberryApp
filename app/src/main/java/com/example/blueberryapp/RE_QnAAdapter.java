package com.example.blueberryapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RE_QnAAdapter extends RecyclerView.Adapter<RE_QnAAdapter.ListViewHolder> implements OnListItemClickListener {

    private ArrayList<RE_QnA> qnaList;
    /*private Context mContext;*/
    OnListItemClickListener qnalistener;
    private static final String Tag = "RecyclerView";
    private Context mContext;
    private String userID = MyApplication.회원ID;

    //private int getMlistPos (String mlist){
//    return mlist.indexOf(mlist);
//}
    //1. 컨텍스트 메뉴를 사용하려면 RecyclerView.ViewHolder를 상속받은 클래스에서
    //OnCreateContextMenuListener리스너를 구현해야 한다.
    public class ListViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {

        private TextView qna_id;
        private TextView title;
        private TextView writing;

        ListViewHolder(final View itemView) {
            super(itemView);

            this.qna_id = itemView.findViewById(R.id.qna_id);
            this.title = itemView.findViewById(R.id.qna_title);
            this.writing = itemView.findViewById(R.id.qna_writing);

            //2. setOnCreateContextMenuListener를 현재 클래서에서 구현한다고 설정 해둔다.
            /*itemView.setOnCreateContextMenuListener(this);*/

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    {
//                        int position = getAdapterPosition();
//
//                        if (qnalistener != null) {
//                            qnalistener.onItemClick(ListViewHolder.this, itemView, position);
//                        }
//                    }
//                }
//            });
        }

        /*@Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
            //3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호충되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를
            //선택했는지 리스너에서 구분하게 된다.

            MenuItem Edit = menu.add(Menu.NONE, 1001,1,"수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002,2,"삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }
        //4. 컨텍스트 메뉴에서 항복 클릭시 동작을 설정합니다.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case 1001: //5. 편집 항목을 선택시

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        //다이얼로그를 보여주기 위해 edit_box파일을 사용합니다.
                        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_box,null,false);
                        builder.setView(view);
                        final Button ButtonSubmit = view.findViewById(R.id.BT_edit);
                        final EditText editTexttitle = view.findViewById(R.id.edit_title);
                        final EditText editTextwriting = view.findViewById(R.id.edit_writing);

                        //6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줍니다.

                        Log.v("실행",String.valueOf(mList));

                        editTexttitle.setText(mList.get(getAdapterPosition()).getTitle());
                        editTextwriting.setText(mList.get(getAdapterPosition()).getWriting());

                        final AlertDialog dialog = builder.create();
                                ButtonSubmit.setOnClickListener(new View.OnClickListener() {

                                    //7. 수정 버튼을 클릭하면 현재 ui에 입력되어 있는 내용으로~
                                    @Override
                                    public void onClick(View v) {

                                        String strTitle = editTexttitle.getText().toString();
                                String strWriting = editTextwriting.getText().toString();
                                RE_QnA qna = new RE_QnA(null,strTitle,strWriting);
                                //8. ListArray에 있는 데이터를 변경하고,
                                mList.set(getAdapterPosition(),qna);

                                //9. 어댑터에서 RecyclerView에 반영하도록 합니다.
                                notifyItemChanged(getAdapterPosition());
                                dialog.dismiss();


                            }
                        });

                        dialog.show();
                        break;

                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemChanged(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),mList.size());
                        break;


                    default:

                }
                return true;


            }
        };*/

    }


    //여기서 메인에 넣을 정보와 어댑터를 이어준다.
    public RE_QnAAdapter(Context mContext, ArrayList<RE_QnA> qnaList) {
        this.mContext = mContext;
        this.qnaList = qnaList;
    }


    @NonNull
    @Override
    //뷰홀더 객체가 생성될때
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qna_list, viewGroup, false);
        ListViewHolder viewHolder = new ListViewHolder(view);

        return new ListViewHolder(view);

    }


    public void setOnItemClickListener(OnListItemClickListener qnalistener) {
        this.qnalistener = qnalistener;
    } //외부에서 리스너를 설정할 수 있도록 메서드 추가하기.


    @Override
    public void onItemClick(ListViewHolder holder, View view, int position) {
        if (qnalistener != null) {
            qnalistener.onItemClick(holder, view, position);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewholder, int position) {

//        viewholder.qna_id.setText(userID);
        viewholder.qna_id.setText(qnaList.get(position).getUserID());
        viewholder.title.setText(qnaList.get(position).getQnATitle());
        viewholder.writing.setText(qnaList.get(position).getQnAWriting());


    }


    @Override
    public int getItemCount() {
        return (null != qnaList ? qnaList.size() : 0);
    }


    public RE_QnA getItem(int position) {
        return qnaList.get(position);
    }

    public void setItems(ArrayList<RE_QnA> items) {
        this.qnaList = items;
    }

    public void addItem(RE_QnA item) {
        qnaList.add(item);
    }

    public void setItem(int position, RE_QnA item) {
        qnaList.set(position, item);
    }

}
