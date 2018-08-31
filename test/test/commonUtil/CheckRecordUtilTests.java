package test.commonUtil;

import commonUtil.CheckRecordUtil;
import domain.CheckRecord;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class CheckRecordUtilTests {
    
    @Test
    public void testGetCheckRecordFromCsvTupleString() {
        String input = "1,2017,10,1,12,30,0,100,20000,20100";
        CheckRecord expect = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 );
        CheckRecord actual = null;
        try {
            actual = CheckRecordUtil.getCheckRecordFromCsvTupleString( input );
            assertTrue( CheckRecordUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCsvTupleStringFromCheckRecord() {
        CheckRecord input = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 );
        String expect = "1,2017,10,1,12,30,0,100,20000,20100";
        String actual = "";
        try {
            actual = CheckRecordUtil.getCsvTupleStringFromCheckRecord( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testCopy() {
        CheckRecord input = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 );
        CheckRecord expect = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 );
        CheckRecord clone = CheckRecordUtil.copy( input );
        
        assertTrue( CheckRecordUtil.equals( expect, clone ) );
    }
    
    @Test
    public void testEquals() {
        CheckRecord data1 = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 );
        CheckRecord data2 = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, -500, 20000, 19500 );
        CheckRecord data3 = new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 );
        
        assertTrue( CheckRecordUtil.equals( data1, data3 ) );
        assertFalse( CheckRecordUtil.equals( data1, data2 ) );
    }
}
