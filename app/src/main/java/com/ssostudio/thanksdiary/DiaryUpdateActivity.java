package com.ssostudio.thanksdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

public class DiaryUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton backBtn, completedBtn;
    private DiaryModel _diaryModel;
    private AppCompatTextView dateTextView, targetTextView;
    private LinearLayout dateUpdateLayout;
    private TextView whyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_update);
        init();
    }

    private void init(){
        getIntentData();
        setDateTextView();
        setTargetTextView();
        setWhyTextView();
        setDateUpdateLayout();
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

    private void getIntentData(){
        Intent intent = getIntent();
        _diaryModel = (DiaryModel) intent.getSerializableExtra("diaryModel");
    }

    private void setBackBtn(){
        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);
    }

    private void setCompletedBtn(){
        completedBtn = findViewById(R.id.completed_button);
        completedBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_button :
                onBackBtnClick();
                break;
            case R.id.completed_button:
                onCompletedBtnClick();
                break;
            case R.id.date_update_ll:
                onDateUpdateLayoutClick();
                break;
        }
    }

    private void onDateUpdateLayoutClick() {
        finish();
    }

    private void onCompletedBtnClick() {

    }

    private void onBackBtnClick() {
        finish();
    }
}
