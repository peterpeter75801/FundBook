package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import repository.FundingStatusDAO;
import repository.impl.FundingStatusDAOImpl;
import service.FundingStatusService;
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
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
        expectDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
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
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        FundingStatus expect = new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
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
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 12000 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
        expectDataList.add( new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            FundingStatus modifiedData = new FundingStatus( 1, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 12000 );
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
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( new FundingStatus( 1, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
        expectDataList.add( new FundingStatus( 2, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
        expectDataList.add( new FundingStatus( 3, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
        expectDataList.add( new FundingStatus( 4, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
        
        try {
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "700", "中華郵政", "12345671234567", "12345671234567", 10000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 0, 0, 0, "", "", "", "", 5000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "500", "土地銀行", "01234560123456", "01234567890123", 100000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 0, 0, 0, "005", "土地銀行", "01234567890123", "01234567890123", 50000 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 ) );
            
            FundingStatus deletedData = new FundingStatus( 5, 'T', 2017, 10, 28, "005", "土地銀行", "11223344556677", "01234567890123", 200000 );
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
