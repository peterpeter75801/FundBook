package view.IncomeRecord;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import common.Contants;
import main.FundBookServices;
import view.MainFrame;

public class IncomeRecordPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordDatePanel incomeRecordDatePanel; 
    private IncomeRecordTablePanel incomeRecordTablePanel;
    
    private IncomeRecordCreateDialog incomeRecordCreateDialog;
    
    private JLabel testLabel;
    private Font generalFont;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton detailButton;
    private JLabel incomeStateInCurrentMonthLabel;
    private JTextField incomeStateInCurrentMonthTextField;
    private JLabel totalPropertyLabel;
    private JTextField totalPropertyTextField;
    private JLabel versionLabel;
    
    public IncomeRecordPanel( FundBookServices fundBookServices, MainFrame ownerFrame ) {
        setLayout( null );
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        incomeRecordDatePanel = new IncomeRecordDatePanel( fundBookServices, this );
        incomeRecordDatePanel.setBounds( 0, 0, 120, 479 );
        add( incomeRecordDatePanel );
        
        incomeRecordTablePanel = new IncomeRecordTablePanel( fundBookServices, this, incomeRecordDatePanel );
        incomeRecordTablePanel.setBounds( 120, 0, 585, 479 );
        add( incomeRecordTablePanel );
        
        incomeRecordDatePanel.setIncomeRecordTablePanel( incomeRecordTablePanel );
        incomeRecordTablePanel.loadIncomeRecordOfCurrentMonth();
        
        incomeRecordCreateDialog = new IncomeRecordCreateDialog( fundBookServices.getIncomeRecordService(), ownerFrame );
        
        createButton = new JButton( "新增(C)" );
        createButton.setBounds( 717, 32, 64, 22 );
        createButton.setFont( generalFont );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        createButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                // incomeRecordCreateDialog.openDialog();
                openIncomeRecordCreateDialog();
            }
        });
        add( createButton );
        
        incomeStateInCurrentMonthLabel = new JLabel( "本月收支狀況(收入-支出): " );
        incomeStateInCurrentMonthLabel.setBounds( 16, 479, 200, 22 );
        incomeStateInCurrentMonthLabel.setFont( generalFont );
        add( incomeStateInCurrentMonthLabel );
        
        incomeStateInCurrentMonthTextField = new JTextField();
        incomeStateInCurrentMonthTextField.setBounds( 216, 479, 80, 22 );
        incomeStateInCurrentMonthTextField.setFont( generalFont );
        incomeStateInCurrentMonthTextField.setEditable( false );
        add( incomeStateInCurrentMonthTextField );
        
        totalPropertyLabel = new JLabel( "總金額: " );
        totalPropertyLabel.setBounds( 16, 506, 64, 22 );
        totalPropertyLabel.setFont( generalFont );
        add( totalPropertyLabel );
        
        totalPropertyTextField = new JTextField();
        totalPropertyTextField.setBounds( 80, 506, 216, 22 );
        totalPropertyTextField.setFont( generalFont );
        totalPropertyTextField.setEditable( false );
        add( totalPropertyTextField );
        
        versionLabel = new JLabel( Contants.VERSION, SwingConstants.RIGHT );
        versionLabel.setBounds( 561, 506, 224, 22 );
        versionLabel.setFont( generalFont );
        add( versionLabel );
        
        setPreferredSize( new Dimension( 793, 533 ) );
    }
    
    public JButton getCreateButton() {
        return createButton;
    }
    
    public void reselectDateList() {
        incomeRecordDatePanel.reselectDateList();
    }
    
    private void openIncomeRecordCreateDialog() {
        int selectedYear, selectedMonth;
        String selectedMonthString = incomeRecordDatePanel.getMonthListSelectedValue();
        try {
            selectedYear = Integer.parseInt( selectedMonthString.substring( 0, 4 ) );
            selectedMonth = Integer.parseInt( selectedMonthString.substring( 5, 7 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            selectedYear = 1900;
            selectedMonth = 1;
        }
        incomeRecordCreateDialog.openDialog( selectedYear, selectedMonth );
    }
}
