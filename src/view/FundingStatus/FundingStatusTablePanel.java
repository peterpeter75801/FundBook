package view.FundingStatus;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import commonUtil.ComparingUtil;
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import main.FundBookServices;
import service.FundingStatusService;

public class FundingStatusTablePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private static final int DEFAULT_ROW_COUNT = 20;
    private static final int TABLE_WIDTH = 665;
    private static final int TABLE_HEIGHT = 440;
    private static final int TABLE_HEADER_HEIGHT = 22;
    private static final int TABLE_ROW_HEIGHT = 22;
    private static final int[] TABLE_COLUMN_WIDTH = { 80, 489, 96, 0 };
    private static final int BORDER_HEIGHT_FIX = 3;
    
    private static final int TYPE_COLUMN = 0;
    private static final int NAME_COLUMN = 1;
    private static final int AMOUNT_COLUMN = 2;
    private static final int ID_COLUMN = 3;
    private static final String[] COLUMN_NAMES = { "種類", "儲存地點/儲存機構", "金額", "id(hidden)" };
    
    private FundingStatusPanel ownerPanel;
    private FundingStatusService fundingStatusService;
    
    private Font dataTableFont;
    private JTable dataTable;
    private JScrollPane dataTableScrollPane;
    
    public FundingStatusTablePanel( FundBookServices fundBookServices, FundingStatusPanel ownerPanel ) {
        setLayout( null );
        
        fundingStatusService = fundBookServices.getFundingStatusService();
        
        this.ownerPanel = ownerPanel;
        
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
        dataTable.removeColumn( dataTable.getColumnModel().getColumn( 3 ) );
        
        dataTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        dataTable.setPreferredScrollableViewportSize( new Dimension( TABLE_WIDTH, TABLE_HEIGHT ) );
        
        // 移除 JTable 中 Tab, Shift+Tab 和 Enter 鍵的預設功能
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
            KeyStroke.getKeyStroke( KeyEvent.VK_TAB, 0 ), "none" );
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put(
            KeyStroke.getKeyStroke( KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK ), "none" );
        dataTable.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( 
            KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "none" );
            
        dataTable.addKeyListener( new MnemonicKeyHandler() );
        dataTable.addKeyListener( new SpecialFocusTraversalPolicyHandler() );
        
        initializeTableStyle();
        
        dataTableScrollPane = new JScrollPane( dataTable );
        dataTableScrollPane.setBounds( 10, 10, TABLE_WIDTH, TABLE_HEIGHT + TABLE_HEADER_HEIGHT + BORDER_HEIGHT_FIX );
        
        add( dataTableScrollPane );
        
        setPreferredSize( new Dimension( 675, 479 ) );
    }
    
    public JTable getDataTable() {
        return dataTable;
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
    
    public void loadFundingStatus() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        
        // 初始化表格
        if( dataTable.getRowCount() > DEFAULT_ROW_COUNT ) {
            for( int i = dataTable.getRowCount() - 1; i >= DEFAULT_ROW_COUNT; i-- ) {
                model.removeRow( i );
            }
        }
        for( int i = 0; i < dataTable.getRowCount(); i++ ) {
            model.setValueAt( "", i, TYPE_COLUMN );
            model.setValueAt( "", i, NAME_COLUMN );
            model.setValueAt( "", i, AMOUNT_COLUMN );
            model.setValueAt( "", i, ID_COLUMN );
        }
        
        // 載入資料
        try {
            List<FundingStatus> fundingStatusList = fundingStatusService.findAll();
            for( int i = 0; i < fundingStatusList.size(); i++ ) {
                FundingStatus data = fundingStatusList.get( i );
                if( i >= dataTable.getRowCount() ) {
                    model.addRow( new Object[] {
                        FundingStatusUtil.getTypeName( data.getType() ),
                        data.getStoredPlaceOrInstitution(),
                        String.format( "%d", Math.abs( data.getAmount() ) ),
                        String.format( "%d", data.getId() )
                    });
                } else {
                    model.setValueAt( FundingStatusUtil.getTypeName( data.getType() ), i, TYPE_COLUMN );
                    model.setValueAt( data.getStoredPlaceOrInstitution(), i, NAME_COLUMN );
                    model.setValueAt( String.format( "%d", Math.abs( data.getAmount() ) ), i, AMOUNT_COLUMN );
                    model.setValueAt( String.format( "%d", data.getId() ), i, ID_COLUMN );
                }
            }
        } catch( Exception e ) {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "讀取資料發生錯誤", "Error", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    public void loadFundingStatusAndSelectId( int idForSelecting ) {
        loadFundingStatus();
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
                case TYPE_COLUMN:
                    setHorizontalAlignment( SwingConstants.CENTER );
                    setBorder( BorderFactory.createCompoundBorder( getBorder(), leftPadding ) );
                    break;
                case NAME_COLUMN:
                    setHorizontalAlignment( SwingConstants.LEFT );
                    setBorder( BorderFactory.createCompoundBorder( getBorder(), leftPadding ) );
                    break;
                case AMOUNT_COLUMN:
                    setHorizontalAlignment( SwingConstants.RIGHT );
                    setBorder( BorderFactory.createCompoundBorder( getBorder(), rightPadding ) );
                    break;
                }
                return this;
            }
        });
    }
    
    private class SpecialFocusTraversalPolicyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            JButton createButton = ownerPanel.getCreateButton();
            JTabbedPane tabbedPane = ownerPanel.getOwnerFrame().getTabbedPane();
            
            if( event.getKeyCode() != KeyEvent.VK_TAB ) {
                return;
            }
            
            if( event.getSource() == dataTable && !event.isShiftDown() ) {
                createButton.requestFocus();
            } else if( event.getSource() == dataTable && event.isShiftDown() ) {
                tabbedPane.requestFocus();
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
                ownerPanel.openFundingStatusCreateDialog();
                break;
            case KeyEvent.VK_M:
                ownerPanel.openFundingStatusAmountMoveDialog();
                break;
            case KeyEvent.VK_A:
                ownerPanel.openFundingStatusAmountUpdateDialog();
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
}
