package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;
import repository.TotalPropertyDAO;
import repository.impl.TotalPropertyDAOImpl;
import service.TotalPropertyService;
import service.impl.TotalPropertyServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class TotalPropertyServiceImplTests {
    
    private final String TOTAL_PROPERTY_CSV_FILE_PATH = 
        Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
    private final String TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP = "./data/TotalProperty/TotalProperty_backup.csv";
    private final String TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP = "./data/TotalProperty/TotalPropertySeq_backup.txt";
    
    private TotalPropertyService totalPropertyService;
    
    @Before
    public void setUp() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        totalPropertyService = null;
        restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
        restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        List<TotalProperty> expectDataList = new ArrayList<TotalProperty>();
        expectDataList.add( new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 ) );
        try {
            totalPropertyService.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            List<TotalProperty> actualDataList = totalPropertyService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, TotalPropertyUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        TotalProperty expect = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 );
        
        try {
            totalPropertyService.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            TotalProperty actual = totalPropertyService.findOne( 1 );
            assertTrue( TotalPropertyUtil.equals( expect, actual ) );
            
            assertNull( totalPropertyService.findOne( 2 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testGetMainTotalAmount() throws IOException {
        int expect1 = 0;
        int expect2 = 1000;
        
        try {
            int actual1 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect1, actual1 );
            
            totalPropertyService.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            int actual2 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testSetMainTotalAmount() throws IOException {
        int expect1 = 1000;
        int expect2 = 2000;
        
        try {
            totalPropertyService.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            int actual1 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect1, actual1 );
            
            totalPropertyService.setMainTotalAmount( 2000 );
            
            int actual2 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testAddToMainTotalAmount() {
        int expect1 = 1000;
        int expect2 = 3000;
        
        try {
            totalPropertyService.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            int actual1 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect1, actual1 );
            
            totalPropertyService.addToMainTotalAmount( 2000 );
            
            int actual2 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testUpdate() throws IOException {
        List<TotalProperty> expectDataList = new ArrayList<TotalProperty>();
        expectDataList.add( new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 2000 ) );
        
        try {
            totalPropertyService.insert( new TotalProperty( 0, 2017, 10, 1, 12, 0, 0, 1000 ) );
            
            TotalProperty modifiedData = new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 2000 );
            totalPropertyService.update( modifiedData );
            
            List<TotalProperty> actualDataList = totalPropertyService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, TotalPropertyUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testDelete() throws IOException {
        List<TotalProperty> expectDataList = new ArrayList<TotalProperty>();
        expectDataList.add( new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 ) );
        
        try {
            totalPropertyService.insert( new TotalProperty( 1, 2017, 10, 1, 12, 0, 0, 1000 ) );
            totalPropertyService.insert( new TotalProperty( 2, 2017, 10, 1, 12, 0, 0, 2000 ) );
            
            TotalProperty deletedData = new TotalProperty( 2, 2017, 10, 1, 12, 0, 0, 1000 );
            totalPropertyService.delete( deletedData );
            
            List<TotalProperty> actualDataList = totalPropertyService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, TotalPropertyUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
