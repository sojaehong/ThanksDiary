package com.ssostudio.thanksdiary.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import androidx.core.content.IntentCompat;

public class AppUtility {

    public static int[] getDeviceSize(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int deviceWidth = dm.widthPixels;
        int deviceHeight = dm.heightPixels;
        int[] deviceSize = {deviceWidth , deviceHeight};
        return deviceSize;
    }

    public static void restartApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }

}
