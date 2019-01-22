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
    
    private FundingStatusDAO fundingStatusDAO;
    
    @Before
    public void setUp() throws IOException {
        fundingStatusDAO = new FundingStatusDAOImpl();
        backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        fundingStatusDAO = null;
        restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
    }
    
    @Test
    public void testCreateDefault() {
        String[] expectedData = { "1,0,2019,1,21,\"預設可動用資金\",0,\"\",0" };
        String[] actualData = new String[ 1 ];
        try {
            // testing creating default funding status data with incorrect ID
            fundingStatusDAO.createDefault( new FundingStatus( 5, '0', 2019, 01, 21, "預設可動用資金", 0, "", 0 ) );
            
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
            
            assertEquals( 0, i );
            
            // testing creating default funding status data with correct ID
            fundingStatusDAO.createDefault( new FundingStatus( 1, '0', 2019, 01, 21, "預設可動用資金", 0, "", 0 ) );
            
            bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_CSV_FILE_PATH ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            currentTuple = "";
            i = 0;
            for( ; (currentTuple = bufReader.readLine()) != null; i++ ) {
                actualData[ i ] = currentTuple;
            }
            bufReader.close();
            
            assertEquals( 1, i );
            for( i = 0; i < 1; i++ ) {
                assertEquals( "failed at i = " + i, expectedData[ i ], actualData[ i ] );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testInsert() throws IOException {
        String[] expectedData = {
                "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\",0", 
                "2,C,2017,12,30,\"隨身現金\",5000,\"\",0", 
                "3,T,2017,11,28,\"土地銀行 #01234560123456\",100000,\"\",0", 
                "4,D,2017,10,1,\"土地銀行 #01234567890123\",50000,\"\",0", 
                "5,T,2017,10,28,\"土地銀行 #11223344556677\",200000,\"\",0" };
        String[] actualData = new String[ 5 ];
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
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
        FundingStatus expect = new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 );
        FundingStatus actual = null;
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
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
        List<FundingStatus> expectedDataList = new ArrayList<FundingStatus>();
        expectedDataList.add( new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
        expectedDataList.add( new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
        expectedDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
        expectedDataList.add( new FundingStatus( 4, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
        expectedDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
        List<FundingStatus> actualDataList = null;
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
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
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,2017,12,30,\"中華郵政 #12345671234567\",12000,\"\",0" );
        expectedDataList.add( "2,C,2017,12,30,\"隨身現金\",5000,\"\",0" );
        expectedDataList.add( "3,T,2017,11,28,\"土地銀行 #01234560123456\",100000,\"\",0" );
        expectedDataList.add( "4,D,2017,10,1,\"土地銀行 #01234567890123\",50000,\"\",0" );
        expectedDataList.add( "5,T,2017,10,28,\"土地銀行 #11223344556677\",200000,\"\",0" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            FundingStatus modifiedData = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 12000, "", 0 );
            
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
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\",0" );
        expectedDataList.add( "2,C,2017,12,30,\"隨身現金\",5000,\"\",0" );
        expectedDataList.add( "3,T,2017,11,28,\"土地銀行 #01234560123456\",100000,\"\",0" );
        expectedDataList.add( "4,D,2017,10,1,\"土地銀行 #01234567890123\",50000,\"\",0" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            FundingStatus deletedData = new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 );
            
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
        try {
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = fundingStatusDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 5;

            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            int actual2 = fundingStatusDAO.getCurrentSeqNumber();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testRefreshOrderNo() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\",1" );
        expectedDataList.add( "2,C,2017,12,30,\"隨身現金\",5000,\"\",2" );
        expectedDataList.add( "3,T,2017,11,28,\"土地銀行 #01234560123456\",100000,\"\",3" );
        expectedDataList.add( "4,D,2017,10,1,\"土地銀行 #01234567890123\",50000,\"\",4" );
        expectedDataList.add( "5,T,2017,10,28,\"土地銀行 #11223344556677\",200000,\"\",5" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            fundingStatusDAO.refreshOrderNo();
            
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
    public void testGetCount() throws IOException {
        int expect = 5;
        int actual = 0;
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            actual = fundingStatusDAO.getCount();
            assertEquals( expect, actual );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveUp() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\",1" );
        expectedDataList.add( "2,C,2017,12,30,\"隨身現金\",5000,\"\",2" );
        expectedDataList.add( "3,T,2017,11,28,\"土地銀行 #01234560123456\",100000,\"\",3" );
        expectedDataList.add( "5,T,2017,10,28,\"土地銀行 #11223344556677\",200000,\"\",5" );
        expectedDataList.add( "4,D,2017,10,1,\"土地銀行 #01234567890123\",50000,\"\",4" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            fundingStatusDAO.refreshOrderNo();
            fundingStatusDAO.moveUp( 5 );
            
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
    public void testMoveDown() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,D,2017,12,30,\"中華郵政 #12345671234567\",10000,\"\",1" );
        expectedDataList.add( "3,T,2017,11,28,\"土地銀行 #01234560123456\",100000,\"\",3" );
        expectedDataList.add( "2,C,2017,12,30,\"隨身現金\",5000,\"\",2" );
        expectedDataList.add( "4,D,2017,10,1,\"土地銀行 #01234567890123\",50000,\"\",4" );
        expectedDataList.add( "5,T,2017,10,28,\"土地銀行 #11223344556677\",200000,\"\",5" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusDAO.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            fundingStatusDAO.refreshOrderNo();
            fundingStatusDAO.moveDown( 2 );
            
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
