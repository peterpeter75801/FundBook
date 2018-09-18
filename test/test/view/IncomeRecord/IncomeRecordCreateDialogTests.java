package test.view.IncomeRecord;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;

import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import main.FundBookServices;
import repository.impl.IncomeRecordDAOImpl;
import service.IncomeRecordService;
import service.impl.IncomeRecordServiceImpl;
import view.MainFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(value=JUnit4.class)
public class IncomeRecordCreateDialogTests {
    
    private final int TAB_DELAY = 100;
    private final String INCOME_RECORD_SEQ_FILE_PATH = "./data/IncomeRecord/IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "./data/IncomeRecord/IncomeRecordSeq_backup.txt";
    private final String INCOME_RECORD_CSV_FILE_PATH = "./data/IncomeRecord/2017.10.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP = "./data/IncomeRecord/2017.10_backup.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_2 = "./data/IncomeRecord/2018.01.csv";
    private final String INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 = "./data/IncomeRecord/2018.01_backup.csv";
    
    private Calendar calendar;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    
    @Before
    public void setUp() throws IOException {
        backupFile( getIncomeRecordCsvFilePathOfCurrentDay(), getIncomeRecordCsvFilePathBackupOfCurrentDay() );
        backupFile( INCOME_RECORD_CSV_FILE_PATH, INCOME_RECORD_CSV_FILE_PATH_BACKUP );
        backupFile( INCOME_RECORD_CSV_FILE_PATH_2, INCOME_RECORD_CSV_FILE_PATH_BACKUP_2 );
        backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );

        calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        currentYear = calendar.get( Calendar.YEAR );
        currentMonth = calendar.get( Calendar.MONTH ) + 1;
        currentDay = calendar.get( Calendar.DAY_OF_MONTH );
    }
    
    @After
    public void tearDown() throws IOException {
        restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP_2, INCOME_RECORD_CSV_FILE_PATH_2 );
        restoreFile( INCOME_RECORD_CSV_FILE_PATH_BACKUP, INCOME_RECORD_CSV_FILE_PATH );
        restoreFile( getIncomeRecordCsvFilePathBackupOfCurrentDay(), getIncomeRecordCsvFilePathOfCurrentDay() );
    }
    
    @Test
    public void testCreateIncomeRecord() throws IOException {
        int testerSelection = 0;
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( new IncomeRecordDAOImpl() );
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( incomeRecordService );
        
        try {
            // 執行視窗程式
            MainFrame mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 點選收支記錄頁籤"新增"按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 新增資料
            inputString( bot, "test item 1" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "100" );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有新增成功
            IncomeRecord expect = new IncomeRecord( 1, currentYear, currentMonth, currentDay, "test item 1", 0, -100, "", 1 );
            IncomeRecord actual = incomeRecordService.findOne( 1, currentYear, currentMonth );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
            
            // 檢查畫面是否有更新
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, "已新增資料筆數是否有更新為'1'", "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            Thread.sleep( 500 );
            
            // 新增資料
            inputString( bot, "test item 2" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "200" );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );

            inputString( bot, "test item 3" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "300" );
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_LEFT ); bot.keyRelease( KeyEvent.VK_LEFT ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            bot.keyPress( KeyEvent.VK_ESCAPE ); bot.keyRelease( KeyEvent.VK_ESCAPE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有新增成功
            List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
            expectDataList.add( new IncomeRecord( 1, currentYear, currentMonth, currentDay, "test item 1", 0, -100, "", 1 ) );
            expectDataList.add( new IncomeRecord( 2, currentYear, currentMonth, currentDay, "test item 2", 0, -200, "", 2 ) );
            expectDataList.add( new IncomeRecord( 3, currentYear, currentMonth, currentDay, "test item 3", 0, 300, "", 3 ) );
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( currentYear, currentMonth );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面是否顯示正確
            String expectDateString = String.format( "%04d.%02d.%02d", currentYear, currentMonth, currentDay );
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>test item 1</td><td>支</td><td>100</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>test item 2</td><td>支</td><td>200</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>test item 3</td><td>收</td><td>300</td></tr>" + 
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
    public void testCreateIncomeRecord2() throws IOException {
        int testerSelection = 0;
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( new IncomeRecordDAOImpl() );
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( incomeRecordService );
        
        try {
            // 執行視窗程式
            MainFrame mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 點選收支記錄頁籤"新增"按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 新增資料
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "01" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "test item 1" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "100" );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有新增成功
            IncomeRecord expect = new IncomeRecord( 1, 2017, 10, 1, "test item 1", 0, -100, "", 1 );
            IncomeRecord actual = incomeRecordService.findOne( 1, 2017, 10 );
            assertTrue( IncomeRecordUtil.equals( expect, actual ) );
            
            // 檢查畫面是否有更新
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, "已新增資料筆數是否有更新為'1'", "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            Thread.sleep( 500 );
            
            // 新增資料
            inputString( bot, "test item 2" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "200" );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );

            inputString( bot, "test item 3" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "300" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "test comment" );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            inputString( bot, "123" );
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_I ); bot.keyRelease( KeyEvent.VK_I ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            bot.keyPress( KeyEvent.VK_ESCAPE ); bot.keyRelease( KeyEvent.VK_ESCAPE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有新增成功
            List<IncomeRecord> expectDataList = new ArrayList<IncomeRecord>();
            expectDataList.add( new IncomeRecord( 1, 2017, 10, 1, "test item 1", 0, -100, "", 1 ) );
            expectDataList.add( new IncomeRecord( 2, 2017, 10, 1, "test item 2", 0, -200, "", 2 ) );
            expectDataList.add( new IncomeRecord( 3, 2017, 10, 1, "test item 3", 0, 300, "test comment<br />123", 3 ) );
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( 2017, 10 );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面是否顯示正確
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            
            String expectDateString = String.format( "%04d.%02d.%02d", 2017, 10, 1 );
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>test item 1</td><td>支</td><td>100</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>test item 2</td><td>支</td><td>200</td></tr>" + 
                    "<tr><td>" + expectDateString + "</td><td>test item 3</td><td>收</td><td>300</td></tr>" + 
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
    public void testCreateIncomeRecord3() throws IOException {
        int testerSelection = 0;
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( new IncomeRecordDAOImpl() );
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( incomeRecordService );
        
        try {
            // 執行視窗程式
            MainFrame mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 點選收支記錄頁籤"新增"按鈕
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_SPACE ); bot.keyRelease( KeyEvent.VK_SPACE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 新增資料 (日期: 2017.10.01  項目: test item 1  金額: 100(支出))
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "01" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "test item 1" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "100" );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有新增成功
            IncomeRecord expect1 = new IncomeRecord( 1, 2017, 10, 1, "test item 1", 0, -100, "", 1 );
            IncomeRecord actual1 = incomeRecordService.findOne( 1, 2017, 10 );
            assertTrue( IncomeRecordUtil.equals( expect1, actual1 ) );
            
            // 檢查畫面是否有更新
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, "已新增資料筆數是否有更新為'1'", "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            Thread.sleep( 1000 );
            
            // 新增資料 (日期: 2018.01.31  項目: test item 2  金額: 100(支出))
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            // 年: 2017 -> 2018
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            // 月: 10 -> 01
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_DOWN ); bot.keyRelease( KeyEvent.VK_DOWN ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            // 日: 01 -> 31
            bot.keyPress( KeyEvent.VK_UP ); bot.keyRelease( KeyEvent.VK_UP ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "test item 2" );
            // 測試剪下功能
            inputString( bot, "100" );
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_LEFT ); bot.keyRelease( KeyEvent.VK_LEFT ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_LEFT ); bot.keyRelease( KeyEvent.VK_LEFT ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_LEFT ); bot.keyRelease( KeyEvent.VK_LEFT ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            bot.keyPress( KeyEvent.VK_T ); bot.keyRelease( KeyEvent.VK_T ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            // 金額: 100 (測試貼上 & undo功能)
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_CONTEXT_MENU ); bot.keyRelease( KeyEvent.VK_CONTEXT_MENU ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            bot.keyPress( KeyEvent.VK_P ); bot.keyRelease( KeyEvent.VK_P ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            inputString( bot, "123" );
            Thread.sleep( 500 );
            bot.keyPress( KeyEvent.VK_CONTROL );
            bot.keyPress( KeyEvent.VK_Z ); bot.keyRelease( KeyEvent.VK_Z ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_Z ); bot.keyRelease( KeyEvent.VK_Z ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_Z ); bot.keyRelease( KeyEvent.VK_Z ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_CONTROL );
            bot.keyPress( KeyEvent.VK_ENTER ); bot.keyRelease( KeyEvent.VK_ENTER ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            bot.keyPress( KeyEvent.VK_ESCAPE ); bot.keyRelease( KeyEvent.VK_ESCAPE ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查是否有新增成功
            IncomeRecord expect2 = new IncomeRecord( 2, 2018, 1, 31, "test item 2", 0, -100, "", 1 );
            IncomeRecord actual2 = incomeRecordService.findOne( 2, 2018, 1 );
            assertTrue( IncomeRecordUtil.equals( expect2, actual2 ) );
            
            // 檢查畫面是否顯示正確 (2017.10)
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            inputString( bot, "2017" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "10" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            
            String expectDateString1 = String.format( "%04d.%02d.%02d", 2017, 10, 1 );
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString1 + "</td><td>test item 1</td><td>支</td><td>100</td></tr>" + 
                "</table></body></html>", 
                "Check", JOptionPane.YES_NO_OPTION );
            assertEquals( JOptionPane.YES_OPTION, testerSelection );
            Thread.sleep( 500 );
            
            // 檢查畫面是否顯示正確 (2018.01)
            bot.keyPress( KeyEvent.VK_SHIFT );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            bot.keyRelease( KeyEvent.VK_SHIFT );
            inputString( bot, "2018" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            inputString( bot, "01" );
            bot.keyPress( KeyEvent.VK_TAB ); bot.keyRelease( KeyEvent.VK_TAB ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 500 );
            
            String expectDateString2 = String.format( "%04d.%02d.%02d", 2018, 1, 31 );
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString2 + "</td><td>test item 2</td><td>支</td><td>100</td></tr>" + 
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
    
    private void inputString( Robot bot, String s ) {
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
            if( Character.isUpperCase( s.charAt( i ) ) || 
                    shiftPunctuationList.indexOf( s.charAt( i ) ) >= 0 ) {
                bot.keyRelease( KeyEvent.VK_SHIFT );
            }
        }
    }
}
