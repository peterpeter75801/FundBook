package main;

import repository.CheckRecordDAO;
import repository.FundingStatusDAO;
import repository.IncomeRecordDAO;
import repository.TotalPropertyDAO;
import repository.impl.CheckRecordDAOImpl;
import repository.impl.FundingStatusDAOImpl;
import repository.impl.IncomeRecordDAOImpl;
import repository.impl.TotalPropertyDAOImpl;
import service.impl.CheckRecordServiceImpl;
import service.impl.FundingStatusServiceImpl;
import service.impl.IncomeRecordServiceImpl;
import service.impl.TotalPropertyServiceImpl;
import view.MainFrame;

public class FundBook {
    
    public static void main( String args[] ) {
        // Initialize DAOs
        IncomeRecordDAO incomeRecordDAO = new IncomeRecordDAOImpl();
        TotalPropertyDAO totalPropertyDAO = new TotalPropertyDAOImpl();
        FundingStatusDAO fundingStatusDAO = new FundingStatusDAOImpl();
        CheckRecordDAO checkRecordDAO = new CheckRecordDAOImpl();
        
        // Initialize services
        FundBookServices fundBookServices = new FundBookServices();
        fundBookServices.setIncomeRecordService( new IncomeRecordServiceImpl( incomeRecordDAO ) );
        fundBookServices.setTotalPropertyService( new TotalPropertyServiceImpl( totalPropertyDAO ) );
        fundBookServices.setFundingStatusService( new FundingStatusServiceImpl( fundingStatusDAO ) );
        fundBookServices.setCheckRecordService( new CheckRecordServiceImpl( checkRecordDAO ) );
        
        // Initialize UI
        MainFrame mainFrame = new MainFrame( fundBookServices );
        mainFrame.setVisible( true );
    }
}
