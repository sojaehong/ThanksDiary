package com.ssostudio.thanksdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ssostudio.thanksdiary.DiaryUpdateActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.fragment.TodayFragment;
import com.ssostudio.thanksdiary.model.DiaryModel;

public class EtcWriteDialog implements View.OnClickListener {

    private Context _context;
    private DiaryModel _diaryModel;
    private Dialog _dialog;
    private TextInputEditText text;
    private MaterialButton cancelBtn, backBtn, nextBtn;
    private int _type;

    public EtcWriteDialog(Context context, DiaryModel diaryModel){
        _context = context;
        _diaryModel = diaryModel;
    }

    public void onShowDialog(int type){
        _type = type;

        if (_type == 0){
            setWriteDialog();
        }else if (_type == 1){
            setUpdateDialog();
        }

    }

    private void setWriteDialog(){
        dialogInit();
        setCancelBtn();
        setBackBtn();
        setNextBtn();
        setText();
    }

    private void setUpdateDialog(){
        dialogInit();
        setCancelBtn();
        setBackBtn();
        setNextBtn();
        nextBtn.setText(_context.getString(R.string.ok));
        setText();
    }

    private void dialogInit(){
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.etc_write_dialog);

        _dialog.show();

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

    private void setNextBtn(){
        nextBtn = _dialog.findViewById(R.id.next_button);
        nextBtn.setOnClickListener(this);
    }

    private void setText(){
        text = _dialog.findViewById(R.id.target_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                onDialogDismiss();
                break;
            case R.id.back_button:
                new TargetSelectDialog(_context, _diaryModel).onShowDialog(_type);
                onDialogDismiss();
                break;
            case R.id.next_button:
                onNextBtnClick();
                break;
        }

    }

    private void onDialogDismiss(){
        _dialog.dismiss();
    }

    private void onNextBtnClick(){
        String target = text.getText().toString();

        if (target.trim().equals("")){
            TodayFragment.onSnackbarMake(R.string.input_text_p);
        }else{

            if (_type == 0){
                _diaryModel.setDiary_target(target);
                new ContentWriteDialog(_context, _diaryModel).onShowDialog();
            }else if (_type == 1){
                ((DiaryUpdateActivity)_context).updateTarget(target);
            }

            _dialog.dismiss();
        }
    }
}
