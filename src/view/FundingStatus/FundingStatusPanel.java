package view.FundingStatus;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.Contants;
import main.FundBookServices;
import service.FundingStatusService;
import view.MainFrame;

public class FundingStatusPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private FundingStatusService fundingStatusService;
    
    private MainFrame ownerFrame;
    private FundingStatusTablePanel fundingStatusTablePanel;
    
    private Font generalFont;
    private JButton createButton;
    private JButton transferButton;
    private JButton modifyAmountButton;
    private JButton modifyAttribute;
    private JButton disableButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton detailButton;
    private JButton historyButton;
    private JCheckBox showDisabledDataCheckBox;
    private JButton totalAmountDisplayButton;
    private JLabel versionLabel;

    public FundingStatusPanel( FundBookServices fundBookServices, MainFrame ownerFrame ) {
        setLayout( null );
        
        fundingStatusService = fundBookServices.getFundingStatusService();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        this.ownerFrame = ownerFrame;
        
        fundingStatusTablePanel = new FundingStatusTablePanel( fundBookServices, this );
        fundingStatusTablePanel.setBounds( 0, 0, 675, 479 );
        add( fundingStatusTablePanel );
        
        createButton = new JButton( "新增(C)" );
        createButton.setBounds( 685, 32, 96, 22 );
        createButton.setFont( generalFont );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( createButton );
        
        transferButton = new JButton( "移動金額(M)" );
        transferButton.setBounds( 685, 76, 96, 22 );
        transferButton.setFont( generalFont );
        transferButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( transferButton );
        
        modifyAmountButton = new JButton( "修改金額(A)" );
        modifyAmountButton.setBounds( 685, 120, 96, 22 );
        modifyAmountButton.setFont( generalFont );
        modifyAmountButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( modifyAmountButton );
        
        modifyAttribute = new JButton( "修改屬性(U)" );
        modifyAttribute.setBounds( 685, 164, 96, 22 );
        modifyAttribute.setFont( generalFont );
        modifyAttribute.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( modifyAttribute );
        
        disableButton = new JButton( "刪除(D)" );
        disableButton.setBounds( 685, 208, 96, 22 );
        disableButton.setFont( generalFont );
        disableButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( disableButton );
        
        moveUpButton = new JButton( "上移(P)" );
        moveUpButton.setBounds( 685, 252, 96, 22 );
        moveUpButton.setFont( generalFont );
        moveUpButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( moveUpButton );
        
        moveDownButton = new JButton( "下移(N)" );
        moveDownButton.setBounds( 685, 296, 96, 22 );
        moveDownButton.setFont( generalFont );
        moveDownButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( moveDownButton );
        
        detailButton = new JButton( "詳細(R)" );
        detailButton.setBounds( 685, 340, 96, 22 );
        detailButton.setFont( generalFont );
        detailButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( detailButton );
        
        historyButton = new JButton( "更動歷程(H)" );
        historyButton.setBounds( 685, 384, 96, 22 );
        historyButton.setFont( generalFont );
        historyButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( historyButton );
        
        showDisabledDataCheckBox = new JCheckBox( "顯示已刪除的財務儲存狀況資料" );
        showDisabledDataCheckBox.setBounds( 10, 480, 256, 22 );
        showDisabledDataCheckBox.setFont( generalFont );
        add( showDisabledDataCheckBox );
        
        totalAmountDisplayButton = new JButton( "顯示財務儲存狀況總金額(T)" );
        totalAmountDisplayButton.setBounds( 10, 506, 208, 22 );
        totalAmountDisplayButton.setFont( generalFont );
        totalAmountDisplayButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        add( totalAmountDisplayButton );
        
        versionLabel = new JLabel( Contants.VERSION, SwingConstants.RIGHT );
        versionLabel.setBounds( 561, 506, 224, 22 );
        versionLabel.setFont( generalFont );
        add( versionLabel );
        
        setPreferredSize( new Dimension( 793, 533 ) );
        
        // 載入資料
        fundingStatusTablePanel.loadFundingStatus();
    }
    
    public MainFrame getOwnerFrame() {
        return ownerFrame;
    }
}
