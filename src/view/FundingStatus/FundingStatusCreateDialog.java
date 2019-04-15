package view.FundingStatus;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import common.Contants;
import commonUtil.CsvFormatParser;
import domain.FundingStatus;
import service.FundingStatusService;
import view.MainFrame;
import view.common.CopyAndPastePopUpMenu;

public class FundingStatusCreateDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private FundingStatusService fundingStatusService;
    
    private FundingStatus lastCreatedFundingStatus;
    private boolean popupMenuClosedFlag;
    
    private MainFrame ownerFrame;

    private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
    private UndoManager undoManager;
    private FocusHandler focusHandler;
    private MnemonicKeyHandler mnemonicKeyHandler;
    private CopyPasteMenuKeyHandler copyPasteMenuKeyHandler;
    private CopyPasteMouseMenuHandler copyPasteMouseMenuHandler;
    private RadioButtonKeyHandler radioButtonKeyHandler;
    private UndoEditHandler undoEditHandler;
    private UndoHotKeyHandler undoHotKeyHandler;
    private Font generalFont;
    private JPanel dialogPanel;
    private JLabel dateLabel;
    private JTextField yearTextField;
    private JLabel yearLabel;
    private JTextField monthTextField;
    private JLabel monthLabel;
    private JTextField dayTextField;
    private JLabel dayLabel;
    private JLabel typeLabel;
    private JRadioButton cashRadioButton;
    private JRadioButton demandDepositRadioButton;
    private JRadioButton timeDepositRadioButton;
    private JRadioButton creditCardRadioButton;
    private ButtonGroup typeButtonGroup;
    private JLabel fundNameLabel;
    private JTextField fundNameTextField;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;
    private JScrollPane descriptionScrollPane;
    private JButton createButton;
    private JButton finishButton;
    
    public FundingStatusCreateDialog( FundingStatusService fundingStatusService, MainFrame ownerFrame ) {
        super( ownerFrame, "Create Funding Status", true );
        
        this.fundingStatusService = fundingStatusService;
        
        this.ownerFrame = ownerFrame;
        
        copyAndPastePopUpMenu = new CopyAndPastePopUpMenu();
        copyAndPastePopUpMenu.addPopupMenuListener( new PopupMenuClosingHandler() );
        
        undoManager = new UndoManager();
        focusHandler = new FocusHandler();
        mnemonicKeyHandler = new MnemonicKeyHandler();
        copyPasteMenuKeyHandler = new CopyPasteMenuKeyHandler( copyAndPastePopUpMenu );
        copyPasteMouseMenuHandler = new CopyPasteMouseMenuHandler( copyAndPastePopUpMenu );
        radioButtonKeyHandler = new RadioButtonKeyHandler();
        undoEditHandler = new UndoEditHandler();
        undoHotKeyHandler = new UndoHotKeyHandler();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        dialogPanel = new JPanel();
        dialogPanel.setLayout( null );
        
        dateLabel = new JLabel( "日期: " );
        dateLabel.setBounds( 16, 10, 48, 22 );
        dateLabel.setFont( generalFont );
        dialogPanel.add( dateLabel );
        
        yearTextField = new JTextField( 4 );
        yearTextField.setBounds( 64, 10, 40, 22 );
        yearTextField.setFont( generalFont );
        yearTextField.setEditable( false );
        yearTextField.addFocusListener( focusHandler );
        yearTextField.addKeyListener( mnemonicKeyHandler );
        yearTextField.addKeyListener( copyPasteMenuKeyHandler );
        yearTextField.addMouseListener( copyPasteMouseMenuHandler );
        yearTextField.addKeyListener( undoHotKeyHandler );
        yearTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( yearTextField );
        
        yearLabel = new JLabel( "年" );
        yearLabel.setBounds( 104, 10, 16, 22 );
        yearLabel.setFont( generalFont );
        dialogPanel.add( yearLabel );
        
        monthTextField = new JTextField( 2 );
        monthTextField.setBounds( 120, 10, 24, 22 );
        monthTextField.setFont( generalFont );
        monthTextField.setEditable( false );
        monthTextField.addFocusListener( focusHandler );
        monthTextField.addKeyListener( mnemonicKeyHandler );
        monthTextField.addKeyListener( copyPasteMenuKeyHandler );
        monthTextField.addMouseListener( copyPasteMouseMenuHandler );
        monthTextField.addKeyListener( undoHotKeyHandler );
        monthTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( monthTextField );
        
        monthLabel = new JLabel( "月" );
        monthLabel.setBounds( 144, 10, 16, 22 );
        monthLabel.setFont( generalFont );
        dialogPanel.add( monthLabel );
        
        dayTextField = new JTextField( 2 );
        dayTextField.setBounds( 160, 10, 24, 22 );
        dayTextField.setFont( generalFont );
        dayTextField.setEditable( false );
        dayTextField.addFocusListener( focusHandler );
        dayTextField.addKeyListener( mnemonicKeyHandler );
        dayTextField.addKeyListener( copyPasteMenuKeyHandler );
        dayTextField.addMouseListener( copyPasteMouseMenuHandler );
        dayTextField.addKeyListener( undoHotKeyHandler );
        dayTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( dayTextField );
        
        dayLabel = new JLabel( "日" );
        dayLabel.setBounds( 184, 10, 16, 22 );
        dayLabel.setFont( generalFont );
        dialogPanel.add( dayLabel );
        
        typeLabel = new JLabel( "種類: " );
        typeLabel.setBounds( 16, 42, 48, 22 );
        typeLabel.setFont( generalFont );
        dialogPanel.add( typeLabel );
        
        cashRadioButton = new JRadioButton( "現金(C)", true );
        cashRadioButton.setBounds( 64, 42, 80, 22 );
        cashRadioButton.setFont( generalFont );
        cashRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        cashRadioButton.addKeyListener( mnemonicKeyHandler );
        cashRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( cashRadioButton );
        
        demandDepositRadioButton = new JRadioButton( "活存(D)", false );
        demandDepositRadioButton.setBounds( 152, 42, 80, 22 );
        demandDepositRadioButton.setFont( generalFont );
        demandDepositRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        demandDepositRadioButton.addKeyListener( mnemonicKeyHandler );
        demandDepositRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( demandDepositRadioButton );
        
        timeDepositRadioButton = new JRadioButton( "定存(T)", false );
        timeDepositRadioButton.setBounds( 240, 42, 80, 22 );
        timeDepositRadioButton.setFont( generalFont );
        timeDepositRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        timeDepositRadioButton.addKeyListener( mnemonicKeyHandler );
        timeDepositRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( timeDepositRadioButton );
        
        creditCardRadioButton = new JRadioButton( "信用卡(R)", false );
        creditCardRadioButton.setBounds( 328, 42, 96, 22 );
        creditCardRadioButton.setFont( generalFont );
        creditCardRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        creditCardRadioButton.addKeyListener( mnemonicKeyHandler );
        creditCardRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( creditCardRadioButton );
        
        typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add( cashRadioButton );
        typeButtonGroup.add( demandDepositRadioButton );
        typeButtonGroup.add( timeDepositRadioButton );
        typeButtonGroup.add( creditCardRadioButton );
        
        fundNameLabel = new JLabel( "儲存地點/儲存機構: " );
        fundNameLabel.setBounds( 16, 74, 152, 22 );
        fundNameLabel.setFont( generalFont );
        dialogPanel.add( fundNameLabel );
        
        fundNameTextField = new JTextField();
        fundNameTextField.setBounds( 168, 74, 296, 22 );
        fundNameTextField.setFont( generalFont );
        fundNameTextField.addFocusListener( focusHandler );
        fundNameTextField.addKeyListener( mnemonicKeyHandler );
        fundNameTextField.addKeyListener( copyPasteMenuKeyHandler );
        fundNameTextField.addMouseListener( copyPasteMouseMenuHandler );
        fundNameTextField.addKeyListener( undoHotKeyHandler );
        fundNameTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( fundNameTextField );
        
        descriptionLabel = new JLabel( "描述: " );
        descriptionLabel.setBounds( 16, 106, 48, 22 );
        descriptionLabel.setFont( generalFont );
        dialogPanel.add( descriptionLabel );
        
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setSize( 425, 132 );
        descriptionTextArea.setFont( generalFont );
        descriptionTextArea.addKeyListener( copyPasteMenuKeyHandler );
        descriptionTextArea.addMouseListener( copyPasteMouseMenuHandler );
        descriptionTextArea.addKeyListener( undoHotKeyHandler );
        descriptionTextArea.getDocument().addUndoableEditListener( undoEditHandler );
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
        
        createButton = new JButton( "新增" );
        createButton.setBounds( 168, 275, 48, 22 );
        createButton.setFont( generalFont );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        createButton.addKeyListener( mnemonicKeyHandler );
        createButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                createFundingStatus();
            }
        });
        dialogPanel.add( createButton );
        
        finishButton = new JButton( "取消" );
        finishButton.setBounds( 264, 275, 48, 22 );
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
        
        dialogPanel.setPreferredSize( new Dimension( 482, 308 ) );
        add( dialogPanel );
        
        pack();
        setLocationRelativeTo( ownerFrame );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setVisible( false );
    }
    
    public void createFundingStatus() {
        int returnCode = 0;
        try {
            FundingStatus fundingStatus = new FundingStatus();
            fundingStatus.setYear( Integer.parseInt( yearTextField.getText() ) );
            fundingStatus.setMonth( Integer.parseInt( monthTextField.getText() ) );
            fundingStatus.setDay( Integer.parseInt( dayTextField.getText() ) );
            fundingStatus.setStoredPlaceOrInstitution( fundNameTextField.getText() );
            if( cashRadioButton.isSelected() ) {
                fundingStatus.setType( 'C' );
            } else if( demandDepositRadioButton.isSelected() ) {
                fundingStatus.setType( 'D' );
            } else if( timeDepositRadioButton.isSelected() ) {
                fundingStatus.setType( 'T' );
            } else if( creditCardRadioButton.isSelected() ) {
                fundingStatus.setType( 'R' );
            } else {
                fundingStatus.setType( '\0' );
            }
            fundingStatus.setAmount( 0 );
            if( CsvFormatParser.checkSpecialCharacter( descriptionTextArea.getText() ) ) {
                fundingStatus.setDescription( CsvFormatParser.specialCharacterToHtmlFormat( descriptionTextArea.getText() ) );
            } else {
                fundingStatus.setDescription( descriptionTextArea.getText() );
            }
            fundingStatus.setOrderNo( 0 );
            
            returnCode = fundingStatusService.insert( fundingStatus );
            
            lastCreatedFundingStatus = fundingStatus;
        } catch ( Exception e ) {
            e.printStackTrace();
            returnCode = Contants.ERROR;
        }
        
        switch( returnCode ) {
        case Contants.SUCCESS:
            setVisible( false );
            if( lastCreatedFundingStatus != null ) {
                ownerFrame.getFundingStatusPanel().refreshAndFind( lastCreatedFundingStatus );
            } else {
                ownerFrame.getFundingStatusPanel().refresh();
            }
            break;
        case Contants.ERROR:
            JOptionPane.showMessageDialog( null, "更新失敗", "Error", JOptionPane.ERROR_MESSAGE );
            break;
        default:
            break;
        }
    }
    
    public void openDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        
        yearTextField.setText( String.format( "%04d", currentYear ) );
        monthTextField.setText( String.format( "%02d", currentMonth ) );
        dayTextField.setText( String.format( "%02d", currentDay ) );
        
        fundNameTextField.requestFocus();
        setVisible( true );
    }
    
    private class FocusHandler extends FocusAdapter {
        
        @Override
        public void focusGained( FocusEvent event ) {
            if( popupMenuClosedFlag ) {
                popupMenuClosedFlag = false;
            } else {
                JTextField sourceComponent = (JTextField) event.getSource();
                sourceComponent.selectAll();
            }
        }
    }
    
    private class MnemonicKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_ENTER:
                if( event.getSource() instanceof JTextComponent && !((JTextComponent)event.getSource()).isEditable() ) {
                    break;
                }
                if( event.getSource() != finishButton ) {
                    createFundingStatus();
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
    
    private class CopyPasteMenuKeyHandler implements KeyListener {
        
        private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
        
        public CopyPasteMenuKeyHandler( CopyAndPastePopUpMenu copyAndPastePopUpMenu ) {
            this.copyAndPastePopUpMenu = copyAndPastePopUpMenu;
        }
        
        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.getKeyCode() == KeyEvent.VK_CONTEXT_MENU ) {
                JTextComponent eventComponent = (JTextComponent)event.getComponent();
                int showPosX = (eventComponent.getCaret().getMagicCaretPosition() != null) ? 
                        (int)eventComponent.getCaret().getMagicCaretPosition().getX() : 0;
                int showPosY = (eventComponent.getCaret().getMagicCaretPosition() != null) ? 
                        (int)eventComponent.getCaret().getMagicCaretPosition().getY() : 0;
                copyAndPastePopUpMenu.show( eventComponent, showPosX, showPosY );
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
    
    private class CopyPasteMouseMenuHandler extends MouseAdapter {
        
        private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
        
        public CopyPasteMouseMenuHandler( CopyAndPastePopUpMenu copyAndPastePopUpMenu ) {
            this.copyAndPastePopUpMenu = copyAndPastePopUpMenu;
        }
        
        public void mousePressed( MouseEvent event ) {
            if( event.isPopupTrigger() ) {
                copyAndPastePopUpMenu.show( event.getComponent(), event.getX(), event.getY() );
            }
        }
        
        public void mouseReleased( MouseEvent event ) {
            if( event.isPopupTrigger() ) {
                copyAndPastePopUpMenu.show( event.getComponent(), event.getX(), event.getY() );
            }
        }
    }
    
    private class PopupMenuClosingHandler implements PopupMenuListener {
        
        @Override
        public void popupMenuCanceled( PopupMenuEvent event ) {
            popupMenuClosedFlag = true;
            ((JPopupMenu)event.getSource()).getInvoker().requestFocus();
        }

        @Override
        public void popupMenuWillBecomeInvisible( PopupMenuEvent event ) {
            popupMenuClosedFlag = true;
            ((JPopupMenu)event.getSource()).getInvoker().requestFocus();
        }

        @Override
        public void popupMenuWillBecomeVisible( PopupMenuEvent event ) {}
    }
    
    private class RadioButtonKeyHandler implements KeyListener {

        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.getKeyCode() == KeyEvent.VK_C || event.getKeyCode() == KeyEvent.VK_D || 
                    event.getKeyCode() == KeyEvent.VK_T || event.getKeyCode() == KeyEvent.VK_R ) {
                cashRadioButton.setSelected( false );
                demandDepositRadioButton.setSelected( false );
                timeDepositRadioButton.setSelected( false );
                creditCardRadioButton.setSelected( false );
            }
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_C:
                cashRadioButton.requestFocus();
                cashRadioButton.setSelected( true );
                break;
            case KeyEvent.VK_D:
                demandDepositRadioButton.requestFocus();
                demandDepositRadioButton.setSelected( true );
                break;
            case KeyEvent.VK_T:
                timeDepositRadioButton.requestFocus();
                timeDepositRadioButton.setSelected( true );
                break;
            case KeyEvent.VK_R:
                creditCardRadioButton.requestFocus();
                creditCardRadioButton.setSelected( true );
                break;
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
    
    private class UndoEditHandler implements UndoableEditListener {
        
        @Override
        public void undoableEditHappened( UndoableEditEvent event ) {
            undoManager.addEdit( event.getEdit() );
        }
    }
    
    private class UndoHotKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Z && undoManager.canUndo() ) {
                undoManager.undo();
            } else if( event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Y && undoManager.canRedo() ) {
                undoManager.redo();
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
}
