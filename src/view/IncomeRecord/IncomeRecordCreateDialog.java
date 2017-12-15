package view.IncomeRecord;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import service.IncomeRecordService;
import view.MainFrame;

public class IncomeRecordCreateDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordService incomeRecordService;
    
    private MainFrame ownerFrame;
    
    private FocusHandler focusHandler;
    private Font generalFont;
    private Font createdCountFont;
    private JPanel dialogPanel;
    private JLabel classLabel;
    private JTextField classTextField;
    private JTextField classNameTextField;
    private JButton queryClassButton;
    private JLabel incomeTypeLabel;
    private JRadioButton incomeRadioButton;
    private JRadioButton expenditureRadioButton;
    private ButtonGroup incomeTypeButtonGroup;
    private JLabel dateLabel;
    private JTextField yearTextField;
    private JLabel yearLabel;
    private JTextField monthTextField;
    private JLabel monthLabel;
    private JTextField dayTextField;
    private JLabel dayLabel;
    private JLabel itemLabel;
    private JTextField itemTextField;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JLabel dollarLabel;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;
    private JScrollPane descriptionScrollPane;
    private JLabel createdCountLabel;
    private JLabel createdCountValueLabel;
    private JButton createButton;
    private JButton finishButton;
    
    public IncomeRecordCreateDialog( IncomeRecordService incomeRecordService, MainFrame ownerFrame ) {
        super( ownerFrame, "Create Income Record", false );
        
        this.incomeRecordService = incomeRecordService;
        
        this.ownerFrame = ownerFrame;
        
        focusHandler = new FocusHandler();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        createdCountFont = new Font( "細明體", Font.ITALIC, 16 );
        
        dialogPanel = new JPanel();
        dialogPanel.setLayout( null );
        
        classLabel = new JLabel( "類別: " );
        classLabel.setBounds( 16, 10, 48, 22 );
        classLabel.setFont( generalFont );
        dialogPanel.add( classLabel );
        
        classTextField = new JTextField( 6 );
        classTextField.setBounds( 64, 10, 56, 22 );
        classTextField.setFont( generalFont );
        classTextField.addFocusListener( focusHandler );
        dialogPanel.add( classTextField );
        
        classNameTextField = new JTextField();
        classNameTextField.setBounds( 128, 10, 168, 22 );
        classNameTextField.setFont( generalFont );
        classNameTextField.addFocusListener( focusHandler );
        classNameTextField.setEditable( false );
        dialogPanel.add( classNameTextField );
        
        queryClassButton = new JButton( "查詢" );
        queryClassButton.setBounds( 304, 10, 48, 22 );
        queryClassButton.setFont( generalFont );
        queryClassButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        dialogPanel.add( queryClassButton );
        
        incomeTypeLabel = new JLabel( "收支: " );
        incomeTypeLabel.setBounds( 16, 42, 48, 22 );
        incomeTypeLabel.setFont( generalFont );
        dialogPanel.add( incomeTypeLabel );
        
        incomeRadioButton = new JRadioButton( "收(I)", false );
        incomeRadioButton.setBounds( 64, 42, 64, 22 );
        incomeRadioButton.setFont( generalFont );
        incomeRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        dialogPanel.add( incomeRadioButton );
        
        expenditureRadioButton = new JRadioButton( "支(E)", true );
        expenditureRadioButton.setBounds( 136, 42, 64, 22 );
        expenditureRadioButton.setFont( generalFont );
        expenditureRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        dialogPanel.add( expenditureRadioButton );
        
        incomeTypeButtonGroup = new ButtonGroup();
        incomeTypeButtonGroup.add( incomeRadioButton );
        incomeTypeButtonGroup.add( expenditureRadioButton );
        
        dateLabel = new JLabel( "日期: " );
        dateLabel.setBounds( 216+65, 42, 48, 22 );
        dateLabel.setFont( generalFont );
        dialogPanel.add( dateLabel );
        
        yearTextField = new JTextField( 4 );
        yearTextField.setBounds( 264+65, 42, 40, 22 );
        yearTextField.setFont( generalFont );
        yearTextField.addFocusListener( focusHandler );
        dialogPanel.add( yearTextField );
        
        yearLabel = new JLabel( "年" );
        yearLabel.setBounds( 304+65, 42, 16, 22 );
        yearLabel.setFont( generalFont );
        dialogPanel.add( yearLabel );
        
        monthTextField = new JTextField( 2 );
        monthTextField.setBounds( 320+65, 42, 24, 22 );
        monthTextField.setFont( generalFont );
        monthTextField.addFocusListener( focusHandler );
        dialogPanel.add( monthTextField );
        
        monthLabel = new JLabel( "月" );
        monthLabel.setBounds( 344+65, 42, 16, 22 );
        monthLabel.setFont( generalFont );
        dialogPanel.add( monthLabel );
        
        dayTextField = new JTextField( 2 );
        dayTextField.setBounds( 360+65, 42, 24, 22 );
        dayTextField.setFont( generalFont );
        dayTextField.addFocusListener( focusHandler );
        dialogPanel.add( dayTextField );
        
        dayLabel = new JLabel( "日" );
        dayLabel.setBounds( 384+65, 42, 16, 22 );
        dayLabel.setFont( generalFont );
        dialogPanel.add( dayLabel );
        
        itemLabel = new JLabel( "項目: " );
        itemLabel.setBounds( 16, 74, 48, 22 );
        itemLabel.setFont( generalFont );
        dialogPanel.add( itemLabel );
        
        itemTextField = new JTextField();
        itemTextField.setBounds( 64, 74, 240-31, 22 );
        itemTextField.setFont( generalFont );
        itemTextField.addFocusListener( focusHandler );
        dialogPanel.add( itemTextField );
        
        amountLabel = new JLabel( "金額: " );
        amountLabel.setBounds( 328-47, 74, 48, 22 );
        amountLabel.setFont( generalFont );
        dialogPanel.add( amountLabel );
        
        amountTextField = new JTextField();
        amountTextField.setBounds( 376-47, 74, 72, 22 );
        amountTextField.setFont( generalFont );
        amountTextField.addFocusListener( focusHandler );
        dialogPanel.add( amountTextField );
        
        dollarLabel = new JLabel( "元" );
        dollarLabel.setBounds( 449-47, 74, 16, 22 );
        dollarLabel.setFont( generalFont );
        dialogPanel.add( dollarLabel );
        
        descriptionLabel = new JLabel( "描述: " );
        descriptionLabel.setBounds( 16, 106, 48, 22 );
        descriptionLabel.setFont( generalFont );
        dialogPanel.add( descriptionLabel );
        
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setSize( 425, 110 );
        descriptionTextArea.setFont( generalFont );
        descriptionTextArea.setLineWrap( true );
        descriptionTextArea.setWrapStyleWord( true );
        //descriptionTextArea.addKeyListener( undoHotKeyHandler );
        //descriptionTextArea.getDocument().addUndoableEditListener( undoEditHandler );
        descriptionScrollPane = new JScrollPane( descriptionTextArea );
        descriptionScrollPane.setBounds( 16, 128, 449, 115 );
        descriptionScrollPane.setPreferredSize( new Dimension( 449, 115 ) );
        dialogPanel.add( descriptionScrollPane );
        
        Set<AWTKeyStroke> forward = new HashSet<AWTKeyStroke>(
            descriptionTextArea.getFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS ) );
        forward.add( KeyStroke.getKeyStroke( "TAB" ) );
        descriptionTextArea.setFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forward );
        Set<AWTKeyStroke> backward = new HashSet<AWTKeyStroke>(
            descriptionTextArea.getFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS ) );
        backward.add( KeyStroke.getKeyStroke( "shift TAB" ) );
        descriptionTextArea.setFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backward );
        
        createdCountLabel = new JLabel( "已新增資料筆數: " );
        createdCountLabel.setBounds( 16, 253, 128, 22 );
        createdCountLabel.setFont( createdCountFont );
        createdCountLabel.setForeground( Color.BLUE );
        dialogPanel.add( createdCountLabel );
        
        createdCountValueLabel = new JLabel( "_" );
        createdCountValueLabel.setBounds( 144, 253, 24, 22 );
        createdCountValueLabel.setFont( createdCountFont );
        createdCountValueLabel.setForeground( Color.BLUE );
        dialogPanel.add( createdCountValueLabel );
        
        createButton = new JButton( "新增" );
        createButton.setBounds( 168, 290, 48, 22 );
        createButton.setFont( generalFont );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        dialogPanel.add( createButton );
        
        finishButton = new JButton( "取消" );
        finishButton.setBounds( 264, 290, 48, 22 );
        finishButton.setFont( generalFont );
        finishButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        dialogPanel.add( finishButton );
        
        dialogPanel.setPreferredSize( new Dimension( 482, 340 ) );
        add( dialogPanel );
        
        pack();
        setLocationRelativeTo( ownerFrame );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setVisible( false );
    }
    
    public void openDialog( int year, int month ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        incomeRadioButton.setSelected( false );
        expenditureRadioButton.setSelected( true );
        
        classTextField.setText( "" );
        classNameTextField.setText( "" );
        itemTextField.setText( "" );
        amountTextField.setText( "" );
        descriptionTextArea.setText( "" );
        createdCountValueLabel.setText( "" );
        
        yearTextField.setText( String.format( "%04d", year ) );
        monthTextField.setText( String.format( "%02d", month ) );
        if( year == calendar.get( Calendar.YEAR ) && month == calendar.get( Calendar.MONTH ) + 1 ) {
            dayTextField.setText( String.format( "%02d", calendar.get( Calendar.DAY_OF_MONTH ) ) );
        } else {
            dayTextField.setText( "01" );
        }
        
        itemTextField.requestFocus();
        setVisible( true );
    }
    
    private class FocusHandler extends FocusAdapter {
        @Override
        public void focusGained( FocusEvent event ) {
            JTextField sourceComponent = (JTextField) event.getSource();
            sourceComponent.selectAll();
        }
    }
}