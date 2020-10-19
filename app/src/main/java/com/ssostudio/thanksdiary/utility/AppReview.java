package com.ssostudio.thanksdiary.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppReview {

    public static void onAppReview(Context context){
        String appPackageName = "com.ssostudio.thanksdiary";
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
