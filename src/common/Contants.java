package common;

public class Contants {
    
    // Status Codes
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int ERROR_NOT_COMPLETE = -2;
    public static final int ERROR_NOT_SUPPORT = -4;
    public static final int ERROR_INVALID_PARAMETER = -5;
    public static final int ERROR_EXCEED_UPPER_LIMIT = -6;
    public static final int ERROR_VERSION_OUT_OF_DATE = -7;
    public static final int ERROR_NOT_EXIST = -8;
    public static final int ERROR_EMPTY_FILE = -9;
    public static final int ERROR_EMPTY_NECESSARY_PARAMETER = -10;
    public static final int DUPLICATE_DATA = -3;
    public static final int NO_DATA_MODIFIED = -11;
    
    // Storage Configurations
    public static final String FILE_CHARSET = "big5";
    public static final String INITIAL_SEQ_NUMBER = "1";
    
    // Income Record Configurations
    public static final String INCOME_RECORD_CSV_FILE_ATTR_STRING = "id,year,month,day,item,classNo,amount,description,orderNo";
    public static final String INCOME_RECORD_DATA_PATH = "data\\IncomeRecord\\";
    public static final String INCOME_RECORD_SEQ_FILE_PATH = "data\\IncomeRecord\\IncomeRecordSeq.txt";
    
    // Total Property Configurations
    public static final String TOTAL_PROPERTY_CSV_FILE_ATTR_STRING = "id,year,month,day,hour,minute,second,totalAmount";
    public static final String TOTAL_PROPERTY_DATA_PATH = "data\\TotalProperty\\";
    public static final String TOTAL_PROPERTY_FILENAME = "TotalProperty.csv";
    public static final String TOTAL_PROPERTY_SEQ_FILE_PATH = "data\\TotalProperty\\TotalPropertySeq.txt";
    
    // Funding Status Configurations
    public static final String FUNDING_STATUS_CSV_FILE_ATTR_STRING = "id,type,year,month,day,bankCode,bankName,account,interestAccount,balance";
    public static final String FUNDING_STATUS_DATA_PATH = "data\\FundingStatus\\";
    public static final String FUNDING_STATUS_FILENAME = "FundingStatus.csv";
    public static final String FUNDING_STATUS_SEQ_FILE_PATH = "data\\FundingStatus\\FundingStatusSeq.txt";
    
    // Check Record Configurations
    public static final String CHECK_RECORD_CSV_FILE_ATTR_STRING = "id,year,month,day,hour,minute,second,difference,bookAmount,actualAmount";
    public static final String CHECK_RECORD_DATA_PATH = "data\\CheckRecord\\";
    public static final String CHECK_RECORD_FILENAME = "CheckRecord.csv";
    public static final String CHECK_RECORD_SEQ_FILE_PATH = "data\\CheckRecord\\CheckRecordSeq.txt";
    
    // Version Number
    public static final String VERSION = "Version 0.0.9-pre";
}
