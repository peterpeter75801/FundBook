package test.view.FundingStatus;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import common.Contants;
import common.SystemInfo;
import domain.FundingStatus;
import main.FundBook;
import main.FundBookServices;
import repository.CheckRecordDAO;
import repository.FundingStatusDAO;
import repository.IncomeRecordDAO;
import repository.TotalPropertyDAO;
import repository.impl.CheckRecordDAOImpl;
import repository.impl.FundingStatusDAOImpl;
import repository.impl.IncomeRecordDAOImpl;
import repository.impl.TotalPropertyDAOImpl;
import service.FundingStatusService;
import service.impl.CheckRecordServiceImpl;
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
public class FundingStatusTablePanelTests {
    
    private final int TAB_DELAY = 100;
    private final String FUNDING_STATUS_CSV_FILE_PATH = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
    private final String FUNDING_STATUS_CSV_FILE_PATH_BACKUP = Contants.FUNDING_STATUS_DATA_PATH + "FundingStatus_backup.csv";
    private final String FUNDING_STATUS_SEQ_FILE_PATH_BACKUP = "./data/FundingStatus/FundingStatusSeq_backup.txt";
    
    private FundBookServices fundBookServices;
    private FundingStatusService fundingStatusService;
    
    private MainFrame mainFrame = null;
    
    @Before
    public void setUp() throws IOException, URISyntaxException {
        initializeServices();
        fundingStatusService = fundBookServices.getFundingStatusService();
        
        backupFile( FUNDING_STATUS_CSV_FILE_PATH, FUNDING_STATUS_CSV_FILE_PATH_BACKUP );
        backupFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH, FUNDING_STATUS_SEQ_FILE_PATH_BACKUP );
    }
    
    @After
    public void tearDown() throws IOException, InterruptedException {
        fundingStatusService = null;
        fundBookServices = null;
        
        restoreFile( FUNDING_STATUS_SEQ_FILE_PATH_BACKUP, Contants.FUNDING_STATUS_SEQ_FILE_PATH );
        restoreFile( FUNDING_STATUS_CSV_FILE_PATH_BACKUP, FUNDING_STATUS_CSV_FILE_PATH );
        if( mainFrame != null ) {
            mainFrame.dispose();
            Thread.sleep( 1000 );
        }
    }
    
    @Test
    public void testFundingStatusTablePanelDisplay1() {
        int testerSelection = 0;
        try {
            // 新增初始資料
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 12, 30, "中華郵政 #12345671234567", 10000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'C', 2017, 12, 30, "隨身現金", 5000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 11, 28, "土地銀行 #01234560123456", 100000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'D', 2017, 10, 01, "土地銀行 #01234567890123", 50000, "", 0 ) );
            fundingStatusService.insert( new FundingStatus( 0, 'T', 2017, 10, 28, "土地銀行 #11223344556677", 200000, "", 0 ) );
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 切換到財務儲存狀況頁籤
            bot.keyPress( KeyEvent.VK_RIGHT ); bot.keyRelease( KeyEvent.VK_RIGHT ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查執行結果
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                        "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的財務儲存狀況資料:</p><table>" + 
                    "<tr><th>種類</th><th>儲存地點/儲存機構</th><th>金額</th></tr>" + 
                    "<tr><td>活存</td><td>中華郵政 #12345671234567</td><td>10000</td></tr>" + 
                    "<tr><td>現金</td><td>隨身現金</td><td>5000</td></tr>" + 
                    "<tr><td>定存</td><td>土地銀行 #01234560123456</td><td>100000</td></tr>" + 
                    "<tr><td>活存</td><td>土地銀行 #01234567890123</td><td>50000</td></tr>" +
                    "<tr><td>定存</td><td>土地銀行 #11223344556677</td><td>200000</td></tr>" +
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
    public void testFundingStatusTablePanelDisplay2() {
        int testerSelection = 0;
        try {
            // 新增初始資料
            for( int i = 1; i <= 30; i++ ) {
                FundingStatus fundingStatus = new FundingStatus( 0, 'D', 2017, 12, i, "活存帳號" + i, 10000 * i, "", 0 );
                fundingStatusService.insert( fundingStatus );
            }
            
            // 執行視窗程式
            mainFrame = new MainFrame( fundBookServices );
            mainFrame.setVisible( true );
            
            JOptionPane.showMessageDialog( mainFrame, "請切換為英文輸入法", "Message", JOptionPane.INFORMATION_MESSAGE );
            
            Robot bot =  new Robot();
            Thread.sleep( 3000 );
            
            // 切換到財務儲存狀況頁籤
            bot.keyPress( KeyEvent.VK_RIGHT ); bot.keyRelease( KeyEvent.VK_RIGHT ); Thread.sleep( TAB_DELAY );
            Thread.sleep( 1000 );
            
            // 檢查執行結果
            testerSelection = JOptionPane.showConfirmDialog( 
                mainFrame, 
                "<html><head><style type=\"text/css\">" + 
                        "table, th, td {border: 1px solid black; border-collapse: collapse;}</style></head>" + 
                "<body><p>是否有顯示測試的財務儲存狀況資料:</p><table>" + 
                    "<tr><th>種類</th><th>儲存地點/儲存機構</th><th>金額</th></tr>" + 
                    "<tr><td>活存</td><td>活存帳號1</td><td>10000</td></tr>" + 
                    "<tr><td>活存</td><td>活存帳號2</td><td>20000</td></tr>" + 
                    "<tr><td>活存</td><td>活存帳號3</td><td>30000</td></tr>" + 
                    "<tr><td>...</td><td>...</td><td>...</td></tr>" + 
                    "<tr><td>活存</td><td>活存帳號20</td><td>200000</td></tr>" +
                    "<tr><td>...</td><td>...</td><td>...</td></tr>" + 
                "</table>並在右側顯示垂直捲軸?</body></html>", 
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
    
    private void initializeServices() throws URISyntaxException {
        // Initialize system information
        SystemInfo systemInfo = new SystemInfo(
            new File( FundBook.class.getProtectionDomain().getCodeSource().getLocation().toURI() ).getParent() );
        
        // Initialize DAOs
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl( systemInfo );
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl( systemInfo );
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl( systemInfo );
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl( systemInfo );
        
        // Initialize services
        fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( new IncomeRecordServiceImpl( incomeRecordDAO ) );
        fundBookServices.setTotalPropertyService( new TotalPropertyServiceImpl( totalPropertyDAO ) );
        fundBookServices.setFundingStatusService( new FundingStatusServiceImpl( fundingStatusDAO ) );
        fundBookServices.setCheckRecordService( new CheckRecordServiceImpl( checkRecordDAO ) );
        
        // Set services wired relation
        ((IncomeRecordServiceImpl)fundBookServices.getIncomeRecordService()).setTotalPropertyService(
                fundBookServices.getTotalPropertyService() );
    }
}
