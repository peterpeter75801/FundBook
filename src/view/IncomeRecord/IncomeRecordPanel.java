package view.IncomeRecord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import view.MainFrame;

public class IncomeRecordPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordDatePanel incomeRecordDatePanel; 
    private IncomeRecordTablePanel incomeRecordTablePanel;
    
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
    
    public IncomeRecordPanel( MainFrame ownerFrame ) {
        setLayout( null );
        
        //setBorder( new LineBorder( Color.BLACK, 1 ) );
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        //testLabel = new JLabel( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" );
        //testLabel.setBounds( 0, 0, 793, 22 );
        //testLabel.setFont( generalFont );
        //add( testLabel );
        
        incomeRecordDatePanel = new IncomeRecordDatePanel( this );
        incomeRecordDatePanel.setBounds( 0, 0, 120, 479 );
        add( incomeRecordDatePanel );
        
        incomeRecordTablePanel = new IncomeRecordTablePanel( this, incomeRecordDatePanel );
        incomeRecordTablePanel.setBounds( 120, 0, 585, 479 );
        add( incomeRecordTablePanel );
        
        incomeRecordDatePanel.setIncomeRecordTablePanel( incomeRecordTablePanel );
        incomeRecordTablePanel.loadIncomeRecordOfCurrentMonth();
        
        createButton = new JButton( "新增(C)" );
        createButton.setBounds( 717, 32, 64, 22 );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        createButton.setFont( generalFont );
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
        
        setPreferredSize( new Dimension( 793, 533 ) );
    }
    
    public JButton getCreateButton() {
        return createButton;
    }
}
