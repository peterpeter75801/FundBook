package view.IncomeRecord;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import commonUtil.ComparingUtil;
import domain.IncomeRecord;
import main.FundBookServices;
import service.IncomeRecordService;

public class IncomeRecordTablePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private static final int DEFAULT_ROW_COUNT = 20;
    private static final int TABLE_WIDTH = 585;
    private static final int TABLE_HEIGHT = 440;
    private static final int TABLE_HEADER_HEIGHT = 22;
    private static final int TABLE_ROW_HEIGHT = 22;
    private static final int[] TABLE_COLUMN_WIDTH = { 96, 352, 40, 97, 0 };
    private static final int BORDER_HEIGHT_FIX = 3;
    
    private static final int DATE_COLUMN = 0;
    private static final int ITEM_COLUMN = 1;
    private static final int TYPE_COLUMN = 2;
    private static final int AMOUNT_COLUMN = 3;
    private static final int ID_COLUMN = 4;
    private static final String[] COLUMN_NAMES = { "日期", "項目", "收支", "金額", "Seq(hidden)" };
    
    private IncomeRecordService incomeRecordService;
    
    private IncomeRecordPanel ownerPanel;
    private IncomeRecordDatePanel incomeRecordDatePanel;
    
    private Font dataTableFont;
    private JTable dataTable;
    private JScrollPane dataTableScrollPane;
    
    public IncomeRecordTablePanel( FundBookServices fundBookServices, 
            IncomeRecordPanel ownerPanel, IncomeRecordDatePanel incomeRecordDatePanel ) {
        setLayout( null );
        
        incomeRecordService = fundBookServices.getIncomeRecordService();
        
        this.ownerPanel = ownerPanel;
        this.incomeRecordDatePanel = incomeRecordDatePanel;
        
        dataTableFont = new Font( "細明體", Font.PLAIN, 12 );
        
        dataTable = new JTable( new DefaultTableModel( COLUMN_NAMES, DEFAULT_ROW_COUNT ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable( int rowIndex, int columnIndex ) {
                return false;
            }
        });
        
        dataTable.setFont( dataTableFont );
        
        dataTable.getTableHeader().setReorderingAllowed( false );
        dataTable.getTableHeader().setPreferredSize( new Dimension( TABLE_WIDTH, TABLE_HEADER_HEIGHT ) );
        dataTable.setRowHeight( TABLE_ROW_HEIGHT );
        
        dataTable.getColumnModel().getColumn( 0 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 0 ] );
        dataTable.getColumnModel().getColumn( 1 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 1 ] );
        dataTable.getColumnModel().getColumn( 2 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 2 ] );
        dataTable.getColumnModel().getColumn( 3 ).setPreferredWidth( TABLE_COLUMN_WIDTH[ 3 ] );
        dataTable.removeColumn( dataTable.getColumnModel().getColumn( 4 ) );
        
        dataTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        dataTable.setPreferredScrollableViewportSize( new Dimension( TABLE_WIDTH, TABLE_HEIGHT ) );
        //dataTable.addKeyListener( mnemonicKeyHandler );
        
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
            KeyStroke.getKeyStroke( KeyEvent.VK_TAB, 0 ), "none" );
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
            KeyStroke.getKeyStroke( KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK ), "none" );
        
        dataTable.addKeyListener( new MnemonicKeyHandler() );
        dataTable.addKeyListener( new SpecialFocusTraversalPolicyHandler() );
        
        // 移除 JTable 中 Enter 鍵的預設功能
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
            .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "Enter" );
        dataTable.getActionMap().put( "Enter", new AbstractAction() {
        
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed( ActionEvent event ) { /* do nothing */ }
        });
        
        // 設定 JTable 特定欄位的文字對齊方式，以及型態為"收入"資料列的背景顏色
        initializeTableStyle();
        
        dataTableScrollPane = new JScrollPane( dataTable );
        dataTableScrollPane.setBounds( 0, 10, TABLE_WIDTH, TABLE_HEIGHT + TABLE_HEADER_HEIGHT + BORDER_HEIGHT_FIX );
        
        add( dataTableScrollPane );
        
        setPreferredSize( new Dimension( 585, 479 ) );
    }
    
    public JTable getDataTable() {
        return dataTable;
    }
    
    public List<Integer> getAllAmount() {
        List<Integer> amountList = new ArrayList<Integer>();
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        
        for( int i = 0; i < model.getRowCount(); i++ ) {
            int currentAmount = 0;
            try {
                currentAmount = Integer.parseInt( (String)model.getValueAt( i, AMOUNT_COLUMN ) );
            } catch ( NumberFormatException e ) {
                //e.printStackTrace();
                currentAmount = 0;
            }
            
            if( model.getValueAt( i, TYPE_COLUMN ) != null && model.getValueAt( i, TYPE_COLUMN ).equals( "收" ) ) {
                amountList.add( currentAmount );
            } else {
                amountList.add( currentAmount * (-1) );
            }
        }
        
        return amountList;
    }
    
    public int getItemCount() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        for( int i = 0; i < dataTable.getRowCount(); i++ ) {
            if( model.getValueAt( i, ID_COLUMN ) == null || ((String)model.getValueAt( i, ID_COLUMN )).length() <= 0 ) {
                return i;
            }
        }
        return dataTable.getRowCount();
    }
    
    public int getItemIdByIndex( int index ) {
        int id = -1;
        try {
            String itemTableSelectedIdValue = (String) dataTable.getModel().getValueAt( index, ID_COLUMN );
            id = Integer.parseInt( itemTableSelectedIdValue );
        } catch( Exception e ) {
            return -1;
        }
        return id;
    }
    
    public int getItemTableSelectedId() {
        int itemTableSelectedIndex = dataTable.getSelectedRow();
        
        if( itemTableSelectedIndex < 0 ) {
            JOptionPane.showMessageDialog( ownerPanel.getOwnerFrame(), "未選擇資料", "Warning", JOptionPane.WARNING_MESSAGE );
            return -1;
        }
        
        int id = -1;
        String itemTableSelectedIdValue = (String) dataTable.getModel().getValueAt( itemTableSelectedIndex, ID_COLUMN );
        try {
            id = Integer.parseInt( itemTableSelectedIdValue );
        } catch( NumberFormatException e ) {
            JOptionPane.showMessageDialog( ownerPanel.getOwnerFrame(), "選擇無效的資料", "Warning", JOptionPane.WARNING_MESSAGE );
            return -1;
        } catch( StringIndexOutOfBoundsException e ) {
            JOptionPane.showMessageDialog( ownerPanel.getOwnerFrame(), "選擇無效的資料", "Warning", JOptionPane.WARNING_MESSAGE );
            return -1;
        }
        
        return id;
    }
    
    public void loadIncomeRecordOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        
        loadIncomeRecordByMonth( year, month );
    }
    
    public void loadIncomeRecordByMonth( int year, int month ) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        if( dataTable.getRowCount() > DEFAULT_ROW_COUNT ) {
            for( int i = dataTable.getRowCount() - 1; i >= DEFAULT_ROW_COUNT; i-- ) {
                model.removeRow( i );
            }
        }
        for( int i = 0; i < dataTable.getRowCount(); i++ ) {
            model.setValueAt( "", i, DATE_COLUMN );
            model.setValueAt( "", i, ITEM_COLUMN );
            model.setValueAt( "", i, TYPE_COLUMN );
            model.setValueAt( "", i, AMOUNT_COLUMN );
            model.setValueAt( "", i, ID_COLUMN );
        }
        
        try {
            List<IncomeRecord> incomeRecordList = incomeRecordService.findByMonth( year, month );
            for( int i = 0; i < incomeRecordList.size(); i++ ) {
                IncomeRecord data = incomeRecordList.get( i );
                //DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                if( i >= dataTable.getRowCount() ) {
                    model.addRow( new Object[] {
                        String.format( "%04d.%02d.%02d", data.getYear(), data.getMonth(), data.getDay() ),
                        data.getItem(),
                        ((data.getAmount() >= 0) ? "收" : "支"),
                        String.format( "%d", Math.abs( data.getAmount() ) ),
                        String.format( "%d", data.getId() ) });
                } else {
                    model.setValueAt( String.format( "%04d.%02d.%02d", data.getYear(), data.getMonth(), data.getDay() ), i, DATE_COLUMN );
                    model.setValueAt( data.getItem(), i, ITEM_COLUMN );
                    model.setValueAt( ((data.getAmount() >= 0) ? "收" : "支"), i, TYPE_COLUMN );
                    model.setValueAt( String.format( "%d", Math.abs( data.getAmount() ) ), i, AMOUNT_COLUMN );
                    model.setValueAt( String.format( "%d", data.getId() ), i, ID_COLUMN );
                }
            }
        } catch( Exception e ) {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "讀取資料發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    public void loadIncomeRecordByMonthAndSelectId( int year, int month, int idForSelecting ) {
        loadIncomeRecordByMonth( year, month );
        selectDataById( idForSelecting );
    }
    
    public void selectDataById( int id ) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexOfDataForSelecting = 0;
        for( indexOfDataForSelecting = 0; indexOfDataForSelecting < dataTable.getRowCount(); indexOfDataForSelecting++ ) {
            if( ComparingUtil.compare( (String)model.getValueAt( indexOfDataForSelecting, ID_COLUMN ), String.format( "%d", id ) ) == 0 ) {
                break;
            }
        }
        if( indexOfDataForSelecting < dataTable.getRowCount() ) {
            dataTable.setRowSelectionInterval( indexOfDataForSelecting, indexOfDataForSelecting );
            dataTable.scrollRectToVisible( dataTable.getCellRect( indexOfDataForSelecting, 0, true ));
        }
    }
    
    private void initializeTableStyle() {
        dataTable.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            private Border leftPadding = BorderFactory.createEmptyBorder( 0, 3, 0, 0 );
            private Border rightPadding = BorderFactory.createEmptyBorder( 0, 0, 0, 3 );
            
            @Override
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                
                // 設定 JTable 特定欄位的文字對齊方式
                switch( column ) {
                case DATE_COLUMN:
                    setHorizontalAlignment( SwingConstants.LEFT );
                    setBorder( BorderFactory.createCompoundBorder( getBorder(), leftPadding ) );
                    break;
                case ITEM_COLUMN:
                    setHorizontalAlignment( SwingConstants.LEFT );
                    setBorder( BorderFactory.createCompoundBorder( getBorder(), leftPadding ) );
                    break;
                case TYPE_COLUMN:
                    setHorizontalAlignment( SwingConstants.CENTER );
                    break;
                case AMOUNT_COLUMN:
                    setHorizontalAlignment( SwingConstants.RIGHT );
                    setBorder( BorderFactory.createCompoundBorder( getBorder(), rightPadding ) );
                    break;
                }
                
                // set background of income type data row to light gray
                if( isSelected || hasFocus ) {
                    return this;
                }
                String itemType = (String)table.getModel().getValueAt( row, TYPE_COLUMN );
                if( ComparingUtil.compare( "收", itemType ) == 0 ) {
                    setBackground( Color.LIGHT_GRAY );
                } else {
                    setBackground( table.getBackground() );
                }
                return this;
            }
        });
    }
    
    private class SpecialFocusTraversalPolicyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            JButton createButton = ownerPanel.getCreateButton();
            JList<String> monthList = incomeRecordDatePanel.getMonthList();
            
            if( event.getKeyCode() != KeyEvent.VK_TAB ) {
                return;
            }
            
            if( event.getSource() == dataTable && !event.isShiftDown() ) {
                createButton.requestFocus();
            } else if( event.getSource() == dataTable && event.isShiftDown() ) {
                monthList.requestFocus();
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
            case KeyEvent.VK_C:
                ownerPanel.openIncomeRecordCreateDialog();
                break;
            case KeyEvent.VK_U:
                ownerPanel.openIncomeRecordUpdateDialog();
                break;
            case KeyEvent.VK_D:
                ownerPanel.deleteIncomeRecord();
                break;
            case KeyEvent.VK_P:
                ownerPanel.moveUpIncomeRecordData();
                break;
            case KeyEvent.VK_N:
                ownerPanel.moveDownIncomeRecordData();
                break;
            case KeyEvent.VK_S:
                ownerPanel.sortIncomeRecordData();
                break;
            case KeyEvent.VK_Y:
                ownerPanel.copyIncomeRecordData();
                break;
            case KeyEvent.VK_R:
                ownerPanel.openIncomeRecordPropertyDialog();
                break;
            case KeyEvent.VK_T:
                ownerPanel.displayOrHideTotalProperty();
            default:
                break;
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
}
