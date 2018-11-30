package test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import common.Contants;
import commonUtil.DigitalWalletUtil;
import domain.DigitalWallet;
import repository.DigitalWalletDAO;
import repository.impl.DigitalWalletDAOImpl;

@RunWith(value=JUnit4.class)
public class DigitalWalletDAOImplTests {
    
    private final String DIGITAL_WALLET_CSV_FILE_PATH = 
        Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
    private final String DIGITAL_WALLET_CSV_FILE_PATH_BACKUP = "./data/DigitalWallet/DigitalWallet_backup.csv";
    private final String DIGITAL_WALLET_SEQ_FILE_PATH_BACKUP = "./data/DigitalWallet/DigitalWalletSeq_backup.txt";
    
    private DigitalWalletDAO digitalWalletDAO;
    
    @Before
    public void setUp() throws IOException {
        digitalWalletDAO = new DigitalWalletDAOImpl();
        backupFile( DIGITAL_WALLET_CSV_FILE_PATH, DIGITAL_WALLET_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.DIGITAL_WALLET_SEQ_FILE_PATH, DIGITAL_WALLET_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        digitalWalletDAO = null;
        restoreFile( DIGITAL_WALLET_SEQ_FILE_PATH_BACKUP, Contants.DIGITAL_WALLET_SEQ_FILE_PATH );
        restoreFile( DIGITAL_WALLET_CSV_FILE_PATH_BACKUP, DIGITAL_WALLET_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        String[] expectedData = {
                "1,\"悠遊卡\",2017,12,30,\"台北捷運公司\",1000,\"\"",
                "2,\"Google Pay\",2017,12,1,\"Google\",1000,\"\"",
                "3,\"LINE Pay\",2017,11,28,\"LINE Corporation\",500,\"\"" };
        String[] actualData = new String[ 3 ];
        try {
            digitalWalletDAO.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( DIGITAL_WALLET_CSV_FILE_PATH ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            String currentTuple = "";
            int i = 0;
            for( ; (currentTuple = bufReader.readLine()) != null; i++ ) {
                actualData[ i ] = currentTuple;
            }
            bufReader.close();
            
            assertEquals( 3, i );
            for( i = 0; i < 3; i++ ) {
                assertEquals( "failed at i = " + i, expectedData[ i ], actualData[ i ] );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        DigitalWallet expect = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        DigitalWallet actual = null;
        try {
            digitalWalletDAO.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            actual = digitalWalletDAO.findOne( 1 );
            assertTrue( DigitalWalletUtil.equals( expect, actual ) );
            
            assertNull( digitalWalletDAO.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindAll() throws IOException {
        List<DigitalWallet> expectedDataList = new ArrayList<DigitalWallet>();
        expectedDataList.add( new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
        expectedDataList.add( new DigitalWallet( 2, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
        expectedDataList.add( new DigitalWallet( 3, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
        List<DigitalWallet> actualDataList = null;
        
        try {
            digitalWalletDAO.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            actualDataList = digitalWalletDAO.findAll();
            assertEquals( 3, actualDataList.size() );
            for( int i = 0; i < 3; i++ ) {
                assertTrue( DigitalWalletUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,\"悠遊卡\",2017,12,30,\"台北捷運公司\",1000,\"\"" );
        expectedDataList.add( "2,\"Google Pay\",2017,12,1,\"Google\",1000,\"\"" );
        expectedDataList.add( "3,\"LINE Pay\",2017,11,28,\"LINE Corporation\",600,\"\"" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            digitalWalletDAO.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            DigitalWallet modifiedData = new DigitalWallet( 3, "LINE Pay", 2017, 11, 28, "LINE Corporation", 600, "" );
            
            digitalWalletDAO.update( modifiedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( DIGITAL_WALLET_CSV_FILE_PATH ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            String currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            assertEquals( expectedDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectedDataList.size(); i++ ) {
                assertEquals( "failed at i = " + i, expectedDataList.get( i ), actualDataList.get( i ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "2,\"Google Pay\",2017,12,1,\"Google\",1000,\"\"" );
        expectedDataList.add( "3,\"LINE Pay\",2017,11,28,\"LINE Corporation\",500,\"\"" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            digitalWalletDAO.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            DigitalWallet deletedData = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
            
            digitalWalletDAO.delete( deletedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( DIGITAL_WALLET_CSV_FILE_PATH ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            String currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            assertEquals( expectedDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectedDataList.size(); i++ ) {
                assertEquals( "failed at i = " + i, expectedDataList.get( i ), actualDataList.get( i ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testGetCurrentSeqNumber() throws IOException {
        try {
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = digitalWalletDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 3;
            
            digitalWalletDAO.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletDAO.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            int actual2 = digitalWalletDAO.getCurrentSeqNumber();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    private void backupFile( String filePath, String backupFilePath )
            throws IOException {
        File f = new File( filePath );
        if( f.exists() && !f.isDirectory() ) {
            f.renameTo( new File( backupFilePath ) );
        }
    }
   
    private void restoreFile( String backupFilePath, String filePath )
            throws IOException {
        File f = new File( filePath );
        if( f.exists() && !f.isDirectory() ) {
            f.delete();
        }
        f = new File( backupFilePath );
        if( f.exists() && !f.isDirectory() ) {
            f.renameTo( new File( filePath ) );
        }
    }
}
