package view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import main.FundBookServices;
import view.IncomeRecord.IncomeRecordPanel;

public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JTabbedPane tabbedPane;
    private IncomeRecordPanel incomeRecordPanel;
    //private ScheduledItemPanel scheduledItemPanel;
    
    public MainFrame( FundBookServices fundBookServices ) {
        super( "Fund Book" );
        
        tabbedPane = new JTabbedPane();
        
        incomeRecordPanel = new IncomeRecordPanel( fundBookServices, this );
        tabbedPane.addTab( "收支記錄", null, incomeRecordPanel, "記錄每個月的收支狀況" );
        
        add( tabbedPane );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        pack();
    }
}
