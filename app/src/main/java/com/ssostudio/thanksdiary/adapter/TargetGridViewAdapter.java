package com.ssostudio.thanksdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.material.button.MaterialButton;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.dialog.ContentWriteDialog;
import com.ssostudio.thanksdiary.dialog.EtcWriteDialog;
import com.ssostudio.thanksdiary.dialog.TargetSelectDialog;
import com.ssostudio.thanksdiary.model.DiaryModel;

public class TargetGridViewAdapter extends BaseAdapter {

    private Context _context;
    private LayoutInflater inflater;
    private DiaryModel _diaryModel;

    private int[] target = {R.string.me, R.string.family, R.string.friend,
     R.string.work, R.string.food, R.string.nature, R.string.pet, R.string.etc
    };

    public TargetGridViewAdapter(Context context, DiaryModel diaryModel){
        _context = context;
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _diaryModel = diaryModel;
    }

    @Override
    public int getCount() {
        return target.length;
    }

    @Override
    public Object getItem(int i) {
        return target[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.tartget_dialog_item, viewGroup, false);
        }

        MaterialButton button = view.findViewById(R.id.select_button);
        button.setText(_context.getResources().getText(target[i]));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (target[i] == R.string.etc){
                    TargetSelectDialog.onStaticDialogDismiss();
                    new EtcWriteDialog(_context, _diaryModel).onShowDialog();
                }else{
                    _diaryModel.setDiary_target(_context.getResources().getString(target[i]));
                    TargetSelectDialog.onStaticDialogDismiss();
                    new ContentWriteDialog(_context, _diaryModel).onShowDialog();
                }
            }
        });

        return view;
    }
}
