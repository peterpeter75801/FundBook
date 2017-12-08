package test.commonUtil;

import commonUtil.StringUtil;

import junit.framework.TestCase;

public class StringUtilTests extends TestCase {
    
    public void testIsNumber() {
        String[] inputs = { "123", "test", "123test", "+123", "-123", "123-" };
        boolean[] expects = { true, false, false, false, true, false };
        for( int i = 0; i < inputs.length; i++ ) {
            assertEquals( "failed at input " + i, expects[ i ], StringUtil.isNumber( inputs[ i ] ) );
        }
    }
}
