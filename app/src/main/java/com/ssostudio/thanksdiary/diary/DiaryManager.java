package com.ssostudio.thanksdiary.diary;

import com.ssostudio.thanksdiary.fragment.DiaryCalendarFragment;
import com.ssostudio.thanksdiary.fragment.TodayFragment;
import com.ssostudio.thanksdiary.model.DiaryListModel;
import com.ssostudio.thanksdiary.model.DiaryModel;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class DiaryManager {

    public int dateOfUseCount() {
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            String date = model.getDiary_year() + "" + model.getDiary_month() + "" + model.getDiary_day();
            if (!dates.contains(date)) {
                dates.add(date);
            }
        }

        return dates.size();
    }

    public int diaryTotalCount() {
        return DiaryListModel.diaryModels.size();
    }

    public ArrayList<DiaryModel> dDayWriteDiary(int[] dates) {
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<DiaryModel> dDayDiaryList = new ArrayList<>();

        String sDate = DateManager.dateTimeZoneFormat(dates);

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            int[] modelDates = {model.getDiary_year(), model.getDiary_month(), model.getDiary_day()};
            String sModelDate = DateManager.dateTimeZoneFormat(modelDates);

            if (sDate.equals(sModelDate)){
                dDayDiaryList.add(model);
            }
        }

        return dDayDiaryList;
    }

    public ArrayList<DiaryModel> todayWriteDiary() {
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<DiaryModel> dDayDiaryList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            String date = model.getDiary_year() + "" + model.getDiary_month() + "" + model.getDiary_day();
            int year = calendar.get(Calendar.YEAR);
            int month = (calendar.get(Calendar.MONTH) +1);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String d_date =  year + "" + month + "" + day;

            if (date.equals(d_date)){
                dDayDiaryList.add(model);
            }
        }

        return dDayDiaryList;
    }

    public ArrayList<DiaryModel> rangeDiary(int[] startDate, int[] endDate){
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<DiaryModel> dDayDiaryList = new ArrayList<>();

        long startTimestamp = DateManager.intArrayToTimestamp(startDate) /1000;
        long endTimestamp = DateManager.intArrayToTimestamp(endDate) /1000;

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            int[] modelDate = {model.getDiary_year(), model.getDiary_month(), model.getDiary_day()};
            long timestamp = DateManager.intArrayToTimestamp(modelDate) /1000;

            if (timestamp >= startTimestamp && timestamp <= endTimestamp){
                dDayDiaryList.add(model);
            }
        }

        Collections.sort(dDayDiaryList, new Comparator<DiaryModel>() {
            @Override
            public int compare(DiaryModel model1, DiaryModel model2) {
                int[] modelDates1 = {model1.getDiary_year(), model1.getDiary_month(), model1.getDiary_day()};
                int[] modelDates2 = {model2.getDiary_year(), model2.getDiary_month(), model2.getDiary_day()};
                long timestamp1 = DateManager.intArrayToTimestamp(modelDates1) /1000;
                long timestamp2 = DateManager.intArrayToTimestamp(modelDates2) /1000;
                return timestamp1 <= timestamp2 ? 1 : -1;
            }
        });

        return dDayDiaryList;
    }

    public ArrayList<DiaryModel> getAllDiary(){
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;

        Collections.sort(list, new Comparator<DiaryModel>() {
            @Override
            public int compare(DiaryModel model1, DiaryModel model2) {
                int[] modelDates1 = {model1.getDiary_year(), model1.getDiary_month(), model1.getDiary_day()};
                int[] modelDates2 = {model2.getDiary_year(), model2.getDiary_month(), model2.getDiary_day()};
                long timestamp1 = DateManager.intArrayToTimestamp(modelDates1) /1000;
                long timestamp2 = DateManager.intArrayToTimestamp(modelDates2) /1000;
                return timestamp1 <= timestamp2 ? 1 : -1;
            }
        });

        return list;
    }

    public float dailyAvgCount(){
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<String> dates = new ArrayList<>();

        int count = 0;

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            String date = model.getDiary_year() + "" + model.getDiary_month() + "" + model.getDiary_day();

            if (!dates.contains(date)){
                dates.add(date);
                count++;
            }
        }

        float avg = list.size() / (float)count;

        return avg;
    }

    public ArrayList<DiaryModel> getYesterdayList(){
        int[] startDates = DateManager.getYesterdayDate();
        int[] endDates = startDates;
        return getSelectDiaryList(startDates, endDates);
    }

    public ArrayList<DiaryModel> getLastDayList(int day){
        int[] startDates = DateManager.getLastDays(day);
        int[] endDates = DateManager.getYesterdayDate();
        return getSelectDiaryList(startDates, endDates);
    }

    public ArrayList<DiaryModel> getSelectPeriodList(int[] startDates, int[] endDates){
        return getSelectDiaryList(startDates, endDates);
    }

    public ArrayList<Long> getDiaryDatesTimestamp(){
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<Long> dates = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            int[] date = {model.getDiary_year(), model.getDiary_month(), model.getDiary_day()};
            long timestamp = DateManager.intArrayToTimestamp(date) / 1000;

            if (!dates.contains(timestamp)){
                dates.add(timestamp);
            }
        }

        return dates;
    }

    public ArrayList<String> getDiaryDatesString(){
        ArrayList<DiaryModel> list = DiaryListModel.diaryModels;
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            DiaryModel model = list.get(i);
            int[] date = {model.getDiary_year(), model.getDiary_month(), model.getDiary_day()};
            String sDate = DateManager.dateTimeZoneFormat(date);

            if (!dates.contains(sDate)){
                dates.add(sDate);
            }
        }

        return dates;
    }

    private ArrayList<DiaryModel> getSelectDiaryList(int[] startDates, int[] endDates){
        return new DiaryManager().rangeDiary(startDates, endDates);
    }

    public void todayDiaryRefresh(){
        new TodayFragment().todayDiaryDataUpdate();
    }

    public void calendarBottomListRefresh(){
        new DiaryCalendarFragment().bottomSheetRefresh();
    }

    public void calendarDecoratorsRefresh(){
        new DiaryCalendarFragment().calendarDecoratorsRefresh();
    }

    public void diaryRefresh(){
        todayDiaryRefresh();
        calendarDecoratorsRefresh();
        calendarBottomListRefresh();
    }

}
