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
    
    private final String INCOME_RECORD_CSV_FILE_PATH = "./data/IncomeRecord/2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "./data/IncomeRecord/2017.10_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "./data/IncomeRecord/IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "./data/IncomeRecord/IncomeRecordSeq_backup.txt";
    
    public void testInsert() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            incomeRecord.setId( i );
            incomeRecord.setItem( getTestData1().getItem() + i );
            incomeRecord.setAmount( i * 100 );
            incomeRecord.setOrderNo( i );
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
        expect.setOrderNo( 2 );
        
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
            incomeRecord.setOrderNo( i );
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
            incomeRecordService.update( modifiedData, getTestData1().getYear(), getTestData1().getMonth() );
            
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
    
    public void testUpdateDifferentMonth() throws IOException {
        final String INCOME_RECORD_CSV_FILE_PATH_2 = "./data/IncomeRecord/2017.12.csv";
        final String INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 = "./data/IncomeRecord/2017.12_backup.csv";
        
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList1 = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 2; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            incomeRecord.setId( i );
            incomeRecord.setItem( getTestData1().getItem() + i );
            incomeRecord.setAmount( i * 100 );
            incomeRecord.setOrderNo( i );
            expectDataList1.add( incomeRecord );
        }
        List<IncomeRecord> expectDataList2 = new ArrayList<IncomeRecord>();
        for( int i = 3; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            incomeRecord.setId( i );
            incomeRecord.setMonth( 12 );
            incomeRecord.setItem( getTestData1().getItem() + i );
            incomeRecord.setAmount( i * 100 );
            incomeRecord.setItem( "test123" );
            incomeRecord.setOrderNo( 1 );
            expectDataList2.add( incomeRecord );
        }
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_CSV_FILE_PATH_2, INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 3; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                incomeRecordService.insert( incomeRecord );
            }
            
            IncomeRecord modifiedData = getTestData1();
            modifiedData.setId( 3 );
            modifiedData.setMonth( 12 );
            modifiedData.setItem( "test123" );
            modifiedData.setAmount( 300 );
            incomeRecordService.update( modifiedData, getTestData1().getYear(), getTestData1().getMonth() );
            
            List<IncomeRecord> actualDataList1 = incomeRecordService.findByMonth( getTestData1().getYear(), getTestData1().getMonth() );
            assertEquals( expectDataList1.size(), actualDataList1.size() );
            for( int i = 0; i < expectDataList1.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList1.get( i ), actualDataList1.get( i ) ) );
            }
            List<IncomeRecord> actualDataList2 = incomeRecordService.findByMonth( getTestData1().getYear(), 12 );
            assertEquals( expectDataList2.size(), actualDataList2.size() );
            for( int i = 0; i < expectDataList2.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList2.get( i ), actualDataList2.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP_2, INCOME_RECORD_CSV_FILE_PATH_2 );
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
                incomeRecord.setOrderNo( i - 1 );
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
    
    public void testMoveUp() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 5; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            if( i == 3 ) {
                incomeRecord.setId( 5 );
                incomeRecord.setItem( getTestData1().getItem() + 5 );
                incomeRecord.setAmount( 500 );
            } else if( i == 4 || i == 5 ) {
                incomeRecord.setId( i - 1 );
                incomeRecord.setItem( getTestData1().getItem() + (i - 1) );
                incomeRecord.setAmount( (i - 1) * 100 );
            } else {
                incomeRecord.setId( i );
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
            }
            incomeRecord.setOrderNo( i );
            expectDataList.add( incomeRecord );
        }
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            for( int i = 1; i <= 5; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
                incomeRecordService.insert( incomeRecord );
            }
            
            incomeRecordService.moveUp( 2017, 10, 1 );
            incomeRecordService.moveUp( 2017, 10, 5 );
            incomeRecordService.moveUp( 2017, 10, 4 );
            
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
    
    public void testMoveDown() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        for( int i = 1; i <= 3; i++ ) {
            IncomeRecord incomeRecord = getTestData1();
            if( i == 1 ) {
                incomeRecord.setId( 2 );
                incomeRecord.setItem( getTestData1().getItem() + 2 );
                incomeRecord.setAmount( 2 * 100 );
            } else if( i == 2 ) {
                incomeRecord.setId( 1 );
                incomeRecord.setItem( getTestData1().getItem() + 1 );
                incomeRecord.setAmount( 1 * 100 );
            } else {
                incomeRecord.setId( i );
                incomeRecord.setItem( getTestData1().getItem() + i );
                incomeRecord.setAmount( i * 100 );
            }
            incomeRecord.setOrderNo( i );
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
            
            incomeRecordService.moveDown( 2017, 10, 1 );
            
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
    
    public void testSort() throws IOException {
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( incomeRecordDAO );
        
        List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
        expectDataList.add( getTestData1() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 1 );
        expectDataList.add( getTestData3() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 3 );
        expectDataList.add( getTestData4() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 4 );
        expectDataList.add( getTestData2() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 2 );
        expectDataList.add( getTestData5() );
        expectDataList.get( expectDataList.size() - 1 ).setId( 5 );
        for( int i = 0; i < expectDataList.size(); i++ ) {
            expectDataList.get( i ).setOrderNo( i + 1 );
        }
        
        try {
            backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            incomeRecordService.insert( getTestData1() );
            incomeRecordService.insert( getTestData2() );
            incomeRecordService.insert( getTestData3() );
            incomeRecordService.insert( getTestData4() );
            incomeRecordService.insert( getTestData5() );
            
            incomeRecordService.sort( 2017, 10 );
            
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
        testData.setClassNo( 0 );
        testData.setAmount( 100 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData2() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 2 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 200 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData3() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 300 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData4() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 1 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 400 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private IncomeRecord getTestData5() {
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 0 );
        testData.setYear( 2017 );
        testData.setMonth( 10 );
        testData.setDay( 2 );
        testData.setItem( "測試帳" );
        testData.setClassNo( 0 );
        testData.setAmount( 500 );
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
