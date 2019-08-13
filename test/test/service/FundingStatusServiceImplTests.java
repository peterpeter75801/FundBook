package test.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.Contants;
import common.SystemInfo;
import commonUtil.FundingStatusHistoryUtil;
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import domain.FundingStatusHistory;
import main.FundBook;
import main.FundBookServices;
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
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 = "./data/FundingStatusHistory/2.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 = "./data/FundingStatusHistory/2_backup.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 = "./data/FundingStatusHistory/3.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 = "./data/FundingStatusHistory/3_backup.csv";
    private final String FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP = "./data/FundingStatusHistory/FundingStatusHistorySeq_backup.txt";
    
    private FundBookServices fundBookServices;
    private FundingStatusService fundingStatusService;
    private FundingStatusHistoryService fundingStatusHistoryService;
    
    private Calendar calendar;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    
    @Before
    public void setUp() throws Exception {
        fundBookServices = defaultFundBookServices();
        fundingStatusService = fundBookServices.getFundingStatusService();
        fundingStatusHistoryService = fundBookServices.getFundingStatusHistoryService();
        
        backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 );
        backupFile( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH, FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP );
        
        calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        currentYear = calendar.get( Calendar.YEAR );
        currentMonth = calendar.get( Calendar.MONTH ) + 1;
        currentDay = calendar.get( Calendar.DAY_OF_MONTH );
    }
    
    @After
    public void tearDown() throws IOException {
        fundBookServices = null;
        fundingStatusService = null;
        fundingStatusHistoryService = null;
        
        restoreFile( FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_HISTORY_CSV_FILE_PATH );
        restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
    }
    
    @Test
    public void testInitialDefault() {
        List<FundingStatus> expectFundingStatus = new ArrayList<FundingStatus>();
        expectFundingStatus.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
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
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, false ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100000, 100000, "" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            List<FundingStatusHistory> actualHisDataList = new ArrayList<FundingStatusHistory>();
            for( int i = 1; i <= 3; i++ ) {
                actualHisDataList.add( fundingStatusHistoryService.findOne( i ) );
            }
            assertEquals( expectHisDataList.size(), actualHisDataList.size() );
            for( int i = 0; i < expectHisDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectHisDataList.get( i ), actualHisDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        FundingStatus expect = new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, false );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            FundingStatus actual = fundingStatusService.findOne( 3 );
            assertTrue( FundingStatusUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws Exception {
        FundingStatus modifiedData1 = new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 5000, "", 1, false );
        FundingStatus modifiedData2 = new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 12000, "", 2, false );
        FundingStatus modifiedData3 = new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560abcdef", 100000, "", 3, false );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 5000, "", 1, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 12000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560abcdef", 100000, "", 3, false ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100000, 100000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 4, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'U', 
                10000, 12000, 2000, "" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.update( modifiedData1 );
            fundingStatusService.update( modifiedData2 );
            fundingStatusService.update( modifiedData3 );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            List<FundingStatusHistory> actualHisDataList = new ArrayList<FundingStatusHistory>();
            for( int i = 1; i <= 4; i++ ) {
                actualHisDataList.add( fundingStatusHistoryService.findOne( i ) );
            }
            assertEquals( expectHisDataList.size(), actualHisDataList.size() );
            for( int i = 0; i < expectHisDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectHisDataList.get( i ), actualHisDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdateAmount() {
        int inputId1 = 1;
        int inputId2 = 2;
        int inputAmount1 = 5000;
        int inputAmount2 = 15000;
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 5000, "", 1, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 15000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, false ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100000, 100000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 4, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'U', 
                10000, 15000, 5000, "test" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.updateAmount( inputId1, inputAmount1, "" );
            fundingStatusService.updateAmount( inputId2, inputAmount2, "test" );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            List<FundingStatusHistory> actualHisDataList = new ArrayList<FundingStatusHistory>();
            for( int i = 1; i <= 4; i++ ) {
                actualHisDataList.add( fundingStatusHistoryService.findOne( i ) );
            }
            assertEquals( expectHisDataList.size(), actualHisDataList.size() );
            for( int i = 0; i < expectHisDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectHisDataList.get( i ), actualHisDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdateStoredPlaceOrInstitution() {
        int inputId = 3;
        String inputStoredPlaceOrInstitution = "土地銀行 #01234560abcdef";
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560abcdef", 100000, "", 3, false ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100000, 100000, "" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.updateStoredPlaceOrInstitution( inputId, inputStoredPlaceOrInstitution );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            List<FundingStatusHistory> actualHisDataList = new ArrayList<FundingStatusHistory>();
            for( int i = 1; i <= 3; i++ ) {
                actualHisDataList.add( fundingStatusHistoryService.findOne( i ) );
            }
            assertEquals( expectHisDataList.size(), actualHisDataList.size() );
            for( int i = 0; i < expectHisDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectHisDataList.get( i ), actualHisDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdateProperty() {
        int inputId1 = 1;
        String inputStoredPlaceOrInstitution1 = "現金";
        
        int inputId2 = 2;
        String inputStoredPlaceOrInstitution2 = "中華郵政 #1234567test";
        
        int inputId3 = 3;
        Character inputType3 = 'D';
        String inputStoredPlaceOrInstitution3 = "土地銀行 #01234560abcdef";
        String inputDescription3 = "test123";
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay,
                "現金", 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay,
                "中華郵政 #1234567test", 10000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'D', currentYear, currentMonth, currentDay,
                "土地銀行 #01234560abcdef", 100000, "test123", 3, false ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay,
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay,
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.updateProperty( inputId1, null, inputStoredPlaceOrInstitution1, null );
            fundingStatusService.updateProperty( inputId2, null, inputStoredPlaceOrInstitution2, null );
            fundingStatusService.updateProperty( inputId3, inputType3, inputStoredPlaceOrInstitution3, inputDescription3 );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveAmount() {
        int inputSourceId = 3;
        int inputDestinationId = 2;
        int inputAmount = 200;
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10200, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, false ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100200, 100200, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 4, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'U', 
                100200, 100000, -200, "轉出金額200元至'中華郵政 #12345671234567'" ) );
        expectHisDataList.add( new FundingStatusHistory( 5, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'U', 
                10000, 10200, 200, "由'土地銀行 #01234560123456'轉入金額200元" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100200, "", 0, false ) );
            
            fundingStatusService.moveAmount( inputSourceId, inputDestinationId, inputAmount );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            List<FundingStatusHistory> actualHisDataList = new ArrayList<FundingStatusHistory>();
            for( int i = 1; i <= 5; i++ ) {
                actualHisDataList.add( fundingStatusHistoryService.findOne( i ) );
            }
            assertEquals( expectHisDataList.size(), actualHisDataList.size() );
            for( int i = 0; i < expectHisDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectHisDataList.get( i ), actualHisDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 2, false ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100000, 100000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 4, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'D', 
                10000, 10000, 0, "永久刪除" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            FundingStatus deletedData = new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 2, false );
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
    public void testDisable() {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 3, true ) );
        
        List<FundingStatusHistory> expectHisDataList = new ArrayList<FundingStatusHistory>();
        expectHisDataList.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
        expectHisDataList.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 10000, 10000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 0, 0, 0, 'C', 
                0, 100000, 100000, "" ) );
        expectHisDataList.add( new FundingStatusHistory( 4, 2, currentYear, currentMonth, currentDay, 0, 0, 0, 'D', 
                10000, 10000, 0, "" ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.disable( 2 );
            
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
        int expect = 3;
        int actual = 0;
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
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
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 1, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 2, false ) );
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 3, false ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.moveUp( 1 );
            fundingStatusService.moveUp( 3 );
            
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
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 1, false ) );
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, false ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, false ) );
            
            fundingStatusService.moveDown( 1 );
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
    
    @Test
    public void testMoveUpWithDisabledData() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 1, false ) );
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, true ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, true ) );
            
            fundingStatusService.moveUp( 2 );
            fundingStatusService.moveUp( 3 );
            
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
    public void testMoveDownWithDisabledData() throws IOException {
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                "中華郵政 #12345671234567", 10000, "", 1, false ) );
        expectDataList.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 2, false ) );
        expectDataList.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                "土地銀行 #01234560123456", 100000, "", 3, true ) );
        
        try {
            fundingStatusService.initialDefault();
            fundingStatusService.insert( new FundingStatus( 0, 'D', currentYear, currentMonth, currentDay, 
                    "中華郵政 #12345671234567", 10000, "", 0, false ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', currentYear, currentMonth, currentDay, 
                    "土地銀行 #01234560123456", 100000, "", 0, true ) );
            
            fundingStatusService.moveDown( 1 );
            fundingStatusService.moveDown( 2 );
            
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
    
    private FundBookServices defaultFundBookServices() throws URISyntaxException {
        // Initialize system information
        SystemInfo systemInfo = new SystemInfo(
            new File( FundBook.class.getProtectionDomain().getCodeSource().getLocation().toURI() ).getParent() );
        
        // Initialize DAOs
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl( systemInfo );
        FundingStatusHistoryDAO fundingStatusHistoryDAO = new FundingStatusHistoryDAOImpl( systemInfo );
        
        // Initialize services
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setFundingStatusService( new FundingStatusServiceImpl( fundingStatusDAO ) );
        fundBookServices.setFundingStatusHistoryService( new FundingStatusHistoryServiceImpl( fundingStatusHistoryDAO ) );
        
        // Set services wired relation
        ((FundingStatusServiceImpl)fundBookServices.getFundingStatusService()).setFundingStatusHistoryService(
                fundBookServices.getFundingStatusHistoryService() );
        
        return fundBookServices;
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
