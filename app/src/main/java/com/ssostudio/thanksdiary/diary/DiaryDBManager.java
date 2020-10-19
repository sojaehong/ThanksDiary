package com.ssostudio.thanksdiary.diary;

import android.content.Context;
import android.widget.Toast;

import com.ssostudio.thanksdiary.DiaryDetailActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.dbhelper.DBHelperManager;
import com.ssostudio.thanksdiary.fragment.DiaryListFragment;
import com.ssostudio.thanksdiary.model.DiaryListModel;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.AppUtility;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;

public class DiaryDBManager {
    private DBHelperManager _db;
    private Context _context;

    public DiaryDBManager(Context context) {
        _context = context;
        _db = new DBHelperManager(context);
    }

    public void onDiaryWrite(DiaryModel diaryModel) {

        String timestamp = "" + DateManager.getTimestamp();
        diaryModel.setDiary_timestamp(timestamp);
        diaryModel.setDiary_update_timestamp(timestamp);

        _db.onDiaryWrite(diaryModel);
    }

    public ArrayList<DiaryModel> getDiaryAllSelect() {
        return _db.getAllDiarySelect();
    }

    public void diaryModelsRefresh() {
        DiaryListModel.diaryModels = getDiaryAllSelect();
    }

    public void diaryAllDelete(Context context) {
        try{
            _db.diaryAllDelete();
            AppUtility.restartApp(context);
        }catch (Exception e){
            Toast.makeText(_context, "reset error", Toast.LENGTH_SHORT).show();
        }
    }

    public void diaryDelete(int diaryId) {
        try {
            _db.diaryDelete(diaryId);
            diaryModelsRefresh();
            new DiaryManager().diaryRefresh();
            new DiaryListFragment().refreshList();
            Toast.makeText(_context, R.string.delete_completed, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(_context, "delete error", Toast.LENGTH_SHORT).show();
        }

    }

    public void diaryUpdate(DiaryModel diaryModel) {
        try {
            _db.onDiaryUpdate(diaryModel);
            diaryModelsRefresh();
            new DiaryManager().diaryRefresh();
            new DiaryListFragment().refreshList();
            ((DiaryDetailActivity) DiaryDetailActivity._activity).updateDiaryDetail(diaryModel);
            Toast.makeText(_context, R.string.update_completed, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(_context, "update error", Toast.LENGTH_SHORT).show();
        }
    }

}
