package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import main.FundBookServices;
import view.IncomeRecord.IncomeRecordPanel;

public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JTabbedPane tabbedPane;
    private IncomeRecordPanel incomeRecordPanel;
    
    public MainFrame( FundBookServices fundBookServices ) {
        super( "Fund Book" );
        
        tabbedPane = new JTabbedPane();
        
        incomeRecordPanel = new IncomeRecordPanel( fundBookServices, this );
        tabbedPane.addTab( "收支記錄", null, incomeRecordPanel, "記錄每個月的收支狀況" );
        tabbedPane.addKeyListener( new MnemonicKeyHandler() );
        
        add( tabbedPane );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pack();
    }
    
    public IncomeRecordPanel getIncomeRecordPanel() {
        return incomeRecordPanel;
    }
    
    @Override
    public void setVisible( boolean b ) {
        if( b == true ) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
            setLocation( new Point( middle.x - (getWidth() / 2), middle.y - (getHeight() / 2) ) );
        }
        super.setVisible( b );
    }
    
    private class MnemonicKeyHandler implements KeyListener {
        
        @Override
        public void keyPressed( KeyEvent event ) {
            if( tabbedPane.getSelectedComponent() == incomeRecordPanel ) {
                switch( event.getKeyCode() ) {
                case KeyEvent.VK_C:
                    incomeRecordPanel.openIncomeRecordCreateDialog();
                    break;
                case KeyEvent.VK_U:
                    incomeRecordPanel.openIncomeRecordUpdateDialog();
                    break;
                case KeyEvent.VK_D:
                    incomeRecordPanel.deleteIncomeRecord();
                    break;
                case KeyEvent.VK_P:
                    incomeRecordPanel.moveUpIncomeRecordData();
                    break;
                case KeyEvent.VK_N:
                    incomeRecordPanel.moveDownIncomeRecordData();
                    break;
                case KeyEvent.VK_S:
                    incomeRecordPanel.sortIncomeRecordData();
                    break;
                case KeyEvent.VK_Y:
                    incomeRecordPanel.copyIncomeRecordData();
                    break;
                case KeyEvent.VK_R:
                    incomeRecordPanel.openIncomeRecordPropertyDialog();
                    break;
                case KeyEvent.VK_T:
                    incomeRecordPanel.displayOrHideTotalProperty();
                default:
                    break;
                }
            }
        }

        @Override
        public void keyReleased( KeyEvent event ) {}

        @Override
        public void keyTyped( KeyEvent event ) {}
    }
}
