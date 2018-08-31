package test.repository;

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
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import repository.FundingStatusDAO;
import repository.impl.FundingStatusDAOImpl;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class FundingStatusDAOImplTests {
    
    private final String FUNDING_STATUS_CSV_FILE_PATH = 
        Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
    private final String FUNDING_STATUS_CSV_FILE_PATH_BACKUP = "./data/FundingStatus/FundingStatus_backup.csv";
    private final String FUNDING_STATUS_SEQ_FILE_PATH_BACKUP = "./data/FundingStatus/FundingStatusSeq_backup.txt";
    
    @Before
    public void setUp() throws IOException {
        backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        String[] expectedData = {
                "1,D,0,0,0,\"700\",\"中華郵政\",\"12345671234567\",\"12345671234567\",10000", 
                "2,C,0,0,0,\"\",\"\",\"\",\"\",5000", 
                "3,T,2017,11,28,\"005\",\"土地銀行\",\"01234560123456\",\"01234567890123\",100000", 
                "4,D,0,0,0,\"005\",\"土地銀行\",\"01234567890123\",\"01234567890123\",50000", 
                "5,T,2017,10,28,\"005\",\"土地銀行\",\"11223344556677\",\"01234567890123\",200000" };
        String[] actualData = new String[ 5 ];
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_CSV_FILE_PATH ) ),
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
            
            assertEquals( 5, i );
            for( i = 0; i < 5; i++ ) {
                assertEquals( "failed at i = " + i, expectedData[ i ], actualData[ i ] );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatus expect = new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 );
        FundingStatus actual = null;
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            actual = fundingStatusDAO.findOne( 5 );
            assertTrue( FundingStatusUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusDAO.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindAll() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        List<FundingStatus> expectedDataList = new ArrayList<FundingStatus>();
        expectedDataList.add( new FundingStatus( 1, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
        expectedDataList.add( new FundingStatus( 2, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
        expectedDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
        expectedDataList.add( new FundingStatus( 4, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
        expectedDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
        List<FundingStatus> actualDataList = null;
        
        try {
            backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
            
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            actualDataList = fundingStatusDAO.findAll();
            assertEquals( 5, actualDataList.size() );
            for( int i = 0; i < 5; i++ ) {
                assertTrue( FundingStatusUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,0,0,0,\"700\",\"中華郵政\",\"12345671234567\",\"12345671234567\",12000" );
        expectedDataList.add( "2,C,0,0,0,\"\",\"\",\"\",\"\",5000" );
        expectedDataList.add( "3,T,2017,11,28,\"005\",\"土地銀行\",\"01234560123456\",\"01234567890123\",100000" );
        expectedDataList.add( "4,D,0,0,0,\"005\",\"土地銀行\",\"01234567890123\",\"01234567890123\",50000" );
        expectedDataList.add( "5,T,2017,10,28,\"005\",\"土地銀行\",\"11223344556677\",\"01234567890123\",200000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            FundingStatus modifiedData = new FundingStatus( 1, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 12000 );
            
            fundingStatusDAO.update( modifiedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_CSV_FILE_PATH ) ),
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
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,0,0,0,\"700\",\"中華郵政\",\"12345671234567\",\"12345671234567\",10000" );
        expectedDataList.add( "2,C,0,0,0,\"\",\"\",\"\",\"\",5000" );
        expectedDataList.add( "3,T,2017,11,28,\"005\",\"土地銀行\",\"01234560123456\",\"01234567890123\",100000" );
        expectedDataList.add( "4,D,0,0,0,\"005\",\"土地銀行\",\"01234567890123\",\"01234567890123\",50000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            FundingStatus deletedData = new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 );
            
            fundingStatusDAO.delete( deletedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_CSV_FILE_PATH ) ),
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
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        
        try {
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = fundingStatusDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 5;

            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "005", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            int actual2 = fundingStatusDAO.getCurrentSeqNumber();
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
