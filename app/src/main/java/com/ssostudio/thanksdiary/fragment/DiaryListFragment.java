package com.ssostudio.thanksdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssostudio.thanksdiary.DiaryDetailActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.adapter.DiarySelectListViewAdapter;
import com.ssostudio.thanksdiary.dialog.DateRangeDialog;
import com.ssostudio.thanksdiary.diary.DiaryManager;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;

public class DiaryListFragment extends Fragment implements View.OnClickListener {
    private View view;
    private static View staticView;
    private static Context _context;

    FloatingActionButton dateChangeBtn;
    private TextView dateText;
    private ListView listView;
    private DiarySelectListViewAdapter adapter;
    private ArrayList<DiaryModel> _list;
    private LinearLayout llNoResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);
        staticView = view;
        _context = getContext();

        setDateChangeBtn();
        setDateText(getString(R.string.yesterday));
        setListInit();

        return view;
    }

    private void setListInit() {
        if (staticView == null)
            return;

        setListViewInit();

        if (_list == null || _list.size() == 0) {
            noResultVisible();
        } else {
            listViewVisible();
        }

    }

    public void setListViewInit(){
        _list = new DiaryManager().getYesterdayList();
        listView = staticView.findViewById(R.id.diary_list_view);
        adapter = new DiarySelectListViewAdapter(_context, _list);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            boolean firstDragFlag = true;
            boolean dragFlag = false;   //현재 터치가 드래그 인지 확인
            float startYPosition = 0;       //터치이벤트의 시작점의 Y(세로)위치
            float endYPosition = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:       //터치를 한 후 움직이고 있으면
                        dragFlag = true;
                        if(firstDragFlag) {     //터치후 계속 드래그 하고 있다면 ACTION_MOVE가 계속 일어날 것임으로 무브를 시작한 첫번째 터치만 값을 저장함
                            startYPosition = motionEvent.getY(); //첫번째 터치의 Y(높이)를 저장
                            firstDragFlag= false;   //두번째 MOVE가 실행되지 못하도록 플래그 변경
                        }

                        break;

                    case MotionEvent.ACTION_UP :
                        endYPosition = motionEvent.getY();
                        firstDragFlag= true;

                        if(dragFlag) {  //드래그를 하다가 터치를 실행
                            // 시작Y가 끝 Y보다 크다면 터치가 아래서 위로 이루어졌다는 것이고, 스크롤은 아래로내려갔다는 뜻이다.
                            // (startYPosition - endYPosition) > 10 은 터치로 이동한 거리가 10픽셀 이상은 이동해야 스크롤 이동으로 감지하겠다는 뜻임으로 필요하지 않으면 제거해도 된다.
                            if((startYPosition > endYPosition) && (startYPosition - endYPosition) > 10) {
                                dateChangeBtn.hide();
                            }
                            //시작 Y가 끝 보다 작다면 터치가 위에서 아래로 이러우졌다는 것이고, 스크롤이 올라갔다는 뜻이다.
                            else if((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
                                dateChangeBtn.show();
                            }
                        }

                        startYPosition = 0.0f;
                        endYPosition = 0.0f;
                        break;
                }
                return false;
            }
        });

    }

    public void updateListView(ArrayList<DiaryModel> list){
        if (staticView == null)
            return;

        _list = list;

        if (_list == null || _list.size() == 0) {
            noResultVisible();
        } else {
            listViewVisible();
            listView = staticView.findViewById(R.id.diary_list_view);
            adapter =  new DiarySelectListViewAdapter(_context, _list);
            listView.setAdapter(adapter);
        }

    }

    private void noResultVisible() {
        llNoResult = staticView.findViewById(R.id.ll_on_result);
        llNoResult.setVisibility(View.VISIBLE);
        listView = staticView.findViewById(R.id.diary_list_view);
        listView.setVisibility(View.GONE);
    }

    private void listViewVisible() {
        llNoResult = staticView.findViewById(R.id.ll_on_result);
        llNoResult.setVisibility(View.GONE);
        listView = staticView.findViewById(R.id.diary_list_view);
        listView.setVisibility(View.VISIBLE);
    }

    private void setDateChangeBtn() {
        dateChangeBtn = staticView.findViewById(R.id.date_change_button);
        dateChangeBtn.setOnClickListener(this);
    }

    public void setDateText(String sDate) {
        dateText = staticView.findViewById(R.id.date_text);
        dateText.setText(sDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_change_button:
                onDateChangeBtnClick();
                break;
        }
    }

    private void onDateChangeBtnClick() {
        new DateRangeDialog(getContext(), getFragmentManager()).onShowDialog();
    }
}
