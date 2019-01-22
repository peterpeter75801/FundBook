package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.Contants;
import commonUtil.FundingStatusHistoryUtil;
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import domain.FundingStatusHistory;
import repository.FundingStatusDAO;
import repository.FundingStatusHistoryDAO;
import repository.impl.FundingStatusDAOImpl;
import repository.impl.FundingStatusHistoryDAOImpl;
import service.FundingStatusHistoryService;
import service.FundingStatusService;
import service.impl.FundingStatusHistoryServiceImpl;
import service.impl.FundingStatusServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class FundingStatusServiceImplTests {
    
    private final String FUNDING_STATUS_CSV_FILE_PATH = 
            Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
    private final String FUNDING_STATUS_CSV_FILE_PATH_BACKUP = "./data/FundingStatus/FundingStatus_backup.csv";
    private final String FUNDING_STATUS_SEQ_FILE_PATH_BACKUP = "./data/FundingStatus/FundingStatusSeq_backup.txt";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH = "./data/FundingStatusHistory/1.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP = "./data/FundingStatusHistory/1_backup.csv";
    private final String FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP = "./data/FundingStatusHistory/FundingStatusHistorySeq_backup.txt";
    
    private FundingStatusService fundingStatusService;
    private FundingStatusHistoryService fundingStatusHistoryService;
    
    @Before
    public void setUp() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        FundingStatusHistoryDAO fundingStatusHistoryDAO = new FundingStatusHistoryDAOImpl();
        fundingStatusHistoryService = new FundingStatusHistoryServiceImpl( fundingStatusHistoryDAO );
        
        ((FundingStatusServiceImpl)fundingStatusService).setFundingStatusHistoryService( fundingStatusHistoryService );
        
        backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH, FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        fundingStatusService = null;
        restoreFile( FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_HISTORY_CSV_FILE_PATH );
        restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
    }
    
    @Test
    public void testInitialDefault() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        
        List<FundingStatus> expectFundingStatus = new ArrayList<FundingStatus>();
        expectFundingStatus.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1 ) );
        List<FundingStatusHistory> expectFundingStatusHistory = new ArrayList<FundingStatusHistory>();
        expectFundingStatusHistory.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0,
                'C', 0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        
        try {
            fundingStatusService.initialDefault();
            
            List<FundingStatus> actualFundingStatus = fundingStatusService.findAll();
            assertEquals( expectFundingStatus.size(), actualFundingStatus.size() );
            for( int i = 0; i < expectFundingStatus.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( 
                        expectFundingStatus.get( i ), actualFundingStatus.get( i ) ) );
            }
            
            List<FundingStatusHistory> actualFundingStatusHistory = fundingStatusHistoryService.findByFundingStatusId(  
                    Contants.FUNDING_STATUS_DEFAULT_ID );
            assertEquals( expectFundingStatusHistory.size(), actualFundingStatusHistory.size() );
            for( int i = 0; i < expectFundingStatus.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectFundingStatusHistory.get( i ), actualFundingStatusHistory.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testInsert() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 1 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "", 2 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 3 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 4 ) );
        expectDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 5 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        FundingStatus expect = new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 5 );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            FundingStatus actual = fundingStatusService.findOne( 5 );
            assertTrue( FundingStatusUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 12000, "", 1 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "", 2 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 3 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 4 ) );
        expectDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 5 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            FundingStatus modifiedData = new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 12000, "", 0 );
            fundingStatusService.update( modifiedData );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 1 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "", 2 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 3 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 4 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            FundingStatus deletedData = new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 );
            fundingStatusService.delete( deletedData );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            actual = fundingStatusService.getCount();
            assertEquals( expect, actual );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveUp() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 1 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "", 2 ) );
        expectDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 3 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 4 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 5 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            fundingStatusService.moveUp( 1 );
            fundingStatusService.moveUp( 5 );
            fundingStatusService.moveUp( 4 );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveDown() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 1 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 2 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 3 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 2017, 12, 30, "隨身現金", 5000, "", 4 ) );
        expectDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 5 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            fundingStatusService.moveDown( 5 );
            fundingStatusService.moveDown( 2 );
            fundingStatusService.moveDown( 3 );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
