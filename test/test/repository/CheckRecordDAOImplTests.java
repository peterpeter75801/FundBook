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

import junit.framework.TestCase;

public class CheckRecordDAOImplTests extends TestCase {
    
    private final String CHECK_RECORD_CSV_FILE_PATH = 
        Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
    private final String CHECK_RECORD_CSV_FILE_PATH_BACKUP = "data\\CheckRecord\\CheckRecord_backup.csv";
    private final String CHECK_RECORD_SEQ_FILE_PATH_BACKUP = "data\\CheckRecord\\CheckRecordSeq_backup.txt";
    
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
            
            checkRecordDAO.insert( getTestData1() );
            checkRecordDAO.insert( getTestData2() );
            checkRecordDAO.insert( getTestData3() );
            
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
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testFindOne() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        CheckRecord expect = getTestData2();
        expect.setId( 2 );
        CheckRecord actual = null;
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordDAO.insert( getTestData1() );
            checkRecordDAO.insert( getTestData2() );
            checkRecordDAO.insert( getTestData3() );
            
            actual = checkRecordDAO.findOne( 2 );
            assertTrue( CheckRecordUtil.equals( expect, actual ) );
            
            assertNull( checkRecordDAO.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testFindAll() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        List<CheckRecord> expectedDataList = new ArrayList<CheckRecord>();
        expectedDataList.add( getTestData1() );
        expectedDataList.add( getTestData2() );
        expectedDataList.add( getTestData3() );
        for( int i = 1; i <= 3; i++ ) {
            expectedDataList.get( i - 1 ).setId( i );
        }
        List<CheckRecord> actualDataList = null;
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordDAO.insert( getTestData1() );
            checkRecordDAO.insert( getTestData2() );
            checkRecordDAO.insert( getTestData3() );
            
            actualDataList = checkRecordDAO.findAll();
            assertEquals( 3, actualDataList.size() );
            for( int i = 0; i < 3; i++ ) {
                assertTrue( CheckRecordUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
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
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,30,0,100,20000,20100" );
        expectedDataList.add( "2,2017,10,15,12,30,0,-500,20000,19500" );
        expectedDataList.add( "3,2017,10,20,12,30,0,-100,30100,30000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordDAO.insert( getTestData1() );
            checkRecordDAO.insert( getTestData2() );
            checkRecordDAO.insert( getTestData3() );
            
            CheckRecord modifiedData = getTestData3();
            modifiedData.setId( 3 );
            modifiedData.setDifference( -100 );
            modifiedData.setBookAmount( 30100 );
            
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
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testDelete() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,30,0,100,20000,20100" );
        expectedDataList.add( "2,2017,10,15,12,30,0,-500,20000,19500" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            checkRecordDAO.insert( getTestData1() );
            checkRecordDAO.insert( getTestData2() );
            checkRecordDAO.insert( getTestData3() );
            
            CheckRecord deletedData = getTestData3();
            deletedData.setId( 3 );
            
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
        } finally {
            restoreFile( CHECK_RECORD_SEQ_FILE_PATH_BACKUP, Contants.CHECK_RECORD_SEQ_FILE_PATH );
            restoreFile( CHECK_RECORD_CSV_FILE_PATH_BACKUP, CHECK_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testGetCurrentSeqNumber() throws IOException {
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        
        try {
            backupFile( CHECK_RECORD_CSV_FILE_PATH, CHECK_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.CHECK_RECORD_SEQ_FILE_PATH, CHECK_RECORD_SEQ_FILE_PATH_BACKUP );
            
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = checkRecordDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 3;
            
            checkRecordDAO.insert( getTestData1() );
            checkRecordDAO.insert( getTestData2() );
            checkRecordDAO.insert( getTestData3() );
            
            int actual2 = checkRecordDAO.getCurrentSeqNumber();
            assertEquals( expect2, actual2 );
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
