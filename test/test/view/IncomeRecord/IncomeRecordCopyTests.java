package test.view.IncomeRecord;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

import common.Contants;
import common.SystemInfo;
import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
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
import service.IncomeRecordService;
import service.impl.CheckRecordServiceImpl;
import service.impl.DigitalWalletServiceImpl;
import service.impl.FundingStatusHistoryServiceImpl;
import service.impl.FundingStatusServiceImpl;
import service.impl.IncomeRecordServiceImpl;
import service.impl.TotalPropertyServiceImpl;
import view.MainFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordCopyTests {

    private final int TAB_DELAY = 100;
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
        restoreFile( getIncomeRecordCsvFilePathBackupOfCurrentDay(), getIncomeRecordCsvFilePathOfCurrentDay() );
        if( mainFrame != null ) {
            mainFrame.dispose();
            Thread.sleep( 1000 );
        }
    }
    
    @Test
    public void testCopyIncomeRecord() throws IOException {
        int testerSelection = 0;
        try {
            // 新增初始資料
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 1, "test item 1", 0, -100, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 2, "test item 2", 0, -200, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 3, "test item 3", 0, -300, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 4, "test item 4", 0, -400, "", 0 ) );
            incomeRecordService.insert( new IncomeRecord( 0, currentYear, currentMonth, 5, "test item 5", 0, -500, "", 0 ) );
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 選擇第三筆資料，點選"複製"按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有複製成功
            List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
            expectDataList.add( new IncomeRecord( 1, currentYear, currentMonth, 1, "test item 1", 0, -100, "", 1 ) );
            expectDataList.add( new IncomeRecord( 2, currentYear, currentMonth, 2, "test item 2", 0, -200, "", 2 ) );
            expectDataList.add( new IncomeRecord( 3, currentYear, currentMonth, 3, "test item 3", 0, -300, "", 3 ) );
            expectDataList.add( new IncomeRecord( 4, currentYear, currentMonth, 4, "test item 4", 0, -400, "", 4 ) );
            expectDataList.add( new IncomeRecord( 5, currentYear, currentMonth, 5, "test item 5", 0, -500, "", 5 ) );
            expectDataList.add( new IncomeRecord( 6, currentYear, currentMonth, 3, "test item 3", 0, -300, "", 6 ) );
            
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( currentYear, currentMonth );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面是否顯示正確
            String expectDateString = String.format( "%04d.%02d.", currentYear, currentMonth );
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString + "01" + "</td><td>test item 1</td><td>支</td><td>100</td></tr>" + 
                    "<tr><td>" + expectDateString + "02" + "</td><td>test item 2</td><td>支</td><td>600</td></tr>" + 
                    "<tr><td>" + expectDateString + "03" + "</td><td>test item 3</td><td>支</td><td>300</td></tr>" + 
                    "<tr><td>" + expectDateString + "04" + "</td><td>test item 4</td><td>支</td><td>400</td></tr>" + 
                    "<tr><td>" + expectDateString + "05" + "</td><td>test item 5</td><td>支</td><td>500</td></tr>" + 
                    "<tr><td>" + expectDateString + "03" + "</td><td>test item 3</td><td>支</td><td>300</td></tr>" + 
                "</table></body></html>", 
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
}
