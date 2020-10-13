package com.ssostudio.thanksdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.model.DiaryModel;

public class DiaryUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialButton backBtn, completedBtn;
    private DiaryModel _diaryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_update);
        init();
    }

    private void init(){
        getIntentData();
        setBackBtn();
        setCompletedBtn();
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
        }
    }

    private void onCompletedBtnClick() {

    }

    private void onBackBtnClick() {
        finish();
    }
}
