package org.nrum.util;

import org.nrum.nrum.Constant;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by rajdhami on 5/28/2017.
 */
public class MDateConversion {
    static final int startingEngYear = 1944;
    static final int startingEngMonth = 1;
    static final int startingEngDay = 1;
    static int dayOfWeek = Calendar.SATURDAY; // 1944/1/1 is Saturday

    static final int startingNepYear = 2000;
    static final int startingNepMonth = 9;
    static final int startingNepDay = 17;

    /*  The 0s at index 0 are dummy values so as to make the int array of
    *   days in months seems more intuitive that index 1 refers to first
    *   month "Baisakh", index 2 refers to second month "Jesth" and so on.
    */
    private static final HashMap<Integer, int[]> nepaliMap;
    static
    {
        nepaliMap = new HashMap<Integer, int[]>();
        nepaliMap.put(2000, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2001, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2002, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2003, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2004, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2005, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2006, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2007, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2008, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31 });
        nepaliMap.put(2009, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2010, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2011, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2012, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 });
        nepaliMap.put(2013, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2014, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2015, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2016, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 });
        nepaliMap.put(2017, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2018, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2019, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2020, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2021, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2022, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 });
        nepaliMap.put(2023, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2024, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2025, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2026, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2027, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2028, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2029, new int[] { 0, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2030, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2031, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2032, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2033, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2034, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2035, new int[] { 0, 30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31 });
        nepaliMap.put(2036, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2037, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2038, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2039, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 });
        nepaliMap.put(2040, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2041, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2042, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2043, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 });
        nepaliMap.put(2044, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2045, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2046, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2047, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2048, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2049, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 });
        nepaliMap.put(2050, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2051, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2052, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2053, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 });
        nepaliMap.put(2054, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2055, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2056, new int[] { 0, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2057, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2058, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2059, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2060, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2061, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2062, new int[] { 0, 30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31 });
        nepaliMap.put(2063, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2064, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2065, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2066, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31 });
        nepaliMap.put(2067, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2068, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2069, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2070, new int[] { 0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30 });
        nepaliMap.put(2071, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2072, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2073, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31 });
        nepaliMap.put(2074, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2075, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2076, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 });
        nepaliMap.put(2077, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31 });
        nepaliMap.put(2078, new int[] { 0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2079, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30 });
        nepaliMap.put(2080, new int[] { 0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30 });
        nepaliMap.put(2081, new int[] { 0, 31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2082, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2083, new int[] { 0, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2084, new int[] { 0, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2085, new int[] { 0, 31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2086, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2087, new int[] { 0, 31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2088, new int[] { 0, 30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2089, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 });
        nepaliMap.put(2090, new int[] { 0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30 });
    }

    private static final HashMap<Integer, String> nMonthMap;
    static
    {
        nMonthMap = new HashMap<Integer, String>();
        nMonthMap.put(1, "बैसाख");
        nMonthMap.put(2, "जेठ");
        nMonthMap.put(3, "आषाढ");
        nMonthMap.put(4, "साउन");
        nMonthMap.put(5, "भाद्र");
        nMonthMap.put(6, "आश्विन");
        nMonthMap.put(7, "कार्तिक");
        nMonthMap.put(8, "मंसिर ");
        nMonthMap.put(9, "पौष");
        nMonthMap.put(10, "माघ");
        nMonthMap.put(11, "फाल्गुण");
        nMonthMap.put(12, "चैत्र");
    }
    private static final HashMap<Integer, String> rMonthMap;
    static
    {
        rMonthMap = new HashMap<Integer, String>();
        rMonthMap.put(1, "बैसाख");
        rMonthMap.put(2, "जेठ");
        rMonthMap.put(3, "असार");
        rMonthMap.put(4, "साउन");
        rMonthMap.put(5, "भदौ");
        rMonthMap.put(6, "असोज");
        rMonthMap.put(7, "कातिक");
        rMonthMap.put(8, "मंसिर ");
        rMonthMap.put(9, "पुष");
        rMonthMap.put(10, "माघ");
        rMonthMap.put(11, "फागुन");
        rMonthMap.put(12, "चैत");
    }


    public MDateConversion() {}

    public static String getNepaliDate(int engYear, int engMonth, int engDay, String currentLangID) {
        Calendar currentEngDate = new GregorianCalendar();
        currentEngDate.set(engYear, engMonth, engDay);

        Calendar baseEngDate = new GregorianCalendar();
        baseEngDate.set(startingEngYear, startingEngMonth, startingEngDay);
        long totalEngDaysCount = daysBetween(baseEngDate, currentEngDate);
        /*initialize required Nepali date variables with starting Nepali date*/
        int nepYear = startingNepYear;
        int nepMonth = startingNepMonth;
        int nepDay = startingNepDay;

        /*decrement totalEngDaysCount until its value becomes 0*/
        while (totalEngDaysCount != 0) {
            // getting the total number of days in month nepMonth in year nepYear
            int daysInIthMonth = nepaliMap.get(nepYear)[nepMonth];
            nepDay++; // incrementing nepali day
            if (nepDay > daysInIthMonth) {
                nepMonth++;
                nepDay = 1;
            }
            if (nepMonth > 12) {
                nepYear++;
                nepMonth = 1;
            }
            dayOfWeek++; // count the days in terms of 7 days
            if (dayOfWeek > 7) {
                dayOfWeek = 1;
            }
            totalEngDaysCount--;
        }
        if (currentLangID.equals(Constant.CURRENT_LANG_ID_2)) {
            String resultDate = nepYear +"-"+nMonthMap.get(nepMonth)+"-"+nepDay;
            return resultDate;
        } else if(currentLangID.equals(Constant.CURRENT_LANG_ID_3)) {
            String resultDate = nepYear +"-"+rMonthMap.get(nepMonth)+"-"+nepDay;
            return resultDate;
        } else {
            String resultDate = nepYear +"-"+nepMonth+"-"+nepDay;
            return resultDate;
        }
    }

    private static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
}
