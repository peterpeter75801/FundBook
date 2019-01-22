package test.commonUtil;

import java.util.ArrayList;
import java.util.List;

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
    public void testGetFundingStatusHistoryCsvFileName() {
        FundingStatusHistory input = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        String expect = "1.csv";
        String actual1 = FundingStatusHistoryUtil.getFundingStatusHistoryCsvFileName( input );
        String actual2 = FundingStatusHistoryUtil.getFundingStatusHistoryCsvFileName( 1 );
        assertEquals( expect, actual1 );
        assertEquals( expect, actual2 );
    }
    
    @Test
    public void testSortById() {
        List<FundingStatusHistory> input = new ArrayList<FundingStatusHistory>();
        input.add( new FundingStatusHistory( 3, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
        input.add( new FundingStatusHistory( 4, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
        input.add( new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
        input.add( new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
        input.add( new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
        List<FundingStatusHistory> expect = new ArrayList<FundingStatusHistory>();
        expect.add( new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
        expect.add( new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
        expect.add( new FundingStatusHistory( 3, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
        expect.add( new FundingStatusHistory( 4, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
        expect.add( new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
        
        input = FundingStatusHistoryUtil.sortById( input );
        
        assertEquals( expect.size(), input.size() );
        for( int i = 0; i < expect.size(); i++ ) {
            assertTrue( FundingStatusHistoryUtil.equals( expect.get( i ), input.get( i ) ) );
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
    
    @Test
    public void testEqualsIgnoreTime() {
        FundingStatusHistory data1 = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" );
        FundingStatusHistory data2 = new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" );
        FundingStatusHistory data3 = new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 30, 0, 'C', 0, 10000, 10000, "" );
        
        assertTrue( FundingStatusHistoryUtil.equalsIgnoreTime( data1, data3 ) );
        assertFalse( FundingStatusHistoryUtil.equalsIgnoreTime( data1, data2 ) );
    }
}
