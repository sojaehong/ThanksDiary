package com.ssostudio.thanksdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.diary.DiaryManager;
import com.ssostudio.thanksdiary.fragment.DiaryListFragment;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;

public class DateRangeDialog implements View.OnClickListener {
    private Context _context;
    private Dialog _dialog;
    private MaterialButton yesterdayBtn, last3Btn, last7Btn,last30Btn, allBtn,selectPeriodBtn;
    private MaterialButton cancelBtn;
    private FragmentManager _fragmentManager;
    private int[] startDates;
    private int[] endDates;
    private ArrayList<DiaryModel> list;

    public DateRangeDialog(Context context, FragmentManager fragmentManager) {
        _context = context;
        _fragmentManager = fragmentManager;
    }

    public void onShowDialog() {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.date_range_dialog);

        yesterdayBtn = _dialog.findViewById(R.id.yesterday_button);
        yesterdayBtn.setOnClickListener(this);

        last3Btn = _dialog.findViewById(R.id.last_3_days_button);
        last3Btn.setOnClickListener(this);

        last7Btn = _dialog.findViewById(R.id.last_7_days_button);
        last7Btn.setOnClickListener(this);

        last30Btn = _dialog.findViewById(R.id.last_30_days_button);
        last30Btn.setOnClickListener(this);

        allBtn = _dialog.findViewById(R.id.all_button);
        allBtn.setOnClickListener(this);

        selectPeriodBtn = _dialog.findViewById(R.id.select_period_button);
        selectPeriodBtn.setOnClickListener(this);

        cancelBtn = _dialog.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);

        _dialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = _dialog.getWindow();
        window.setAttributes(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yesterday_button:
                onYesterdayBtnClick();
                break;
            case R.id.last_3_days_button:
                onLast3BtnClick();
                break;
            case R.id.last_7_days_button:
                onLast7BtnClick();
                break;
            case R.id.last_30_days_button:
                onLast30BtnClick();
                break;
            case R.id.all_button:
                onAllDiaryBtnClick();
                break;
            case R.id.select_period_button:
                onSelectPeriodBtnClick();
                break;
            case R.id.cancel_button:
                onCancelBtnClick();
                break;
        }
    }

    private void onYesterdayBtnClick() {
        list = new DiaryManager().getYesterdayList();
        updateListFragment(_context.getString(R.string.yesterday));
        _dialog.dismiss();
    }

    private void onLast3BtnClick() {
        list = new DiaryManager().getLastDayList(3);
        updateListFragment(_context.getString(R.string.last_3_days));
        _dialog.dismiss();
    }

    private void onLast7BtnClick() {
        list = new DiaryManager().getLastDayList(7);
        updateListFragment(_context.getString(R.string.last_7_days));
        _dialog.dismiss();
    }

    private void onLast30BtnClick() {
        list = new DiaryManager().getLastDayList(30);
        updateListFragment(_context.getString(R.string.last_30_days));
        _dialog.dismiss();
    }

    private void onAllDiaryBtnClick(){
        list = new DiaryManager().getAllDiary();
        updateListFragment(_context.getString(R.string.all));
        _dialog.dismiss();
    }

    private void onCancelBtnClick(){
        _dialog.dismiss();
    }

    private void onSelectPeriodBtnClick() {
        _dialog.hide();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTheme(R.style.DatePikerTheme);
        builder.setTitleText(R.string.date_range);

        MaterialDatePicker picker = builder.build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> range = (Pair<Long, Long>) selection;
                startDates = DateManager.timestampToIntArray(range.first);
                endDates = DateManager.timestampToIntArray(range.second);
                String sStartDate = DateManager.dateTimeZoneFormat(startDates);
                String sEndDate = DateManager.dateTimeZoneFormat(endDates);
                list = new DiaryManager().getSelectPeriodList(startDates, endDates);
                updateListFragment(sStartDate + " ~ " + sEndDate);
                _dialog.dismiss();
            }
        });

        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dialog.show();
            }
        });

        picker.show(_fragmentManager, picker.toString());
    }

    private void updateListFragment(String rangeDateText){
        new DiaryListFragment().updateListView(list);
        new DiaryListFragment().setDateText(rangeDateText);
    }

    private void dateLog(){
            String t1 = DateManager.dateTimeZoneFormat(startDates);
            String t2 = DateManager.dateTimeZoneFormat(endDates);
            long ts1 = DateManager.intArrayToTimestamp(startDates);
            long ts2 = DateManager.intArrayToTimestamp(endDates);
            int[] st = DateManager.timestampToIntArray(ts1);
            int[] et = DateManager.timestampToIntArray(ts2);
            String t3 = DateManager.dateTimeZoneFormat(st);
            String t4 = DateManager.dateTimeZoneFormat(et);

        Log.d("RangeDiary" , t1
                + " : " + t2
                + " : " + ts1
                + " : " + ts2
                + " : " + t3
                + " : " + t4
        );
    }
}
