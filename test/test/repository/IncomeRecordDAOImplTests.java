package test.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import repository.IncomeRecordDAO;
import repository.impl.IncomeRecordDAOImpl;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordDAOImplTests {
    
    private final int INITIAL_SEQ_NUMBER = 1;
    private final String INCOME_RECORD_CSV_FILE_PATH = "./data/IncomeRecord/2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "./data/IncomeRecord/2017.10_backup.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_2 = "./data/IncomeRecord/2017.12.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 = "./data/IncomeRecord/2017.12_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "./data/IncomeRecord/IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "./data/IncomeRecord/IncomeRecordSeq_backup.txt";
    private final String FILE_CHARSET = "big5";
    
    private IncomeRecordDAO incomeRecordDAO;
    
    @Before
    public void setUp() throws IOException, URISyntaxException {
        incomeRecordDAO = new IncomeRecordDAOImpl();
        backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
        backupFile( INCOME_RECORD_CSV_FILE_PATH_2, INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 );
        backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        incomeRecordDAO = null;
        restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP_2, INCOME_RECORD_CSV_FILE_PATH_2 );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        String[] expectedData = {
                "1,2017,10,1,\"測試帳1\",0,100,\"\",0",
                "2,2017,10,1,\"測試帳2\",0,200,\"\",0",
                "3,2017,10,1,\"測試帳3\",0,300,\"\",0" };
        String[] actualData = new String[ 3 ];
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
        IncomeRecord expect = new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 0 );
        IncomeRecord actual = null;
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            actual = incomeRecordDAO.findOne( 2, 2017, 10 );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
            
            assertNull( incomeRecordDAO.findOne( 4, 2017, 10 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindByMonth() throws IOException {
        List<IncomeRecord> expectedDataList = new ArrayList<IncomeRecord>();
        expectedDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        expectedDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        expectedDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
        List<IncomeRecord> actualDataList = null;
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            actualDataList = incomeRecordDAO.findByMonth( 2017, 10 );
            assertEquals( 3, actualDataList.size() );
            for( int i = 0; i < 3; i++ ) {
                assertTrue( IncomeRecordUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",0" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,250,\"\",0" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",0" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord modifiedData = new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 250, "", 0 );
            incomeRecordDAO.update( modifiedData, modifiedData.getYear(), modifiedData.getMonth() );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
    public void testUpdateDifferentMonth() throws IOException {
        List<String> expectedDataList1 = new ArrayList<String>();
        expectedDataList1.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",0" );
        expectedDataList1.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",0" );
        List<String> expectedDataList2 = new ArrayList<String>();
        expectedDataList2.add( "3,2017,12,1,\"測試帳3\",0,300,\"\",0" );
        List<String> actualDataList1 = new ArrayList<String>();
        List<String> actualDataList2 = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord modifiedData = new IncomeRecord( 3, 2017, 12, 1, "測試帳3", 0, 300, "", 0 );
            incomeRecordDAO.updateDifferentMonth( modifiedData, 2017, 10, 2017, 12 );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            String currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList1.add( currentTuple );
            }
            bufReader.close();
            
            bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH_2 ) ),
                    FILE_CHARSET
                )
            );
            bufReader.readLine();    // skip attribute titles
            
            currentTuple = "";
            while( (currentTuple = bufReader.readLine()) != null ){
                actualDataList2.add( currentTuple );
            }
            bufReader.close();
            
            assertEquals( expectedDataList1.size(), actualDataList1.size() );
            for( int i = 0; i < expectedDataList1.size(); i++ ) {
                assertEquals( "failed at i = " + i, expectedDataList1.get( i ), actualDataList1.get( i ) );
            }
            assertEquals( expectedDataList2.size(), actualDataList2.size() );
            for( int i = 0; i < expectedDataList2.size(); i++ ) {
                assertEquals( "failed at i = " + i, expectedDataList2.get( i ), actualDataList2.get( i ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",0" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",0" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord deletedData = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
            incomeRecordDAO.delete( deletedData, 2017, 10 );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
        try {
            // 測試初始情況
            int expect1 = INITIAL_SEQ_NUMBER - 1;
            int actual1 = incomeRecordDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = INITIAL_SEQ_NUMBER + 5;
            
            for( int i = 1; i <= 5; i++ ) {
                incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳", 0, 100, "", 0 ) );
            }
            
            int actual2 = incomeRecordDAO.getCurrentSeqNumber();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testRefreshOrderNo() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",2" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            incomeRecordDAO.refreshOrderNo( 2017, 10 );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
    public void testGetCount() throws IOException {
        int expect = 3;
        int actual = 0;
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            actual = incomeRecordDAO.getCount( 2017, 10 );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveUp() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",2" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 2 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 3 ) );
            
            incomeRecordDAO.moveUp( 2017, 10, 3 );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
    public void testMoveDown() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",2" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 2 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 3 ) );
            
            incomeRecordDAO.moveDown( 2017, 10, 2 );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
    public void testSort() throws IOException {
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        expectedDataList.add( "4,2017,10,1,\"測試帳4\",0,400,\"\",4" );
        expectedDataList.add( "2,2017,10,2,\"測試帳2\",0,200,\"\",2" );
        expectedDataList.add( "5,2017,10,2,\"測試帳5\",0,500,\"\",5" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 2, "測試帳2", 0, 200, "", 2 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 3 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳4", 0, 400, "", 4 ) );
        	incomeRecordDAO.insert( new IncomeRecord( 0, 2017, 10, 2, "測試帳5", 0, 500, "", 5 ) );
            
            incomeRecordDAO.sort( 2017, 10 );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( INCOME_RECORD_CSV_FILE_PATH ) ),
                    FILE_CHARSET
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
