package com.ssostudio.thanksdiary.utility;

import android.content.Context;
import android.content.Intent;

import java.io.File;

public class ShareManager {

    public static void textShare(Context context, String title, String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(intent.EXTRA_TEXT, text);

        Intent chooser = Intent.createChooser(intent, title);
        context.startActivity(chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void appShare(Context context){
        String uri = "https://play.google.com/store/apps/details?id=com.ssostudio.thanksdiary";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(intent.EXTRA_TEXT, uri);

        Intent chooser = Intent.createChooser(intent, null);
        context.startActivity(chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void fileShare(Context context){
        File extRoot = context.getExternalFilesDir(null);
    }

}
