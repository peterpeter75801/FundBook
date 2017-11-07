package test.commonUtil;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import junit.framework.TestCase;

public class IncomeRecordUtilTests extends TestCase {
    
    public void testGetIncomeRecordFromCsvTupleString() {
        String input = "1,2017,10,1,\"測試帳\",,100,\"\"";
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
        String expect = "1,2017,10,1,\"測試帳\",,100,\"\"";
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
        testData.setSubclass( '\0' );
        testData.setAmount( 100 );
        testData.setDescription( "" );
        return testData;
    }
}
