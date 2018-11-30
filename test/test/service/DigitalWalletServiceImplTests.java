package test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import common.Contants;
import commonUtil.DigitalWalletUtil;
import domain.DigitalWallet;
import repository.DigitalWalletDAO;
import repository.impl.DigitalWalletDAOImpl;
import service.DigitalWalletService;
import service.impl.DigitalWalletServiceImpl;

@RunWith(value=JUnit4.class)
public class DigitalWalletServiceImplTests {
    
    private final String DIGITAL_WALLET_CSV_FILE_PATH = 
            Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
    private final String DIGITAL_WALLET_CSV_FILE_PATH_BACKUP = "./data/DigitalWallet/DigitalWallet_backup.csv";
    private final String DIGITAL_WALLET_SEQ_FILE_PATH_BACKUP = "./data/DigitalWallet/DigitalWalletSeq_backup.txt";
    
    private DigitalWalletService digitalWalletService;
    
    @Before
    public void setUp() throws IOException {
        DigitalWalletDAO digitalWalletDAO = new DigitalWalletDAOImpl();
        digitalWalletService = new DigitalWalletServiceImpl( digitalWalletDAO );
        
        backupFile( DIGITAL_WALLET_CSV_FILE_PATH, DIGITAL_WALLET_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.DIGITAL_WALLET_SEQ_FILE_PATH, DIGITAL_WALLET_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException {
        digitalWalletService = null;
        restoreFile( DIGITAL_WALLET_SEQ_FILE_PATH_BACKUP, Contants.DIGITAL_WALLET_SEQ_FILE_PATH );
        restoreFile( DIGITAL_WALLET_CSV_FILE_PATH_BACKUP, DIGITAL_WALLET_CSV_FILE_PATH );
    }
    
    @Test
    public void testInsert() throws IOException {
        List<DigitalWallet> expectDataList = new ArrayList<DigitalWallet>();
        expectDataList.add( new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
        expectDataList.add( new DigitalWallet( 2, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
        expectDataList.add( new DigitalWallet( 3, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
        
        try {
            digitalWalletService.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            List<DigitalWallet> actualDataList = digitalWalletService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, DigitalWalletUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testFindOne() throws IOException {
        DigitalWallet expect = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
        
        try {
            digitalWalletService.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            DigitalWallet actual = digitalWalletService.findOne( 1 );
            assertTrue( DigitalWalletUtil.equals( expect, actual ) );
            
            assertNull( digitalWalletService.findOne( 6 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testUpdate() throws IOException {
        List<DigitalWallet> expectDataList = new ArrayList<DigitalWallet>();
        expectDataList.add( new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
        expectDataList.add( new DigitalWallet( 2, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
        expectDataList.add( new DigitalWallet( 3, "LINE Pay", 2017, 11, 28, "LINE Corporation", 600, "" ) );
        
        try {
            digitalWalletService.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            DigitalWallet modifiedData = new DigitalWallet( 3, "LINE Pay", 2017, 11, 28, "LINE Corporation", 600, "" );
            digitalWalletService.update( modifiedData );
            
            List<DigitalWallet> actualDataList = digitalWalletService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, DigitalWalletUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
        } catch( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testDelete() throws IOException {
        List<DigitalWallet> expectDataList = new ArrayList<DigitalWallet>();
        expectDataList.add( new DigitalWallet( 2, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
        expectDataList.add( new DigitalWallet( 3, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
        
        try {
            digitalWalletService.insert( new DigitalWallet( 0, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "Google Pay", 2017, 12, 1, "Google", 1000, "" ) );
            digitalWalletService.insert( new DigitalWallet( 0, "LINE Pay", 2017, 11, 28, "LINE Corporation", 500, "" ) );
            
            DigitalWallet deletedData = new DigitalWallet( 1, "悠遊卡", 2017, 12, 30, "台北捷運公司", 1000, "" );
            digitalWalletService.delete( deletedData );
            
            List<DigitalWallet> actualDataList = digitalWalletService.findAll();
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, DigitalWalletUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
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
