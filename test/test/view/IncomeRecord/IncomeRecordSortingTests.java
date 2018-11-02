package test.view.IncomeRecord;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import main.FundBookServices;
import repository.impl.IncomeRecordDAOImpl;
import repository.impl.TotalPropertyDAOImpl;
import service.IncomeRecordService;
import service.TotalPropertyService;
import service.impl.IncomeRecordServiceImpl;
import service.impl.TotalPropertyServiceImpl;
import view.MainFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import common.Contants;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordSortingTests {
    
    private final String INCOME_RECORD_CSV_FILE_PATH = "./data/IncomeRecord/2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "./data/IncomeRecord/2017.10_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "./data/IncomeRecord/IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "./data/IncomeRecord/IncomeRecordSeq_backup.txt";
    private final String TOTAL_PROPERTY_CSV_FILE_PATH = Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
    private final String TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP = "./data/TotalProperty/TotalProperty_backup.csv";
    private final String TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP = "./data/TotalProperty/TotalPropertySeq_backup.txt";
    
    private FundBookServices fundBookServices;
    private IncomeRecordService incomeRecordService;
    private TotalPropertyService totalPropertyService;
    
    private MainFrame mainFrame = null;
    
    @Before
    public void setUp() throws IOException {
        incomeRecordService = new IncomeRecordServiceImpl( new IncomeRecordDAOImpl() );
        totalPropertyService = new TotalPropertyServiceImpl( new TotalPropertyDAOImpl() );
        ((IncomeRecordServiceImpl)incomeRecordService).setTotalPropertyService( totalPropertyService );
        fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( incomeRecordService );
        fundBookServices.setTotalPropertyService( totalPropertyService );
        
        backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
        backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
        backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException, InterruptedException {
        fundBookServices = null;
        incomeRecordService = null;
        totalPropertyService = null;
        
        restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
        restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        if( mainFrame != null ) {
            mainFrame.dispose();
            Thread.sleep( 1000 );
        }
    }
    
    @Test
    public void testMoveUp() throws IOException {
        int testerSelection = 0;
        try {
            // 新增初始資料
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳2", 0, 200, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 在月份選單選擇2017.10
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            Thread.sleep( 1000 );
            
            // 選擇第一筆資料，點選上移按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_UP ); bot.keyRelease( KeyEvent.VK_UP ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( 100 );
            Thread.sleep( 500 );
            
            // 選擇第三筆資料，點選上移按鈕
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( 100 );
            Thread.sleep( 500 );
            
            // 檢查執行結果
            List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
            expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
            expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 2 ) );
            expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "測試帳2", 0, 200, "", 3 ) );
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面顯示
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>顯示的收支記錄資料是否如下:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳3</td><td>收</td><td>300</td></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳2</td><td>收</td><td>200</td></tr>" + 
                "</table></body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
                    
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testMoveDown() throws IOException {
        int testerSelection = 0;
        try {
            // 新增初始資料
        	for( int i = 1; i <= 30; i++ ) {
                IncomeRecord incomeRecord = new IncomeRecord( 0, 2017, 10, i, "測試帳" + i, 0, 100 * i, "", 0 );
                incomeRecordService.insert( incomeRecord );
            }
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 在月份選單選擇2017.10
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            Thread.sleep( 1000 );
            
            // 選擇第一筆資料，點選下移按鈕30次
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_UP ); bot.keyRelease( KeyEvent.VK_UP ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            for( int i = 0; i < 30; i++ ) {
                bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( 100 );
            }
            Thread.sleep( 500 );
            
            // 檢查執行結果
            List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
            for( int i = 2; i <= 30; i++ ) {
                expectDataList.add( new IncomeRecord( i, 2017, 10, i, "測試帳" + i, 0, 100 * i, "", i - 1 ) );
            }
            expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 30 ) );
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面顯示
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>顯示的收支記錄資料是否如下:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td><td>...</td></tr>" + 
                    "<tr><td>2017.10.12</td><td>測試帳12</td><td>收</td><td>1200</td></tr>" + 
                    "<tr><td>2017.10.13</td><td>測試帳13</td><td>收</td><td>1300</td></tr>" + 
                    "<tr><td>2017.10.14</td><td>測試帳14</td><td>收</td><td>1400</td></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td><td>...</td></tr>" + 
                    "<tr><td>2017.10.30</td><td>測試帳30</td><td>收</td><td>3000</td></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                "</table>並在右側顯示垂直捲軸?</body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
                    
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    @Test
    public void testSort() throws IOException {
        int testerSelection = 0;
        try {
            // 新增初始資料
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳1", 0, 100, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 2, "測試帳2", 0, 200, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳3", 0, 300, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 1, "測試帳4", 0, 400, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, 2017, 10, 2, "測試帳5", 0, 500, "", 0 ) );
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 在月份選單選擇2017.10
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            Thread.sleep( 1000 );
            
            // 選擇排序按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( 100 );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( 100 );
            Thread.sleep( 500 );
            
            // 檢查執行結果
            List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
            expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "測試帳1", 0, 100, "", 1 ) );
            expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "測試帳3", 0, 300, "", 2 ) );
            expectDataList.add( new IncomeRecord( 4, 2017, 10, 1, "測試帳4", 0, 400, "", 3 ) );
            expectDataList.add( new IncomeRecord( 2, 2017, 10, 2, "測試帳2", 0, 200, "", 4 ) );
            expectDataList.add( new IncomeRecord( 5, 2017, 10, 2, "測試帳5", 0, 500, "", 5 ) );
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面顯示
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>顯示的收支記錄資料是否如下:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳3</td><td>收</td><td>300</td></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳4</td><td>收</td><td>400</td></tr>" + 
                    "<tr><td>2017.10.02</td><td>測試帳2</td><td>收</td><td>200</td></tr>" + 
                    "<tr><td>2017.10.02</td><td>測試帳5</td><td>收</td><td>500</td></tr>" + 
                "</table></body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
                    
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
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
    
    private void inputString( Robot bot, String s ) throws InterruptedException {
        HashMap<Character, Integer> charToKeyCodeMap = new HashMap<Character, Integer>();
        charToKeyCodeMap.put( 'a', KeyEvent.VK_A ); charToKeyCodeMap.put( 'A', KeyEvent.VK_A );
        charToKeyCodeMap.put( 'b', KeyEvent.VK_B ); charToKeyCodeMap.put( 'B', KeyEvent.VK_B );
        charToKeyCodeMap.put( 'c', KeyEvent.VK_C ); charToKeyCodeMap.put( 'C', KeyEvent.VK_C );
        charToKeyCodeMap.put( 'd', KeyEvent.VK_D ); charToKeyCodeMap.put( 'D', KeyEvent.VK_D );
        charToKeyCodeMap.put( 'e', KeyEvent.VK_E ); charToKeyCodeMap.put( 'E', KeyEvent.VK_E );
        charToKeyCodeMap.put( 'f', KeyEvent.VK_F ); charToKeyCodeMap.put( 'F', KeyEvent.VK_F );
        charToKeyCodeMap.put( 'g', KeyEvent.VK_G ); charToKeyCodeMap.put( 'G', KeyEvent.VK_G );
        charToKeyCodeMap.put( 'h', KeyEvent.VK_H ); charToKeyCodeMap.put( 'H', KeyEvent.VK_H );
        charToKeyCodeMap.put( 'i', KeyEvent.VK_I ); charToKeyCodeMap.put( 'I', KeyEvent.VK_I );
        charToKeyCodeMap.put( 'j', KeyEvent.VK_J ); charToKeyCodeMap.put( 'J', KeyEvent.VK_J );
        charToKeyCodeMap.put( 'k', KeyEvent.VK_K ); charToKeyCodeMap.put( 'K', KeyEvent.VK_K );
        charToKeyCodeMap.put( 'l', KeyEvent.VK_L ); charToKeyCodeMap.put( 'L', KeyEvent.VK_L );
        charToKeyCodeMap.put( 'm', KeyEvent.VK_M ); charToKeyCodeMap.put( 'M', KeyEvent.VK_M );
        charToKeyCodeMap.put( 'n', KeyEvent.VK_N ); charToKeyCodeMap.put( 'N', KeyEvent.VK_N );
        charToKeyCodeMap.put( 'o', KeyEvent.VK_O ); charToKeyCodeMap.put( 'O', KeyEvent.VK_O );
        charToKeyCodeMap.put( 'p', KeyEvent.VK_P ); charToKeyCodeMap.put( 'P', KeyEvent.VK_P );
        charToKeyCodeMap.put( 'q', KeyEvent.VK_Q ); charToKeyCodeMap.put( 'Q', KeyEvent.VK_Q );
        charToKeyCodeMap.put( 'r', KeyEvent.VK_R ); charToKeyCodeMap.put( 'R', KeyEvent.VK_R );
        charToKeyCodeMap.put( 's', KeyEvent.VK_S ); charToKeyCodeMap.put( 'S', KeyEvent.VK_S );
        charToKeyCodeMap.put( 't', KeyEvent.VK_T ); charToKeyCodeMap.put( 'T', KeyEvent.VK_T );
        charToKeyCodeMap.put( 'u', KeyEvent.VK_U ); charToKeyCodeMap.put( 'U', KeyEvent.VK_U );
        charToKeyCodeMap.put( 'v', KeyEvent.VK_V ); charToKeyCodeMap.put( 'V', KeyEvent.VK_V );
        charToKeyCodeMap.put( 'w', KeyEvent.VK_W ); charToKeyCodeMap.put( 'W', KeyEvent.VK_W );
        charToKeyCodeMap.put( 'x', KeyEvent.VK_X ); charToKeyCodeMap.put( 'X', KeyEvent.VK_X );
        charToKeyCodeMap.put( 'y', KeyEvent.VK_Y ); charToKeyCodeMap.put( 'Y', KeyEvent.VK_Y );
        charToKeyCodeMap.put( 'z', KeyEvent.VK_Z ); charToKeyCodeMap.put( 'Z', KeyEvent.VK_Z );
        charToKeyCodeMap.put( '1', KeyEvent.VK_1 ); charToKeyCodeMap.put( '!', KeyEvent.VK_1 );
        charToKeyCodeMap.put( '2', KeyEvent.VK_2 ); charToKeyCodeMap.put( '@', KeyEvent.VK_2 );
        charToKeyCodeMap.put( '3', KeyEvent.VK_3 ); charToKeyCodeMap.put( '#', KeyEvent.VK_3 );
        charToKeyCodeMap.put( '4', KeyEvent.VK_4 ); charToKeyCodeMap.put( '$', KeyEvent.VK_4 );
        charToKeyCodeMap.put( '5', KeyEvent.VK_5 ); charToKeyCodeMap.put( '%', KeyEvent.VK_5 );
        charToKeyCodeMap.put( '6', KeyEvent.VK_6 ); charToKeyCodeMap.put( '^', KeyEvent.VK_6 );
        charToKeyCodeMap.put( '7', KeyEvent.VK_7 ); charToKeyCodeMap.put( '&', KeyEvent.VK_7 );
        charToKeyCodeMap.put( '8', KeyEvent.VK_8 ); charToKeyCodeMap.put( '*', KeyEvent.VK_8 );
        charToKeyCodeMap.put( '9', KeyEvent.VK_9 ); charToKeyCodeMap.put( '(', KeyEvent.VK_9 );
        charToKeyCodeMap.put( '0', KeyEvent.VK_0 ); charToKeyCodeMap.put( ')', KeyEvent.VK_0 );
        charToKeyCodeMap.put( '-', KeyEvent.VK_MINUS ); charToKeyCodeMap.put( '_', KeyEvent.VK_MINUS );
        charToKeyCodeMap.put( '=', KeyEvent.VK_EQUALS ); charToKeyCodeMap.put( '+', KeyEvent.VK_EQUALS );
        charToKeyCodeMap.put( '[', KeyEvent.VK_OPEN_BRACKET ); charToKeyCodeMap.put( '{', KeyEvent.VK_OPEN_BRACKET );
        charToKeyCodeMap.put( ']', KeyEvent.VK_CLOSE_BRACKET ); charToKeyCodeMap.put( '}', KeyEvent.VK_CLOSE_BRACKET );
        charToKeyCodeMap.put( '\\', KeyEvent.VK_BACK_SLASH ); charToKeyCodeMap.put( '|', KeyEvent.VK_BACK_SLASH );
        charToKeyCodeMap.put( ';', KeyEvent.VK_SEMICOLON ); charToKeyCodeMap.put( ':', KeyEvent.VK_SEMICOLON );
        charToKeyCodeMap.put( '\'', KeyEvent.VK_QUOTE ); charToKeyCodeMap.put( '\"', KeyEvent.VK_QUOTE );
        charToKeyCodeMap.put( ',', KeyEvent.VK_COMMA ); charToKeyCodeMap.put( '<', KeyEvent.VK_COMMA );
        charToKeyCodeMap.put( '.', KeyEvent.VK_PERIOD ); charToKeyCodeMap.put( '>', KeyEvent.VK_PERIOD );
        charToKeyCodeMap.put( '/', KeyEvent.VK_SLASH ); charToKeyCodeMap.put( '?', KeyEvent.VK_SLASH );
        charToKeyCodeMap.put( '`', KeyEvent.VK_BACK_QUOTE ); charToKeyCodeMap.put( '~', KeyEvent.VK_BACK_QUOTE );
        charToKeyCodeMap.put( ' ', KeyEvent.VK_SPACE );
        ArrayList<Character> shiftPunctuationList = new ArrayList<Character>( 
                Arrays.asList( '~', '!', '@', '#', '$', '%', '^', '&', '*', 
                '(', ')', '_', '+', '{', '}', '|', ':', '\"', '<', '>', '?' ) );
        for( int i = 0; i < s.length(); i++ ) {
            if( Character.isUpperCase( s.charAt( i ) ) || 
                    shiftPunctuationList.indexOf( s.charAt( i ) ) >= 0 ) {
                bot.keyPress( KeyEvent.VK_SHIFT );
            }
            bot.keyPress( charToKeyCodeMap.get( s.charAt( i ) ) );
            bot.keyRelease( charToKeyCodeMap.get( s.charAt( i ) ) );
            Thread.sleep( 100 );
            if( Character.isUpperCase( s.charAt( i ) ) || 
                    shiftPunctuationList.indexOf( s.charAt( i ) ) >= 0 ) {
                bot.keyRelease( KeyEvent.VK_SHIFT );
            }
        }
    }
}
