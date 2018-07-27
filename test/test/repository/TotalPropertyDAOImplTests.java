package test.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;
import repository.TotalPropertyDAO;
import repository.impl.TotalPropertyDAOImpl;

import junit.framework.TestCase;

public class TotalPropertyDAOImplTests extends TestCase {

    private final String TOTAL_PROPERTY_CSV_FILE_PATH = 
        Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
    private final String TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP = "./data/TotalProperty/TotalProperty_backup.csv";
    private final String TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP = "./data/TotalProperty/TotalPropertySeq_backup.txt";
    
    public void testCreate() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        String[] expectedData = { "1,2017,10,1,12,0,0,1000" };
        String[] actualData = new String[ 1 ];
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            
            TotalProperty input = getTestData1();
            input.setId( 1 );
            totalPropertyDAO.create( input );
            totalPropertyDAO.create( input );   // test create again
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( TOTAL_PROPERTY_CSV_FILE_PATH ) ),
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
            
            assertEquals( 1, i );
            for( i = 0; i < 1; i++ ) {
                assertEquals( "failed at i = " + i, expectedData[ i ], actualData[ i ] );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testInsert() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        String[] expectedData = { "1,2017,10,1,12,0,0,1000" };
        String[] actualData = new String[ 1 ];
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyDAO.insert( getTestData1() );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( TOTAL_PROPERTY_CSV_FILE_PATH ) ),
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
            
            assertEquals( 1, i );
            for( i = 0; i < 1; i++ ) {
                assertEquals( "failed at i = " + i, expectedData[ i ], actualData[ i ] );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testFindOne() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        TotalProperty expect = getTestData1();
        expect.setId( 1 );
        TotalProperty actual = null;
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyDAO.insert( getTestData1() );
            
            actual = totalPropertyDAO.findOne( 1 );
            assertTrue( TotalPropertyUtil.equals( expect, actual ) );
            
            assertNull( totalPropertyDAO.findOne( 2 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testFindAll() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        List<TotalProperty> expectedDataList = new ArrayList<TotalProperty>();
        expectedDataList.add( getTestData1() );
        expectedDataList.get( expectedDataList.size() - 1 ).setId( 1 );
        List<TotalProperty> actualDataList = null;
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyDAO.insert( getTestData1() );
            
            actualDataList = totalPropertyDAO.findAll();
            assertEquals( 1, actualDataList.size() );
            for( int i = 0; i < 1; i++ ) {
                assertTrue( TotalPropertyUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testUpdate() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,0,0,2000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyDAO.insert( getTestData1() );
            
            TotalProperty modifiedData = getTestData1();
            modifiedData.setId( 1 );
            modifiedData.setTotalAmount( 2000 );
            
            totalPropertyDAO.update( modifiedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( TOTAL_PROPERTY_CSV_FILE_PATH ) ),
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
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testDelete() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,0,0,1000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 2; i++ ) {
                TotalProperty totalProperty = getTestData1();
                totalProperty.setDay( i );
                totalProperty.setTotalAmount( 1000 * i );
                totalPropertyDAO.insert( totalProperty );
            }
            
            TotalProperty deletedData = getTestData1();
            deletedData.setId( 2 );
            
            totalPropertyDAO.delete( deletedData );
            
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( TOTAL_PROPERTY_CSV_FILE_PATH ) ),
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
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testGetCurrentSeqNumber() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = totalPropertyDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 1;
            totalPropertyDAO.insert( getTestData1() );
            
            int actual2 = totalPropertyDAO.getCurrentSeqNumber();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    private TotalProperty getTestData1() {
        TotalProperty testData = new TotalProperty();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setHour( 12 );
        testData.setMinute( 0 );
        testData.setSecond( 0 );
        testData.setTotalAmount( 1000 );
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
