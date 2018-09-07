package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import repository.IncomeRecordDAO;
import repository.impl.IncomeRecordDAOImpl;
import service.IncomeRecordService;
import service.impl.IncomeRecordServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordServiceImplTests {
    
    private final String INCOME_RECORD_CSV_FILE_PATH = "./data/IncomeRecord/2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "./data/IncomeRecord/2017.10_backup.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_2 = "./data/IncomeRecord/2017.12.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 = "./data/IncomeRecord/2017.12_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "./data/IncomeRecord/IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "./data/IncomeRecord/IncomeRecordSeq_backup.txt";
    
    @Before
    public void setUp() throws IOException {
        backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
        backupFile( INCOME_RECORD_CSV_FILE_PATH_2, INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 );
        backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
    	restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP_2, INCOME_RECORD_CSV_FILE_PATH_2 );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 2 ) );
        expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 3 ) );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        IncomeRecord expect = new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 2 );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord actual = incomeRecordService.findOne( 2, 2017, 10 );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 2 ) );
        expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "test123", 0, 300, "", 3 ) );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord modifiedData = new IncomeRecord( 3, 2017, 10, 1, "test123", 0, 300, "", 0 );
            incomeRecordService.update( modifiedData, 2017, 10 );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testUpdateDifferentMonth() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList1 = new ArrayList<IncomeRecord>();
        expectDataList1.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        expectDataList1.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 2 ) );
        List<IncomeRecord> expectDataList2 = new ArrayList<IncomeRecord>();
        expectDataList2.add( new IncomeRecord( 3, 2017, 12, 1, "test123", 0, 300, "", 1 ) );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord modifiedData = new IncomeRecord( 3, 2017, 12, 1, "test123", 0, 300, "", 0 );
            incomeRecordService.update( modifiedData, 2017, 10 );
            
            List<IncomeRecord> actualDataList1 = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList1.size(), actualDataList1.size() );
            for( int i = 0; i < expectDataList1.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList1.get( i ), actualDataList1.get( i ) ) );
            }
            List<IncomeRecord> actualDataList2 = incomeRecordService.findByMonth( 2017, 12 );
            assertEquals( expectDataList2.size(), actualDataList2.size() );
            for( int i = 0; i < expectDataList2.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList2.get( i ), actualDataList2.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testDelete() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 1 ) );
        expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 2 ) );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            IncomeRecord deletedData = new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 0 );
            incomeRecordService.delete( deletedData );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveUp() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
        expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 2 ) );
        expectDataList.add( new IncomeRecord( 5, 2017, 10, 1, "測試帳5", 0, 500, "", 3 ) );
        expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 4 ) );
        expectDataList.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳4", 0, 400, "", 5 ) );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳4", 0, 400, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳5", 0, 500, "", 0 ) );
            
            incomeRecordService.moveUp( 2017, 10, 1 );
            incomeRecordService.moveUp( 2017, 10, 5 );
            incomeRecordService.moveUp( 2017, 10, 4 );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testMoveDown() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 1 ) );
        expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 2 ) );
        expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 3 ) );
        
        try {
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
        	incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            incomeRecordService.moveDown( 2017, 10, 1 );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }

    @Test
    public void testSort() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳", 0, 100, "", 1 ) );
        expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳", 0, 300, "", 2 ) );
        expectDataList.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳", 0, 400, "", 3 ) );
        expectDataList.add( new IncomeRecord( 2, 2017, 10, 2, "測試帳", 0, 200, "", 4 ) );
        expectDataList.add( new IncomeRecord( 5, 2017, 10, 2, "測試帳", 0, 500, "", 5 ) );
        
        try {
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳", 0, 100, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 2, "測試帳", 0, 200, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳", 0, 300, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳", 0, 400, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 2, "測試帳", 0, 500, "", 0 ) );
            
            incomeRecordService.sort( 2017, 10 );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
