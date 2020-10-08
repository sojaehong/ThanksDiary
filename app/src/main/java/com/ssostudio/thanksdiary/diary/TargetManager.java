package com.ssostudio.thanksdiary.diary;

import android.content.Context;

import com.ssostudio.thanksdiary.R;

public class TargetManager {

    public static String TargetCheck(Context context, String target) {

        String s = "";

        switch (target) {
            case "" + R.string.me:
                s = context.getResources().getText(R.string.me).toString();
                break;
            case "" + R.string.family:
                s = context.getResources().getText(R.string.family).toString();
                break;
            case "" + R.string.friend:
                s = context.getResources().getText(R.string.friend).toString();
                break;
            case "" + R.string.pet:
                s = context.getResources().getText(R.string.pet).toString();
                break;
            case "" + R.string.work:
                s = context.getResources().getText(R.string.work).toString();
                break;
            case "" + R.string.food:
                s = context.getResources().getText(R.string.food).toString();
                break;
            case "" + R.string.nature:
                s = context.getResources().getText(R.string.nature).toString();
                break;
            default:
                s = target;
        }

        return s;
    }
}
