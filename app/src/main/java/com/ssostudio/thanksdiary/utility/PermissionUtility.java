package com.ssostudio.thanksdiary.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class PermissionUtility {
    private final static String TAG = "PermissionCheck";

    public static boolean storagePermissionCheck(final Activity activity, final int reqCode) {
        boolean isCheck = false;

        final String permissionCheck = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionResult = activity.getApplicationContext().checkSelfPermission(permissionCheck);
            Log.d(TAG, "" + permissionResult);
            if (permissionResult == PackageManager.PERMISSION_DENIED) { // 해당 퍼미션이 거부상태면 true, 아니면 false
                /*
                 * 해당 권한이 거부된 적이 있는지 유무 판별 해야함.
                 * 거부된 적이 있으면 true, 거부된 적이 없으면 false 리턴
                 */

//                Log.d(TAG, "true");

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionCheck)) {
                    // 거부된 전적이 있으므로 더욱 상세한 설명을 동반한 퍼미션 권한 요구.
//                    Log.d(TAG, "거절된 적 있음");

//                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
//                    dialog.setTitle("권한이 필요합니다.")
//                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"" + permissionCheck + "\"권한이 필요합니다. 계속 하시겠습니까?")
//                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                        activity.requestPermissions(new String[]{permissionCheck}, reqCode);
//                                    }
//                                }
//                            })
//                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(activity, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
//                                }
//                            }).create().show();

                    activity.requestPermissions(new String[]{permissionCheck}, reqCode);

                } else {
                    // 거부된 전적이 없음. 최초 퍼미션 권한 요구.
//                    Log.d(TAG, "거절된적 없음 최초의 퍼미션 권한 요구");
                    activity.requestPermissions(new String[]{permissionCheck}, reqCode);
                }

            } else {
                // 이미 권한을 지님.
//                Log.d(TAG, "이미 권한을 지님.");
                isCheck = true;
            }
        } else {
            // 이미 권한을 지님.
//            Log.d(TAG, "이미 권한을 지님.");
            isCheck = true;
        }

        return isCheck;
    }

}
