package com.ssostudio.thanksdiary.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

    //해당 context의 서비스를 사용하기위해서 context객체를 받는다.
    public static int getConnectivityStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){
                //데이터 연결
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI){
                //와이파이 연결
                return TYPE_WIFI;
            }
        }
        //연결이 되지않은 상태
        return TYPE_NOT_CONNECTED;
    }
}
