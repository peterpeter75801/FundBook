package test.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import junit.framework.TestCase;
import repository.IncomeRecordDAO;
import repository.impl.IncomeRecordDAOImpl;

public class IncomeRecordDAOImplTests extends TestCase {
    
    private final int INITIAL_SEQ_NUMBER = 1;
    private final String INCOME_RECORD_CSV_FILE_PATH = "data\\IncomeRecord\\2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "data\\IncomeRecord\\2017.10_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "data\\IncomeRecord\\IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "data\\IncomeRecord\\IncomeRecordSeq_backup.txt";
    private final String FILE_CHARSET = "big5";
    
    public void testInsert() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        String[] expectedData = {
                "1,2017,10,1,\"測試帳1\",0,100,\"\",0",
                "2,2017,10,1,\"測試帳2\",0,200,\"\",0",
                "3,2017,10,1,\"測試帳3\",0,300,\"\",0" };
        String[] actualData = new String[ 3 ];
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testFindOne() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecord expect = getTestData1();
        expect.setId( 2 );
        expect.setItem( "測試帳2" );
        expect.setAmount( 200 );
        IncomeRecord actual = null;
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
            actual = incomeRecordDAO.findOne( 2, getTestData1().getYear(), getTestData1().getMonth() );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
            
            assertNull( incomeRecordDAO.findOne( 4, getTestData1().getYear(), getTestData1().getMonth() ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testFindByMonth() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        List<IncomeRecord> expectedDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            incomeRecord.setId( i );
            incomeRecord.setItem( "測試帳" + i );
            incomeRecord.setAmount( 100 * i );
            expectedDataList.add( incomeRecord );
        }
        List<IncomeRecord> actualDataList = null;
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
            actualDataList = incomeRecordDAO.findByMonth( 2017, 10 );
            assertEquals( 3, actualDataList.size() );
            for( int i = 0; i < 3; i++ ) {
                assertTrue( IncomeRecordUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testUpdate() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",0" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,250,\"\",0" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",0" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
            IncomeRecord modifiedData = getTestData1();
            modifiedData.setId( 2 );
            modifiedData.setItem( "測試帳2" );
            modifiedData.setAmount( 250 );
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testDelete() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",0" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",0" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
            IncomeRecord deletedData = getTestData1();
            deletedData.setId( 1 );
            
            incomeRecordDAO.delete( deletedData, deletedData.getYear(), deletedData.getMonth() );
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testGetCurrentSeqNumber() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            // 測試初始情況
            int expect1 = INITIAL_SEQ_NUMBER - 1;
            int actual1 = incomeRecordDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = INITIAL_SEQ_NUMBER + 5;
            
            for( int i = 1; i <= 5; i++ ) {
                incomeRecordDAO.insert( getTestData1() );
            }
            
            int actual2 = incomeRecordDAO.getCurrentSeqNumber();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testRefreshOrderNo() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",2" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testGetCount() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        int expect = 3;
        int actual = 0;
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
            actual = incomeRecordDAO.getCount( 2017, 10 );
            assertEquals( expect, actual );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testMoveUp() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",2" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecord.setOrderNo( i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testMoveDown() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        expectedDataList.add( "2,2017,10,1,\"測試帳2\",0,200,\"\",2" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecord.setOrderNo( i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    public void testSort() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,\"測試帳1\",0,100,\"\",1" );
        expectedDataList.add( "3,2017,10,1,\"測試帳3\",0,300,\"\",3" );
        expectedDataList.add( "4,2017,10,1,\"測試帳4\",0,400,\"\",4" );
        expectedDataList.add( "2,2017,10,2,\"測試帳2\",0,200,\"\",2" );
        expectedDataList.add( "5,2017,10,2,\"測試帳5\",0,500,\"\",5" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 5; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                if( i == 2 || i == 5 ) {
                    incomeRecord.setDay( 2 );
                }
                incomeRecord.setItem( "測試帳" + i );
                incomeRecord.setAmount( 100 * i );
                incomeRecord.setOrderNo( i );
                incomeRecordDAO.insert( incomeRecord );
            }
            
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        }
    }
    
    private IncomeRecord getTestData1() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 100 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
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
