package view.IncomeRecord;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;

import common.Contants;
import domain.IncomeRecord;
import main.FundBookServices;
import service.IncomeRecordService;
import service.TotalPropertyService;
import view.MainFrame;
import view.common.CopyAndPastePopUpMenu;

public class IncomeRecordPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordService incomeRecordService;
    private TotalPropertyService totalPropertyService;
    
    private MainFrame ownerFrame;
    private IncomeRecordDatePanel incomeRecordDatePanel; 
    private IncomeRecordTablePanel incomeRecordTablePanel;
    private IncomeRecordCreateDialog incomeRecordCreateDialog;
    private IncomeRecordUpdateDialog incomeRecordUpdateDialog;
    private IncomeRecordPropertyDialog incomeRecordPropertyDialog;
    
    private CopyAndPastePopUpMenu copyAndPastePopUpMenu;
    private FocusHandler focusHandler;
    private MnemonicKeyHandler mnemonicKeyHandler;
    private CopyPasteMenuKeyHandler copyPasteMenuKeyHandler;
    private CopyPasteMouseMenuHandler copyPasteMouseMenuHandler; 
    private Font generalFont;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton sortButton;
    private JButton copyButton;
    private JButton detailButton;
    private JLabel revenueInCurrentMonthLabel;
    private JTextField revenueInCurrentMonthTextField;
    private JLabel costInCurrentMonthLabel;
    private JTextField costInCurrentMonthTextField;
    private JLabel incomeStateInCurrentMonthLabel1;
    private JLabel incomeStateInCurrentMonthLabel2;
    private JTextField incomeStateInCurrentMonthTextField;
    private JButton totalPropertyDisplayButton;
    private JLabel totalPropertyLabel;
    private JTextField totalPropertyTextField;
    private JLabel versionLabel;
    
    private boolean popupMenuClosedFlag;
    private boolean totalPropertyDisplayFlag;
    
    public IncomeRecordPanel( FundBookServices fundBookServices, MainFrame ownerFrame ) {
        setLayout( null );
        
        incomeRecordService = fundBookServices.getIncomeRecordService();
        totalPropertyService = fundBookServices.getTotalPropertyService();
        
        copyAndPastePopUpMenu = new CopyAndPastePopUpMenu();
        copyAndPastePopUpMenu.addPopupMenuListener( new PopupMenuClosingHandler() );
        
        focusHandler = new FocusHandler();
        mnemonicKeyHandler = new MnemonicKeyHandler();
        copyPasteMenuKeyHandler = new CopyPasteMenuKeyHandler( copyAndPastePopUpMenu );
        copyPasteMouseMenuHandler = new CopyPasteMouseMenuHandler( copyAndPastePopUpMenu );
        
        generalFont = new Font( "細明體", Font.PLAIN, 16 );
        
        this.ownerFrame = ownerFrame;
        
        incomeRecordDatePanel = new IncomeRecordDatePanel( fundBookServices, this );
        incomeRecordDatePanel.setBounds( 0, 0, 120, 479 );
        add( incomeRecordDatePanel );
        
        incomeRecordTablePanel = new IncomeRecordTablePanel( fundBookServices, this, incomeRecordDatePanel );
        incomeRecordTablePanel.setBounds( 120, 0, 585, 479 );
        add( incomeRecordTablePanel );
        
        incomeRecordDatePanel.setIncomeRecordTablePanel( incomeRecordTablePanel );
        
        incomeRecordCreateDialog = new IncomeRecordCreateDialog( fundBookServices.getIncomeRecordService(), ownerFrame );
        incomeRecordUpdateDialog = new IncomeRecordUpdateDialog( fundBookServices.getIncomeRecordService(), ownerFrame );
        incomeRecordPropertyDialog = new IncomeRecordPropertyDialog( fundBookServices.getIncomeRecordService(), ownerFrame );
        
        createButton = new JButton( "新增(C)" );
        createButton.setBounds( 717, 32, 64, 22 );
        createButton.setFont( generalFont );
        createButton.addKeyListener( mnemonicKeyHandler );
        createButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        createButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                openIncomeRecordCreateDialog();
            }
        });
        add( createButton );
        
        updateButton = new JButton( "修改(U)" );
        updateButton.setBounds( 717, 76, 64, 22 );
        updateButton.setFont( generalFont );
        updateButton.addKeyListener( mnemonicKeyHandler );
        updateButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        updateButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                openIncomeRecordUpdateDialog();
            }
        });
        add( updateButton );
        
        deleteButton = new JButton( "刪除(D)" );
        deleteButton.setBounds( 717, 120, 64, 22 );
        deleteButton.setFont( generalFont );
        deleteButton.addKeyListener( mnemonicKeyHandler );
        deleteButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        deleteButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                deleteIncomeRecord();
            }
        });
        add( deleteButton );
        
        moveUpButton = new JButton( "上移(P)" );
        moveUpButton.setBounds( 717, 164, 64, 22 );
        moveUpButton.setFont( generalFont );
        moveUpButton.addKeyListener( mnemonicKeyHandler );
        moveUpButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        moveUpButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                moveUpIncomeRecordData();
            }
        });
        add( moveUpButton );
        
        moveDownButton = new JButton( "下移(N)" );
        moveDownButton.setBounds( 717, 208, 64, 22 );
        moveDownButton.setFont( generalFont );
        moveDownButton.addKeyListener( mnemonicKeyHandler );
        moveDownButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        moveDownButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                moveDownIncomeRecordData();
            }
        });
        add( moveDownButton );
        
        sortButton = new JButton( "排序(S)" );
        sortButton.setBounds( 717, 252, 64, 22 );
        sortButton.setFont( generalFont );
        sortButton.addKeyListener( mnemonicKeyHandler );
        sortButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        sortButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                sortIncomeRecordData();
            }
        });
        add( sortButton );
        
        copyButton = new JButton( "複製(Y)" );
        copyButton.setBounds( 717, 296, 64, 22 );
        copyButton.setFont( generalFont );
        copyButton.addKeyListener( mnemonicKeyHandler );
        copyButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        copyButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                copyIncomeRecordData();
            }
        });
        add( copyButton );
        
        detailButton = new JButton( "詳細(R)" );
        detailButton.setBounds( 717, 340, 64, 22 );
        detailButton.setFont( generalFont );
        detailButton.addKeyListener( mnemonicKeyHandler );
        detailButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        detailButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                openIncomeRecordPropertyDialog();
            }
        });
        add( detailButton );
        incomeStateInCurrentMonthLabel1 = new JLabel( "本月收支狀況( " );
        incomeStateInCurrentMonthLabel1.setBounds( 16, 479, 112, 22 );
        incomeStateInCurrentMonthLabel1.setFont( generalFont );
        add( incomeStateInCurrentMonthLabel1 );
        
        revenueInCurrentMonthLabel = new JLabel( "收入 " );
        revenueInCurrentMonthLabel.setBounds( 128, 479, 40, 22 );
        revenueInCurrentMonthLabel.setFont( generalFont );
        add( revenueInCurrentMonthLabel );
        
        revenueInCurrentMonthTextField = new JTextField();
        revenueInCurrentMonthTextField.setBounds( 168, 479, 56, 22 );
        revenueInCurrentMonthTextField.setFont( generalFont );
        revenueInCurrentMonthTextField.setEditable( false );
        revenueInCurrentMonthTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        revenueInCurrentMonthTextField.addFocusListener( focusHandler );
        revenueInCurrentMonthTextField.addKeyListener( copyPasteMenuKeyHandler );
        revenueInCurrentMonthTextField.addMouseListener( copyPasteMouseMenuHandler );
        add( revenueInCurrentMonthTextField );
        
        costInCurrentMonthLabel = new JLabel( " - 支出 " );
        costInCurrentMonthLabel.setBounds( 224, 479, 64, 22 );
        costInCurrentMonthLabel.setFont( generalFont );
        add( costInCurrentMonthLabel );
        
        costInCurrentMonthTextField = new JTextField();
        costInCurrentMonthTextField.setBounds( 288, 479, 56, 22 );
        costInCurrentMonthTextField.setFont( generalFont );
        costInCurrentMonthTextField.setEditable( false );
        costInCurrentMonthTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        costInCurrentMonthTextField.addFocusListener( focusHandler );
        costInCurrentMonthTextField.addKeyListener( copyPasteMenuKeyHandler );
        costInCurrentMonthTextField.addMouseListener( copyPasteMouseMenuHandler );
        add( costInCurrentMonthTextField );
        
        incomeStateInCurrentMonthLabel2 = new JLabel( " ): " );
        incomeStateInCurrentMonthLabel2.setBounds( 344, 479, 32, 22 );
        incomeStateInCurrentMonthLabel2.setFont( generalFont );
        add( incomeStateInCurrentMonthLabel2 );
        
        incomeStateInCurrentMonthTextField = new JTextField();
        incomeStateInCurrentMonthTextField.setBounds( 376, 479, 80, 22 );
        incomeStateInCurrentMonthTextField.setFont( generalFont );
        incomeStateInCurrentMonthTextField.setEditable( false );
        incomeStateInCurrentMonthTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        incomeStateInCurrentMonthTextField.addFocusListener( focusHandler );
        incomeStateInCurrentMonthTextField.addKeyListener( copyPasteMenuKeyHandler );
        incomeStateInCurrentMonthTextField.addMouseListener( copyPasteMouseMenuHandler );
        add( incomeStateInCurrentMonthTextField );
        
        totalPropertyDisplayButton = new JButton( "顯示帳面總金額(T)" );
        totalPropertyDisplayButton.setBounds( 16, 506, 144, 22 );
        totalPropertyDisplayButton.setFont( generalFont );
        totalPropertyDisplayButton.addKeyListener( mnemonicKeyHandler );
        totalPropertyDisplayButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        totalPropertyDisplayButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                displayOrHideTotalProperty();
            }
        });
        add( totalPropertyDisplayButton );
        
        totalPropertyLabel = new JLabel( "帳面總金額: " );
        totalPropertyLabel.setBounds( 16, 506, 96, 22 );
        totalPropertyLabel.setFont( generalFont );
        totalPropertyLabel.setVisible( false );
        add( totalPropertyLabel );
        
        totalPropertyTextField = new JTextField();
        totalPropertyTextField.setBounds( 112, 506, 120, 22 );
        totalPropertyTextField.setFont( generalFont );
        totalPropertyTextField.setEditable( false );
        totalPropertyTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        totalPropertyTextField.addFocusListener( focusHandler );
        totalPropertyTextField.addKeyListener( copyPasteMenuKeyHandler );
        totalPropertyTextField.addMouseListener( copyPasteMouseMenuHandler );
        totalPropertyTextField.setVisible( false );
        add( totalPropertyTextField );
        
        versionLabel = new JLabel( Contants.VERSION, SwingConstants.RIGHT );
        versionLabel.setBounds( 561, 506, 224, 22 );
        versionLabel.setFont( generalFont );
        add( versionLabel );
        
        setPreferredSize( new Dimension( 793, 533 ) );

        popupMenuClosedFlag = false;
        totalPropertyDisplayFlag = false;
        
        // 載入目前月份資料
        incomeRecordDatePanel.reselectDateList();
        
        // 載入帳面總金額
        loadTotalProperty();
    }
    
    public JButton getCreateButton() {
        return createButton;
    }
    
    public MainFrame getOwnerFrame() {
        return ownerFrame;
    }
    
    public void computeIncomeStateInCurrentMonth() {
        int totalRevenue = 0;
        int totalCost = 0;
        int totalAmount = 0;
        List<Integer> amountsOfSelectedMonth = incomeRecordTablePanel.getAllAmount();
        for( Integer amount : amountsOfSelectedMonth ) {
            if( amount < 0 ) {
                totalCost -= amount;
            } else {
                totalRevenue += amount;
            }
            totalAmount += amount;
        }
        revenueInCurrentMonthTextField.setText( String.format( "%d", totalRevenue ) );
        costInCurrentMonthTextField.setText( String.format( "%d", totalCost ) );
        incomeStateInCurrentMonthTextField.setText( String.format( "%d", totalAmount ) );
    }
    
    public void reselectDateList() {
        incomeRecordDatePanel.reselectDateList();
    }
    
    public void selectTableDataById( int id ) {
        incomeRecordTablePanel.selectDataById( id );
    }
    
    public void loadTotalProperty() {
        try {
            totalPropertyTextField.setText( String.format( "%d", totalPropertyService.getMainTotalAmount() ) );
        } catch ( Exception e ) {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "載入帳面總金額錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    public void displayOrHideTotalProperty() {
        if( totalPropertyDisplayFlag ) {
            totalPropertyDisplayButton.setText( "顯示帳面總金額(T)" );
            totalPropertyDisplayButton.setBounds( 16, 506, 144, 22 );
            totalPropertyLabel.setVisible( false );
            totalPropertyTextField.setVisible( false );
            totalPropertyDisplayFlag = false;
        } else {
            totalPropertyDisplayButton.setText( "隱藏(T)" );
            totalPropertyDisplayButton.setBounds( 256, 506, 64, 22 );
            totalPropertyLabel.setVisible( true );
            totalPropertyTextField.setVisible( true );
            totalPropertyDisplayFlag = true;
        }
    }
    
    public void copyIncomeRecordData() {
        int selectedYear, selectedMonth;
        IncomeRecord incomeRecordForCopy = null;
        String selectedMonthString = incomeRecordDatePanel.getMonthListSelectedValue();
        try {
            selectedYear = Integer.parseInt( selectedMonthString.substring( 0, 4 ) );
            selectedMonth = Integer.parseInt( selectedMonthString.substring( 5, 7 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            selectedYear = 1900;
            selectedMonth = 1;
        }
        
        int selectedId = incomeRecordTablePanel.getItemTableSelectedId();
        if( selectedId == -1 ) {
            return;
        }
        
        int returnCode = 0;
        try {
            incomeRecordForCopy = incomeRecordService.findOne( selectedId, selectedYear, selectedMonth );
            returnCode = incomeRecordService.insert( incomeRecordForCopy );
        } catch ( Exception e ) {
            e.printStackTrace();
            returnCode = Contants.ERROR;
        }
        switch( returnCode ) {
        case Contants.SUCCESS:
            if( incomeRecordForCopy != null ) {
                refreshAndFind( incomeRecordForCopy );
            } else {
                reselectDateList();
            }
            loadTotalProperty();
            break;
        case Contants.ERROR:
            JOptionPane.showMessageDialog( null, "複製失敗", "Error", JOptionPane.ERROR_MESSAGE );
            break;
        default:
            break;
        }
    }
    
    public void deleteIncomeRecord() {
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
        
        int selectedId = incomeRecordTablePanel.getItemTableSelectedId();
        if( selectedId == -1 ) {
            return;
        }
        
        int comfirmation = JOptionPane.showConfirmDialog( 
                ownerFrame, "確認刪除?", "Check", JOptionPane.YES_NO_OPTION );
        if( comfirmation != JOptionPane.YES_OPTION ) {
            return;
        }
        
        IncomeRecord incomeRecordForDelete = new IncomeRecord();
        incomeRecordForDelete.setId( selectedId );
        incomeRecordForDelete.setYear( selectedYear );
        incomeRecordForDelete.setMonth( selectedMonth );
        
        int returnCode = 0;
        try {
            returnCode = incomeRecordService.delete( incomeRecordForDelete );
        } catch ( Exception e ) {
            e.printStackTrace();
            returnCode = Contants.ERROR;
        }
        switch( returnCode ) {
        case Contants.SUCCESS:
            int nextSelectedItemId = incomeRecordTablePanel.getItemIdByIndex( incomeRecordTablePanel.getDataTable().getSelectedRow() + 1 );
            if( nextSelectedItemId == -1 ) {
                nextSelectedItemId = incomeRecordTablePanel.getItemIdByIndex( incomeRecordTablePanel.getDataTable().getSelectedRow() - 1 );
            }
            
            if( nextSelectedItemId != -1 ) {
                refreshAndFind( new IncomeRecord( nextSelectedItemId, selectedYear, selectedMonth, 0, "", 0, 0, "", 0 ) );
            } else {
                reselectDateList();
            }
            
            loadTotalProperty();
            break;
        case Contants.ERROR:
            JOptionPane.showMessageDialog( null, "刪除失敗", "Error", JOptionPane.ERROR_MESSAGE );
            break;
        default:
            break;
        }
    }
    
    public void openIncomeRecordCreateDialog() {
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
    
    public void openIncomeRecordUpdateDialog() {
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
        
        int selectedId = incomeRecordTablePanel.getItemTableSelectedId();
        if( selectedId != -1 ) {
            incomeRecordUpdateDialog.openDialog( selectedId, selectedYear, selectedMonth );
        }
    }
    
    public void openIncomeRecordPropertyDialog() {
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
        
        int selectedId = incomeRecordTablePanel.getItemTableSelectedId();
        if( selectedId != -1 ) {
            incomeRecordPropertyDialog.openDialog( selectedId, selectedYear, selectedMonth );
        }
    }
    
    public void moveUpIncomeRecordData() {
        int selectedYear, selectedMonth;
        int selectedIndex = incomeRecordTablePanel.getDataTable().getSelectedRow();
        int status = Contants.SUCCESS;
        
        String selectedMonthString = incomeRecordDatePanel.getMonthListSelectedValue();
        try {
            selectedYear = Integer.parseInt( selectedMonthString.substring( 0, 4 ) );
            selectedMonth = Integer.parseInt( selectedMonthString.substring( 5, 7 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            return;
        }
        
        try {
            status = incomeRecordService.moveUp( selectedYear, selectedMonth, selectedIndex + 1 );
        } catch( Exception e ) {
            e.printStackTrace();
            status = Contants.ERROR;
        }
        
        if( status == Contants.SUCCESS ) {
            int selectedDataId = incomeRecordTablePanel.getItemTableSelectedId();
            reselectDateList();
            incomeRecordTablePanel.selectDataById( selectedDataId );
        } else if( status == Contants.NO_DATA_MODIFIED ) {
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex, selectedIndex );
        } else {
            JOptionPane.showMessageDialog( ownerFrame, "移動帳目發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    public void moveDownIncomeRecordData() {
        int selectedYear, selectedMonth;
        int selectedIndex = incomeRecordTablePanel.getDataTable().getSelectedRow();
        int status = Contants.SUCCESS;
        
        String selectedMonthString = incomeRecordDatePanel.getMonthListSelectedValue();
        try {
            selectedYear = Integer.parseInt( selectedMonthString.substring( 0, 4 ) );
            selectedMonth = Integer.parseInt( selectedMonthString.substring( 5, 7 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            return;
        }
        
        try {
            status = incomeRecordService.moveDown( selectedYear, selectedMonth, selectedIndex + 1 );
        } catch( Exception e ) {
            e.printStackTrace();
            status = Contants.ERROR;
        }
        
        if( status == Contants.SUCCESS ) {
            int selectedDataId = incomeRecordTablePanel.getItemTableSelectedId();
            reselectDateList();
            incomeRecordTablePanel.selectDataById( selectedDataId );
        } else if( status == Contants.NO_DATA_MODIFIED ) {
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex, selectedIndex );
        } else {
            JOptionPane.showMessageDialog( ownerFrame, "移動帳目發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    public void sortIncomeRecordData() {
        int selectedYear, selectedMonth;
        int selectedIndex = incomeRecordTablePanel.getDataTable().getSelectedRow();
        int status = Contants.SUCCESS;
        
        String selectedMonthString = incomeRecordDatePanel.getMonthListSelectedValue();
        try {
            selectedYear = Integer.parseInt( selectedMonthString.substring( 0, 4 ) );
            selectedMonth = Integer.parseInt( selectedMonthString.substring( 5, 7 ) );
        } catch( Exception e ) {
            e.printStackTrace();
            return;
        }
        
        try {
            status = incomeRecordService.sort( selectedYear, selectedMonth );
        } catch( Exception e ) {
            e.printStackTrace();
            status = Contants.ERROR;
        }
        
        if( status == Contants.SUCCESS ) {
            reselectDateList();
        } else {
            JOptionPane.showMessageDialog( ownerFrame, "排序帳目發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }
        if( selectedIndex >= 0 ) {
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex, selectedIndex );
        }
    }
    
    public void refreshAndFind( IncomeRecord dataToFind ) {
        incomeRecordDatePanel.setYearTextFieldValue( dataToFind.getYear() );
        incomeRecordDatePanel.setMonthTextFieldValue( dataToFind.getMonth() );
        incomeRecordDatePanel.selectMonthListData( dataToFind.getYear(), dataToFind.getMonth() );
        
        incomeRecordDatePanel.setSelectingIdWhileLoadingTable( dataToFind.getId() );
        reselectDateList();
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
                if( event.getSource() == createButton ) {
                    openIncomeRecordCreateDialog();
                } else if( event.getSource() == updateButton ) {
                    openIncomeRecordUpdateDialog();
                } else if( event.getSource() == deleteButton ) {
                    deleteIncomeRecord();
                } else if( event.getSource() == moveUpButton ) {
                    moveUpIncomeRecordData();
                } else if( event.getSource() == moveDownButton ) {
                    moveDownIncomeRecordData();
                } else if( event.getSource() == sortButton ) {
                    sortIncomeRecordData();
                } else if( event.getSource() == detailButton ) {
                    openIncomeRecordPropertyDialog();
                } 
                break;
            case KeyEvent.VK_C:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    createButton.requestFocus();
                }
                openIncomeRecordCreateDialog();
                break;
            case KeyEvent.VK_U:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    updateButton.requestFocus();
                }
                openIncomeRecordUpdateDialog();
                break;
            case KeyEvent.VK_D:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    deleteButton.requestFocus();
                }
                deleteIncomeRecord();
                break;
            case KeyEvent.VK_P:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    moveUpButton.requestFocus();
                }
                moveUpIncomeRecordData();
                break;
            case KeyEvent.VK_N:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    moveDownButton.requestFocus();
                }
                moveDownIncomeRecordData();
                break;
            case KeyEvent.VK_S:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    sortButton.requestFocus();
                }
                sortIncomeRecordData();
                break;
            case KeyEvent.VK_Y:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    copyButton.requestFocus();
                }
                copyIncomeRecordData();
                break;
            case KeyEvent.VK_R:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    detailButton.requestFocus();
                }
                openIncomeRecordPropertyDialog();
                break;
            case KeyEvent.VK_T:
                if( event.getSource() != incomeRecordTablePanel.getDataTable() ) {
                    totalPropertyDisplayButton.requestFocus();
                }
                displayOrHideTotalProperty();
                break;
            default:
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
}
