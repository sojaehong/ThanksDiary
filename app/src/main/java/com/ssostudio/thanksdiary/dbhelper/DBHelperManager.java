package com.ssostudio.thanksdiary.dbhelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ssostudio.thanksdiary.MainActivity;
import com.ssostudio.thanksdiary.R;
import com.ssostudio.thanksdiary.model.DiaryModel;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DBHelperManager extends SQLiteOpenHelper {
    private SQLiteDatabase _db = null;
    private SQLiteStatement _statement = null;
    private DiaryModel _diaryModel;
    private ArrayList<DiaryModel> diaryModels;
    private static Context _context;
    private final static String DB_NAME = "thanksDiaryDB.db";
    private final static String DB_FULL_PATH = MainActivity._context.getDatabasePath(DB_NAME).getAbsolutePath();
    private final static String TAG = "DBHelperTAG";
    private final static String BACKUP_PATH = Environment.getExternalStorageDirectory().getPath() + "/TTDBackup";
    private final static String BACKUP_NAME = "/backup";

    public DBHelperManager(@Nullable Context context) {
        super(context, DB_FULL_PATH, null, 1);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE diary (diary_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "diary_year INTEGER," +
                "diary_month INTEGER," +
                "diary_day INTEGER," +
                "diary_target TEXT," +
                "diary_content TEXT," +
                "diary_timestamp TEXT," +
                "diary_update_timestamp TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS diary");
        onCreate(db);
    }

    public void onDiaryWrite(DiaryModel diaryModel) {
        try {

            _db = getWritableDatabase();

            String sql = "INSERT INTO diary(diary_year, diary_month, diary_day, diary_target," +
                    " diary_content, diary_timestamp, diary_update_timestamp) " +
                    "VALUES (?,?,?,?,?,?,?)";
            _statement = _db.compileStatement(sql);

            _statement.bindLong(1, diaryModel.getDiary_year());
            _statement.bindLong(2, diaryModel.getDiary_month());
            _statement.bindLong(3, diaryModel.getDiary_day());
            _statement.bindString(4, diaryModel.getDiary_target());
            _statement.bindString(5, diaryModel.getDiary_content());
            _statement.bindString(6, diaryModel.getDiary_timestamp());
            _statement.bindString(7, diaryModel.getDiary_update_timestamp());

            _statement.execute();

            _statement.close();
            _db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DiaryModel> getAllDiarySelect() {

        diaryModels = new ArrayList<>();

        try {

            _db = getReadableDatabase();

            String sql = "SELECT * FROM diary";
            Cursor cursor = _db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                DiaryModel diaryModel = new DiaryModel();
                diaryModel.setDiary_id(cursor.getInt(0));
                diaryModel.setDiary_year(cursor.getInt(1));
                diaryModel.setDiary_month(cursor.getInt(2));
                diaryModel.setDiary_day(cursor.getInt(3));
                diaryModel.setDiary_target(cursor.getString(4));
                diaryModel.setDiary_content(cursor.getString(5));
                diaryModel.setDiary_timestamp(cursor.getString(6));
                diaryModel.setDiary_update_timestamp(cursor.getString(7));
                diaryModels.add(diaryModel);
            }

            _db.close();

            return diaryModels;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diaryModels;
    }

    public void diaryAllDelete() {

        try {

            _db = getWritableDatabase();

            String sql = "DELETE FROM diary";

            _statement = _db.compileStatement(sql);
            _statement.execute();

            _statement.close();
            _db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void diaryDelete(int diaryId){

        try{

            _db = getWritableDatabase();

            String sql = "DELETE FROM diary WHERE diary_id = ?";

            _statement = _db.compileStatement(sql);
            _statement.bindLong(1, diaryId);
            _statement.execute();

            _statement.close();
            _db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // DB 복원
    public static boolean importDB(Activity activity) {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = DB_FULL_PATH;
                String backupDBPath = BACKUP_PATH + BACKUP_NAME; // From SD directory.
                File backupDB = new File(backupDBPath, "");

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDBPath).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Toast.makeText(activity, R.string.recovery_ok, Toast.LENGTH_LONG).show();

                return true;
            }
        } catch (Exception e) {
            Toast.makeText(activity, R.string.recovery_failed, Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    // DB 백업
    public static boolean exportDB(Activity activity) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = _context.getDatabasePath(DB_NAME);

            if (sd.canWrite()) {
                File currentDB = new File(data, "");

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(BACKUP_PATH + BACKUP_NAME).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Toast.makeText(activity, R.string.backup_ok, Toast.LENGTH_LONG).show();

                return true;
            }

        } catch (Exception e) {
            Toast.makeText(activity, R.string.backup_failed, Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
