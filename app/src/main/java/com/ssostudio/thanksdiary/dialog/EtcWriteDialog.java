package com.ssostudio.thanksdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.fragment.TodayFragment;
import com.ssostudio.thanksdiary.model.DiaryModel;

public class EtcWriteDialog implements View.OnClickListener {

    private Context _context;
    private DiaryModel _diaryModel;
    private Dialog _dialog;
    private TextInputEditText text;

    public EtcWriteDialog(Context context, DiaryModel diaryModel){
        _context = context;
        _diaryModel = diaryModel;
    }

    public void onShowDialog(){
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.etc_write_dialog);

        MaterialButton cancelBtn = _dialog.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);

        MaterialButton backBtn = _dialog.findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);

        MaterialButton nextBtn = _dialog.findViewById(R.id.next_button);
        nextBtn.setOnClickListener(this);

        text = _dialog.findViewById(R.id.target_text);

        _dialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = _dialog.getWindow();
        window.setAttributes(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button:
                onDialogDismiss();
                break;
            case R.id.back_button:
                new TargetSelectDialog(_context, _diaryModel).onShowDialog();
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
            _diaryModel.setDiary_target(target);
            new ContentWriteDialog(_context, _diaryModel).onShowDialog();
            _dialog.dismiss();
        }
    }
}
