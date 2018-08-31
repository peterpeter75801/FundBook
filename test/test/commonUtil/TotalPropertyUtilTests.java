package test.commonUtil;

import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class TotalPropertyUtilTests {
    
    @Test
    public void testGetTotalPropertyFromCsvTupleString() {
        String input = "1,2017,10,1,12,0,0,1000";
        TotalProperty expect = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        TotalProperty actual = null;
        try {
            actual = TotalPropertyUtil.getTotalPropertyFromCsvTupleString( input );
            assertTrue( TotalPropertyUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCsvTupleStringFromTotalProperty() {
        TotalProperty input = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        String expect = "1,2017,10,1,12,0,0,1000";
        String actual = "";
        try {
            actual = TotalPropertyUtil.getCsvTupleStringFromTotalProperty( input );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testCopy() {
        TotalProperty input = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        TotalProperty expect = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        TotalProperty clone = TotalPropertyUtil.copy( input );
        
        assertTrue( TotalPropertyUtil.equals( expect, clone ) );
    }
    
    @Test
    public void testEquals() {
        TotalProperty data1 = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        TotalProperty data2 = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 2000 );
        TotalProperty data3 = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        
        assertTrue( TotalPropertyUtil.equals( data1, data3 ) );
        assertFalse( TotalPropertyUtil.equals( data1, data2 ) );
    }
}
