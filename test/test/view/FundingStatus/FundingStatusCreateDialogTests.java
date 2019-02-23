package test.view.FundingStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import common.Contants;
import common.SystemInfo;
import commonUtil.FundingStatusHistoryUtil;
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import domain.FundingStatusHistory;
import main.FundBook;
import main.FundBookServices;
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
import service.impl.CheckRecordServiceImpl;
import service.impl.DigitalWalletServiceImpl;
import service.impl.FundingStatusHistoryServiceImpl;
import service.impl.FundingStatusServiceImpl;
import service.impl.IncomeRecordServiceImpl;
import service.impl.TotalPropertyServiceImpl;
import view.MainFrame;

@RunWith(value=JUnit4.class)
public class FundingStatusCreateDialogTests {
    
    private final int TAB_DELAY = 100;
    private final String FUNDING_STATUS_SEQ_FILE_PATH_BACKUP = "./data/FundingStatus/FundingStatusSeq_backup.txt";
    private final String FUNDING_STATUS_CSV_FILE_PATH = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
    private final String FUNDING_STATUS_CSV_FILE_PATH_BACKUP = Contants.FUNDING_STATUS_DATA_PATH + "FundingStatus_backup.csv";
    private final String FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP = "./data/FundingStatusHistory/FundingStatusHistorySeq_backup.txt";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH = "./data/FundingStatusHistory/1.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP = "./data/FundingStatusHistory/1_backup.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2 = "./data/FundingStatusHistory/2.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 = "./data/FundingStatusHistory/2_backup.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3 = "./data/FundingStatusHistory/3.csv";
    private final String FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 = "./data/FundingStatusHistory/3_backup.csv";
    
    private FundBookServices fundBookServices;
    
    private Calendar calendar;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMinute;
    private int currentSecond;
    
    private boolean numLockKeyStateBackup;
    
    private MainFrame mainFrame = null;
    
