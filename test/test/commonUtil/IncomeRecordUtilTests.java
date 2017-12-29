package test.commonUtil;

import java.util.ArrayList;
import java.util.List;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import junit.framework.TestCase;

public class IncomeRecordUtilTests extends TestCase {
    
    public void testGetIncomeRecordFromCsvTupleString() {
        String input = "1,2017,10,1,\"測試帳\",0,100,\"\",0";
        IncomeRecord expect = getTestData1();
        IncomeRecord actual = null;
        try {
            actual = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( input );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testGetCsvTupleStringFromIncomeRecord() {
        IncomeRecord input = getTestData1();
        String expect = "1,2017,10,1,\"測試帳\",0,100,\"\",0";
        String actual = "";
        try {
            actual = IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    public void testGetIncomeRecordCsvFileName() {
        IncomeRecord incomeRecord = getTestData1();
        String expect = "2017.10.csv";
        String actual1 = IncomeRecordUtil.getIncomeRecordCsvFileName( incomeRecord );
        String actual2 = IncomeRecordUtil.getIncomeRecordCsvFileName( 2017, 10 );
        assertEquals( expect, actual1 );
        assertEquals( expect, actual2 );
    }
    
    public void testSortById() {
        List<IncomeRecord> input = new ArrayList<IncomeRecord>();
        input.add( getTestData1() );
        input.add( getTestData3() );
        input.add( getTestData4() );
        input.add( getTestData2() );
        input.add( getTestData5() );
        List<IncomeRecord> expect = new ArrayList<IncomeRecord>();
        expect.add( getTestData1() );
        expect.add( getTestData2() );
        expect.add( getTestData3() );
        expect.add( getTestData4() );
        expect.add( getTestData5() );
        
        input = IncomeRecordUtil.sortById( input );
        
        assertEquals( expect.size(), input.size() );
        for( int i = 0; i < expect.size(); i++ ) {
            assertTrue( IncomeRecordUtil.equals( expect.get( i ), input.get( i ) ) );
        }
    }
    
    public void testSortByDay() {
        List<IncomeRecord> input = new ArrayList<IncomeRecord>();
        input.add( getTestData1() );
        input.add( getTestData2() );
        input.add( getTestData3() );
        input.add( getTestData4() );
        input.add( getTestData5() );
        List<IncomeRecord> expect = new ArrayList<IncomeRecord>();
        expect.add( getTestData1() );
        expect.add( getTestData3() );
        expect.add( getTestData4() );
        expect.add( getTestData2() );
        expect.add( getTestData5() );
        
        input = IncomeRecordUtil.sortByDay( input );
        
        assertEquals( expect.size(), input.size() );
        for( int i = 0; i < expect.size(); i++ ) {
            assertTrue( IncomeRecordUtil.equals( expect.get( i ), input.get( i ) ) );
        }
    }
    
    public void testCopy() {
        IncomeRecord expect = getTestData1();
        IncomeRecord clone = IncomeRecordUtil.copy( getTestData1() );
        
        assertTrue( IncomeRecordUtil.equals( expect, clone ) );
    }
    
    public void testEquals() {
        IncomeRecord data1 = getTestData1();
        IncomeRecord data2 = getTestData1();
        data2.setAmount( 200 );
        IncomeRecord data3 = getTestData1();
        
        assertTrue( IncomeRecordUtil.equals( data1, data3 ) );
        assertFalse( IncomeRecordUtil.equals( data1, data2 ) );
    }
    
    private IncomeRecord getTestData1() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 1 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 100 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData2() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 2 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 2 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 200 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData3() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 3 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 300 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData4() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 4 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 400 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData5() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 5 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 2 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 500 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
}
