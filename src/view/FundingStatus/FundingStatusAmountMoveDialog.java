package view.FundingStatus;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import common.Contants;
import commonUtil.StringUtil;
import domain.FundingStatus;
import service.FundingStatusService;
import view.MainFrame;
import view.common.CopyAndPastePopUpMenu;

public class FundingStatusAmountMoveDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private static final String[] PREVIEW_TABLE_COLUMN_NAMES = { "儲存地點/儲存機構", "移動前金額", "移動後金額" };
    private static final int DEFAULT_PREVIEW_TABLE_ROW_COUNT = 2;
    private static final int TABLE_WIDTH = 450;
    private static final int TABLE_HEADER_HEIGHT = 22;
    private static final int TABLE_ROW_HEIGHT = 22;
    private static final int[] TABLE_COLUMN_WIDTH = { 290, 80, 80 };
    private static final int SOURCE_FUND_ROW = 0;
    private static final int DESTINATION_FUND_ROW = 1;
    private static final int FUND_NAME_COLUMN = 0;
    private static final int PRE_AMOUNT_COLUMN = 1;
    private static final int POST_AMOUNT_COLUMN = 2;
    
    private enum STATUS { CONFIGURING, PREVIEWING, UPDATING, CLOSING }
    
    private FundingStatusService fundingStatusService;
    
    private FundingStatus sourceFundingStatus;
    private FundingStatus destinationFundingStatus;
    private int amountToMove;
    
    private boolean popupMenuClosedFlag;
    private STATUS dialogStatus;
    
    private MainFrame ownerFrame;
    
    private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
    private UndoManager undoManager;
    private FocusHandler focusHandler;
    private MnemonicKeyHandler mnemonicKeyHandler;
    private CopyPasteMenuKeyHandler copyPasteMenuKeyHandler;
    private CopyPasteMouseMenuHandler copyPasteMouseMenuHandler;
    private UndoEditHandler undoEditHandler;    // TODO: 如果在預覽狀態按下Ctrl+Z的話，則進行回復+取消預覽
    private UndoHotKeyHandler undoHotKeyHandler;
    private Font generalFont;
    private JPanel dialogPanel;
    private JLabel sourceFundLabel;
    private JTextField sourceFundTextField;
    private JLabel destinationFundLabel;
    private JComboBox<FundingStatusListItem> destinationFundComboBox;
    private JTextField destinationFundTextField;
    private JLabel amountToMoveLabel;
    private JTextField amountToMoveTextField;
    private JLabel dollarLabel;
    private JLabel previewLabel;
    private JTable previewTable;
    private JScrollPane previewTableScrollPane;
    private JButton previewAndConfirmButton;
    private JButton cancelButton;
    
    public FundingStatusAmountMoveDialog( FundingStatusService fundingStatusService, MainFrame ownerFrame ) {
        super( ownerFrame, "Move Funding Status Amount", true );
        
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
        
        sourceFundLabel = new JLabel( "移出金額項目: " );
        sourceFundLabel.setBounds( 16, 10, 112, 22 );
        sourceFundLabel.setFont( generalFont );
        dialogPanel.add( sourceFundLabel );
        
        sourceFundTextField = new JTextField();
        sourceFundTextField.setBounds( 128, 10, 336, 22 );
        sourceFundTextField.setFont( generalFont );
        sourceFundTextField.setEditable( false );
        sourceFundTextField.addFocusListener( focusHandler );
        sourceFundTextField.addKeyListener( mnemonicKeyHandler );
        sourceFundTextField.addKeyListener( copyPasteMenuKeyHandler );
        sourceFundTextField.addMouseListener( copyPasteMouseMenuHandler );
        dialogPanel.add( sourceFundTextField );
        
        destinationFundLabel = new JLabel( "移入金額項目: " );
        destinationFundLabel.setBounds( 16, 42, 112, 22 );
        destinationFundLabel.setFont( generalFont );
        dialogPanel.add( destinationFundLabel );
        
        destinationFundComboBox = new JComboBox<FundingStatusListItem>();
        destinationFundComboBox.setBounds( 128, 42, 336, 22 );
        destinationFundComboBox.setFont( generalFont );
        destinationFundComboBox.addKeyListener( mnemonicKeyHandler );
        dialogPanel.add( destinationFundComboBox );
        
        destinationFundTextField = new JTextField();
        destinationFundTextField.setBounds( 128, 42, 336, 22 );
        destinationFundTextField.setFont( generalFont );
        destinationFundTextField.setEditable( false );
        destinationFundTextField.setVisible( false );
        destinationFundTextField.addFocusListener( focusHandler );
        destinationFundTextField.addKeyListener( mnemonicKeyHandler );
        destinationFundTextField.addKeyListener( copyPasteMenuKeyHandler );
        destinationFundTextField.addMouseListener( copyPasteMouseMenuHandler );
        dialogPanel.add( destinationFundTextField );
        
        amountToMoveLabel = new JLabel( "移動金額: " );
        amountToMoveLabel.setBounds( 16, 74, 80, 22 );
        amountToMoveLabel.setFont( generalFont );
        dialogPanel.add( amountToMoveLabel );
        
        amountToMoveTextField = new JTextField();
        amountToMoveTextField.setBounds( 96, 74, 72, 22 );
        amountToMoveTextField.setFont( generalFont );
        amountToMoveTextField.addFocusListener( focusHandler );
        amountToMoveTextField.addKeyListener( mnemonicKeyHandler );
        amountToMoveTextField.addKeyListener( copyPasteMenuKeyHandler );
        amountToMoveTextField.addMouseListener( copyPasteMouseMenuHandler );
        amountToMoveTextField.addKeyListener( undoHotKeyHandler );
        amountToMoveTextField.getDocument().addUndoableEditListener( undoEditHandler );
        dialogPanel.add( amountToMoveTextField );
        
        dollarLabel = new JLabel( "元" );
        dollarLabel.setBounds( 168, 74, 16, 22 );
        dollarLabel.setFont( generalFont );
        dialogPanel.add( dollarLabel );
        
        previewLabel = new JLabel( "預覽: " );
        previewLabel.setBounds( 16, 106, 48, 22 );
        previewLabel.setFont( generalFont );
        dialogPanel.add( previewLabel );
        
        initialPreviewTable();
        previewTable.addKeyListener( mnemonicKeyHandler );
        previewTableScrollPane = new JScrollPane( previewTable );
        previewTableScrollPane.setBounds( 16, 128, 450, 69 );
        dialogPanel.add( previewTableScrollPane );
        
        previewAndConfirmButton = new JButton( "預覽" );
        previewAndConfirmButton.setBounds( 168, 227, 48, 22 );
        previewAndConfirmButton.setFont( generalFont );
        previewAndConfirmButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        previewAndConfirmButton.addKeyListener( mnemonicKeyHandler );
        previewAndConfirmButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                previewOrMoveFundingStatusAmount();
            }
        });
        dialogPanel.add( previewAndConfirmButton );
        
        cancelButton = new JButton( "取消" );
        cancelButton.setBounds( 264, 227, 48, 22 );
        cancelButton.setFont( generalFont );
        cancelButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        cancelButton.addKeyListener( mnemonicKeyHandler );
        cancelButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                cancelConfiguration();
            }
        });
        dialogPanel.add( cancelButton );
        
        dialogPanel.setPreferredSize( new Dimension( 482, 272 ) );
        add( dialogPanel );
        
        pack();
        setLocationRelativeTo( ownerFrame );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        addComponentListener( new ComponentAdapter() {
            @Override
            public void componentHidden( ComponentEvent e ) {
                System.out.println("componentHidden()");
                dialogStatus = STATUS.CLOSING;
            }
        });
        setVisible( false );
        
        dialogStatus = STATUS.CLOSING;
    }
    
    public void openDialog( int sourceId ) {
        sourceFundingStatus = null;
        destinationFundingStatus = null;
        
        try {
            sourceFundingStatus = fundingStatusService.findOne( sourceId );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if( sourceFundingStatus == null ) {
            JOptionPane.showMessageDialog( this, "載入移出金額項目資料失敗", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }
        sourceFundTextField.setText( sourceFundingStatus.getStoredPlaceOrInstitution() );
        
        initializeDestinationFundComboBox();
        destinationFundComboBox.setVisible( true );
        destinationFundTextField.setVisible( false );
        
        amountToMove = 0;
        amountToMoveTextField.setText( "" );
        amountToMoveTextField.setEditable( true );
        
        clearPreviewTable();
        setPreviewTableSourceFundName( sourceFundingStatus.getStoredPlaceOrInstitution() );
        setPreviewTableSourceFundPreAmount( sourceFundingStatus.getAmount() );
        
        previewAndConfirmButton.setText( "預覽" );
        
        dialogStatus = STATUS.CONFIGURING;
        destinationFundComboBox.requestFocus();
        setVisible( true );
    }
    
    private void previewOrMoveFundingStatusAmount() {
        int returnCode = 0;
        int selectedDestinationFundId = destinationFundComboBox.getItemAt( destinationFundComboBox.getSelectedIndex() ).getId();
        if( selectedDestinationFundId == sourceFundingStatus.getId() ) {
            JOptionPane.showMessageDialog( this, "移出金額項目不能與移入金額項目相同", "Warning", JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        if( !StringUtil.isNumber( amountToMoveTextField.getText() ) ) {
            JOptionPane.showMessageDialog( this, "移動金額需要為數字", "Warning", JOptionPane.WARNING_MESSAGE );
            return;
        }
        amountToMove = Integer.parseInt( amountToMoveTextField.getText() );
        if( amountToMove <= 0 ) {
            JOptionPane.showMessageDialog( this, "移動金額需要大於0", "Warning", JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        try {
            destinationFundingStatus = fundingStatusService.findOne( selectedDestinationFundId );
        } catch ( Exception e ) {
            JOptionPane.showMessageDialog( this, "載入移入金額項目資料失敗", "Error", JOptionPane.ERROR_MESSAGE );
            e.printStackTrace();
            return;
        }
        
        switch( dialogStatus ) {
        case CONFIGURING:
            setPreviewTableDestinationFundName( destinationFundingStatus.getStoredPlaceOrInstitution() );
            setPreviewTableDestinationFundPreAmount( destinationFundingStatus.getAmount() );
            setPreviewTableSourceFundPostAmount( sourceFundingStatus.getAmount() - amountToMove );
            setPreviewTableDestinationFundPostAmount( destinationFundingStatus.getAmount() + amountToMove );
            
            previewAndConfirmButton.setText( "確認" );
            destinationFundComboBox.setEnabled( false );
            destinationFundComboBox.setVisible( false );
            destinationFundTextField.setText(
                    destinationFundComboBox.getItemAt( destinationFundComboBox.getSelectedIndex() ).getName() );
            destinationFundTextField.setVisible( true );
            amountToMoveTextField.setEditable( false );
            
            dialogStatus = STATUS.PREVIEWING;
            previewAndConfirmButton.requestFocus();
            break;
        case PREVIEWING:
            try {
                fundingStatusService.moveAmount( sourceFundingStatus.getId(), destinationFundingStatus.getId(), amountToMove );
            } catch ( Exception e ) {
                e.printStackTrace();
                returnCode = Contants.ERROR;
            }
            dialogStatus = STATUS.UPDATING;
            break;
        default:
            break;
        }
        
        if( dialogStatus == STATUS.UPDATING ) {
            switch( returnCode ) {
            case Contants.SUCCESS:
                setVisible( false );
                if( sourceFundingStatus != null ) {
                    ownerFrame.getFundingStatusPanel().refreshAndFind( sourceFundingStatus );
                } else {
                    ownerFrame.getFundingStatusPanel().refresh();
                }
                dialogStatus = STATUS.CLOSING;
                break;
            case Contants.ERROR:
                JOptionPane.showMessageDialog( null, "更新失敗", "Error", JOptionPane.ERROR_MESSAGE );
                dialogStatus = STATUS.PREVIEWING;
                break;
            }
        }
    }
    
    private void cancelConfiguration() {
        switch( dialogStatus ) {
        case CONFIGURING:
            dialogStatus = STATUS.CLOSING;
            setVisible( false );
            break;
        case PREVIEWING:
            destinationFundComboBox.setEnabled( true );
            destinationFundComboBox.setVisible( true );
            destinationFundTextField.setVisible( false );
            
            amountToMoveTextField.setEditable( true );
            setPreviewTableDestinationFundName( "" );
            setPreviewTableDestinationFundPreAmount( null );
            setPreviewTableSourceFundPostAmount( null );
            setPreviewTableDestinationFundPostAmount( null );
            
            dialogStatus = STATUS.CONFIGURING;
            destinationFundComboBox.requestFocus();
            break;
        default:
            break;
        }
    }
    
    private void initializeDestinationFundComboBox() {
        destinationFundComboBox.setEnabled( true );
        try {
            List<FundingStatus> fundingStatusList = fundingStatusService.findAll();
            List<FundingStatusListItem> items = new ArrayList<FundingStatusListItem>();
            for( FundingStatus fundingStatus : fundingStatusList ) {
                items.add( new FundingStatusListItem( fundingStatus.getId(), fundingStatus.getStoredPlaceOrInstitution() ) );
            }
            destinationFundComboBox.setModel( new DefaultComboBoxModel<FundingStatusListItem>(
                    items.toArray( new FundingStatusListItem[ items.size() ] ) ) );
            if( items.size() > 0 ) {
                destinationFundComboBox.setSelectedIndex( 0 );
            }
        } catch (Exception e) {
            destinationFundComboBox.setModel( new DefaultComboBoxModel<FundingStatusListItem>() );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "取得財務儲存狀況清單失敗", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    private void initialPreviewTable() {
        previewTable = new JTable( new DefaultTableModel( PREVIEW_TABLE_COLUMN_NAMES, DEFAULT_PREVIEW_TABLE_ROW_COUNT ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable( int rowIndex, int columnIndex ) {
                return false;
            }
        });
        previewTable.setFont( new Font( "細明體", Font.PLAIN, 12 ) );
        previewTable.getTableHeader().setReorderingAllowed( false );
        previewTable.getTableHeader().setPreferredSize( new Dimension( TABLE_WIDTH, TABLE_HEADER_HEIGHT ) );
        previewTable.setRowHeight( TABLE_ROW_HEIGHT );
        previewTable.getColumnModel().getColumn( 0 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 0 ] );
        previewTable.getColumnModel().getColumn( 1 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 1 ] );
        previewTable.getColumnModel().getColumn( 2 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 2 ] );
        previewTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        
        // 設定 table 的 TAB 鍵切換元件功能
        previewTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
            KeyStroke.getKeyStroke( KeyEvent.VK_TAB, 0 ), "none" );
        previewTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
            KeyStroke.getKeyStroke( KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK ), "none" );
        previewTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( 
            KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "none" );
        previewTable.addKeyListener( new SpecialFocusTraversalPolicyHandler() );
        
        // 設定 table 的欄位樣式
        previewTable.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            private Border padding = BorderFactory.createEmptyBorder( 0, 3, 0, 3 );
            
            @Override
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                switch( column ) {
                case FUND_NAME_COLUMN:
                    setHorizontalAlignment( SwingConstants.LEFT );
                    break;
                case PRE_AMOUNT_COLUMN:
                    setHorizontalAlignment( SwingConstants.RIGHT );
                    break;
                case POST_AMOUNT_COLUMN:
                    setHorizontalAlignment( SwingConstants.RIGHT );
                    break;
                }
                setBorder( BorderFactory.createCompoundBorder( getBorder(), padding ) );
                return this;
            }
        });
        
        //setPreferredSize( new Dimension( TABLE_WIDTH, TABLE_HEADER_HEIGHT + TABLE_ROW_HEIGHT * DEFAULT_PREVIEW_TABLE_ROW_COUNT ) );
    }
    
    private void clearPreviewTable() {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        for( int i = 0; i < previewTable.getRowCount(); i++ ) {
            for( int j = 0; j < previewTable.getColumnCount(); j++ ) {
                model.setValueAt( "", i, j );
            }
        }
        previewTable.getSelectionModel().clearSelection();
    }
    
    private void setPreviewTableSourceFundName( String fundName ) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.setValueAt( fundName, SOURCE_FUND_ROW, FUND_NAME_COLUMN );
    }
    
    private void setPreviewTableDestinationFundName( String fundName ) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.setValueAt( fundName, DESTINATION_FUND_ROW, FUND_NAME_COLUMN );
    }
    
    private void setPreviewTableSourceFundPreAmount( Integer amount ) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.setValueAt( (amount != null) ? Integer.toString( amount ) : "", SOURCE_FUND_ROW, PRE_AMOUNT_COLUMN );
    }
    
    private void setPreviewTableDestinationFundPreAmount( Integer amount ) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.setValueAt( (amount != null) ? Integer.toString( amount ) : "", DESTINATION_FUND_ROW, PRE_AMOUNT_COLUMN );
    }
    
    private void setPreviewTableSourceFundPostAmount( Integer amount ) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.setValueAt( (amount != null) ? Integer.toString( amount ) : "", SOURCE_FUND_ROW, POST_AMOUNT_COLUMN );
    }
    
    private void setPreviewTableDestinationFundPostAmount( Integer amount ) {
        DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
        model.setValueAt( (amount != null) ? Integer.toString( amount ) : "", DESTINATION_FUND_ROW, POST_AMOUNT_COLUMN );
    }
    
    private class FundingStatusListItem {
        
        private int id;
        private String name;
        
        public FundingStatusListItem( int id, String name ) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        public String toString() {
            return name;
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
    
    private class SpecialFocusTraversalPolicyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            if( event.getKeyCode() != KeyEvent.VK_TAB ) {
                return;
            }
            
            if( event.getSource() == previewTable && !event.isShiftDown() ) {
                previewAndConfirmButton.requestFocus();
            } else if( event.getSource() == previewTable && event.isShiftDown() ) {
                amountToMoveTextField.requestFocus();
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
    
    private class MnemonicKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            switch( event.getKeyCode() ) {
            case KeyEvent.VK_ENTER:
                if( event.getSource() instanceof JTextComponent && !((JTextComponent)event.getSource()).isEditable() ) {
                    break;
                } else if( event.getSource() == destinationFundComboBox || event.getSource() == previewTable ) {
                    break;
                }
                if( event.getSource() != cancelButton ) {
                    previewOrMoveFundingStatusAmount();
                } else {
                    cancelConfiguration();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                cancelConfiguration();
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
    
    private class UndoEditHandler implements UndoableEditListener {
        
        @Override
        public void undoableEditHappened( UndoableEditEvent event ) {
            undoManager.addEdit( event.getEdit() );
        }
    }
    
    private class UndoHotKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            if( dialogStatus == STATUS.PREVIEWING ) {
                return;
            }
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
