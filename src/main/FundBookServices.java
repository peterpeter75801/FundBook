package main;

import service.CheckRecordService;
import service.DigitalWalletService;
import service.FundingStatusHistoryService;
import service.FundingStatusService;
import service.IncomeRecordService;
import service.TotalPropertyService;

public class FundBookServices {
    
    private IncomeRecordService incomeRecordService;
    private TotalPropertyService totalPropertyService;
    private FundingStatusService fundingStatusService;
    private FundingStatusHistoryService fundingStatusHistoryService;
    private CheckRecordService checkRecordService;
    private DigitalWalletService digitalWalletService;
    
    public void setIncomeRecordService( IncomeRecordService incomeRecordService ) {
        this.incomeRecordService = incomeRecordService;
    }

    public IncomeRecordService getIncomeRecordService() {
        return incomeRecordService;
    }

    public void setTotalPropertyService( TotalPropertyService totalPropertyService ) {
        this.totalPropertyService = totalPropertyService;
    }

    public TotalPropertyService getTotalPropertyService() {
        return totalPropertyService;
    }

    public void setFundingStatusService( FundingStatusService fundingStatusService ) {
        this.fundingStatusService = fundingStatusService;
    }

    public FundingStatusService getFundingStatusService() {
        return fundingStatusService;
    }

    public void setFundingStatusHistoryService( FundingStatusHistoryService fundingStatusHistoryService ) {
        this.fundingStatusHistoryService = fundingStatusHistoryService;
    }

    public FundingStatusHistoryService getFundingStatusHistoryService() {
        return fundingStatusHistoryService;
    }

    public void setCheckRecordService( CheckRecordService checkRecordService ) {
        this.checkRecordService = checkRecordService;
    }

    public CheckRecordService getCheckRecordService() {
        return checkRecordService;
    }
    
    public void setDigitalWalletService( DigitalWalletService digitalWalletService ) {
        this.digitalWalletService = digitalWalletService;
    }
    
    public DigitalWalletService getDigitalWalletService() {
        return digitalWalletService;
    }
}
