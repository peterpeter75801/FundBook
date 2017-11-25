package test.commonUtil;

import commonUtil.CheckRecordUtil;
import domain.CheckRecord;
import junit.framework.TestCase;

public class CheckRecordUtilTests extends TestCase {
    
    public void testGetCheckRecordFromCsvTupleString() {
        String input = "1,2017,10,1,12,30,0,100,20000,20100";
        CheckRecord expect = getTestData1();
        CheckRecord actual = null;
        try {
            actual = CheckRecordUtil.getCheckRecordFromCsvTupleString( input );
            assertTrue( CheckRecordUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testGetCsvTupleStringFromCheckRecord() {
        CheckRecord input = getTestData1();
        String expect = "1,2017,10,1,12,30,0,100,20000,20100";
        String actual = "";
        try {
            actual = CheckRecordUtil.getCsvTupleStringFromCheckRecord( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testCopy() {
        CheckRecord expect = getTestData1();
        CheckRecord clone = CheckRecordUtil.copy( getTestData1() );
        
        assertTrue( CheckRecordUtil.equals( expect, clone ) );
    }
    
    public void testEquals() {
        CheckRecord data1 = getTestData1();
        CheckRecord data2 = getTestData2();
        CheckRecord data3 = getTestData3();
        
        assertTrue( CheckRecordUtil.equals( data1, data3 ) );
        assertFalse( CheckRecordUtil.equals( data1, data2 ) );
    }
    
    private CheckRecord getTestData1() {
        CheckRecord testData = new CheckRecord();
        testData.setId( 1 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setHour( 12 );
        testData.setMinute( 30 );
        testData.setSecond( 0 );
        testData.setDifference( 100 );
        testData.setBookAmount( 20000 );
        testData.setActualAmount( 20100 );
        return testData;
    }
    
    private CheckRecord getTestData2() {
        CheckRecord testData = new CheckRecord();
        testData.setId( 2 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setHour( 12 );
        testData.setMinute( 30 );
        testData.setSecond( 0 );
        testData.setDifference( -500 );
        testData.setBookAmount( 20000 );
        testData.setActualAmount( 19500 );
        return testData;
    }
    
    private CheckRecord getTestData3() {
        CheckRecord testData = new CheckRecord();
        testData.setId( 1 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setHour( 12 );
        testData.setMinute( 30 );
        testData.setSecond( 0 );
        testData.setDifference( 100 );
        testData.setBookAmount( 20000 );
        testData.setActualAmount( 20100 );
        return testData;
    }
}
