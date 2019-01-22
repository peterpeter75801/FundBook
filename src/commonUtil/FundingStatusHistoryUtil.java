package commonUtil;

import java.util.List;

import domain.FundingStatusHistory;

public class FundingStatusHistoryUtil {
    
    private final static int ATTRIBUTE_NUMBER = 13;
    
    public static FundingStatusHistory getFundingStatusHistoryFromCsvTupleString( String tuple ) throws Exception {
        String[] csvDataArray = CsvFormatParser.parseFromTuple( tuple );
        FundingStatusHistory fundingStatusHistory =new FundingStatusHistory();
        fundingStatusHistory.setId( Integer.parseInt( csvDataArray[ 0 ] ) );
        fundingStatusHistory.setFundingStatusId( Integer.parseInt( csvDataArray[ 1 ] ) );
        fundingStatusHistory.setYear( Integer.parseInt( csvDataArray[ 2 ] ) );
        fundingStatusHistory.setMonth( Integer.parseInt( csvDataArray[ 3 ] ) );
        fundingStatusHistory.setDay( Integer.parseInt( csvDataArray[ 4 ] ) );
        fundingStatusHistory.setHour( Integer.parseInt( csvDataArray[ 5 ] ) );
        fundingStatusHistory.setMinute( Integer.parseInt( csvDataArray[ 6 ] ) );
        fundingStatusHistory.setSecond( Integer.parseInt( csvDataArray[ 7 ] ) );
        fundingStatusHistory.setAction( (csvDataArray[ 8 ].length() <= 0) ? '\0' : csvDataArray[ 8 ].charAt( 0 ) );
        fundingStatusHistory.setOriginAmount( Integer.parseInt( csvDataArray[ 9 ] ) );
        fundingStatusHistory.setModifiedAmount( Integer.parseInt( csvDataArray[ 10 ] ) );
        fundingStatusHistory.setDifference( Integer.parseInt( csvDataArray[ 11 ] ) );
        fundingStatusHistory.setDescription( csvDataArray[ 12 ] );
        return fundingStatusHistory;
    }
    
    public static String getCsvTupleStringFromFundingStatusHistory( FundingStatusHistory fundingStatusHistory ) {
        String[] csvDataArray = new String[ ATTRIBUTE_NUMBER ];
        csvDataArray[ 0 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getId() );
        csvDataArray[ 1 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getFundingStatusId() );
        csvDataArray[ 2 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getYear() );
        csvDataArray[ 3 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getMonth() );
        csvDataArray[ 4 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getDay() );
        csvDataArray[ 5 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getHour() );
        csvDataArray[ 6 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getMinute() );
        csvDataArray[ 7 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getSecond() );
        csvDataArray[ 8 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getAction() );
        csvDataArray[ 9 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getOriginAmount() );
        csvDataArray[ 10 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getModifiedAmount() );
        csvDataArray[ 11 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getDifference() );
        csvDataArray[ 12 ] = CsvFormatParser.toCsvData( fundingStatusHistory.getDescription() );
        return CsvFormatParser.mergeCsvDataToATuple( csvDataArray );
    }
    
    public static String getFundingStatusHistoryCsvFileName( FundingStatusHistory fundingStatusHistory ) {
        return String.format( "%d.csv", fundingStatusHistory.getFundingStatusId() );
    }
    
    public static String getFundingStatusHistoryCsvFileName( int fundingStatusId ) {
        return String.format( "%d.csv", fundingStatusId );
    }
    
    public static List<FundingStatusHistory> sortById( List<FundingStatusHistory> fundingStatusHistoryList ) {
        if( fundingStatusHistoryList == null ) {
            return fundingStatusHistoryList;
        }
        for( int i = 1; i < fundingStatusHistoryList.size(); i++ ) {
            for( int j = 0; j < fundingStatusHistoryList.size() - 1; j++ ) {
                if( fundingStatusHistoryList.get( j ).getId() > fundingStatusHistoryList.get( j + 1 ).getId() ) {
                    FundingStatusHistory swap = fundingStatusHistoryList.get( j );
                    fundingStatusHistoryList.set( j, fundingStatusHistoryList.get( j + 1 ) );
                    fundingStatusHistoryList.set( j + 1, swap );
                }
            }
        }
        return fundingStatusHistoryList;
    }
    
    public static FundingStatusHistory copy( FundingStatusHistory fundingStatusHistory ) {
        if( fundingStatusHistory == null ) {
            return null;
        } else {
            FundingStatusHistory clone = new FundingStatusHistory();
            clone.setId( fundingStatusHistory.getId() );
            clone.setFundingStatusId( fundingStatusHistory.getFundingStatusId() );
            clone.setYear( fundingStatusHistory.getYear() );
            clone.setMonth( fundingStatusHistory.getMonth() );
            clone.setDay( fundingStatusHistory.getDay() );
            clone.setHour( fundingStatusHistory.getHour() );
            clone.setMinute( fundingStatusHistory.getMinute() );
            clone.setSecond( fundingStatusHistory.getSecond() );
            clone.setAction( fundingStatusHistory.getAction() );
            clone.setOriginAmount( fundingStatusHistory.getOriginAmount() );
            clone.setModifiedAmount( fundingStatusHistory.getModifiedAmount() );
            clone.setDifference( fundingStatusHistory.getDifference() );
            clone.setDescription( fundingStatusHistory.getDescription() );
            return clone;
        }
    }
    
    public static boolean equals( FundingStatusHistory data1, FundingStatusHistory data2 ) {
        if( data1 == null && data2 == null ) {
            return true;
        } else if( data1 == null || data2 == null ) {
            return false;
        } else if( ComparingUtil.compare( data1.getId(), data2.getId() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getFundingStatusId(), data2.getFundingStatusId() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getYear(), data2.getYear() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getMonth(), data2.getMonth() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDay(), data2.getDay() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getHour(), data2.getHour() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getMinute(), data2.getMinute() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getSecond(), data2.getSecond() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getAction(), data2.getAction() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getOriginAmount(), data2.getOriginAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getModifiedAmount(), data2.getModifiedAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDifference(), data2.getDifference() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDescription(), data2.getDescription() ) != 0 ) {
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean equalsIgnoreTime( FundingStatusHistory data1, FundingStatusHistory data2 ) {
        if( data1 == null && data2 == null ) {
            return true;
        } else if( data1 == null || data2 == null ) {
            return false;
        } else if( ComparingUtil.compare( data1.getId(), data2.getId() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getFundingStatusId(), data2.getFundingStatusId() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getYear(), data2.getYear() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getMonth(), data2.getMonth() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDay(), data2.getDay() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getAction(), data2.getAction() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getOriginAmount(), data2.getOriginAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getModifiedAmount(), data2.getModifiedAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDifference(), data2.getDifference() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDescription(), data2.getDescription() ) != 0 ) {
            return false;
        } else {
            return true;
        }
    }
}
