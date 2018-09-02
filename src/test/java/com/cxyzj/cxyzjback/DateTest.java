package com.cxyzj.cxyzjback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {


    public static void main(String[] args) {

        DateFormat dateTime = DateFormat.getDateTimeInstance();
        String dt = dateTime.format(new Date());

        System.out.println(dt);
    }

}
