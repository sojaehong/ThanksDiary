package com.ssostudio.thanksdiary.utility;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.preferences.PreferenceManager;

public class RewardAdManager {

    private static RewardedAd rewardedAd;
    final static String debug = "RewardedTest";

    public static RewardedAd setRewardedAd(Activity activity) {
        rewardedAd = new RewardedAd(activity, activity.getString(R.string.reward_ad_unit_id_test));

        RewardedAdLoadCallback rewardedAdLoadCallback = new RewardedAdLoadCallback() {

            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
//                Log.d(debug, "onRewardedAdLoaded");
            }

            @Override
            public void onRewardedAdFailedToLoad(int i) {
                // Ad failed to load.
//                Log.d(debug, "onRewardedAdFailedToLoad : " + i);
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), rewardedAdLoadCallback);

        return rewardedAd;
    }

    public static void onRewardedAdClick(final Activity activity) {
        if (rewardedAd != null && rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
//                    Log.d(debug, "onRewardedAdOpened");
                }

                @Override
                public void onRewardedAdClosed() {
                    rewardedAd = setRewardedAd(activity);
//                    Log.d(debug, "onRewardedAdClosed");
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    long freeDate = DateManager.getFreeDateTimestamp(3);
                    PreferenceManager.setFreeDate(activity.getApplicationContext(), freeDate);
                    AppUtility.restartApp(activity.getApplicationContext());
//                    String date = DateManager.dateTimeZoneFullFormat(DateManager.timestampToIntArray(freeDate));
//
//                    long nowTimestamp = DateManager.getTimestamp();
//                    String nowDate = DateManager.dateTimeZoneFullFormat(DateManager.timestampToIntArray(nowTimestamp));
//
//                    Log.d(debug, "onUserEarnedReward : " + freeDate + " : "
//                            + date + " : "
//                            + nowTimestamp + " : "
//                            + nowDate + " : "
//                            + (freeDate > nowTimestamp) + " : "
//                    );
                }

                @Override
                public void onRewardedAdFailedToShow(int i) {
//                    Log.d(debug, "onRewardedAdFailedToShow");
                }
            };

            rewardedAd.show(activity, adCallback);
        } else {
//            Log.d(debug, "reward load failed!!");
        }
    }

}
