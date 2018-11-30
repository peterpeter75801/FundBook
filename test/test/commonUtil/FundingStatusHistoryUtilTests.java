package test.commonUtil;

import commonUtil.FundingStatusHistoryUtil;
import domain.FundingStatusHistory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class FundingStatusHistoryUtilTests {
    
    @Test
    public void testGetFundingStatusHistoryFromCsvTupleString() {
        String input = "1,1,2017,12,30,12,0,0,C,0,10000,10000,\"\"";
        FundingStatusHistory expect = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        FundingStatusHistory actual = null;
        try {
            actual = FundingStatusHistoryUtil.getFundingStatusHistoryFromCsvTupleString( input );
            assertTrue( FundingStatusHistoryUtil.equals( expect, actual ) );
        } catch ( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCsvTupleStringFromFundingStatusHistory() {
        FundingStatusHistory input = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        String expect = "1,1,2017,12,30,12,0,0,C,0,10000,10000,\"\"";
        String actual = null;
        try {
            actual = FundingStatusHistoryUtil.getCsvTupleStringFromFundingStatusHistory( input );
            assertEquals( expect, actual );
        } catch ( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testCopy() {
        FundingStatusHistory input = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        FundingStatusHistory expect = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        FundingStatusHistory clone = FundingStatusHistoryUtil.copy( input );
        
        assertTrue( FundingStatusHistoryUtil.equals( expect, clone ) );
    }
    
    @Test
    public void testEquals() {
        FundingStatusHistory data1 = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        FundingStatusHistory data2 = new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" );
        FundingStatusHistory data3 = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        
        assertTrue( FundingStatusHistoryUtil.equals( data1, data3 ) );
        assertFalse( FundingStatusHistoryUtil.equals( data1, data2 ) );
    }
}
