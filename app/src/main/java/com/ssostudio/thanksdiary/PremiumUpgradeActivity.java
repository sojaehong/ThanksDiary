package com.ssostudio.thanksdiary;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.SkuDetails;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssostudio.thanksdiary.utility.BillingManager;
import com.ssostudio.thanksdiary.utility.RewardAdManager;

public class PremiumUpgradeActivity extends AppCompatActivity implements View.OnClickListener {
    private BillingManager billingManager;
    private FloatingActionButton backBtn;
    private MaterialButton upgradeBtn, restoreBtn, freeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_upgrade);
        init();
    }

    private void init() {
        setBillingManager();
        setBackBtn();
        setUpgradeBtn();
        setRestoreBtn();
        setFreeBtn();
    }

    private void setBillingManager() {
        billingManager = new BillingManager(this);
    }

    private void setBackBtn() {
        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);
    }

    private void setUpgradeBtn() {
        upgradeBtn = findViewById(R.id.upgrade_button);
        upgradeBtn.setOnClickListener(this);
    }

    private void setRestoreBtn() {
        restoreBtn = findViewById(R.id.restore_button);
        restoreBtn.setOnClickListener(this);
    }

    private void setFreeBtn() {
        freeBtn = findViewById(R.id.free_button);
        freeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upgrade_button:
                onUpgradeBtnClick();
                break;
            case R.id.restore_button:
                onRestoreClick();
                break;
            case R.id.free_button:
                onFreeButton();
                break;
            case R.id.back_button:
                onBackBtnClick();
                break;
        }
    }

    private void onFreeButton() {
        RewardAdManager.onRewardedAdClick(this);
    }

    private void onUpgradeBtnClick() {
        SkuDetails premiumSku = billingManager.getPremiumUpgradeSku();
        if (premiumSku != null) {
            if (billingManager.isPremiumCheck()) {
                billingManager.onPurchaseHistoryRestored();
            } else {
                billingManager.purchase(premiumSku);
            }
        }
    }

    private void onRestoreClick() {
        if (billingManager.isPremiumCheck()) {
            billingManager.onPurchaseHistoryRestored();
        } else {

        }
    }

    private void onBackBtnClick() {
        finish();
    }



}
