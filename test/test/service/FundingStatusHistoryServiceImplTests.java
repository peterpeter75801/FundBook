package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.FundingStatusHistoryUtil;
import domain.FundingStatusHistory;
import repository.FundingStatusHistoryDAO;
import repository.impl.FundingStatusHistoryDAOImpl;
import service.FundingStatusHistoryService;
import service.impl.FundingStatusHistoryServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class FundingStatusHistoryServiceImplTests {
    
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH = 
            Contants.FUNDING_STATUS_HISTORY_DATA_PATH + Contants.FUNDING_STATUS_HISTORY_FILENAME;
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP = "./data/FundingStatusHistory/FundingStatusHistory_backup.csv";
    private final String FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP = "./data/FundingStatusHistory/FundingStatusHistorySeq_backup.txt";
    
    private FundingStatusHistoryService fundingStatusHistoryService;
    
    @Before
    public void setUp() throws IOException {
        FundingStatusHistoryDAO fundingStatusHistoryDAO = new FundingStatusHistoryDAOImpl();
        fundingStatusHistoryService = new FundingStatusHistoryServiceImpl( fundingStatusHistoryDAO );
        
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        fundingStatusHistoryService = null;
        restoreFile( FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_HISTORY_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        List<FundingStatusHistory> expectDataList = new ArrayList<FundingStatusHistory>();
        expectDataList.add( new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
        expectDataList.add( new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
        expectDataList.add( new FundingStatusHistory( 3, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
        expectDataList.add( new FundingStatusHistory( 4, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
        expectDataList.add( new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
        
        try {
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            List<FundingStatusHistory> actualDataList = fundingStatusHistoryService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        FundingStatusHistory expect = new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" );
        
        try {
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            FundingStatusHistory actual = fundingStatusHistoryService.findOne( 5 );
            assertTrue( FundingStatusHistoryUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusHistoryService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() throws IOException {
        List<FundingStatusHistory> expectDataList = new ArrayList<FundingStatusHistory>();
        expectDataList.add( new FundingStatusHistory( 1, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
        expectDataList.add( new FundingStatusHistory( 2, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
        expectDataList.add( new FundingStatusHistory( 3, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
        expectDataList.add( new FundingStatusHistory( 4, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
        
        try {
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 1, 2017, 12, 30, 12, 0, 0, 'C', 0, 10000, 10000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 2, 2017, 12, 30, 12, 0, 0, 'U', 5000, 10000, 5000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 10, 1, 12, 0, 0, 'C', 0, 50000, 50000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 11, 28, 12, 0, 0, 'U', 100000, 120000, 20000, "" ) );
            fundingStatusHistoryService.insert( new FundingStatusHistory( 0, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" ) );
            
            FundingStatusHistory deletedData = new FundingStatusHistory( 5, 3, 2017, 12, 1, 12, 0, 0, 'M', 120000, 90000, -30000, "" );
            fundingStatusHistoryService.delete( deletedData );
            
            List<FundingStatusHistory> actualDataList = fundingStatusHistoryService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
