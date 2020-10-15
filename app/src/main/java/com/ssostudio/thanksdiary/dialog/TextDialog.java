package com.ssostudio.thanksdiary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.diary.DiaryDBManager;
import com.ssostudio.thanksdiary.model.DiaryModel;

public class TextDialog implements View.OnClickListener {
    private Dialog _dialog;
    private Context _context;
    private static int _type = 0;
    private int _diaryId;
    private String _text;
    private DiaryModel _diaryModel;

    public TextDialog(Context context) {
        _context = context;
    }

    public void onShowDialog(int type, String text, int diaryId) {
        _diaryId = diaryId;
        setDialog(type, text);
    }

    public void onShowDialog(int type, String text) {
        setDialog(type, text);
    }

    public void onShowDialog(int type, String text, DiaryModel diaryModel){
        _diaryModel = diaryModel;
        setDialog(type, text);
    }

    private void setDialog(int type, String text){
        _type = type;
        _text = text;

        dialogInit();

        _dialog.show();
    }

    private void dialogInit() {
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.text_dialog);

        setText();
        setOkButton();
        setCancelButton();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = _dialog.getWindow();
        window.setAttributes(layoutParams);
    }

    private void setText() {
        TextView textView = _dialog.findViewById(R.id.dialog_content_text_view);
        textView.setText(_text);
    }

    private void setOkButton() {
        MaterialButton okButton = _dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
    }

    private void setCancelButton() {
        MaterialButton cancelButton = _dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                onOkButtonClick();
                break;
            case R.id.cancel_button:
                onCancelButtonClick();
                break;
        }
    }

    private void onOkButtonClick(){

        switch (_type){
            case 0:
                // 일기 삭제
                new DiaryDBManager(_context).diaryDelete(_diaryId);
                break;
            case 1:
                // 디테일 화면에서 삭제
                new DiaryDBManager(_context).diaryDelete(_diaryId);
                ((Activity)_context).finish();
                break;
            case 2:
                // 다이어리 리셋
                new DiaryDBManager(_context).diaryAllDelete(_context);
                break;
            case 3:
                // 다이어리 수정
                new DiaryDBManager(_context).diaryUpdate(_diaryModel);
                ((Activity)_context).finish();
                break;
        }

        _dialog.dismiss();
    }

    private void onCancelButtonClick(){
        _dialog.dismiss();
    }
}
