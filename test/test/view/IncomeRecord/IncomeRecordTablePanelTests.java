package test.view.IncomeRecord;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;

import common.Contants;
import common.SystemInfo;
import domain.IncomeRecord;
import main.FundBook;
import main.FundBookServices;
import service.IncomeRecordService;
import service.impl.CheckRecordServiceImpl;
import service.impl.DigitalWalletServiceImpl;
import service.impl.FundingStatusHistoryServiceImpl;
import service.impl.FundingStatusServiceImpl;
import service.impl.IncomeRecordServiceImpl;
import service.impl.TotalPropertyServiceImpl;
import view.MainFrame;
import repository.CheckRecordDAO;
import repository.DigitalWalletDAO;
import repository.FundingStatusDAO;
import repository.FundingStatusHistoryDAO;
import repository.IncomeRecordDAO;
import repository.TotalPropertyDAO;
import repository.impl.CheckRecordDAOImpl;
import repository.impl.DigitalWalletDAOImpl;
import repository.impl.FundingStatusDAOImpl;
import repository.impl.FundingStatusHistoryDAOImpl;
import repository.impl.IncomeRecordDAOImpl;
import repository.impl.TotalPropertyDAOImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordTablePanelTests {
    
    private final int TAB_DELAY = 100;
    private final String INCOME_RECORD_CSV_FILE_PATH = "./data/IncomeRecord/2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "./data/IncomeRecord/2017.10_backup.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_2 = "./data/IncomeRecord/2018.01.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 = "./data/IncomeRecord/2018.01_backup.csv";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "./data/IncomeRecord/IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "./data/IncomeRecord/IncomeRecordSeq_backup.txt";
    private final String TOTAL_PROPERTY_CSV_FILE_PATH = Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
    private final String TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP = "./data/TotalProperty/TotalProperty_backup.csv";
    private final String TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP = "./data/TotalProperty/TotalPropertySeq_backup.txt";
    
    private FundBookServices fundBookServices;
    private IncomeRecordService incomeRecordService;
    
    private Calendar calendar;
    private int currentYear;
    private int currentMonth;
    
    private MainFrame mainFrame = null;
    
    @Before
    public void setUp() throws IOException, URISyntaxException {
        fundBookServices = defaultFundBookServices();
        incomeRecordService = fundBookServices.getIncomeRecordService();
        backupFile( getIncomeRecordCsvFilePathOfCurrentDay(), getIncomeRecordCsvFilePathBackupOfCurrentDay() );
        backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
        backupFile( INCOME_RECORD_CSV_FILE_PATH_2, INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 );
        backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
        backupFile( TOTAL_PROPERTY_CSV_FILE_PATH, TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.TOTAL_PROPERTY_SEQ_FILE_PATH, TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP );
        
        calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        currentYear = calendar.get( Calendar.YEAR );
        currentMonth = calendar.get( Calendar.MONTH ) + 1;
    }
    
    @After
    public void tearDown() throws IOException, InterruptedException {
        fundBookServices = null;
        incomeRecordService = null;
        restoreFile( TOTAL_PROPERTY_SEQ_FILE_PATH_BACKUP, Contants.TOTAL_PROPERTY_SEQ_FILE_PATH );
        restoreFile( TOTAL_PROPERTY_CSV_FILE_PATH_BACKUP, TOTAL_PROPERTY_CSV_FILE_PATH );
        restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP_2, INCOME_RECORD_CSV_FILE_PATH_2 );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        restoreFile( getIncomeRecordCsvFilePathBackupOfCurrentDay(), getIncomeRecordCsvFilePathOfCurrentDay() );
        if( mainFrame != null ) {
            mainFrame.dispose();
            Thread.sleep( 1000 );
        }
    }
    
    @Test
    public void testIncomeRecordTablePanelDisplay1() throws IOException {
        int testerSelection = 0;
        try {
            // 新增初始資料
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 1, "測試帳1", 0, 100, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 1, "測試帳2", 0, 200, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 1, "測試帳3", 0, 300, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 1, "測試帳4", 0, -400, "", 0 ) );
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            // 檢查執行結果
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            String expectDateString = String.format( "%04d.%02d.%02d", 
                calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1, 1 );
            
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                        "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>測試帳2</td><td>收</td><td>200</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>測試帳3</td><td>收</td><td>300</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>測試帳4</td><td>支</td><td>400</td></tr>" +
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
    public void testIncomeRecordTablePanelDisplay2() throws IOException {
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
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查執行結果
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                        "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                    "<tr><td>2017.10.02</td><td>測試帳2</td><td>收</td><td>200</td></tr>" + 
                    "<tr><td>2017.10.03</td><td>測試帳3</td><td>收</td><td>300</td></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td><td>...</td></tr>" + 
                    "<tr><td>2017.10.20</td><td>測試帳20</td><td>收</td><td>2000</td></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td><td>...</td></tr>" + 
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
    public void testIncomeRecordTablePanelDisplay3() throws IOException {
        int testerSelection = 0;
        try {
            // 新增初始資料
            for( int i = 1; i <= 30; i++ ) {
                IncomeRecord incomeRecord = new IncomeRecord( 0, 2017, 10, i, "測試帳" + i, 0, 100 * i, "", 0 );
                incomeRecordService.insert( incomeRecord );
            }
            for( int i = 1; i <= 5; i++ ) {
                IncomeRecord incomeRecord = new IncomeRecord( 0, 2018, 1, i, "測試帳" + i, 0, 100 * i, "", 0 );
                incomeRecordService.insert( incomeRecord );
            }
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 在月份選單選擇2017.10 (測試undo功能)
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "2018" );
            bot.keyPress( KeyEvent.VK_CONTROL );
            bot.keyPress( KeyEvent.VK_Z ); bot.keyRelease( KeyEvent.VK_Z ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_CONTROL );
            inputString( bot, "7" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查執行結果
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                        "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>2017.10.01</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                    "<tr><td>2017.10.02</td><td>測試帳2</td><td>收</td><td>200</td></tr>" + 
                    "<tr><td>2017.10.03</td><td>測試帳3</td><td>收</td><td>300</td></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td><td>...</td></tr>" + 
                    "<tr><td>2017.10.20</td><td>測試帳20</td><td>收</td><td>2000</td></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td><td>...</td></tr>" + 
                "</table>並在右側顯示垂直捲軸?</body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            
            // 在月份選單選擇2018.01
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查執行結果
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                        "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>2018.01.01</td><td>測試帳1</td><td>收</td><td>100</td></tr>" + 
                    "<tr><td>2018.01.02</td><td>測試帳2</td><td>收</td><td>200</td></tr>" + 
                    "<tr><td>2018.01.03</td><td>測試帳3</td><td>收</td><td>300</td></tr>" + 
                    "<tr><td>2018.01.04</td><td>測試帳4</td><td>收</td><td>400</td></tr>" + 
                    "<tr><td>2018.01.05</td><td>測試帳5</td><td>收</td><td>500</td></tr>" + 
                "</table>並隱藏右側的垂直捲軸?</body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    private String getIncomeRecordCsvFilePathOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        return String.format( "./data/IncomeRecord/%04d.%02d.csv", 
            calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1 );
    }
    
    private String getIncomeRecordCsvFilePathBackupOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        return String.format( "./data/IncomeRecord/%04d.%02d_backup.csv", 
            calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1 );
    }
    
    private FundBookServices defaultFundBookServices() throws URISyntaxException {
        // Initialize system information
        SystemInfo systemInfo = new SystemInfo(
            new File( FundBook.class.getProtectionDomain().getCodeSource().getLocation().toURI() ).getParent() );
        
        // Initialize DAOs
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl( systemInfo );
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl( systemInfo );
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl( systemInfo );
        FundingStatusHistoryDAO fundingStatusHistoryDAO = new FundingStatusHistoryDAOImpl( systemInfo );
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl( systemInfo );
        DigitalWalletDAO digitalWalletDAO = new DigitalWalletDAOImpl( systemInfo );
        
        // Initialize services
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( new IncomeRecordServiceImpl( incomeRecordDAO ) );
        fundBookServices.setTotalPropertyService( new TotalPropertyServiceImpl( totalPropertyDAO ) );
        fundBookServices.setFundingStatusService( new FundingStatusServiceImpl( fundingStatusDAO ) );
        fundBookServices.setFundingStatusHistoryService( new FundingStatusHistoryServiceImpl( fundingStatusHistoryDAO ) );
        fundBookServices.setCheckRecordService( new CheckRecordServiceImpl( checkRecordDAO ) );
        fundBookServices.setDigitalWalletService( new DigitalWalletServiceImpl( digitalWalletDAO ) );
        
        // Set services wired relation
        ((IncomeRecordServiceImpl)fundBookServices.getIncomeRecordService()).setTotalPropertyService(
                fundBookServices.getTotalPropertyService() );
        
        return fundBookServices;
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
