package com.ssostudio.thanksdiary.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OtherApps {
    public static void onOtherAppsShow(Context context){
        Uri uri = Uri.parse("https://play.google.com/store/search?q=pub:개발자-쏘&c=apps");
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
