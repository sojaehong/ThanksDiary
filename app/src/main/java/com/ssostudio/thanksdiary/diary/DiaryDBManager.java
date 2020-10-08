package com.ssostudio.thanksdiary.diary;

import android.content.Context;
import android.widget.Toast;

import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.dbhelper.DBHelperManager;
import com.ssostudio.thanksdiary.fragment.DiaryCalendarFragment;
import com.ssostudio.thanksdiary.model.DiaryListModel;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.AppUtility;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;

public class DiaryDBManager {
    private DBHelperManager _db;
    private Context _context;

    public DiaryDBManager(Context context){
        _context = context;
        _db = new DBHelperManager(context);
    }

    public void onDiaryWrite(DiaryModel diaryModel){

        String timestamp = "" + DateManager.getTimestamp();
        diaryModel.setDiary_timestamp(timestamp);
        diaryModel.setDiary_update_timestamp(timestamp);

        _db.onDiaryWrite(diaryModel);
    }

    public ArrayList<DiaryModel> getDiaryAllSelect(){
        return _db.getAllDiarySelect();
    }

    public void diaryModelsRefresh(){
        DiaryListModel.diaryModels = getDiaryAllSelect();
    }

    public void diaryAllDelete(Context context){
        _db.diaryAllDelete();
        AppUtility.restartApp(context);
    }

    public void diaryDelete(int diaryId){
        _db.diaryDelete(diaryId);
        diaryModelsRefresh();
        new DiaryManager().diaryRefresh();
        Toast.makeText(_context, R.string.delete_completed, Toast.LENGTH_SHORT).show();
    }

}
