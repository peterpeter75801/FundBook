package commonUtil;

public class DateUtil {
    
    public static int getMaxDayValue( String yearStr, String monthStr ) {
        final int[] normalYearDayList = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        final int[] leapYearDayList = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        
        int year = (StringUtil.isNumber( yearStr ) && Integer.parseInt( yearStr ) >= 1900)
            ? Integer.parseInt( yearStr ) : 1900;
        int month = (StringUtil.isNumber( monthStr ) && Integer.parseInt( monthStr ) >= 1 && Integer.parseInt( monthStr ) <= 12)
            ? Integer.parseInt( monthStr ) : 1;
            
        boolean isLeapYear = false;
        if( year % 4 == 0 && year % 100 == 0 && year % 400 == 0 ) {
            isLeapYear = true;
        } else if( year % 4 == 0 && year % 100 == 0 && year % 400 != 0 ) {
            isLeapYear = false;
        } else if( year % 4 == 0 && year % 100 != 0 && year % 400 == 0 ) {
            isLeapYear = true;
        } else if( year % 4 == 0 && year % 100 != 0 && year % 400 != 0 ) {
            isLeapYear = true;
        } else {
            isLeapYear = false;
        }
        
        if( isLeapYear ) {
            return leapYearDayList[ month ];
        } else {
            return normalYearDayList[ month ];
        }
    }
}
