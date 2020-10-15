package com.ssostudio.thanksdiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.dialog.TextDialog;
import com.ssostudio.thanksdiary.diary.DiaryDBManager;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.Arrays;

public class DiaryDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private DiaryModel _diaryModel;
    private DiaryDBManager _dbManager;
    public static Activity _activity;

    private AppCompatTextView dateText, targetText, whyText;
    private MaterialButton deleteBtn, updateBtn, backBtn;
    private AdView mAdView;
    private FrameLayout adContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        init();
    }

    private void init(){
        _activity = this;

        _dbManager = new DiaryDBManager(getApplicationContext());

        getIntentData();

        setAdMobUI();
        setAdMobLoad();

        setDateText();
        setTargetText();
        setWhyText();
        setDeleteBtn();
        setUpdateBtn();
        setBackBtn();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        _diaryModel = (DiaryModel) intent.getSerializableExtra("diaryModel");
    }

    private void setDateText(){
        int year = _diaryModel.getDiary_year();
        int month = _diaryModel.getDiary_month();
        int day = _diaryModel.getDiary_day();

        int[] date = {year, month, day};

        dateText = findViewById(R.id.date_text);
        dateText.setText(DateManager.dateTimeZoneFullFormat(date));
    }

    private void setTargetText(){
        targetText = findViewById(R.id.detail_target_a_text);
        targetText.setText(_diaryModel.getDiary_target());
    }

    private void setWhyText(){
        whyText = findViewById(R.id.detail_why_a_text);
        whyText.setText(_diaryModel.getDiary_content());
    }

    private void setDeleteBtn(){
        deleteBtn = findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(this);
    }

    private void setUpdateBtn(){
        updateBtn = findViewById(R.id.update_button);
        updateBtn.setOnClickListener(this);
    }

    private void setBackBtn(){
        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);
    }

    public void updateDiaryDiteil(DiaryModel diaryModel){
        if (_diaryModel == null)
            return;

        _diaryModel = diaryModel;
        setDateText();
        setTargetText();
        setWhyText();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.delete_button:
                onDeleteBtnClick();
                break;
            case R.id.update_button:
                onUpdateBtnClick();
                break;
            case R.id.back_button:
                onBackBtnClick();
                break;
        }

    }

    private void onDeleteBtnClick(){
        new TextDialog(this).onShowDialog(1,getApplicationContext().getString(R.string.delete_q), _diaryModel.getDiary_id());
    }

    private void onUpdateBtnClick(){
        Intent intent = new Intent(getApplicationContext(), DiaryUpdateActivity.class);
        intent.putExtra("diaryModel",_diaryModel);
        startActivity(intent);
    }

    private void onBackBtnClick(){
        finish();
    }

    private void setAdMobUI() {
        if (MainActivity.isPremium || MainActivity.isFree)
            return;

        LinearLayout mainView = findViewById(R.id.ll_bottom);

        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MainActivity.deviceWidth, actionBarHeight);
        lp.addRule(RelativeLayout.ABOVE, R.id.adFrame);
        mainView.setLayoutParams(lp);
    }

    private void setAdMobLoad() {
        if (MainActivity.isPremium || MainActivity.isFree)
            return;

        //admob setting
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adContainerView = findViewById(R.id.adFrame);

        mAdView = new AdView(this);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id_test));
        adContainerView.addView(mAdView);

        // Ad Test Device setting
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("BDEFD70AD95EF10E88425F412EEC574E"))
                        .build());
        // 앱 등록시 제거 필요

        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        mAdView.setAdSize(adSize);
        mAdView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
