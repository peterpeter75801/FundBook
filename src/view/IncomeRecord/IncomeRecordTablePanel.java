package view.IncomeRecord;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        
        //dataTable.setFocusTraversalKeysEnabled( false );
        dataTable.addKeyListener( new SpecialFocusTraversalPolicyHandler() );
        
        // 移除 JTable 中 Enter 鍵的預設功能
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
            .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "Enter" );
        dataTable.getActionMap().put( "Enter", new AbstractAction() {
        
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed( ActionEvent event ) { /* do nothing */ }
        });
        
        // 設定 JTable 特定欄位的文字對齊方式
        initializeTableAlignment();
        
        dataTableScrollPane = new JScrollPane( dataTable );
        dataTableScrollPane.setBounds( 0, 10, TABLE_WIDTH, TABLE_HEIGHT + TABLE_HEADER_HEIGHT + BORDER_HEIGHT_FIX );
        
        add( dataTableScrollPane );
        
        setPreferredSize( new Dimension( 585, 479 ) );
    }
    
    public JTable getDataTable() {
        return dataTable;
    }
    
    public void loadIncomeRecordOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        
        searchIncomeRecordByMonth( year, month );
    }
    
    public void searchIncomeRecordByMonth( int year, int month ) {
        try {
            List<IncomeRecord> incomeRecordList = incomeRecordService.findByMonth( year, month );
            for( int i = 0; i < incomeRecordList.size(); i++ ) {
                IncomeRecord data = incomeRecordList.get( i );
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                if( i >= dataTable.getRowCount() ) {
                    model.addRow( new Object[] {
                        String.format( "%04d.%02d.%02d", data.getYear(), data.getMonth(), data.getDay() ),
                        data.getItem(),
                        ((data.getAmount() >= 0) ? "收" : "支"),
                        String.format( "%d", Math.abs( data.getAmount() ) ),
                        String.format( "%d", data.getId() ) });
                } else {
                    model.setValueAt( String.format( "%04d.%02d.%02d", data.getYear(), data.getMonth(), data.getDay() ), i, 0 );
                    model.setValueAt( data.getItem(), i, 1 );
                    model.setValueAt( ((data.getAmount() >= 0) ? "收" : "支"), i, 2 );
                    model.setValueAt( String.format( "%d", Math.abs( data.getAmount() ) ), i, 3 );
                    model.setValueAt( String.format( "%d", data.getId() ), i, 4 );
                }
            }
        } catch( Exception e ) {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "讀取資料發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    private void initializeTableAlignment() {
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            private Border padding = BorderFactory.createEmptyBorder( 0, 3, 0, 0 );
            @Override
            public Component getTableCellRendererComponent( JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column ) {
                super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                setBorder( BorderFactory.createCompoundBorder( getBorder(), padding ) );
                return this;
            }
        };
        leftRenderer.setHorizontalAlignment( SwingConstants.LEFT );
        dataTable.getColumnModel().getColumn( 0 ).setCellRenderer( leftRenderer );
        dataTable.getColumnModel().getColumn( 1 ).setCellRenderer( leftRenderer );
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        dataTable.getColumnModel().getColumn( 2 ).setCellRenderer( centerRenderer );
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            private Border padding = BorderFactory.createEmptyBorder( 0, 0, 0, 3 );
            @Override
            public Component getTableCellRendererComponent( JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column ) {
                super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                setBorder( BorderFactory.createCompoundBorder( getBorder(), padding ) );
                return this;
            }
        };
        rightRenderer.setHorizontalAlignment( SwingConstants.RIGHT );
        dataTable.getColumnModel().getColumn( 3 ).setCellRenderer( rightRenderer );
        
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
}
