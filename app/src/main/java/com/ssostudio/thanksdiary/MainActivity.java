package com.ssostudio.thanksdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.ssostudio.thanksdiary.adapter.MainViewPagerAdapter;
import com.ssostudio.thanksdiary.diary.DiaryDBManager;
import com.ssostudio.thanksdiary.fragment.DiaryCalendarFragment;
import com.ssostudio.thanksdiary.model.DiaryListModel;
import com.ssostudio.thanksdiary.preferences.PreferenceManager;
import com.ssostudio.thanksdiary.utility.AppUtility;
import com.ssostudio.thanksdiary.utility.DateManager;
import com.ssostudio.thanksdiary.utility.RewardAdManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static int deviceWidth;
    public static int deviceHeight;
    public static Context _context;
    private DiaryDBManager _db;

    private ViewPager viewPager;
    private AdView mAdView;
    private FrameLayout adContainerView;
    private static int tabPosition = 0;

    public static boolean isPremium = false;
    public static boolean isFree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        _context = getApplicationContext();
        _db = new DiaryDBManager(_context);

        getIsPremium();
        checkFreeDate();
        setAdMobLoad();
        getDeviceSize();
        setViewPager();
        setTabLayout();
        setAdMobUI();
        getAllDiary();
        setRewardAdLoad();
    }

    private void getIsPremium() {
        isPremium = PreferenceManager.getIsPremium(getApplicationContext());
    }

    private void checkFreeDate(){
        long nowTimestamp = DateManager.getTimestamp();
        long freeTimestamp = PreferenceManager.getFreeDate(getApplicationContext());
        isFree = freeTimestamp > nowTimestamp;
    }

    private void getDeviceSize() {
        int[] deviceSize = AppUtility.getDeviceSize(getApplicationContext());
        deviceWidth = deviceSize[0];
        deviceHeight = deviceSize[1];
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    private void setAdMobUI() {
        if (isPremium || isFree)
            return;

        LinearLayout mainView = findViewById(R.id.main_view);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(deviceWidth, deviceHeight);
        lp.addRule(RelativeLayout.ABOVE, R.id.adFrame);
        mainView.setLayoutParams(lp);
    }

    private void setViewPager() {
        viewPager = findViewById(R.id.main_viewpager);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void setTabLayout() {
        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
//                if (tabPosition != 2 && DiaryCalendarFragment.isBottomExpanede) {
//                    new DiaryCalendarFragment().onStaticBottomSheetHide();
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setAdMobLoad() {
        if (isPremium || isFree)
            return;

        //admob setting
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adContainerView = findViewById(R.id.adFrame);

        mAdView = new AdView(this);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id_main));
        adContainerView.addView(mAdView);

        // Ad Test Device setting
//        MobileAds.setRequestConfiguration(
//                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("BDEFD70AD95EF10E88425F412EEC574E"))
//                        .build());
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

    private void setRewardAdLoad(){
        if (isPremium || isFree)
            return;

        RewardAdManager.setRewardedAd(this);
    }

    private void getAllDiary() {
        DiaryListModel.diaryModels = _db.getDiaryAllSelect();
    }

    @Override
    public void onBackPressed() {
//        Log.d("CheckLog", " " + tabPosition + " : " + DiaryCalendarFragment.isBottomExpanede);

        if (tabPosition == 2 && DiaryCalendarFragment.isBottomExpanede) {
            new DiaryCalendarFragment().onStaticBottomSheetHide();
        } else {
            super.onBackPressed();
        }

    }
}
