package helpers;

import java.sql.Timestamp;
import java.util.Calendar;

public class CalendarHelper {
    public static Timestamp getTimestamp() {
        return new java.sql.Timestamp(
                Calendar.getInstance().getTime().getTime()
        );
    }
}
