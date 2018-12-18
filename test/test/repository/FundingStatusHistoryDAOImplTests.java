package test.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.FundingStatusHistoryUtil;
import domain.FundingStatusHistory;
import repository.FundingStatusHistoryDAO;
import repository.impl.FundingStatusHistoryDAOImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class FundingStatusHistoryDAOImplTests {
    
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH = "./data/FundingStatusHistory/1.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP = "./data/FundingStatusHistory/1_backup.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 = "./data/FundingStatusHistory/2.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 = "./data/FundingStatusHistory/2_backup.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 = "./data/FundingStatusHistory/3.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 = "./data/FundingStatusHistory/3_backup.csv";
    private final String FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP = "./data/FundingStatusHistory/FundingStatusHistorySeq_backup.txt";
    
    private FundingStatusHistoryDAO fundingStatusHistoryDAO;
    
    @Before
    public void setUp() throws IOException {
        fundingStatusHistoryDAO = new FundingStatusHistoryDAOImpl();
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 );
        backupFile( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH, FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        fundingStatusHistoryDAO = null;
        restoreFile( FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_HISTORY_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws Exception {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,1,2017,12,30,12,0,0,C,0,10000,10000,\"\"" );
        expectedDataList.add( "2,2,2017,12,30,12,0,0,U,5000,10000,5000,\"\"" );
        expectedDataList.add( "3,3,2017,10,1,12,0,0,C,0,50000,50000,\"\"" );
        expectedDataList.add( "4,3,2017,11,28,12,0,0,U,100000,120000,20000,\"\"" );
        expectedDataList.add( "5,3,2017,12,1,12,0,0,M,120000,90000,-30000,\"\"" );
        List<String> actualDataList = new ArrayList<String>();
        try {
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            // get data of "fundingStatusId = 1"
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_HISTORY_CSV_FILE_PATH ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            String currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            // get data of "fundingStatusId = 2"
            bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            // get data of "fundingStatusId = 3"
            bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            // verify data
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
    public void testFindOneByIndex() {
        FundingStatusHistory expect = new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" );
        FundingStatusHistory actual = null;
        try {
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            actual = fundingStatusHistoryDAO.findOne( 5, 3 );
            assertTrue( FundingStatusHistoryUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusHistoryDAO.findOne( 6, 3 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() {
        FundingStatusHistory expect = new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" );
        FundingStatusHistory actual = null;
        try {
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            actual = fundingStatusHistoryDAO.findOne( 5 );
            assertTrue( FundingStatusHistoryUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusHistoryDAO.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindAllByIndex() {
        List<FundingStatusHistory> expectedDataList = new ArrayList<FundingStatusHistory>();
        expectedDataList.add( new FundingStatusHistory( 3, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
        expectedDataList.add( new FundingStatusHistory( 4, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
        expectedDataList.add( new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
        List<FundingStatusHistory> actualDataList = null;
        
        try {
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            actualDataList = fundingStatusHistoryDAO.findAll( 3 );
            assertEquals( 3, actualDataList.size() );
            for( int i = 0; i < 3; i++ ) {
                assertTrue( FundingStatusHistoryUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindAll() {
        List<FundingStatusHistory> expectedDataList = new ArrayList<FundingStatusHistory>();
        expectedDataList.add( new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
        expectedDataList.add( new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
        expectedDataList.add( new FundingStatusHistory( 3, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
        expectedDataList.add( new FundingStatusHistory( 4, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
        expectedDataList.add( new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
        List<FundingStatusHistory> actualDataList = null;
        
        try {
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            actualDataList = fundingStatusHistoryDAO.findAll();
            assertEquals( 5, actualDataList.size() );
            for( int i = 0; i < 5; i++ ) {
                assertTrue( FundingStatusHistoryUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,1,2017,12,30,12,0,0,C,0,10000,10000,\"\"" );
        expectedDataList.add( "2,2,2017,12,30,12,0,0,U,5000,10000,5000,\"\"" );
        expectedDataList.add( "3,3,2017,10,1,12,0,0,C,0,50000,50000,\"\"" );
        expectedDataList.add( "4,3,2017,11,28,12,0,0,U,100000,120000,20000,\"\"" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            FundingStatusHistory deletedData = new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" );
            
            fundingStatusHistoryDAO.delete( deletedData );
            
            // get data of "fundingStatusId = 1"
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_HISTORY_CSV_FILE_PATH ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            String currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            // get data of "fundingStatusId = 2"
            bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList.add( currentTuple );
            }
            bufReader.close();
            
            // get data of "fundingStatusId = 3"
            bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 ) ),
                    Contants.FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            currentTuple = "";
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
    public void testGetCurrentSeqNumber() {
        try {
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = fundingStatusHistoryDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 5;
            
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryDAO.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            int actual2 = fundingStatusHistoryDAO.getCurrentSeqNumber();
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
