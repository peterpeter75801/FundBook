package view.IncomeRecord;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import commonUtil.CsvFormatParser;
import domain.IncomeRecord;
import service.IncomeRecordService;
import view.MainFrame;

public class IncomeRecordPropertyDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordService incomeRecordService;
    
    private FocusHandler focusHandler;
    private MnemonicKeyHandler mnemonicKeyHandler;
    private Font generalFont;
    private JPanel dialogPanel;
    private JLabel classLabel;
    private JTextField classTextField;
    private JTextField classNameTextField;
    private JLabel incomeTypeLabel;
    private JTextField incomeTypeTextField;
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
    private JButton confirmButton;
    
    public IncomeRecordPropertyDialog( IncomeRecordService incomeRecordService, MainFrame ownerFrame ) {
        super( ownerFrame, "Income Record Properties", false );
        
        this.incomeRecordService = incomeRecordService;
        
        focusHandler = new FocusHandler();
        mnemonicKeyHandler = new MnemonicKeyHandler();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        dialogPanel = new JPanel();
        dialogPanel.setLayout( null );
        
        classLabel = new JLabel( "類別: " );
        classLabel.setBounds( 16, 10, 48, 22 );
        classLabel.setFont( generalFont );
        //dialogPanel.add( classLabel );
        
        classTextField = new JTextField( 6 );
        classTextField.setBounds( 64, 10, 56, 22 );
        classTextField.setFont( generalFont );
        classTextField.addFocusListener( focusHandler );
        classTextField.addKeyListener( mnemonicKeyHandler );
        classTextField.setEditable( false );
        //dialogPanel.add( classTextField );
        
        classNameTextField = new JTextField();
        classNameTextField.setBounds( 128, 10, 168, 22 );
        classNameTextField.setFont( generalFont );
        classNameTextField.addFocusListener( focusHandler );
        classNameTextField.setEditable( false );
        //dialogPanel.add( classNameTextField );
        
        incomeTypeLabel = new JLabel( "收支: " );
        //incomeTypeLabel.setBounds( 16, 42, 48, 22 );
        incomeTypeLabel.setBounds( 16, 42-32, 48, 22 );
        incomeTypeLabel.setFont( generalFont );
        dialogPanel.add( incomeTypeLabel );
        
        incomeTypeTextField = new JTextField();
        //incomeTypeTextField.setBounds( 64, 42, 24, 22 );
        incomeTypeTextField.setBounds( 64, 42-32, 24, 22 );
        incomeTypeTextField.setFont( generalFont );
        incomeTypeTextField.setEditable( false );
        dialogPanel.add( incomeTypeTextField );
        
        dateLabel = new JLabel( "日期: " );
        //dateLabel.setBounds( 216+65, 42, 48, 22 );
        dateLabel.setBounds( 216+65, 42-32, 48, 22 );
        dateLabel.setFont( generalFont );
        dialogPanel.add( dateLabel );
        
        yearTextField = new JTextField( 4 );
        //yearTextField.setBounds( 264+65, 42, 40, 22 );
        yearTextField.setBounds( 264+65, 42-32, 40, 22 );
        yearTextField.setFont( generalFont );
        yearTextField.setEditable( false );
        yearTextField.addFocusListener( focusHandler );
        yearTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( yearTextField );
        
        yearLabel = new JLabel( "年" );
        //yearLabel.setBounds( 304+65, 42, 16, 22 );
        yearLabel.setBounds( 304+65, 42-32, 16, 22 );
        yearLabel.setFont( generalFont );
        dialogPanel.add( yearLabel );
        
        monthTextField = new JTextField( 2 );
        //monthTextField.setBounds( 320+65, 42, 24, 22 );
        monthTextField.setBounds( 320+65, 42-32, 24, 22 );
        monthTextField.setFont( generalFont );
        monthTextField.setEditable( false );
        monthTextField.addFocusListener( focusHandler );
        monthTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( monthTextField );
        
        monthLabel = new JLabel( "月" );
        //monthLabel.setBounds( 344+65, 42, 16, 22 );
        monthLabel.setBounds( 344+65, 42-32, 16, 22 );
        monthLabel.setFont( generalFont );
        dialogPanel.add( monthLabel );
        
        dayTextField = new JTextField( 2 );
        //dayTextField.setBounds( 360+65, 42, 24, 22 );
        dayTextField.setBounds( 360+65, 42-32, 24, 22 );
        dayTextField.setFont( generalFont );
        dayTextField.setEditable( false );
        dayTextField.addFocusListener( focusHandler );
        dayTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( dayTextField );
        
        dayLabel = new JLabel( "日" );
        //dayLabel.setBounds( 384+65, 42, 16, 22 );
        dayLabel.setBounds( 384+65, 42-32, 16, 22 );
        dayLabel.setFont( generalFont );
        dialogPanel.add( dayLabel );
        
        itemLabel = new JLabel( "項目: " );
        //itemLabel.setBounds( 16, 74, 48, 22 );
        itemLabel.setBounds( 16, 74-32, 48, 22 );
        itemLabel.setFont( generalFont );
        dialogPanel.add( itemLabel );
        
        itemTextField = new JTextField();
        //itemTextField.setBounds( 64, 74, 240-31, 22 );
        itemTextField.setBounds( 64, 74-32, 240-31, 22 );
        itemTextField.setFont( generalFont );
        itemTextField.setEditable( false );
        itemTextField.addFocusListener( focusHandler );
        itemTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( itemTextField );
        
        amountLabel = new JLabel( "金額: " );
        //amountLabel.setBounds( 328-47, 74, 48, 22 );
        amountLabel.setBounds( 328-47, 74-32, 48, 22 );
        amountLabel.setFont( generalFont );
        dialogPanel.add( amountLabel );
        
        amountTextField = new JTextField();
        //amountTextField.setBounds( 376-47, 74, 72, 22 );
        amountTextField.setBounds( 376-47, 74-32, 72, 22 );
        amountTextField.setFont( generalFont );
        amountTextField.setEditable( false );
        amountTextField.addFocusListener( focusHandler );
        amountTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( amountTextField );
        
        dollarLabel = new JLabel( "元" );
        //dollarLabel.setBounds( 449-47, 74, 16, 22 );
        dollarLabel.setBounds( 449-47, 74-32, 16, 22 );
        dollarLabel.setFont( generalFont );
        dialogPanel.add( dollarLabel );
        
        descriptionLabel = new JLabel( "描述: " );
        //descriptionLabel.setBounds( 16, 106, 48, 22 );
        descriptionLabel.setBounds( 16, 106-32, 48, 22 );
        descriptionLabel.setFont( generalFont );
        dialogPanel.add( descriptionLabel );
        
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setSize( 425, 132 );
        descriptionTextArea.setFont( generalFont );
        descriptionTextArea.setEditable( false );
        descriptionTextArea.setLineWrap( true );
        descriptionTextArea.setWrapStyleWord( true );
        descriptionScrollPane = new JScrollPane( descriptionTextArea );
        //descriptionScrollPane.setBounds( 16, 128, 449, 137 );
        descriptionScrollPane.setBounds( 16, 128-32, 449, 137 );
        descriptionScrollPane.setPreferredSize( new Dimension( 449, 137 ) );
        dialogPanel.add( descriptionScrollPane );
        
        Set<AWTKeyStroke> forward = new HashSet<AWTKeyStroke>(
            descriptionTextArea.getFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS ) );
        forward.add( KeyStroke.getKeyStroke( "TAB" ) );
        descriptionTextArea.setFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forward );
        Set<AWTKeyStroke> backward = new HashSet<AWTKeyStroke>(
            descriptionTextArea.getFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS ) );
        backward.add( KeyStroke.getKeyStroke( "shift TAB" ) );
        descriptionTextArea.setFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backward );
        
        confirmButton = new JButton( "確認" );
        //confirmButton.setBounds( 224, 290, 48, 22 );
        confirmButton.setBounds( 224, 290-32, 48, 22 );
        confirmButton.setFont( generalFont );
        confirmButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        confirmButton.addKeyListener( mnemonicKeyHandler );
        confirmButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                setVisible( false );
            }
        });
        dialogPanel.add( confirmButton );
        
        //dialogPanel.setPreferredSize( new Dimension( 482, 340 ) );
        dialogPanel.setPreferredSize( new Dimension( 482, 340-32 ) );
        add( dialogPanel );
        
        pack();
        setLocationRelativeTo( ownerFrame );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setVisible( false );
    }
    
    public void openDialog( int id, int year, int month ) {
        IncomeRecord incomeRecord = null;
        
        try {
            incomeRecord = incomeRecordService.findOne( id, year, month );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if( incomeRecord == null ) {
            JOptionPane.showMessageDialog( this, "載入資料失敗", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }
        
        classTextField.setText( "" );
        classNameTextField.setText( "" );
        if( incomeRecord.getAmount() > 0 ) {
            incomeTypeTextField.setText( "收" );
        } else if( incomeRecord.getAmount() < 0 ) {
            incomeTypeTextField.setText( "支" );
        } else {
            incomeTypeTextField.setText( "" );
        }
        yearTextField.setText( String.format( "%04d", incomeRecord.getYear() ) );
        monthTextField.setText( String.format( "%02d", incomeRecord.getMonth() ) );
        dayTextField.setText( String.format( "%02d", incomeRecord.getDay() ) );
        itemTextField.setText( incomeRecord.getItem() );
        amountTextField.setText( String.format( "%d", Math.abs( incomeRecord.getAmount() ) ) );
        descriptionTextArea.setText( CsvFormatParser.restoreCharacterFromHtmlFormat( incomeRecord.getDescription() ) );
        
        confirmButton.requestFocus();
        setVisible( true );
    }
    
    private class FocusHandler extends FocusAdapter {
        @Override
        public void focusGained( FocusEvent event ) {
            JTextField sourceComponent = (JTextField) event.getSource();
            sourceComponent.selectAll();
        }
    }
    
    private class MnemonicKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_ESCAPE:
                setVisible( false );
                break;
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
}
