package test.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import common.Contants;
import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;
import repository.TotalPropertyDAO;
import repository.impl.TotalPropertyDAOImpl;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class TotalPropertyDAOImplTests {

    private final String TOTAL_PROPERTY_CSV_FILE_PATH = 
        Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
    private final String TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP = "./data/TotalProperty/TotalProperty_backup.csv";
    private final String TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP = "./data/TotalProperty/TotalPropertySeq_backup.txt";
    
    @Before
    public void setUp() throws IOException {
    	backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
    	restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
        restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
    }
    
    @Test
    public void testCreate() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        String[] expectedData = { "1,2017,10,1,12,0,0,1000" };
        String[] actualData = new String[ 1 ];
        
        try {
            TotalProperty input = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
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
        }
    }
    
    @Test
    public void testInsert() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        String[] expectedData = { "1,2017,10,1,12,0,0,1000" };
        String[] actualData = new String[ 1 ];
        try {
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
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
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        TotalProperty expect = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        TotalProperty actual = null;
        try {
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            actual = totalPropertyDAO.findOne( 1 );
            assertTrue( TotalPropertyUtil.equals( expect, actual ) );
            
            assertNull( totalPropertyDAO.findOne( 2 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testFindAll() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        List<TotalProperty> expectedDataList = new ArrayList<TotalProperty>();
        expectedDataList.add( new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 ) );
        List<TotalProperty> actualDataList = null;
        
        try {
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            actualDataList = totalPropertyDAO.findAll();
            assertEquals( 1, actualDataList.size() );
            for( int i = 0; i < 1; i++ ) {
                assertTrue( TotalPropertyUtil.equals( expectedDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testUpdate() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,0,0,2000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            TotalProperty modifiedData = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 2000 );
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
        }
    }

    @Test
    public void testDelete() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        
        List<String> expectedDataList = new ArrayList<String>();
        expectedDataList.add( "1,2017,10,1,12,0,0,1000" );
        List<String> actualDataList = new ArrayList<String>();
        
        try {
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 2, 12, 0, 0, 2000 ) );
            
            TotalProperty deletedData = new TotalProperty( 2, 2017, 10, 1, 12, 0, 0, 1000 );
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
        }
    }

    @Test
    public void testGetCurrentSeqNumber() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        
        try {
            // 測試初始情況
            int expect1 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
            int actual1 = totalPropertyDAO.getCurrentSeqNumber();
            assertEquals( expect1, actual1 );
            
            // 測試新增資料後的情況
            int expect2 = Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) + 1;
            totalPropertyDAO.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            int actual2 = totalPropertyDAO.getCurrentSeqNumber();
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
