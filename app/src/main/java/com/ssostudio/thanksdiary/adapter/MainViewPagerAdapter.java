package com.ssostudio.thanksdiary.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ssostudio.thanksdiary.MainActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.fragment.DiaryCalendarFragment;
import com.ssostudio.thanksdiary.fragment.SettingFragment;
import com.ssostudio.thanksdiary.fragment.TodayFragment;
import com.ssostudio.thanksdiary.fragment.DiaryListFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TodayFragment();
            case 1:
                return new DiaryListFragment();
            case 2:
                return new DiaryCalendarFragment();
            case 3:
                return new SettingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return MainActivity._context.getString(R.string.today);
            case 1:
                return MainActivity._context.getString(R.string.list);
            case 2:
                return MainActivity._context.getString(R.string.calendar);
            case 3:
                return MainActivity._context.getString(R.string.setting);
        }
        return super.getPageTitle(position);
    }
}
