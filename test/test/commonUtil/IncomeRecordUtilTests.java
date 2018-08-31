package test.commonUtil;

import java.util.ArrayList;
import java.util.List;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordUtilTests {
    
    @Test
    public void testGetIncomeRecordFromCsvTupleString() {
        String input = "1,2017,10,1,\"測試帳\",0,100,\"\",0";
        IncomeRecord expect = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        IncomeRecord actual = null;
        try {
            actual = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( input );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCsvTupleStringFromIncomeRecord() {
        IncomeRecord input = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        String expect = "1,2017,10,1,\"測試帳\",0,100,\"\",0";
        String actual = "";
        try {
            actual = IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetIncomeRecordCsvFileName() {
        IncomeRecord incomeRecord = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        String expect = "2017.10.csv";
        String actual1 = IncomeRecordUtil.getIncomeRecordCsvFileName( incomeRecord );
        String actual2 = IncomeRecordUtil.getIncomeRecordCsvFileName( 2017, 10 );
        assertEquals( expect, actual1 );
        assertEquals( expect, actual2 );
    }
    
    @Test
    public void testSortById() {
        List<IncomeRecord> input = new ArrayList<IncomeRecord>();
        input.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 ) );
        input.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳", 0, 300, "", 0 ) );
        input.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳", 0, 400, "", 0 ) );
        input.add( new IncomeRecord( 2, 2017, 10, 2, "測試帳", 0, 200, "", 0 ) );
        input.add( new IncomeRecord( 5, 2017, 10, 2, "測試帳", 0, 500, "", 0 ) );
        List<IncomeRecord> expect = new ArrayList<IncomeRecord>();
        expect.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 ) );
        expect.add( new IncomeRecord( 2, 2017, 10, 2, "測試帳", 0, 200, "", 0 ) );
        expect.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳", 0, 300, "", 0 ) );
        expect.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳", 0, 400, "", 0 ) );
        expect.add( new IncomeRecord( 5, 2017, 10, 2, "測試帳", 0, 500, "", 0 ) );
        
        input = IncomeRecordUtil.sortById( input );
        
        assertEquals( expect.size(), input.size() );
        for( int i = 0; i < expect.size(); i++ ) {
            assertTrue( IncomeRecordUtil.equals( expect.get( i ), input.get( i ) ) );
        }
    }
    
    @Test
    public void testSortByDay() {
        List<IncomeRecord> input = new ArrayList<IncomeRecord>();
        input.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 ) );
        input.add( new IncomeRecord( 2, 2017, 10, 2, "測試帳", 0, 200, "", 0 ) );
        input.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳", 0, 300, "", 0 ) );
        input.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳", 0, 400, "", 0 ) );
        input.add( new IncomeRecord( 5, 2017, 10, 2, "測試帳", 0, 500, "", 0 ) );
        List<IncomeRecord> expect = new ArrayList<IncomeRecord>();
        expect.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 ) );
        expect.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳", 0, 300, "", 0 ) );
        expect.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳", 0, 400, "", 0 ) );
        expect.add( new IncomeRecord( 2, 2017, 10, 2, "測試帳", 0, 200, "", 0 ) );
        expect.add( new IncomeRecord( 5, 2017, 10, 2, "測試帳", 0, 500, "", 0 ) );
        
        input = IncomeRecordUtil.sortByDay( input );
        
        assertEquals( expect.size(), input.size() );
        for( int i = 0; i < expect.size(); i++ ) {
            assertTrue( IncomeRecordUtil.equals( expect.get( i ), input.get( i ) ) );
        }
    }
    
    @Test
    public void testCompareYearMonth() {
        IncomeRecord incomeRecord1 = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        IncomeRecord incomeRecord2 = new IncomeRecord( 2, 2017, 10, 2, "測試帳", 0, 200, "", 0 );
        IncomeRecord incomeRecord3 = new IncomeRecord( 5, 2017, 12, 2, "測試帳", 0, 500, "", 0 );
            
        assertTrue( IncomeRecordUtil.compareYearMonth( incomeRecord1, incomeRecord2 ) == 0 );
        assertFalse( IncomeRecordUtil.compareYearMonth( incomeRecord1, incomeRecord3 ) == 0 );
    }
    
    @Test
    public void testCopy() {
        IncomeRecord input = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        IncomeRecord expect = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        IncomeRecord clone = IncomeRecordUtil.copy( input );
        
        assertTrue( IncomeRecordUtil.equals( expect, clone ) );
    }
    
    @Test
    public void testEquals() {
        IncomeRecord data1 = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        IncomeRecord data2 = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 200, "", 0 );
        IncomeRecord data3 = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
        
        assertTrue( IncomeRecordUtil.equals( data1, data3 ) );
        assertFalse( IncomeRecordUtil.equals( data1, data2 ) );
    }
}
