package test.commonUtil;

import commonUtil.StringUtil;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class StringUtilTests {
    
    @Test
    public void testIsNumber() {
        String[] inputs = { "123", "test", "123test", "+123", "-123", "123-" };
        boolean[] expects = { true, false, false, false, true, false };
        for( int i = 0; i < inputs.length; i++ ) {
            assertEquals( "failed at input " + i, expects[ i ], StringUtil.isNumber( inputs[ i ] ) );
        }
    }
    
    @Test
    public void testDecreaseDateTimeTextFieldValue() {
        // Year test
        String[] inputs1 = { "2017", "2018", "1900" };
        int input2 = 1900;
        int input3 = Integer.MAX_VALUE;
        int input4 = 4;
        boolean input5 = false;
        String[] expects = { "2016", "2017", "1900" };
        for( int i = 0; i < inputs1.length; i++ ) {
            assertEquals( expects[ i ], StringUtil.decreaseDateTimeTextFieldValue( inputs1[ i ], input2, input3, input4, input5 ) );
        }
        
        // Month test
        inputs1 = new String[]{ "01", "02", "12" };
        input2 = 1;
        input3 = 12;
        input4 = 2;
        input5 = true;
        expects = new String[]{ "12", "01", "11" };
        for( int i = 0; i < inputs1.length; i++ ) {
            assertEquals( expects[ i ], StringUtil.decreaseDateTimeTextFieldValue( inputs1[ i ], input2, input3, input4, input5 ) );
        }
        
        // Day test
        inputs1 = new String[]{ "01", "02", "31" };
        input2 = 1;
        input3 = 31;
        input4 = 2;
        input5 = true;
        expects = new String[]{ "31", "01", "30" };
        for( int i = 0; i < inputs1.length; i++ ) {
            assertEquals( expects[ i ], StringUtil.decreaseDateTimeTextFieldValue( inputs1[ i ], input2, input3, input4, input5 ) );
        }
    }
    
    @Test
    public void testIncreaseDateTimeTextFieldValue() {
        // Year test
        String[] inputs1 = { "2017", "2018", "1900" };
        int input2 = 1900;
        int input3 = Integer.MAX_VALUE;
        int input4 = 4;
        boolean input5 = false;
        String[] expects = { "2018", "2019", "1901" };
        for( int i = 0; i < inputs1.length; i++ ) {
            assertEquals( expects[ i ], StringUtil.increaseDateTimeTextFieldValue( inputs1[ i ], input2, input3, input4, input5 ) );
        }
        
        // Month test
        inputs1 = new String[]{ "01", "02", "12" };
        input2 = 1;
        input3 = 12;
        input4 = 2;
        input5 = true;
        expects = new String[]{ "02", "03", "01" };
        for( int i = 0; i < inputs1.length; i++ ) {
            assertEquals( expects[ i ], StringUtil.increaseDateTimeTextFieldValue( inputs1[ i ], input2, input3, input4, input5 ) );
        }
        
        // Day test
        inputs1 = new String[]{ "01", "02", "31" };
        input2 = 1;
        input3 = 12;
        input4 = 2;
        input5 = true;
        expects = new String[]{ "02", "03", "01" };
        for( int i = 0; i < inputs1.length; i++ ) {
            assertEquals( expects[ i ], StringUtil.increaseDateTimeTextFieldValue( inputs1[ i ], input2, input3, input4, input5 ) );
        }
    }
}
