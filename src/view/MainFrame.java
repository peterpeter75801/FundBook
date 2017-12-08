package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import view.IncomeRecord.IncomeRecordPanel;

public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JTabbedPane tabbedPane;
    private IncomeRecordPanel incomeRecordPanel;
    //private ScheduledItemPanel scheduledItemPanel;
    
    public MainFrame() {
        super( "Fund Book" );
        
        tabbedPane = new JTabbedPane();
        
        incomeRecordPanel = new IncomeRecordPanel( this );
        //scheduledItemPanel = new ScheduledItemPanel( this );
        tabbedPane.addTab( "收支記錄", null, incomeRecordPanel, "記錄每個月的收支狀況" );
        //tabbedPane.addTab( "事項排程", null, scheduledItemPanel, "預計執行事項排程" );
        
        add( tabbedPane );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pack();
    }
}
