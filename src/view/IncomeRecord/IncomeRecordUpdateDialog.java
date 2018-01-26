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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import common.Contants;
import commonUtil.CsvFormatParser;
import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import service.IncomeRecordService;
import view.MainFrame;

public class IncomeRecordUpdateDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordService incomeRecordService;
    
    private IncomeRecord originIncomeRecord;
    
    private MainFrame ownerFrame;
    
    private FocusHandler focusHandler;
    private MnemonicKeyHandler mnemonicKeyHandler;
    private RadioButtonKeyHandler radioButtonKeyHandler;
    private Font generalFont;
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
    private JButton createButton;
    private JButton finishButton;
    
    public IncomeRecordUpdateDialog( IncomeRecordService incomeRecordService, MainFrame ownerFrame ) {
        super( ownerFrame, "Update Income Record", false );
        
        this.incomeRecordService = incomeRecordService;
        
        this.ownerFrame = ownerFrame;
        
        focusHandler = new FocusHandler();
        mnemonicKeyHandler = new MnemonicKeyHandler();
        radioButtonKeyHandler = new RadioButtonKeyHandler();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
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
        classTextField.addKeyListener( mnemonicKeyHandler );
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
        incomeRadioButton.addKeyListener( mnemonicKeyHandler );
        incomeRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( incomeRadioButton );
        
        expenditureRadioButton = new JRadioButton( "支(E)", true );
        expenditureRadioButton.setBounds( 136, 42, 64, 22 );
        expenditureRadioButton.setFont( generalFont );
        expenditureRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        expenditureRadioButton.addKeyListener( mnemonicKeyHandler );
        expenditureRadioButton.addKeyListener( radioButtonKeyHandler );
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
        yearTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( yearTextField );
        
        yearLabel = new JLabel( "年" );
        yearLabel.setBounds( 304+65, 42, 16, 22 );
        yearLabel.setFont( generalFont );
        dialogPanel.add( yearLabel );
        
        monthTextField = new JTextField( 2 );
        monthTextField.setBounds( 320+65, 42, 24, 22 );
        monthTextField.setFont( generalFont );
        monthTextField.addFocusListener( focusHandler );
        monthTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( monthTextField );
        
        monthLabel = new JLabel( "月" );
        monthLabel.setBounds( 344+65, 42, 16, 22 );
        monthLabel.setFont( generalFont );
        dialogPanel.add( monthLabel );
        
        dayTextField = new JTextField( 2 );
        dayTextField.setBounds( 360+65, 42, 24, 22 );
        dayTextField.setFont( generalFont );
        dayTextField.addFocusListener( focusHandler );
        dayTextField.addKeyListener( mnemonicKeyHandler );
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
        itemTextField.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( itemTextField );
        
        amountLabel = new JLabel( "金額: " );
        amountLabel.setBounds( 328-47, 74, 48, 22 );
        amountLabel.setFont( generalFont );
        dialogPanel.add( amountLabel );
        
        amountTextField = new JTextField();
        amountTextField.setBounds( 376-47, 74, 72, 22 );
        amountTextField.setFont( generalFont );
        amountTextField.addFocusListener( focusHandler );
        amountTextField.addKeyListener( mnemonicKeyHandler );
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
        descriptionTextArea.setSize( 425, 132 );
        descriptionTextArea.setFont( generalFont );
        descriptionTextArea.setLineWrap( true );
        descriptionTextArea.setWrapStyleWord( true );
        //descriptionTextArea.addKeyListener( undoHotKeyHandler );
        //descriptionTextArea.getDocument().addUndoableEditListener( undoEditHandler );
        descriptionScrollPane = new JScrollPane( descriptionTextArea );
        descriptionScrollPane.setBounds( 16, 128, 449, 137 );
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
        
        createButton = new JButton( "修改" );
        createButton.setBounds( 168, 290, 48, 22 );
        createButton.setFont( generalFont );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        createButton.addKeyListener( mnemonicKeyHandler );
        createButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                updateIncomeRecord();
            }
        });
        dialogPanel.add( createButton );
        
        finishButton = new JButton( "取消" );
        finishButton.setBounds( 264, 290, 48, 22 );
        finishButton.setFont( generalFont );
        finishButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        finishButton.addKeyListener( mnemonicKeyHandler );
        finishButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                setVisible( false );
            }
        });
        dialogPanel.add( finishButton );
        
        dialogPanel.setPreferredSize( new Dimension( 482, 340 ) );
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
        
        originIncomeRecord = IncomeRecordUtil.copy( incomeRecord );
        
        classTextField.setText( "" );
        classNameTextField.setText( "" );
        
        incomeRadioButton.setSelected( false );
        expenditureRadioButton.setSelected( false );
        if( incomeRecord.getAmount() != null && incomeRecord.getAmount() > 0 ) {
            incomeRadioButton.setSelected( true );
        } else {
            expenditureRadioButton.setSelected( true );
        }
        
        yearTextField.setText( String.format( "%04d", incomeRecord.getYear() ) );
        monthTextField.setText( String.format( "%02d", incomeRecord.getMonth() ) );
        dayTextField.setText( String.format( "%02d", incomeRecord.getDay() ) );
        itemTextField.setText( incomeRecord.getItem() );
        amountTextField.setText( String.format( "%d", Math.abs( incomeRecord.getAmount() ) ) );
        
        itemTextField.requestFocus();
        setVisible( true );
    }
    
    public void updateIncomeRecord() {
        int returnCode = 0;
        try {
            IncomeRecord incomeRecord = new IncomeRecord();
            incomeRecord.setId( originIncomeRecord.getId() );
            incomeRecord.setYear( Integer.parseInt( yearTextField.getText() ) );
            incomeRecord.setMonth( Integer.parseInt( monthTextField.getText() ) );
            incomeRecord.setDay( Integer.parseInt( dayTextField.getText() ) );
            incomeRecord.setItem( itemTextField.getText() );
            incomeRecord.setClassNo( 0 );
            if( incomeRadioButton.isSelected() ) {
                incomeRecord.setAmount( Integer.parseInt( amountTextField.getText() ) );
            } else {
                incomeRecord.setAmount( Integer.parseInt( amountTextField.getText() ) * (-1) );
            }
            if( CsvFormatParser.checkSpecialCharacter( descriptionTextArea.getText() ) ) {
                incomeRecord.setDescription( CsvFormatParser.specialCharacterToHtmlFormat( descriptionTextArea.getText() ) );
            } else {
                incomeRecord.setDescription( descriptionTextArea.getText() );
            }
            incomeRecord.setOrderNo( 0 );
            
            returnCode = incomeRecordService.update( incomeRecord );
        } catch ( Exception e ) {
            e.printStackTrace();
            returnCode = Contants.ERROR;
        }
        
        switch( returnCode ) {
        case Contants.SUCCESS:
            setVisible( false );
            ownerFrame.getIncomeRecordPanel().reselectDateList();
            break;
        case Contants.ERROR:
            JOptionPane.showMessageDialog( null, "更新失敗", "Error", JOptionPane.ERROR_MESSAGE );
            break;
        default:
            break;
        }
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
                if( event.getSource() != finishButton ) {
                    updateIncomeRecord();
                } else {
                    setVisible( false );
                }
                break;
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
    
    private class RadioButtonKeyHandler implements KeyListener {

        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.getKeyCode() == KeyEvent.VK_I || event.getKeyCode() == KeyEvent.VK_E ) {
                incomeRadioButton.setSelected( false );
                expenditureRadioButton.setSelected( false );
            }
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_I:
                incomeRadioButton.requestFocus();
                incomeRadioButton.setSelected( true );
                break;
            case KeyEvent.VK_E:
                expenditureRadioButton.requestFocus();
                expenditureRadioButton.setSelected( true );
                break;
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
}
