package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Contants;
import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;

import junit.framework.TestCase;
import repository.TotalPropertyDAO;
import repository.impl.TotalPropertyDAOImpl;
import service.TotalPropertyService;
import service.impl.TotalPropertyServiceImpl;

public class TotalPropertyServiceImplTests extends TestCase {
    
    private final String TOTAL_PROPERTY_CSV_FILE_PATH = 
        Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
    private final String TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP = "data\\TotalProperty\\TotalProperty_backup.csv";
    private final String TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP = "data\\TotalProperty\\TotalPropertySeq_backup.txt";
    
    public void testInsert() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        TotalPropertyService totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        List<TotalProperty> expectDataList = new ArrayList<TotalProperty>();
        expectDataList.add( getTestData1() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 1 );
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyService.insert( getTestData1() );
            
            List<TotalProperty> actualDataList = totalPropertyService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, TotalPropertyUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
        TotalPropertyService totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        TotalProperty expect = getTestData1();
        expect.setId( 1 );
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyService.insert( getTestData1() );
            
            TotalProperty actual = totalPropertyService.findOne( 1 );
            assertTrue( TotalPropertyUtil.equals( expect, actual ) );
            
            assertNull( totalPropertyService.findOne( 2 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testGetMainTotalAmount() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        TotalPropertyService totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        int expect1 = 0;
        int expect2 = 1000;
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            int actual1 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect1, actual1 );
            
            totalPropertyService.insert( getTestData1() );
            
            int actual2 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect2, actual2 );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
            restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        }
    }
    
    public void testSetMainTotalAmount() throws IOException {
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        TotalPropertyService totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        int expect1 = 1000;
        int expect2 = 2000;
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            totalPropertyService.insert( getTestData1() );
            
            int actual1 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect1, actual1 );
            
            totalPropertyService.setMainTotalAmount( 2000 );
            
            int actual2 = totalPropertyService.getMainTotalAmount();
            assertEquals( expect2, actual2 );
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
        TotalPropertyService totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        List<TotalProperty> expectDataList = new ArrayList<TotalProperty>();
        expectDataList.add( getTestData1() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 1 );
        expectDataList.get( expectDataList.size() - 1 ).setTotalAmount( 2000 );
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            
            totalPropertyService.insert( getTestData1() );
            
            TotalProperty modifiedData = getTestData1();
            modifiedData.setId( 1 );
            modifiedData.setTotalAmount( 2000 );
            totalPropertyService.update( modifiedData );
            
            List<TotalProperty> actualDataList = totalPropertyService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, TotalPropertyUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
        TotalPropertyService totalPropertyService = new TotalPropertyServiceImpl( totalPropertyDAO );
        
        List<TotalProperty> expectDataList = new ArrayList<TotalProperty>();
        expectDataList.add( getTestData1() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 1 );
        
        try {
            backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
            backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 2; i++ ) {
                TotalProperty totalProperty = getTestData1();
                totalProperty.setId( i );
                totalProperty.setTotalAmount( i * 1000 );
                totalPropertyService.insert( totalProperty );
            }
            
            TotalProperty deletedData = getTestData1();
            deletedData.setId( 2 );
            totalPropertyService.delete( deletedData );
            
            List<TotalProperty> actualDataList = totalPropertyService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, TotalPropertyUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
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
