package test.commonUtil;

import commonUtil.DigitalWalletUtil;
import domain.DigitalWallet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class DigitalWalletUtilTests {
    
    @Test
    public void testGetDigitalWalletFromCsvTupleString() {
        String input = "1,\"悠遊卡\",2017,12,30,\"台北捷運公司\",1000,\"\"";
        DigitalWallet expect = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        DigitalWallet actual = null;
        try {
            actual = DigitalWalletUtil.getDigitalWalletFromCsvTupleString( input );
            assertTrue( DigitalWalletUtil.equals( expect, actual ) );
        } catch ( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCsvTupleStringFromDigitalWallet() {
        DigitalWallet input = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        String expect = "1,\"悠遊卡\",2017,12,30,\"台北捷運公司\",1000,\"\"";
        String actual = null;
        try {
            actual = DigitalWalletUtil.getCsvTupleStringFromDigitalWallet( input );
            assertEquals( expect, actual );
        } catch ( Exception e ) {
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testCopy() {
        DigitalWallet input = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        DigitalWallet expect = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        DigitalWallet clone = DigitalWalletUtil.copy( input );
        
        assertTrue( DigitalWalletUtil.equals( expect, clone ) );
    }
    
    @Test
    public void testEquals() {
        DigitalWallet data1 = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        DigitalWallet data2 = new DigitalWallet( 2, "Google Pay", 2017, 12, 1, "Google", 1000, "" );
        DigitalWallet data3 = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        
        assertTrue( DigitalWalletUtil.equals( data1, data3 ) );
        assertFalse( DigitalWalletUtil.equals( data1, data2 ) );
    }
}
