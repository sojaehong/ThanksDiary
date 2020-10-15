package com.ssostudio.thanksdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.dialog.DateSelectDialog;
import com.ssostudio.thanksdiary.dialog.TargetSelectDialog;
import com.ssostudio.thanksdiary.dialog.TextDialog;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

public class DiaryUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton backBtn, completedBtn;
    private DiaryModel _diaryModel;
    private AppCompatTextView dateTextView, targetTextView;
    private LinearLayout dateUpdateLayout, targetUpdateLayout;
    private TextView whyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_update);

        init();
    }

    private void init() {
        getIntentData();
        setDateTextView();
        setTargetTextView();
        setWhyTextView();
        setDateUpdateLayout();
        setTargetUpdateLayout();
        setBackBtn();
        setCompletedBtn();
    }

    private void setWhyTextView() {
        whyTextView = findViewById(R.id.why_a_text);
        whyTextView.setText(_diaryModel.getDiary_content());
    }

    private void setDateUpdateLayout() {
        dateUpdateLayout = findViewById(R.id.date_update_ll);
        dateUpdateLayout.setOnClickListener(this);
    }

    private void setTargetUpdateLayout() {
        targetUpdateLayout = findViewById(R.id.target_update_ll);
        targetUpdateLayout.setOnClickListener(this);
    }

    private void setTargetTextView() {
        targetTextView = findViewById(R.id.target_a_text);
        targetTextView.setText(_diaryModel.getDiary_target());
    }

    private void setDateTextView() {
        int year = _diaryModel.getDiary_year();
        int month = _diaryModel.getDiary_month();
        int day = _diaryModel.getDiary_day();

        int[] date = {year, month, day};

        dateTextView = findViewById(R.id.date_text);
        dateTextView.setText(DateManager.dateTimeZoneFullFormat(date));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        _diaryModel = (DiaryModel) intent.getSerializableExtra("diaryModel");
    }

    private void setBackBtn() {
        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);
    }

    private void setCompletedBtn() {
        completedBtn = findViewById(R.id.completed_button);
        completedBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_button:
                onBackBtnClick();
                break;
            case R.id.completed_button:
                onCompletedBtnClick();
                break;
            case R.id.date_update_ll:
                onDateUpdateLayoutClick();
                break;
            case R.id.target_update_ll:
                onTargetUpdateLayoutClick();
                break;
        }
    }

    private void onDateUpdateLayoutClick() {
        new DateSelectDialog(this).onShowDialog(1);
    }

    private void onTargetUpdateLayoutClick() {
        new TargetSelectDialog(this, _diaryModel).onShowDialog(1);
    }

    private void onCompletedBtnClick() {
        if (whyTextView == null)
            return;

        _diaryModel.setDiary_content(whyTextView.getText().toString());
        new TextDialog(this).onShowDialog(3, getString(R.string.update_q), _diaryModel);
    }

    private void onBackBtnClick() {
        finish();
    }

    public void updateDate(int[] date) {
        if (dateTextView == null || _diaryModel == null)
            return;

        dateTextView = findViewById(R.id.date_text);
        dateTextView.setText(DateManager.dateTimeZoneFullFormat(date));

        _diaryModel.setDiary_year(date[0]);
        _diaryModel.setDiary_month(date[1]);
        _diaryModel.setDiary_day(date[2]);

    }

    public void updateTarget(String target) {
        if (targetTextView == null || _diaryModel == null)
            return;

        targetTextView.setText(target);
        _diaryModel.setDiary_target(target);
    }
}
