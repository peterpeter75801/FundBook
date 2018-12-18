package commonUtil;

import domain.FundingStatus;

public class FundingStatusUtil {
    
    private final static int ATTRIBUTE_NUMBER = 9;
    
    public static FundingStatus getFundingStatusFromCsvTupleString( String tuple ) throws Exception {
        String[] csvDataArray = CsvFormatParser.parseFromTuple( tuple );
        FundingStatus fundingStatus = new FundingStatus();
        fundingStatus.setId( Integer.parseInt( csvDataArray[ 0 ] ) );
        fundingStatus.setType( (csvDataArray[ 1 ].length() <= 0) ? '\0' : csvDataArray[ 1 ].charAt( 0 ) );
        fundingStatus.setYear( Integer.parseInt( csvDataArray[ 2 ] ) );
        fundingStatus.setMonth( Integer.parseInt( csvDataArray[ 3 ] ) );
        fundingStatus.setDay( Integer.parseInt( csvDataArray[ 4 ] ) );
        fundingStatus.setStoredPlaceOrInstitution( csvDataArray[ 5 ] );
        fundingStatus.setAmount( Integer.parseInt( csvDataArray[ 6 ] ) );
        fundingStatus.setDescription( csvDataArray[ 7 ] );
        fundingStatus.setOrderNo( Integer.parseInt( csvDataArray[ 8 ] ) );
        return fundingStatus;
    }
    
    public static String getCsvTupleStringFromFundingStatus( FundingStatus fundingStatus ) {
        String[] csvDataArray = new String[ ATTRIBUTE_NUMBER ];
        csvDataArray[ 0 ] = CsvFormatParser.toCsvData( fundingStatus.getId() );
        csvDataArray[ 1 ] = CsvFormatParser.toCsvData( fundingStatus.getType() );
        csvDataArray[ 2 ] = CsvFormatParser.toCsvData( fundingStatus.getYear() );
        csvDataArray[ 3 ] = CsvFormatParser.toCsvData( fundingStatus.getMonth() );
        csvDataArray[ 4 ] = CsvFormatParser.toCsvData( fundingStatus.getDay() );
        csvDataArray[ 5 ] = CsvFormatParser.toCsvData( fundingStatus.getStoredPlaceOrInstitution() );
        csvDataArray[ 6 ] = CsvFormatParser.toCsvData( fundingStatus.getAmount() );
        csvDataArray[ 7 ] = CsvFormatParser.toCsvData( fundingStatus.getDescription() );
        csvDataArray[ 8 ] = CsvFormatParser.toCsvData( fundingStatus.getOrderNo() );
        return CsvFormatParser.mergeCsvDataToATuple( csvDataArray );
    }
    
    public static FundingStatus copy( FundingStatus fundingStatus ) {
        if( fundingStatus == null ) {
            return null;
        } else {
            FundingStatus clone = new FundingStatus();
            clone.setId( fundingStatus.getId() );
            clone.setType( fundingStatus.getType() );
            clone.setYear( fundingStatus.getYear() );
            clone.setMonth( fundingStatus.getMonth() );
            clone.setDay( fundingStatus.getDay() );
            clone.setStoredPlaceOrInstitution( fundingStatus.getStoredPlaceOrInstitution() );
            clone.setAmount( fundingStatus.getAmount() );
            clone.setDescription( fundingStatus.getDescription() );
            clone.setOrderNo( fundingStatus.getOrderNo() );
            return clone;
        }
    }
    
    public static boolean equals( FundingStatus data1, FundingStatus data2 ) {
        if( data1 == null && data2 == null ) {
            return true;
        } else if( data1 == null || data2 == null ) {
            return false;
        } else if( ComparingUtil.compare( data1.getId(), data2.getId() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getType(), data2.getType() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getYear(), data2.getYear() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getMonth(), data2.getMonth() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDay(), data2.getDay() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getStoredPlaceOrInstitution(), data2.getStoredPlaceOrInstitution() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getAmount(), data2.getAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDescription(), data2.getDescription() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getOrderNo(), data2.getOrderNo() ) != 0 ) {
            return false;
        } else {
            return true;
        }
    }
}
