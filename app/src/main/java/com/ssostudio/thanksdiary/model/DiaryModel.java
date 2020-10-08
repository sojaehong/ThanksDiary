package com.ssostudio.thanksdiary.model;

import java.io.Serializable;

public class DiaryModel implements Serializable {
    private int diary_id;
    private int diary_year;
    private int diary_month;
    private int diary_day;
    private String diary_target;
    private String diary_content;
    private String diary_timestamp;
    private String diary_update_timestamp;

    public int getDiary_id() {
        return diary_id;
    }

    public void setDiary_id(int diary_id) {
        this.diary_id = diary_id;
    }

    public int getDiary_year() {
        return diary_year;
    }

    public void setDiary_year(int diary_year) {
        this.diary_year = diary_year;
    }

    public int getDiary_month() {
        return diary_month;
    }

    public void setDiary_month(int diary_month) {
        this.diary_month = diary_month;
    }

    public int getDiary_day() {
        return diary_day;
    }

    public void setDiary_day(int diary_day) {
        this.diary_day = diary_day;
    }

    public String getDiary_target() {
        return diary_target;
    }

    public void setDiary_target(String diary_target) {
        this.diary_target = diary_target;
    }

    public String getDiary_content() {
        return diary_content;
    }

    public void setDiary_content(String diary_content) {
        this.diary_content = diary_content;
    }

    public String getDiary_timestamp() {
        return diary_timestamp;
    }

    public void setDiary_timestamp(String diary_timestamp) {
        this.diary_timestamp = diary_timestamp;
    }

    public String getDiary_update_timestamp() {
        return diary_update_timestamp;
    }

    public void setDiary_update_timestamp(String diary_update_timestamp) {
        this.diary_update_timestamp = diary_update_timestamp;
    }
}
