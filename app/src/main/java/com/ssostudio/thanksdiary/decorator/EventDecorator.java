package com.ssostudio.thanksdiary.decorator;

import android.graphics.Color;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.ssostudio.thanksdiary.utility.DateManager;

import java.util.ArrayList;

public class EventDecorator implements DayViewDecorator {
    ArrayList<String> _dates;

    public EventDecorator(ArrayList<String> dates){
        _dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        int[] date = DateManager.calendarDayToIntArray(day);
        String sDate = DateManager.dateTimeZoneFormat(date);
        return _dates.contains(sDate);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, Color.RED));
    }
}
