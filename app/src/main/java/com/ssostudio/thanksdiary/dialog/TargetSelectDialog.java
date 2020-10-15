package com.ssostudio.thanksdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.adapter.TargetGridViewAdapter;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

public class TargetSelectDialog implements View.OnClickListener {
    private Context _context;
    private Dialog _dialog;
    private DiaryModel _diaryModel;
    private int _type;
    public TargetGridViewAdapter adapter;
    private static Dialog _dialogStatic;
    private MaterialButton backBtn, cancelBtn;
    private TextView textView;

    public TargetSelectDialog(Context context, DiaryModel diaryModel){
        _context = context;
        _diaryModel = diaryModel;
    }

    public void onShowDialog(int type){
        _type = type;

        if (_type == 0){
            setWriteDialog();
        }else if(_type == 1){
            setUpdateDialog();
        }

    }

    private void setWriteDialog(){
        dialogInit();
        setCancelBtn();
        setBackBtn();
        setDateText();
    }

    private void setUpdateDialog(){
        dialogInit();
        setCancelBtn();

        textView = _dialog.findViewById(R.id.date_text);
        textView.setVisibility(View.GONE);

        backBtn = _dialog.findViewById(R.id.back_button);
        backBtn.setVisibility(View.GONE);

    }

    private void dialogInit(){

        _dialog = new Dialog(_context);

        adapter = new TargetGridViewAdapter(_context, _diaryModel, _type);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.target_select_dialog);

        GridView gridView = _dialog.findViewById(R.id.target_grid_view);
        gridView.setAdapter(adapter);

        _dialog.show();

        _dialogStatic = _dialog;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = _dialog.getWindow();
        window.setAttributes(layoutParams);

    }

    private void setCancelBtn(){
        cancelBtn = _dialog.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);
    }

    private void setBackBtn(){
        backBtn = _dialog.findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);
    }

    private void setDateText(){
        int[] date = {_diaryModel.getDiary_year(), _diaryModel.getDiary_month(), _diaryModel.getDiary_day()};

        String dateString = DateManager.dateTimeZoneFormat(date);

        TextView textView = _dialog.findViewById(R.id.date_text);
        textView.setText(dateString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                onDialogDismiss();
                break;
            case R.id.back_button:
                onBackBtnClick();
                break;
        }
    }

    private void onBackBtnClick(){
        new DateSelectDialog(_context).onShowDialog(0);
        onDialogDismiss();
    }

    private void onDialogDismiss(){
        _dialog.dismiss();
    }

    public static void onStaticDialogDismiss(){
        _dialogStatic.dismiss();
    }
}
