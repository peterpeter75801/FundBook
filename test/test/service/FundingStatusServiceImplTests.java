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

import junit.framework.TestCase;

public class FundingStatusServiceImplTests extends TestCase {
    
    private final String FUNDING_STATUS_CSV_FILE_PATH = 
            Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
    private final String FUNDING_STATUS_CSV_FILE_PATH_BACKUP = "data\\FundingStatus\\FundingStatus_backup.csv";
    private final String FUNDING_STATUS_SEQ_FILE_PATH_BACKUP = "data\\FundingStatus\\FundingStatusSeq_backup.txt";
    
    public void testInsert() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( getTestData1() );
        expectDataList.add( getTestData2() );
        expectDataList.add( getTestData3() );
        expectDataList.add( getTestData4() );
        expectDataList.add( getTestData5() );
        for( int i = 1; i <= 5; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
            
            fundingStatusService.insert( getTestData1() );
            fundingStatusService.insert( getTestData2() );
            fundingStatusService.insert( getTestData3() );
            fundingStatusService.insert( getTestData4() );
            fundingStatusService.insert( getTestData5() );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
            restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
        }
    }
    
    public void testFindOne() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        FundingStatus expect = getTestData5();
        expect.setId( 5 );
        
        try {
            backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
            
            fundingStatusService.insert( getTestData1() );
            fundingStatusService.insert( getTestData2() );
            fundingStatusService.insert( getTestData3() );
            fundingStatusService.insert( getTestData4() );
            fundingStatusService.insert( getTestData5() );
            
            FundingStatus actual = fundingStatusService.findOne( 5 );
            assertTrue( FundingStatusUtil.equals( expect, actual ) );
            
            assertNull( fundingStatusService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
            restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
        }
    }
    
    public void testUpdate() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( getTestData1() );
        expectDataList.get( 0 ).setBalance( 12000 );
        expectDataList.add( getTestData2() );
        expectDataList.add( getTestData3() );
        expectDataList.add( getTestData4() );
        expectDataList.add( getTestData5() );
        for( int i = 1; i <= 5; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
            
            fundingStatusService.insert( getTestData1() );
            fundingStatusService.insert( getTestData2() );
            fundingStatusService.insert( getTestData3() );
            fundingStatusService.insert( getTestData4() );
            fundingStatusService.insert( getTestData5() );
            
            FundingStatus modifiedData = getTestData1();
            modifiedData.setId( 1 );
            modifiedData.setBalance( 12000 );
            fundingStatusService.update( modifiedData );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
            restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
        }
    }
    
    public void testDelete() throws IOException {
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        FundingStatusService fundingStatusService = new FundingStatusServiceImpl( fundingStatusDAO );
        
        List<FundingStatus> expectDataList = new ArrayList<FundingStatus>();
        expectDataList.add( getTestData1() );
        expectDataList.add( getTestData2() );
        expectDataList.add( getTestData3() );
        expectDataList.add( getTestData4() );
        for( int i = 1; i <= 4; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
            
            fundingStatusService.insert( getTestData1() );
            fundingStatusService.insert( getTestData2() );
            fundingStatusService.insert( getTestData3() );
            fundingStatusService.insert( getTestData4() );
            fundingStatusService.insert( getTestData5() );
            
            FundingStatus deletedData = getTestData5();
            deletedData.setId( 5 );
            fundingStatusService.delete( deletedData );
            
            List<FundingStatus> actualDataList = fundingStatusService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
            restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
        }
    }
    
    private FundingStatus getTestData1() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 0 );
        testData.setType( 'D' );
        testData.setYear( 0 );
        testData.setMonth( 0 );
        testData.setDay( 0 );
        testData.setBankCode( "700" );
        testData.setBankName( "中華郵政" );
        testData.setAccount( "12345671234567" );
        testData.setInterestAccount( "12345671234567" );
        testData.setBalance( 10000 );
        return testData;
    }
    
    private FundingStatus getTestData2() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 0 );
        testData.setType( 'C' );
        testData.setYear( 0 );
        testData.setMonth( 0 );
        testData.setDay( 0 );
        testData.setBankCode( "" );
        testData.setBankName( "" );
        testData.setAccount( "" );
        testData.setInterestAccount( "" );
        testData.setBalance( 5000 );
        return testData;
    }
    
    private FundingStatus getTestData3() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 0 );
        testData.setType( 'T' );
        testData.setYear( 2017 );
        testData.setMonth( 11 );
        testData.setDay( 28 );
        testData.setBankCode( "005" );
        testData.setBankName( "土地銀行" );
        testData.setAccount( "01234560123456" );
        testData.setInterestAccount( "01234567890123" );
        testData.setBalance( 100000 );
        return testData;
    }
    
    private FundingStatus getTestData4() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 0 );
        testData.setType( 'D' );
        testData.setYear( 0 );
        testData.setMonth( 0 );
        testData.setDay( 0 );
        testData.setBankCode( "005" );
        testData.setBankName( "土地銀行" );
        testData.setAccount( "01234567890123" );
        testData.setInterestAccount( "01234567890123" );
        testData.setBalance( 50000 );
        return testData;
    }
    
    private FundingStatus getTestData5() {
        FundingStatus testData = new FundingStatus();
        testData.setId( 0 );
        testData.setType( 'T' );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 28 );
        testData.setBankCode( "005" );
        testData.setBankName( "土地銀行" );
        testData.setAccount( "11223344556677" );
        testData.setInterestAccount( "01234567890123" );
        testData.setBalance( 200000 );
        return testData;
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
