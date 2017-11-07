package test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import junit.framework.TestCase;

import repository.IncomeRecordDAO;
import repository.impl.IncomeRecordDAOImpl;
import service.IncomeRecordService;
import service.impl.IncomeRecordServiceImpl;

public class IncomeRecordServiceImplTests extends TestCase {
    
    private final String INCOME_RECORD_CSV_FILE_PATH = "data\\IncomeRecord\\2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "data\\IncomeRecord\\2017.10_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "data\\IncomeRecord\\IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "data\\IncomeRecord\\IncomeRecordSeq_backup.txt";
    
    public void testInsert() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            incomeRecord.setId( i );
            incomeRecord.setItem( getTestData1().getItem() + i );
            incomeRecord.setAmount( i * 100 );
            expectDataList.add( incomeRecord );
        }
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                incomeRecordService.insert( incomeRecord );
            }
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( getTestData1().getYear(), getTestData1().getMonth() );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        IncomeRecord expect = getTestData1();
        expect.setId( 2 );
        expect.setItem( getTestData1().getItem() + 2 );
        expect.setAmount( 200 );
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                incomeRecordService.insert( incomeRecord );
            }
            
            IncomeRecord actual = incomeRecordService.findOne( 2, getTestData1().getYear(), getTestData1().getMonth() );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
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
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            incomeRecord.setId( i );
            incomeRecord.setItem( getTestData1().getItem() + i );
            incomeRecord.setAmount( i * 100 );
            if( i == 3 ) {
                incomeRecord.setItem( "test123" );
            }
            expectDataList.add( incomeRecord );
        }
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                incomeRecordService.insert( incomeRecord );
            }
            
            IncomeRecord modifiedData = getTestData1();
            modifiedData.setId( 3 );
            modifiedData.setItem( "test123" );
            modifiedData.setAmount( 300 );
            incomeRecordService.update( modifiedData );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( getTestData1().getYear(), getTestData1().getMonth() );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            if( i == 2 || i == 3 ) {
                incomeRecord.setId( i );
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                expectDataList.add( incomeRecord );
            }
        }
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                incomeRecordService.insert( incomeRecord );
            }
            
            IncomeRecord deletedData = getTestData1();
            deletedData.setId( 1 );
            incomeRecordService.delete( deletedData );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( getTestData1().getYear(), getTestData1().getMonth() );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
        testData.setSubclass( '\0' );
        testData.setAmount( 100 );
        testData.setDescription( "" );
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
