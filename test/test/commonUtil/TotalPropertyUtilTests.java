package test.commonUtil;

import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;
import junit.framework.TestCase;

public class TotalPropertyUtilTests extends TestCase {
    
    public void testGetTotalPropertyFromCsvTupleString() {
        String input = "1,2017,10,1,12,0,0,1000";
        TotalProperty expect = getTestData1();
        TotalProperty actual = null;
        try {
            actual = TotalPropertyUtil.getTotalPropertyFromCsvTupleString( input );
            assertTrue( TotalPropertyUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testGetCsvTupleStringFromTotalProperty() {
        TotalProperty input = getTestData1();
        String expect = "1,2017,10,1,12,0,0,1000";
        String actual = "";
        try {
            actual = TotalPropertyUtil.getCsvTupleStringFromTotalProperty( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testCopy() {
        TotalProperty expect = getTestData1();
        TotalProperty clone = TotalPropertyUtil.copy( getTestData1() );
        
        assertTrue( TotalPropertyUtil.equals( expect, clone ) );
    }
    
    public void testEquals() {
        TotalProperty data1 = getTestData1();
        TotalProperty data2 = getTestData1();
        data2.setTotalAmount( 2000 );
        TotalProperty data3 = getTestData1();
        
        assertTrue( TotalPropertyUtil.equals( data1, data3 ) );
        assertFalse( TotalPropertyUtil.equals( data1, data2 ) );
    }
    
    private TotalProperty getTestData1() {
        TotalProperty testData = new TotalProperty();
        testData.setId( 1 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setHour( 12 );
        testData.setMinute( 0 );
        testData.setSecond( 0 );
        testData.setTotalAmount( 1000 );
        return testData;
    }
}
