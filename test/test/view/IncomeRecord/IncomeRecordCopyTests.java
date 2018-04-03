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
import junit.framework.TestCase;
import main.FundBookServices;
import repository.impl.IncomeRecordDAOImpl;
import service.IncomeRecordService;
import service.impl.IncomeRecordServiceImpl;
import view.MainFrame;

public class IncomeRecordCopyTests extends TestCase {

    private final int TAB_DELAY = 100;
    private final String INCOME_RECORD_SEQ_FILE_PATH = "data\\IncomeRecord\\IncomeRecordSeq.txt";
    private final String INCOME_RECORD_SEQ_FILE_PATH_BACKUP = "data\\IncomeRecord\\IncomeRecordSeq_backup.txt";
    
    public void testCopyIncomeRecord() throws IOException {
        int testerSelection = 0;
        IncomeRecordService incomeRecordService = new IncomeRecordServiceImpl( new IncomeRecordDAOImpl() );
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( incomeRecordService );
        
        try {
            backupFile( getIncomeRecordCsvFilePathOfCurrentDay(), getIncomeRecordCsvFilePathBackupOfCurrentDay() );
            backupFile( INCOME_RECORD_SEQ_FILE_PATH, INCOME_RECORD_SEQ_FILE_PATH_BACKUP );
            
            // 新增初始資料
            for( int i = 1; i <= 5; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setItem( getTestData1().getItem() + " " + i );
                incomeRecord.setAmount( i * (-100) );
                incomeRecord.setDay( i );
                incomeRecordService.insert( incomeRecord );
            }
            
            // 執行視窗程式
            MainFrame mainFrame = new MainFrame( fundBookServices );
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
            for( int i = 1; i <= 6; i++ ) {
                IncomeRecord incomeRecord = getTestData1();
                incomeRecord.setId( i );
                incomeRecord.setDay( i );
                incomeRecord.setItem( getTestData1().getItem() + " " + i );
                incomeRecord.setAmount( i * (-100) );
                incomeRecord.setOrderNo( i );
                if( i == 6 ) {
                    incomeRecord.setDay( 3 );
                    incomeRecord.setItem( getTestData1().getItem() + " 3" );
                    incomeRecord.setAmount( -300 );
                }
                expectDataList.add( incomeRecord );
            }
            List<IncomeRecord> actualDataList = incomeRecordService.findByMonth( getTestData1().getYear(), getTestData1().getMonth() );
            assertEquals( expectDataList.size(), actualDataList.size() );
            for( int i = 0; i < expectDataList.size(); i++ ) {
                assertTrue( "failed at i = " + i, IncomeRecordUtil.equals( expectDataList.get( i ), actualDataList.get( i ) ) );
            }
            
            // 檢查畫面是否顯示正確
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            String expectDateString = String.format( "%04d.%02d.", 
                calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1 );
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                    "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的收支記錄資料:</p><table>" + 
                    "<tr><th>日期</th><th>項目</th><th>收支</th><th>金額</th></tr>" + 
                    "<tr><td>" + expectDateString + "01" + "</td><td>test item 1</td><td>支</td><td>100</td></tr>" + 
                    "<tr><td>" + expectDateString + "02" + "</td><td>modified item</td><td>支</td><td>600</td></tr>" + 
                    "<tr><td>" + expectDateString + "03" + "</td><td>modified item 2</td><td>收</td><td>300</td></tr>" + 
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
        } finally {
            restoreFile( INCOME_RECORD_SEQ_FILE_PATH_BACKUP, INCOME_RECORD_SEQ_FILE_PATH );
            restoreFile( getIncomeRecordCsvFilePathBackupOfCurrentDay(), getIncomeRecordCsvFilePathOfCurrentDay() );
        }
    } 
    
    private IncomeRecord getTestData1() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        IncomeRecord testData = new IncomeRecord();
        testData.setId( 0 );
        testData.setYear( calendar.get( Calendar.YEAR ) );
        testData.setMonth( calendar.get( Calendar.MONTH ) + 1 );
        testData.setDay( calendar.get( Calendar.DAY_OF_MONTH ) );
        testData.setItem( "test item" );
        testData.setClassNo( 0 );
        testData.setAmount( -100 );
        testData.setDescription( "" );
        testData.setOrderNo( 0 );
        return testData;
    }
    
    private String getIncomeRecordCsvFilePathOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        return String.format( "data\\IncomeRecord\\%04d.%02d.csv", 
            calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1 );
    }
    
    private String getIncomeRecordCsvFilePathBackupOfCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        return String.format( "data\\IncomeRecord\\%04d.%02d_backup.csv", 
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
}
