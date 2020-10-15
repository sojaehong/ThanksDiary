package com.ssostudio.thanksdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.MainActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.diary.DiaryDBManager;
import com.ssostudio.thanksdiary.diary.DiaryManager;
import com.ssostudio.thanksdiary.fragment.DiaryCalendarFragment;
import com.ssostudio.thanksdiary.fragment.TodayFragment;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

public class ContentWriteDialog implements View.OnClickListener {

    private Context _context;
    private DiaryModel _diaryModel;
    private Dialog _dialog;
    private DiaryDBManager _db;

    private TextView contentText;

    public ContentWriteDialog(Context context, DiaryModel diaryModel){
        _context = context;
        _diaryModel = diaryModel;
        _db = new DiaryDBManager(_context);
    }

    public void onShowDialog(){
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.content_write_dialog);

        MaterialButton cancelBtn = _dialog.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);

        MaterialButton backBtn = _dialog.findViewById(R.id.back_button);
        backBtn.setOnClickListener(this);

        MaterialButton okBtn = _dialog.findViewById(R.id.ok_button);
        okBtn.setOnClickListener(this);

        int[] date = {_diaryModel.getDiary_year(), _diaryModel.getDiary_month(), _diaryModel.getDiary_day()};

        String dateString = DateManager.dateTimeZoneFormat(date);

        TextView dateText = _dialog.findViewById(R.id.date_text);
        dateText.setText(dateString);

        String target = _diaryModel.getDiary_target();
        TextView targetText = _dialog.findViewById(R.id.target_text);
        targetText.setText(target);

        contentText = _dialog.findViewById(R.id.content_text);

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
                new TargetSelectDialog(_context, _diaryModel).onShowDialog(0);
                onDialogDismiss();
                break;
            case R.id.ok_button:
                onOkBtnClick();
                break;
        }

    }

    private void onDialogDismiss(){
        _dialog.dismiss();
    }

    private void onOkBtnClick(){
        String content = contentText.getText().toString();

        if (content.trim().equals("")){
            Toast.makeText(_context, R.string.input_text_p, Toast.LENGTH_SHORT).show();
        }else{
            _diaryModel.setDiary_content(content);
            _db.onDiaryWrite(_diaryModel);
            Toast.makeText(_context, R.string.completed, Toast.LENGTH_SHORT).show();
            _db.diaryModelsRefresh();
            new DiaryManager().todayDiaryRefresh();
            new DiaryCalendarFragment().calendarDecoratorsRefresh();
            onDialogDismiss();
        }

    }
}
