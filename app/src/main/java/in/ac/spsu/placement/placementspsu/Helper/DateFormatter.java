package in.ac.spsu.placement.placementspsu.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    public static String dateToString(int dayOfMonth, int month, int year) {
        month += 1;
        String d= ""+dayOfMonth,m= ""+month;
        if(dayOfMonth<10) d = "0"+dayOfMonth;
        if(month<10) m = "0"+month;
        return d+"/"+m+"/"+year;
    }
    
    public static Date stringToDate(String data){
        Date res = null;
        try {
            res = new SimpleDateFormat("dd/MM/yyyy").parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}
