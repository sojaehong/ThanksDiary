package com.ssostudio.thanksdiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.ImportExportActivity;
import com.ssostudio.thanksdiary.PremiumUpgradeActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.dialog.TextDialog;
import com.ssostudio.thanksdiary.diary.DiaryDBManager;
import com.ssostudio.thanksdiary.utility.AppReview;
import com.ssostudio.thanksdiary.utility.OtherApps;
import com.ssostudio.thanksdiary.utility.ShareManager;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private static View staticView;
    private MaterialButton upgradeBtn;
    private MaterialButton reviewBtn, shareBtn, otherAppsBtn, diaryRestBtn;
    private MaterialButton IEBtn;
    private DiaryDBManager _dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        staticView = view;

        _dbManager = new DiaryDBManager(getContext());

        init();

        return view;
    }

    private void init() {
        setUpgradeBtn();
        setIEBtn();
        setReviewBtn();
        setShareBtn();
        setOtherAppsBtn();
        setDiaryResetBtn();
    }

    private void setIEBtn() {
        IEBtn = staticView.findViewById(R.id.import_export_button);
        IEBtn.setOnClickListener(this);
    }

    private void setUpgradeBtn() {
        upgradeBtn = staticView.findViewById(R.id.upgrade_button);
        upgradeBtn.setOnClickListener(this);
    }

    private void setReviewBtn() {
        reviewBtn = staticView.findViewById(R.id.review_button);
        reviewBtn.setOnClickListener(this);
    }

    private void setShareBtn() {
        shareBtn = staticView.findViewById(R.id.share_button);
        shareBtn.setOnClickListener(this);
    }

    private void setOtherAppsBtn() {
        otherAppsBtn = staticView.findViewById(R.id.other_app_button);
        otherAppsBtn.setOnClickListener(this);
    }

    private  void setDiaryResetBtn(){
        diaryRestBtn = staticView.findViewById(R.id.all_delete_button);
        diaryRestBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upgrade_button:
                onUpgradeBtnClick();
                break;
            case R.id.review_button:
                onReviewBtnClick();
                break;
            case R.id.share_button:
                onShareBtnClick();
                break;
            case R.id.other_app_button:
                onOtherAppsBtnClick();
                break;
            case R.id.import_export_button:
                onIEBtnClick();
                break;
            case  R.id.all_delete_button:
                onDiaryResetBtnClick();
                break;
        }
    }

    private void onDiaryResetBtnClick() {
        new TextDialog(getContext()).onShowDialog(2, getContext().getString(R.string.reset_q));
    }

    private void onUpgradeBtnClick() {
        Intent intent = new Intent(getActivity(), PremiumUpgradeActivity.class);
        startActivity(intent);
    }

    private void onReviewBtnClick(){
        AppReview.onAppReview(getContext());
    }

    private void onShareBtnClick(){
        ShareManager.appShare(getContext());
    }

    private void onOtherAppsBtnClick() {
        OtherApps.onOtherAppsShow(getContext());
    }

    private void onIEBtnClick() {
        Intent intent = new Intent(getActivity(), ImportExportActivity.class);
        startActivity(intent);
    }

}
