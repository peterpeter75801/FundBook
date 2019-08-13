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

public class FundingStatusPropertyUpdateDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private FundingStatusService fundingStatusService;
    
    private FundingStatus originalFundingStatus;
    private boolean popupMenuClosedFlag;
    private boolean defaultTypeFlag;
    
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
    private JLabel typeLabel;
    private JTextField typeTextField;
    private JRadioButton cashRadioButton;
    private JRadioButton demandDepositRadioButton;
    private JRadioButton timeDepositRadioButton;
    private JRadioButton creditCardRadioButton;
    private ButtonGroup typeButtonGroup;
    private JLabel fundNameLabel;
    private JTextField fundNameTextField;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JLabel dollarLabel;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;
    private JScrollPane descriptionScrollPane;
    private JButton updateButton;
    private JButton cancelButton;
    
    public FundingStatusPropertyUpdateDialog( FundingStatusService fundingStatusService, MainFrame ownerFrame ) {
        super( ownerFrame, "Update Funding Status Property", true );
        
        this.fundingStatusService = fundingStatusService;
        
        this.ownerFrame = ownerFrame;
        
        copyAndPastePopUpMenu = new CopyAndPastePopUpMenu();
        copyAndPastePopUpMenu.addPopupMenuListener( new PopupMenuClosingHandler() );
        
        undoManager = new UndoManager();
        focusHandler = new FocusHandler();
        mnemonicKeyHandler = new MnemonicKeyHandler();
        copyPasteMenuKeyHandler = new CopyPasteMenuKeyHandler( copyAndPastePopUpMenu );
        copyPasteMouseMenuHandler = new CopyPasteMouseMenuHandler( copyAndPastePopUpMenu );
        undoEditHandler = new UndoEditHandler();
        undoHotKeyHandler = new UndoHotKeyHandler();
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        dialogPanel = new JPanel();
        dialogPanel.setLayout( null );
        
        typeLabel = new JLabel( "種類: " );
        typeLabel.setBounds( 16, 10, 48, 22 );
        typeLabel.setFont( generalFont );
        dialogPanel.add( typeLabel );
        
        typeTextField = new JTextField();
        typeTextField.setBounds( 64, 10, 72, 22 );
        typeTextField.setFont( generalFont );
        typeTextField.setEditable( false );
        typeTextField.setVisible( false );
        typeTextField.addFocusListener( focusHandler );
        typeTextField.addKeyListener( mnemonicKeyHandler );
        typeTextField.addKeyListener( copyPasteMenuKeyHandler );
        typeTextField.addMouseListener( copyPasteMouseMenuHandler );
        typeTextField.addKeyListener( undoHotKeyHandler );
        typeTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( typeTextField );
        
        cashRadioButton = new JRadioButton( "現金(C)", true );
        cashRadioButton.setBounds( 64, 10, 80, 22 );
        cashRadioButton.setFont( generalFont );
        cashRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        cashRadioButton.addKeyListener( mnemonicKeyHandler );
        cashRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( cashRadioButton );
        
        demandDepositRadioButton = new JRadioButton( "活存(D)", false );
        demandDepositRadioButton.setBounds( 152, 10, 80, 22 );
        demandDepositRadioButton.setFont( generalFont );
        demandDepositRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        demandDepositRadioButton.addKeyListener( mnemonicKeyHandler );
        demandDepositRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( demandDepositRadioButton );
        
        timeDepositRadioButton = new JRadioButton( "定存(T)", false );
        timeDepositRadioButton.setBounds( 240, 10, 80, 22 );
        timeDepositRadioButton.setFont( generalFont );
        timeDepositRadioButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        timeDepositRadioButton.addKeyListener( mnemonicKeyHandler );
        timeDepositRadioButton.addKeyListener( radioButtonKeyHandler );
        dialogPanel.add( timeDepositRadioButton );
        
        creditCardRadioButton = new JRadioButton( "信用卡(R)", false );
        creditCardRadioButton.setBounds( 328, 10, 96, 22 );
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
        fundNameLabel.setBounds( 16, 42, 152, 22 );
        fundNameLabel.setFont( generalFont );
        dialogPanel.add( fundNameLabel );
        
        fundNameTextField = new JTextField();
        fundNameTextField.setBounds( 168, 42, 296, 22 );
        fundNameTextField.setFont( generalFont );
        fundNameTextField.addFocusListener( focusHandler );
        fundNameTextField.addKeyListener( mnemonicKeyHandler );
        fundNameTextField.addKeyListener( copyPasteMenuKeyHandler );
        fundNameTextField.addMouseListener( copyPasteMouseMenuHandler );
        fundNameTextField.addKeyListener( undoHotKeyHandler );
        fundNameTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( fundNameTextField );
        
        amountLabel = new JLabel( "金額: " );
        amountLabel.setBounds( 16, 74, 48, 22 );
        amountLabel.setFont( generalFont );
        dialogPanel.add( amountLabel );
        
        amountTextField = new JTextField();
        amountTextField.setBounds( 64, 74, 72, 22 );
        amountTextField.setFont( generalFont );
        amountTextField.setEditable( false );
        amountTextField.addFocusListener( focusHandler );
        amountTextField.addKeyListener( mnemonicKeyHandler );
        amountTextField.addKeyListener( copyPasteMenuKeyHandler );
        amountTextField.addMouseListener( copyPasteMouseMenuHandler );
        amountTextField.addKeyListener( undoHotKeyHandler );
        amountTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( amountTextField );
        
        dollarLabel = new JLabel( "元" );
        dollarLabel.setBounds( 136, 74, 16, 22 );
        dollarLabel.setFont( generalFont );
        dialogPanel.add( dollarLabel );
        
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
        
        updateButton = new JButton( "修改" );
        updateButton.setBounds( 168, 275, 48, 22 );
        updateButton.setFont( generalFont );
        updateButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        updateButton.addKeyListener( mnemonicKeyHandler );
        updateButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                updateFundingStatusProperty();
            }
        });
        dialogPanel.add( updateButton );
        
        cancelButton = new JButton( "取消" );
        cancelButton.setBounds( 264, 275, 48, 22 );
        cancelButton.setFont( generalFont );
        cancelButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        cancelButton.addKeyListener( mnemonicKeyHandler );
        cancelButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                setVisible( false );
            }
        });
        dialogPanel.add( cancelButton );
        
        dialogPanel.setPreferredSize( new Dimension( 482, 308 ) );
        add( dialogPanel );
        
        pack();
        setLocationRelativeTo( ownerFrame );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setVisible( false );
    }
    
    public void openDialog( int fundingStatusId ) {
        try {
            originalFundingStatus = fundingStatusService.findOne( fundingStatusId );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if( originalFundingStatus == null ) {
            JOptionPane.showMessageDialog( this, "載入資料失敗", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( originalFundingStatus.getType() == '0' ) {
            typeTextField.setVisible( true );
            typeTextField.setText( "預設" );
            cashRadioButton.setVisible( false );
            demandDepositRadioButton.setVisible( false );
            timeDepositRadioButton.setVisible( false );
            creditCardRadioButton.setVisible( false );
            defaultTypeFlag = true;
        } else {
            typeTextField.setVisible( false );
            cashRadioButton.setVisible( true );
            demandDepositRadioButton.setVisible( true );
            timeDepositRadioButton.setVisible( true );
            creditCardRadioButton.setVisible( true );
            cashRadioButton.setSelected( false );
            demandDepositRadioButton.setSelected( false );
            timeDepositRadioButton.setSelected( false );
            creditCardRadioButton.setSelected( false );
            defaultTypeFlag = false;
        }
        switch( originalFundingStatus.getType() ) {
        case 'C':
            cashRadioButton.setSelected( true );
            break;
        case 'D':
            demandDepositRadioButton.setSelected( true );
            break;
        case 'T':
            timeDepositRadioButton.setSelected( true );
            break;
        case 'R':
            creditCardRadioButton.setSelected( true );
            break;
        default:
            break;
        }
        fundNameTextField.setText( originalFundingStatus.getStoredPlaceOrInstitution() );
        amountTextField.setText( Integer.toString( originalFundingStatus.getAmount() ) );
        descriptionTextArea.setText( CsvFormatParser.restoreCharacterFromHtmlFormat( originalFundingStatus.getDescription() ) );
        
        popupMenuClosedFlag = false;
        
        fundNameTextField.requestFocus();
        setVisible( true );
    }
    
    private void updateFundingStatusProperty() {
        int returnCode = 0;
        char selectedType = '\0';
        
        if( defaultTypeFlag ) {
            selectedType = 0;
        } else if( cashRadioButton.isSelected() ) {
            selectedType = 'C';
        } else if ( demandDepositRadioButton.isSelected() ) {
            selectedType = 'D';
        } else if ( timeDepositRadioButton.isSelected() ) {
            selectedType = 'T';
        } else if ( creditCardRadioButton.isSelected() ) {
            selectedType = 'R';
        }
        String description = "";
        if( CsvFormatParser.checkSpecialCharacter( descriptionTextArea.getText() ) ) {
            description = CsvFormatParser.specialCharacterToHtmlFormat( descriptionTextArea.getText() );
        } else {
            description = descriptionTextArea.getText();
        }
        try {
            returnCode = fundingStatusService.updateProperty( originalFundingStatus.getId(), 
                    selectedType, fundNameTextField.getText(), description);
        } catch ( Exception e ) {
            e.printStackTrace();
            returnCode = Contants.ERROR;
        }
        
        switch( returnCode ) {
        case Contants.SUCCESS:
            setVisible( false );
            if( originalFundingStatus != null ) {
                ownerFrame.getFundingStatusPanel().refreshAndFind( originalFundingStatus );
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
                if( event.getSource() != cancelButton ) {
                    updateFundingStatusProperty();
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
