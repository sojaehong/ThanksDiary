package com.ssostudio.thanksdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssostudio.thanksdiary.dbhelper.DBHelperManager;
import com.ssostudio.thanksdiary.utility.AppUtility;
import com.ssostudio.thanksdiary.utility.PermissionUtility;

public class ImportExportActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private FloatingActionButton backBtn;
    private MaterialButton backupBtn, restoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
        init();
    }

    private void init(){
        setBackBtn();
        setBackupBtn();
        setRestoreBtn();
    }

    private void setBackBtn(){
        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);
    }

    private void setBackupBtn(){
        backupBtn = findViewById(R.id.backup_button);
        backupBtn.setOnClickListener(this);
    }

    private void setRestoreBtn(){
        restoreBtn = findViewById(R.id.restore_button);
        restoreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button:
                onBackBtnClick();
                break;
            case R.id.backup_button:
                onBackupBtnClick();
                break;
            case R.id.restore_button:
                onRestoreBtnClick();
                break;
        }
    }

    private void onBackBtnClick(){
        finish();
    }

    private void onBackupBtnClick(){
        if (PermissionUtility.storagePermissionCheck(this, 100)){
            DBHelperManager.exportDB(this);
        }
    }

    private void onRestoreBtnClick(){
        if (PermissionUtility.storagePermissionCheck(this, 101)){
            if (DBHelperManager.importDB(this)){
                AppUtility.restartApp(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 100:
                DBHelperManager.exportDB(this);
                break;
            case 101:
                if (DBHelperManager.importDB(this)){
                    AppUtility.restartApp(this);
                }
                break;
        }

    }
}
