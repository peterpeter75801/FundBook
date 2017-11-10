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
    
    // Storage Configurations
    public static final String FILE_CHARSET = "big5";
    public static final String INITIAL_SEQ_NUMBER = "1";
    
    // Total Property Configurations
    public static final String TOTAL_PROPERTY_CSV_FILE_ATTR_STRING = "id,year,month,day,hour,minute,second,totalAmount";
    public static final String TOTAL_PROPERTY_DATA_PATH = "data\\TotalProperty\\";
    public static final String TOTAL_PROPERTY_FILENAME = "TotalProperty.csv";
    public static final String TOTAL_PROPERTY_SEQ_FILE_PATH = "data\\TotalProperty\\TotalPropertySeq.txt";
}
