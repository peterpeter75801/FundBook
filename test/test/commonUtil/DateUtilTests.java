package test.commonUtil;

import commonUtil.DateUtil;
import junit.framework.TestCase;

public class DateUtilTests extends TestCase {
    
    public void testGetMaxDayValue() {
        final int YEAR = 0;
        final int MONTH = 1;
        
        String input1[] = { "2017", "10" };
        String input2[] = { "2000", "02" };
        String input3[] = { "1900", "02" };
        String input4[] = { "yyyy", "mm" };
        String input5[] = { "abcd", "06" };
        
        assertEquals( 31, DateUtil.getMaxDayValue( input1[YEAR], input1[MONTH] ) );
        assertEquals( 29, DateUtil.getMaxDayValue( input2[YEAR], input2[MONTH] ) );
        assertEquals( 28, DateUtil.getMaxDayValue( input3[YEAR], input3[MONTH] ) );
        assertEquals( 31, DateUtil.getMaxDayValue( input4[YEAR], input4[MONTH] ) );
        assertEquals( 30, DateUtil.getMaxDayValue( input5[YEAR], input5[MONTH] ) );
    }
}
