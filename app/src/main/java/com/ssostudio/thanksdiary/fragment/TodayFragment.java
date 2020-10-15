package com.ssostudio.thanksdiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ssostudio.thanksdiary.DiaryDetailActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.adapter.DiaryListViewAdapter;
import com.ssostudio.thanksdiary.dialog.DateSelectDialog;
import com.ssostudio.thanksdiary.diary.DiaryManager;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.FormatChangeManager;

import java.util.ArrayList;


public class TodayFragment extends Fragment implements View.OnClickListener {
    private View view;
    private static View staticView;
    private FloatingActionButton writeBtn;
    private TextView dailyAvgText, totalCountText, todayCountText, anotherDayCountText;
    private ListView listView;
    private ArrayList<DiaryModel> list;
    private int totalCount;
    private int todayCount;
    private int anotherDayCount;
    private DiaryListViewAdapter adapter;
    private ImageView moreBtn;
    private LinearLayout llNoResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today, container, false);

        staticView = view;
        listView = view.findViewById(R.id.diary_list_view);

        setWriteBtn();
        setDailyAvgText();
        setTotalCountText();
        getTodayList();
        setTodayCountText();
        setAnotherDayCountText();
        setListInit();

        return view;
    }

    //     FloatingActionButton Remove
    private void setWriteBtn() {
        writeBtn = view.findViewById(R.id.thanks_write_button);
        writeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.thanks_write_button:
                onWriteBtnClick();
                break;
        }
    }

    private void onWriteBtnClick() {
        new DateSelectDialog(getContext()).onShowDialog(0);
    }

    public static void onSnackbarMake(int stringId) {
        Snackbar.make(staticView, stringId, Snackbar.LENGTH_SHORT).show();
    }

    private void setDailyAvgText() {
        String count = FormatChangeManager.floatToCommaFormat(new DiaryManager().dailyAvgCount());

        dailyAvgText = staticView.findViewById(R.id.daily_average_text);
        dailyAvgText.setText(count);
    }

    private void setTotalCountText() {
        totalCount = new DiaryManager().diaryTotalCount();
        String count = FormatChangeManager.intToCommaFormat(totalCount);
        totalCountText = staticView.findViewById(R.id.total_count_text);
        totalCountText.setText(count);
    }

    private void getTodayList() {
        todayCountText = staticView.findViewById(R.id.today_count_text);
        list = new DiaryManager().todayWriteDiary();
    }

    private void setTodayCountText() {
        todayCount = list.size();
        String count = FormatChangeManager.intToCommaFormat(todayCount);
        todayCountText.setText(count);
    }

    private void setListInit() {
        setListViewInit();

        if (list == null || list.size() == 0) {
            noResultVisible();
        } else {
            listViewVisible();
        }
    }

    private void setListViewInit() {
        adapter = new DiaryListViewAdapter(getContext(), list);
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
                        if (firstDragFlag) {     //터치후 계속 드래그 하고 있다면 ACTION_MOVE가 계속 일어날 것임으로 무브를 시작한 첫번째 터치만 값을 저장함
                            startYPosition = motionEvent.getY(); //첫번째 터치의 Y(높이)를 저장
                            firstDragFlag = false;   //두번째 MOVE가 실행되지 못하도록 플래그 변경
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        endYPosition = motionEvent.getY();
                        firstDragFlag = true;

                        if (dragFlag) {  //드래그를 하다가 터치를 실행
                            // 시작Y가 끝 Y보다 크다면 터치가 아래서 위로 이루어졌다는 것이고, 스크롤은 아래로내려갔다는 뜻이다.
                            // (startYPosition - endYPosition) > 10 은 터치로 이동한 거리가 10픽셀 이상은 이동해야 스크롤 이동으로 감지하겠다는 뜻임으로 필요하지 않으면 제거해도 된다.
                            if ((startYPosition > endYPosition) && (startYPosition - endYPosition) > 10) {
                                writeBtn.hide();
                            }
                            //시작 Y가 끝 보다 작다면 터치가 위에서 아래로 이러우졌다는 것이고, 스크롤이 올라갔다는 뜻이다.
                            else if ((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
                                writeBtn.show();
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

    private void setAnotherDayCountText() {
        anotherDayCount = totalCount - todayCount;
        String count = FormatChangeManager.intToCommaFormat(anotherDayCount);
        anotherDayCountText = staticView.findViewById(R.id.another_day_count_text);
        anotherDayCountText.setText(count);
    }

    public void todayDiaryDataUpdate() {
        if (staticView == null)
            return;

        getTodayList();

        if (list == null || list.size() == 0) {
            noResultVisible();
        } else {
            listViewVisible();
            adapter = (DiaryListViewAdapter) ((ListView) staticView.findViewById(R.id.diary_list_view)).getAdapter();
            adapter.listUpdate(list);
            adapter.notifyDataSetChanged();
        }

        setTodayCountText();
        setTotalCountText();
        setDailyAvgText();
        setAnotherDayCountText();
    }

    @Override
    public void onResume() {
        super.onResume();
        todayDiaryDataUpdate();
    }
}
