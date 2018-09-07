package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.CheckRecordUtil;
import domain.CheckRecord;
import repository.CheckRecordDAO;
import repository.impl.CheckRecordDAOImpl;
import service.CheckRecordService;
import service.impl.CheckRecordServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class CheckRecordServiceImplTests {
    
    private final String CHECK_RECORD_CSV_FILE_PATH = 
            Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
    private final String CHECK_RECORD_CSV_FILE_PATH_BACKUP = "./data/CheckRecord/CheckRecord_backup.csv";
    private final String CHECK_RECORD_SEQ_FILE_PATH_BACKUP = "./data/CheckRecord/CheckRecordSeq_backup.txt";
    
    @Before
    public void setUp() throws IOException {
    	backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
    	restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
        restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        List<CheckRecord> expectDataList = new ArrayList<CheckRecord>();
        expectDataList.add( new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
        expectDataList.add( new CheckRecord( 2, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
        expectDataList.add( new CheckRecord( 3, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
        
        try {
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            List<CheckRecord> actualDataList = checkRecordService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, CheckRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        CheckRecord expect = new CheckRecord( 2, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 );
        
        try {
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            CheckRecord actual = checkRecordService.findOne( 2 );
            assertTrue( CheckRecordUtil.equals( expect, actual ) );
            
            assertNull( checkRecordService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testUpdate() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        List<CheckRecord> expectDataList = new ArrayList<CheckRecord>();
        expectDataList.add( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
        expectDataList.add( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
        expectDataList.add( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, -100, 30100, 30000 ) );
        for( int i = 1; i <= 3; i++ ) {
            expectDataList.get( i - 1 ).setId( i );
        }
        
        try {
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            CheckRecord modifiedData = new CheckRecord( 3, 2017, 10, 20, 12, 30, 0, -100, 30100, 30000 );
            checkRecordService.update( modifiedData );
            
            List<CheckRecord> actualDataList = checkRecordService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, CheckRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testDelete() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecordService checkRecordService = new CheckRecordServiceImpl( checkRecordDAO );
        
        List<CheckRecord> expectDataList = new ArrayList<CheckRecord>();
        expectDataList.add( new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
        expectDataList.add( new CheckRecord( 2, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
        
        try {
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordService.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            CheckRecord deletedData = new CheckRecord( 3, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 );
            checkRecordService.delete( deletedData );
            
            List<CheckRecord> actualDataList = checkRecordService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, CheckRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
