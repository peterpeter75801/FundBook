package test.commonUtil;

import commonUtil.FundingStatusUtil;
import domain.FundingStatus;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class FundingStatusUtilTests {
    
    @Test
    public void testGetFundingStatusFromCsvTupleString() {
        String input = "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\"";
        FundingStatus expect = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "" );
        FundingStatus actual = null;
        try {
            actual = FundingStatusUtil.getFundingStatusFromCsvTupleString( input );
            assertTrue( FundingStatusUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCsvTupleStringFromFundingStatus() {
        FundingStatus input = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "" );
        String expect = "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\"";
        String actual = "";
        try {
            actual = FundingStatusUtil.getCsvTupleStringFromFundingStatus( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testCopy() {
        FundingStatus input = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "" );
        FundingStatus expect = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "" );
        FundingStatus clone = FundingStatusUtil.copy( input );
        
        assertTrue( FundingStatusUtil.equals( expect, clone ) );
    }
    
    @Test
    public void testEquals() {
        FundingStatus data1 = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "" );
        FundingStatus data2 = new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "" );
        FundingStatus data3 = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "" );
        
        assertTrue( FundingStatusUtil.equals( data1, data3 ) );
        assertFalse( FundingStatusUtil.equals( data1, data2 ) );
    }
}
