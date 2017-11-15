package test.commonUtil;

import commonUtil.FundingStatusUtil;
import domain.FundingStatus;

import junit.framework.TestCase;

public class FundingStatusUtilTests extends TestCase {
    
    public void testGetFundingStatusFromCsvTupleString() {
        String input = "1,D,0,0,0,\"700\",\"中華郵政\",\"12345671234567\",\"12345671234567\",10000";
        FundingStatus expect = getTestData1();
        FundingStatus actual = null;
        try {
            actual = FundingStatusUtil.getFundingStatusFromCsvTupleString( input );
            assertTrue( FundingStatusUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testGetCsvTupleStringFromFundingStatus() {
        FundingStatus input = getTestData1();
        String expect = "1,D,0,0,0,\"700\",\"中華郵政\",\"12345671234567\",\"12345671234567\",10000";
        String actual = "";
        try {
            actual = FundingStatusUtil.getCsvTupleStringFromFundingStatus( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testCopy() {
        FundingStatus expect = getTestData1();
        FundingStatus clone = FundingStatusUtil.copy( getTestData1() );
        
        assertTrue( FundingStatusUtil.equals( expect, clone ) );
    }
    
    public void testEquals() {
        FundingStatus data1 = getTestData1();
        FundingStatus data2 = getTestData2();
        FundingStatus data3 = getTestData3();
        
        assertTrue( FundingStatusUtil.equals( data1, data3 ) );
        assertFalse( FundingStatusUtil.equals( data1, data2 ) );
    }
    
    private FundingStatus getTestData1() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 1 );
        testData.setType( 'D' );
        testData.setYear( 0 );
        testData.setMonth( 0 );
        testData.setDay( 0 );
        testData.setBankCode( "700" );
        testData.setBankName( "中華郵政" );
        testData.setAccount( "12345671234567" );
        testData.setInterestAccount( "12345671234567" );
        testData.setBalance( 10000 );
        return testData;
    }
    
    private FundingStatus getTestData2() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 2 );
        testData.setType( 'C' );
        testData.setYear( 0 );
        testData.setMonth( 0 );
        testData.setDay( 0 );
        testData.setBankCode( "" );
        testData.setBankName( "" );
        testData.setAccount( "" );
        testData.setInterestAccount( "" );
        testData.setBalance( 5000 );
        return testData;
    }
    
    private FundingStatus getTestData3() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 1 );
        testData.setType( 'D' );
        testData.setYear( 0 );
        testData.setMonth( 0 );
        testData.setDay( 0 );
        testData.setBankCode( "700" );
        testData.setBankName( "中華郵政" );
        testData.setAccount( "12345671234567" );
        testData.setInterestAccount( "12345671234567" );
        testData.setBalance( 10000 );
        return testData;
    }
}
