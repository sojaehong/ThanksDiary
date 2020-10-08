package com.ssostudio.thanksdiary.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.Calendar;

public class DateSelectDialog implements View.OnClickListener {
    private Context _context;
    private Dialog _dialog;

    public DateSelectDialog (Context context){
        _context = context;
    }

    public void onShowDialog(){
        _dialog = new Dialog(_context);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setContentView(R.layout.date_select_dialog);

        _dialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = _dialog.getWindow();
        window.setAttributes(layoutParams);

        MaterialButton todayBtn =  _dialog.findViewById(R.id.today_button);
        todayBtn.setOnClickListener(this);

        MaterialButton anotherDayBtn = _dialog.findViewById(R.id.another_day_button);
        anotherDayBtn.setOnClickListener(this);

        MaterialButton cancelBtn = _dialog.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.today_button:
                onTodayBtnClick();
                break;
            case R.id.another_day_button:
                onAnotherDatBtnClick();
                break;
            case R.id.cancel_button:
                onDialogDismiss();
                break;
        }

    }

    private void onTodayBtnClick(){
        int[] date = DateManager.getTodayDate();

        new TargetSelectDialog(_context, diaryModelSetDate(date)).onShowDialog();
        onDialogDismiss();
    }

    private void onAnotherDatBtnClick(){
        onDatePickerShow();
        _dialog.hide();
    }

    public void onDatePickerShow(){
        int[] date = DateManager.getTodayDate();

        DatePickerDialog dialog = new DatePickerDialog(_context, listener, date[0], date[1] - 1, date[2]);

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, _context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    _dialog.show();
                }
            }
        });

        Calendar maxDate = Calendar.getInstance();
        int[] yesterday = DateManager.getYesterdayDate();
        maxDate.set( yesterday[0], yesterday[1] - 1,yesterday[2]);

        dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int[] date = {year, monthOfYear + 1 , dayOfMonth};
            new TargetSelectDialog(_context, diaryModelSetDate(date)).onShowDialog();
            onDialogDismiss();
        }
    };

    private void onDialogDismiss(){
        _dialog.dismiss();
    }

    private DiaryModel diaryModelSetDate(int[] date){
        DiaryModel diaryModel = new DiaryModel();
        diaryModel.setDiary_year(date[0]);
        diaryModel.setDiary_month(date[1]);
        diaryModel.setDiary_day(date[2]);

        return diaryModel;
    }

}