    @Before
    public void setUp() throws Exception {
        fundBookServices = defaultFundBookServices();
        backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 );
        backupFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 );
        backupFile( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH, FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP );
        
        
        calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        currentYear = calendar.get( Calendar.YEAR );
        currentMonth = calendar.get( Calendar.MONTH ) + 1;
        currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        currentHour = calendar.get( Calendar.HOUR_OF_DAY );
        currentMinute = calendar.get( Calendar.MINUTE );
        currentSecond = calendar.get( Calendar.SECOND );
        
        try {
            numLockKeyStateBackup = Toolkit.getDefaultToolkit().getLockingKeyState( KeyEvent.VK_NUM_LOCK );
            Toolkit.getDefaultToolkit().setLockingKeyState( KeyEvent.VK_NUM_LOCK, false );
        } catch( UnsupportedOperationException e ) {
            System.out.println( "Host system doesn't allow accessing the state of this key programmatically." );
        }
        
        // Initial default funding status data
        fundBookServices.getFundingStatusService().initialDefault();
    }
    
    @After
    public void tearDown() throws IOException, InterruptedException {
        fundBookServices = null;
        restoreFile( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH, FUNDING_STATUS_HISTORY_SEQ_FILE_PATH_BACKUP );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_3, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_3 );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH_2, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP_2 );
        restoreFile( FUNDING_STATUS_HISTORY_CSV_FILE_PATH, FUNDING_STATUS_HISTORY_CSV_FILE_PATH_BACKUP );
        restoreFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
        restoreFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        try {
            Toolkit.getDefaultToolkit().setLockingKeyState( KeyEvent.VK_NUM_LOCK, numLockKeyStateBackup );
        } catch( UnsupportedOperationException e ) {
            System.out.println( "Host system doesn't allow accessing the state of this key programmatically." );
        }
        if( mainFrame != null ) {
            mainFrame.dispose();
            Thread.sleep( 1000 );
        }
    }
    
    @Test
    public void testCreateFundingStatus() {
        int testerSelection = 0;
        try {
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            int isLinuxBasedOSSelection = JOptionPane.showOptionDialog(
                    mainFrame, "Is the OS linux based?", "Check", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
                    new String[]{UIManager.getString("OptionPane.yesButtonText"), UIManager.getString("OptionPane.noButtonText")}, 
                    UIManager.getString("OptionPane.noButtonText") );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 點選財務儲存狀況頁籤
            bot.keyPress( KeyEvent.VK_RIGHT ); bot.keyRelease( KeyEvent.VK_RIGHT ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 點選"新增"按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 新增資料 - 活存
            inputString( bot, "test funding status - Demand Deposit" );
            textCopyTesting( bot, isLinuxBasedOSSelection );    // 測試複製功能
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_D ); bot.keyRelease( KeyEvent.VK_D ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 點選"新增"按鈕
            bot.keyPress( KeyEvent.VK_C ); bot.keyRelease( KeyEvent.VK_C ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 新增資料 - 定存
            textPasteTesting( bot, isLinuxBasedOSSelection );   // 測試貼上功能
            inputString( bot, " - Time Deposit" );
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_T ); bot.keyRelease( KeyEvent.VK_T ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查財務儲存狀況資料是否有新增成功
            List<FundingStatus> expectFundingStatus = new ArrayList<FundingStatus>();
            expectFundingStatus.add( new FundingStatus( 1, '0', currentYear, currentMonth, currentDay, 
                    "預設可動用資金", 0, "", 1, false ) );
            expectFundingStatus.add( new FundingStatus( 2, 'D', currentYear, currentMonth, currentDay, 
                    "test funding status - Demand Deposit", 0, "", 2, false ) );
            expectFundingStatus.add( new FundingStatus( 3, 'T', currentYear, currentMonth, currentDay, 
                    "test funding status - Time Deposit", 0, "", 3, false ) );
            List<FundingStatus> actualFundingStatus = fundBookServices.getFundingStatusService().findAll();
            assertEquals( expectFundingStatus.size(), actualFundingStatus.size() );
            for( int i = 0; i < expectFundingStatus.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusUtil.equals( 
                        expectFundingStatus.get( i ), actualFundingStatus.get( i ) ) );
            }
            
            // 檢查財務儲存狀況歷史紀錄是否有新增成功
            List<FundingStatusHistory> expectFundingStatusHistory = new ArrayList<FundingStatusHistory>();
            expectFundingStatusHistory.add( new FundingStatusHistory( 1, 1, currentYear, currentMonth, currentDay, 
                    currentHour, currentMinute, currentSecond, 'C', 0, 0, 0, Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION ) );
            expectFundingStatusHistory.add( new FundingStatusHistory( 2, 2, currentYear, currentMonth, currentDay, 
                    currentHour, currentMinute, currentSecond, 'C', 0, 0, 0, "" ) );
            expectFundingStatusHistory.add( new FundingStatusHistory( 3, 3, currentYear, currentMonth, currentDay, 
                    currentHour, currentMinute, currentSecond, 'C', 0, 0, 0, "" ) );
            List<FundingStatusHistory> actualFundingStatusHistory = fundBookServices.getFundingStatusHistoryService().findAll();
            assertEquals( expectFundingStatusHistory.size(), actualFundingStatusHistory.size() );
            for( int i = 0; i < expectFundingStatusHistory.size(); i++ ) {
                assertTrue( "failed at i = " + i, FundingStatusHistoryUtil.equalsIgnoreTime( 
                        expectFundingStatusHistory.get( i ), actualFundingStatusHistory.get( i ) ) );
            }
            
            // 檢查畫面是否顯示正確
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的財務儲存狀況資料:</p><table>" + 
                    "<tr><th>種類</th><th>儲存地點/儲存機構</th><th>金額</th></tr>" + 
                    "<tr><td>預設</td><td>預設可動用資金</td><td>0</td></tr>" + 
                    "<tr><td>活存</td><td>test funding status - Demand Deposit</td><td>0</td></tr>" + 
                    "<tr><td>定存</td><td>test funding status - Time Deposit</td><td>0</td></tr>" + 
                "</table></body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            e.printStackTrace();
            assertTrue( e.getMessage(), false );
        }
    }
    
    private void textCopyTesting( Robot bot, int isLinuxBasedOSSelection ) throws InterruptedException {
        // 選取前19位的字元(test funding status)
        bot.keyPress( KeyEvent.VK_HOME ); bot.keyRelease( KeyEvent.VK_HOME ); Thread.sleep( TAB_DELAY );
        bot.keyPress( KeyEvent.VK_SHIFT );
        for( int i = 0; i < 19; i++ ) {
            bot.keyPress( KeyEvent.VK_RIGHT ); bot.keyRelease( KeyEvent.VK_RIGHT ); Thread.sleep( TAB_DELAY );
        }
        bot.keyRelease( KeyEvent.VK_SHIFT );
        
        // 顯示複製貼上選單
        if( isLinuxBasedOSSelection == JOptionPane.YES_OPTION ) {
            JOptionPane.showMessageDialog( mainFrame, "Please press context menu key in 3 seconds after closing this dialog.", 
                    "Message", JOptionPane.INFORMATION_MESSAGE );
            Thread.sleep( 3000 );
        } else {
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( TAB_DELAY );
        }
        Thread.sleep( 1000 );
        
        // 選擇"複製"功能
        bot.keyPress( KeyEvent.VK_C ); bot.keyRelease( KeyEvent.VK_C ); Thread.sleep( TAB_DELAY );
        Thread.sleep( 1000 );
    }
    
    private void textPasteTesting( Robot bot, int isLinuxBasedOSSelection ) throws InterruptedException {
        // 顯示複製貼上選單
        if( isLinuxBasedOSSelection == JOptionPane.YES_OPTION ) {
            JOptionPane.showMessageDialog( mainFrame, "Please press context menu key in 3 seconds after closing this dialog.", 
                    "Message", JOptionPane.INFORMATION_MESSAGE );
            Thread.sleep( 3000 );
        } else {
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( TAB_DELAY );
        }
        Thread.sleep( 1000 );
        
        // 選擇"貼上"功能，貼上複製的字串(test funding status)
        bot.keyPress( KeyEvent.VK_P ); bot.keyRelease( KeyEvent.VK_P ); Thread.sleep( TAB_DELAY );
        Thread.sleep( 1000 );
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
        ((FundingStatusServiceImpl)fundBookServices.getFundingStatusService()).setFundingStatusHistoryService(
                fundBookServices.getFundingStatusHistoryService() );
        
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
