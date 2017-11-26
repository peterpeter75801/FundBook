package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.CheckRecordUtil;
import domain.CheckRecord;

import junit.framework.TestCase;
import repository.CheckRecordDAO;
import repository.impl.CheckRecordDAOImpl;
import service.CheckRecordService;
import service.impl.CheckRecordServiceImpl;

public class CheckRecordServiceImplTests extends TestCase {
    
    private final String CHECK_RECORD_CSV_FILE_PATH = 
            Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
    private final String CHECK_RECORD_CSV_FILE_PATH_BACKUP = "data\\CheckRecord\\CheckRecord_backup.csv";
    private final String CHECK_RECORD_SEQ_FILE_PATH_BACKUP = "data\\CheckRecord\\CheckRecordSeq_backup.txt";
    
    public void testInsert() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        List<CheckRecord> expectDataList = new ArrayList<CheckRecord>();
        expectDataList.add( getTestData1() );
        expectDataList.add( getTestData2() );
        expectDataList.add( getTestData3() );
        for( int i = 1; i <= 3; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordService.insert( getTestData1() );
            checkRecordService.insert( getTestData2() );
            checkRecordService.insert( getTestData3() );
            
            List<CheckRecord> actualDataList = checkRecordService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, CheckRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testFindOne() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        CheckRecord expect = getTestData2();
        expect.setId( 2 );
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordService.insert( getTestData1() );
            checkRecordService.insert( getTestData2() );
            checkRecordService.insert( getTestData3() );
            
            CheckRecord actual = checkRecordService.findOne( 2 );
            assertTrue( CheckRecordUtil.equals( expect, actual ) );
            
            assertNull( checkRecordService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testUpdate() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        List<CheckRecord> expectDataList = new ArrayList<CheckRecord>();
        expectDataList.add( getTestData1() );
        expectDataList.add( getTestData2() );
        expectDataList.add( getTestData3() );
        expectDataList.get( expectDataList.size() - 1 ).setDifference( -100 );
        expectDataList.get( expectDataList.size() - 1 ).setBookAmount( 30100 );
        for( int i = 1; i <= 3; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordService.insert( getTestData1() );
            checkRecordService.insert( getTestData2() );
            checkRecordService.insert( getTestData3() );
            
            CheckRecord modifiedData = getTestData3();
            modifiedData.setId( 3 );
            modifiedData.setDifference( -100 );
            modifiedData.setBookAmount( 30100 );
            checkRecordService.update( modifiedData );
            
            List<CheckRecord> actualDataList = checkRecordService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, CheckRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testDelete() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        List<CheckRecord> expectDataList = new ArrayList<CheckRecord>();
        expectDataList.add( getTestData1() );
        expectDataList.add( getTestData2() );
        for( int i = 1; i <= 2; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordService.insert( getTestData1() );
            checkRecordService.insert( getTestData2() );
            checkRecordService.insert( getTestData3() );
            
            CheckRecord deletedData = getTestData3();
            deletedData.setId( 3 );
            checkRecordService.delete( deletedData );
            
            List<CheckRecord> actualDataList = checkRecordService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, CheckRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    private CheckRecord getTestData1() {
        CheckRecord testData = new CheckRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setHour( 12 );
        testData.setMinute( 30 );
        testData.setSecond( 0 );
        testData.setDifference( 100 );
        testData.setBookAmount( 20000 );
        testData.setActualAmount( 20100 );
        return testData;
    }
    
    private CheckRecord getTestData2() {
        CheckRecord testData = new CheckRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 15 );
        testData.setHour( 12 );
        testData.setMinute( 30 );
        testData.setSecond( 0 );
        testData.setDifference( -500 );
        testData.setBookAmount( 20000 );
        testData.setActualAmount( 19500 );
        return testData;
    }
    
    private CheckRecord getTestData3() {
        CheckRecord testData = new CheckRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 20 );
        testData.setHour( 12 );
        testData.setMinute( 30 );
        testData.setSecond( 0 );
        testData.setDifference( 0 );
        testData.setBookAmount( 30000 );
        testData.setActualAmount( 30000 );
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
