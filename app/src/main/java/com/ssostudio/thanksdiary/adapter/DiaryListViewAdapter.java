package com.ssostudio.thanksdiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssostudio.thanksdiary.DiaryDetailActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.dialog.TextDialog;
import com.ssostudio.thanksdiary.diary.DiaryDBManager;
import com.ssostudio.thanksdiary.diary.DiaryManager;
import com.ssostudio.thanksdiary.fragment.DiaryCalendarFragment;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;

public class DiaryListViewAdapter extends BaseAdapter {

    private Context _context;
    private LayoutInflater inflater;
    private ArrayList<DiaryModel> _list;
    private DiaryDBManager _dbManager;

    public DiaryListViewAdapter(Context context, ArrayList<DiaryModel> list){
        _context = context;
        inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _list = list;
        _dbManager = new DiaryDBManager(context);
    }

    @Override
    public int getCount() {
        return _list.size();
    }

    @Override
    public Object getItem(int i) {
        return _list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);
        }

        final DiaryModel diaryModel = _list.get(i);
        int[] date = {diaryModel.getDiary_year(), diaryModel.getDiary_month(), diaryModel.getDiary_day()};
        String dateString = DateManager.dateTimeZoneFormat(date);
        String target = diaryModel.getDiary_target();
        String content =  diaryModel.getDiary_content();

        TextView dateText = view.findViewById(R.id.date_text);
        dateText.setText(dateString);

        TextView targetText = view.findViewById(R.id.target_text);
        targetText.setText(target);

        TextView contentText = view.findViewById(R.id.content_text);
        contentText.setText(content);

        ImageView deleteBtn = view.findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                _dbManager.diaryDelete(diaryModel.getDiary_id());
                new TextDialog(_context).onShowDialog(0,"삭제하시겠습니까?", diaryModel.getDiary_id());
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, DiaryDetailActivity.class);
                intent.putExtra("diaryModel",  diaryModel);
                _context.startActivity(intent);
            }
        });

        return view;
    }

    public void listUpdate(ArrayList<DiaryModel> list){
        _list = list;
    }
}
