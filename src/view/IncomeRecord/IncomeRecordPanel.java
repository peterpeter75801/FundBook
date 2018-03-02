package view.IncomeRecord;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import common.Contants;
import domain.IncomeRecord;
import main.FundBookServices;
import service.IncomeRecordService;
import view.MainFrame;

public class IncomeRecordPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private IncomeRecordService incomeRecordService;
    
    private MainFrame ownerFrame;
    private IncomeRecordDatePanel incomeRecordDatePanel; 
    private IncomeRecordTablePanel incomeRecordTablePanel;
    private IncomeRecordCreateDialog incomeRecordCreateDialog;
    private IncomeRecordUpdateDialog incomeRecordUpdateDialog;
    private IncomeRecordPropertyDialog incomeRecordPropertyDialog;
    
    private Font generalFont;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton sortButton;
    private JButton detailButton;
    private JLabel incomeStateInCurrentMonthLabel;
    private JTextField incomeStateInCurrentMonthTextField;
    private JLabel totalPropertyLabel;
    private JTextField totalPropertyTextField;
    private JLabel versionLabel;
    
    public IncomeRecordPanel( FundBookServices fundBookServices, MainFrame ownerFrame ) {
        setLayout( null );
        
        incomeRecordService = fundBookServices.getIncomeRecordService();
        
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
        sortButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        sortButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                sortIncomeRecordData();
            }
        });
        add( sortButton );
        
        detailButton = new JButton( "詳細(R)" );
        detailButton.setBounds( 717, 296, 64, 22 );
        detailButton.setFont( generalFont );
        detailButton.setMargin( new Insets( 0, 0, 0, 0 ) );
        detailButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent event ) {
                openIncomeRecordPropertyDialog();
            }
        });
        add( detailButton );
        
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
        
        // 載入目前月份資料
        incomeRecordDatePanel.reselectDateList();
    }
    
    public JButton getCreateButton() {
        return createButton;
    }
    
    public MainFrame getOwnerFrame() {
        return ownerFrame;
    }
    
    public void computeIncomeStateInCurrentMonth() {
        int totalAmount = 0;
        List<Integer> amountsOfSelectedMonth = incomeRecordTablePanel.getAllAmount();
        for( Integer amount : amountsOfSelectedMonth ) {
            totalAmount += amount;
        }
        incomeStateInCurrentMonthTextField.setText( String.format( "%d", totalAmount ) );
    }
    
    public void reselectDateList() {
        incomeRecordDatePanel.reselectDateList();
    }
    
    private void deleteIncomeRecord() {
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
            reselectDateList();
            break;
        case Contants.ERROR:
            JOptionPane.showMessageDialog( null, "刪除失敗", "Error", JOptionPane.ERROR_MESSAGE );
            break;
        default:
            break;
        }
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
    
    private void openIncomeRecordUpdateDialog() {
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
    
    private void openIncomeRecordPropertyDialog() {
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
    
    private void moveUpIncomeRecordData() {
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
            reselectDateList();
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex - 1, selectedIndex - 1 );
        } else if( status == Contants.NO_DATA_MODIFIED ) {
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex, selectedIndex );
        } else {
            JOptionPane.showMessageDialog( ownerFrame, "移動帳目發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    private void moveDownIncomeRecordData() {
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
            reselectDateList();
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex + 1, selectedIndex + 1 );
        } else if( status == Contants.NO_DATA_MODIFIED ) {
            incomeRecordTablePanel.getDataTable().setRowSelectionInterval( selectedIndex, selectedIndex );
        } else {
            JOptionPane.showMessageDialog( ownerFrame, "移動帳目發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    private void sortIncomeRecordData() {
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
}
