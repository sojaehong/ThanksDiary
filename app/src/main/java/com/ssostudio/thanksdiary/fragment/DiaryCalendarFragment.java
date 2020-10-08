package com.ssostudio.thanksdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.ssostudio.thanksdiary.DiaryDetailActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.adapter.DiaryListViewAdapter;
import com.ssostudio.thanksdiary.decorator.EventDecorator;
import com.ssostudio.thanksdiary.decorator.SaturdayDecorator;
import com.ssostudio.thanksdiary.decorator.SundayDecorator;
import com.ssostudio.thanksdiary.decorator.TodayDecorator;
import com.ssostudio.thanksdiary.diary.DiaryManager;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;
import java.util.Calendar;

public class DiaryCalendarFragment extends Fragment {
    private View view;
    private static View staticView;
    private MaterialCalendarView materialCalendarView;
    private BottomSheetBehavior bottomSheetBehavior;
    private DiaryListViewAdapter adapter;
    public static boolean isBottomExpanede = false;
    private ListView listView;
    private LinearLayout llNoResult;
    private FloatingActionButton closeBtn;
    private static Context _context;
    private static int[] _dates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        staticView = view;
        _context = getContext();

        setCalendarView();
        setBottomSheetBehavior();
//        setCloseImg();
        setBottomCloseBtn();

        return view;
    }

    // 상단 화면 종료 버튼 이미지 셋팅
//    private void setCloseImg() {
//        ImageView closeImg = staticView.findViewById(R.id.close_image);
//        closeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBottomSheetHide();
//            }
//        });
//    }

    private void setCalendarView() {
        materialCalendarView = staticView.findViewById(R.id.calendarView);
        initCalendarDecorators();
    }

    private void setCalendarState() {
        int[] date = DateManager.getTodayDate();
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SATURDAY)
                .setMaximumDate(CalendarDay.from(date[0], date[1] - 1, date[2]))
                .setCalendarDisplayMode(CalendarMode.MONTHS).commit();
    }

    private void initCalendarDecorators() {
        if (staticView == null)
            return;

        ArrayList<String> dates = new DiaryManager().getDiaryDatesString();
        materialCalendarView = staticView.findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(new SundayDecorator(),
                new SaturdayDecorator(),
                new TodayDecorator(),
                new EventDecorator(dates));

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int[] dates = DateManager.calendarDayToIntArray(date);

                _dates = dates;

                setBottomSheetList(_dates);

                onBottomSheetExpanded();
            }
        });
    }

    public void bottomSheetRefresh(){
        if (staticView == null || !isBottomExpanede || _dates == null)
            return;

        setBottomSheetList(_dates);
    }

    public void calendarDecoratorsRefresh() {
        if (staticView == null)
            return;

        ArrayList<String> dates = new DiaryManager().getDiaryDatesString();
        materialCalendarView = staticView.findViewById(R.id.calendarView);
        materialCalendarView.removeDecorators();
        materialCalendarView.addDecorators(new SundayDecorator(),
                new SaturdayDecorator(),
                new TodayDecorator(),
                new EventDecorator(dates));
    }

    public void setBottomSheetList(int[] dates){
        String sDate = DateManager.dateTimeZoneFullFormat(dates);
        setBottomDateText(sDate);
        ArrayList<DiaryModel> list = new DiaryManager().dDayWriteDiary(dates);
        setBottomDiaryListView(list);
    }

    private void setBottomSheetBehavior() {
        LinearLayout bottomLayout = staticView.findViewById(R.id.ll_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomLayout);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    isBottomExpanede = true;
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    isBottomExpanede = true;
                } else {
                    isBottomExpanede = false;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        onBottomSheetHide();

        bottomSheetRefresh();

    }

    private void onBottomSheetHide() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void onStaticBottomSheetHide() {
        LinearLayout bottomLayout = staticView.findViewById(R.id.ll_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void onBottomSheetExpanded() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void onBottomSheetCollapsed() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void onBottomSheetSetting() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
    }

    private void setBottomDateText(String sDate) {
        TextView dateText = staticView.findViewById(R.id.date_text);
        dateText.setText(sDate);
    }

    private void setBottomCloseBtn() {
        closeBtn = staticView.findViewById(R.id.close_button);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBottomSheetHide();
            }
        });
    }

    private void setBottomDiaryListView(ArrayList<DiaryModel> list) {
        if (staticView == null)
            return;

        listView = staticView.findViewById(R.id.diary_list_view);
        adapter = new DiaryListViewAdapter(_context, list);
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
                                closeBtn.hide();
                            }
                            //시작 Y가 끝 보다 작다면 터치가 위에서 아래로 이러우졌다는 것이고, 스크롤이 올라갔다는 뜻이다.
                            else if ((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
                                closeBtn.show();
                            }
                        }

                        startYPosition = 0.0f;
                        endYPosition = 0.0f;
                        break;
                }
                return false;
            }
        });

        if (list == null || list.size() == 0) {
            noResultVisible();
        } else {
            listViewVisible();
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

}
