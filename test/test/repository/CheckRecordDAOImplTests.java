package test.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.CheckRecordUtil;
import domain.CheckRecord;
import repository.CheckRecordDAO;
import repository.impl.CheckRecordDAOImpl;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class CheckRecordDAOImplTests {
    
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
        String[] expectedData = {
                "1,2017,10,1,12,30,0,100,20000,20100", 
                "2,2017,10,15,12,30,0,-500,20000,19500", 
                "3,2017,10,20,12,30,0,0,30000,30000" };
        String[] actualData = new String[ 3 ];
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( CHECK_RECORD_CSV_FILE_PATH ) ),
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
            
            assertEquals( 3, i );
            for( i = 0; i < 3; i++ ) {
                assertEquals( "failed at i = " + i, expectedData[ i ], actualData[ i ] );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testFindOne() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecord expect = new CheckRecord( 2, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 );
        CheckRecord actual = null;
        try {
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            actual = checkRecordDAO.findOne( 2 );
            assertTrue( CheckRecordUtil.equals( expect, actual ) );
            
            assertNull( checkRecordDAO.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testFindAll() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        List<CheckRecord> expectedDataList = new ArrayList<CheckRecord>();
        expectedDataList.add( new CheckRecord( 1, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
        expectedDataList.add( new CheckRecord( 2, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
        expectedDataList.add( new CheckRecord( 3, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
        List<CheckRecord> actualDataList = null;
        
        try {
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            actualDataList = checkRecordDAO.findAll();
            assertEquals( 3, actualDataList.size() );
            for( int i = 0; i < 3; i++ ) {
                assertTrue( CheckRecordUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testUpdate() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,30,0,100,20000,20100" );
        expectedDataList.add( "2,2017,10,15,12,30,0,-500,20000,19500" );
        expectedDataList.add( "3,2017,10,20,12,30,0,-100,30100,30000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            CheckRecord modifiedData = new CheckRecord( 3, 2017, 10, 20, 12, 30, 0, -100, 30100, 30000 );
            
            checkRecordDAO.update( modifiedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( CHECK_RECORD_CSV_FILE_PATH ) ),
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
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,30,0,100,20000,20100" );
        expectedDataList.add( "2,2017,10,15,12,30,0,-500,20000,19500" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            CheckRecord deletedData = new CheckRecord( 3, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 );
            
            checkRecordDAO.delete( deletedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( CHECK_RECORD_CSV_FILE_PATH ) ),
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
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        
        try {
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = checkRecordDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 3;

            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 1, 12, 30, 0, 100, 20000, 20100 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 15, 12, 30, 0, -500, 20000, 19500 ) );
            checkRecordDAO.insert( new CheckRecord( 0, 2017, 10, 20, 12, 30, 0, 0, 30000, 30000 ) );
            
            int actual2 = checkRecordDAO.getCurrentSeqNumber();
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
