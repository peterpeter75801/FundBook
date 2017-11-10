package commonUtil;

import domain.TotalProperty;

public class TotalPropertyUtil {
    
    private final static int ATTRIBUTE_NUMBER = 8;
    
    public static TotalProperty getTotalPropertyFromCsvTupleString( String tuple ) throws Exception {
        String[] csvDataArray = CsvFormatParser.parseFromTuple( tuple );
        TotalProperty totalProperty = new TotalProperty();
        totalProperty.setId( Integer.parseInt( csvDataArray[ 0 ] ) );
        totalProperty.setYear( Integer.parseInt( csvDataArray[ 1 ] ) );
        totalProperty.setMonth( Integer.parseInt( csvDataArray[ 2 ] ) );
        totalProperty.setDay( Integer.parseInt( csvDataArray[ 3 ] ) );
        totalProperty.setHour( Integer.parseInt( csvDataArray[ 4 ] ) );
        totalProperty.setMinute( Integer.parseInt( csvDataArray[ 5 ] ) );
        totalProperty.setSecond( Integer.parseInt( csvDataArray[ 6 ] ) );
        totalProperty.setTotalAmount( Integer.parseInt( csvDataArray[ 7 ] ) );
        return totalProperty;
    }
    
    public static String getCsvTupleStringFromTotalProperty( TotalProperty incomeRecord ) {
        String[] csvDataArray = new String[ ATTRIBUTE_NUMBER ];
        csvDataArray[ 0 ] = CsvFormatParser.toCsvData( incomeRecord.getId() );
        csvDataArray[ 1 ] = CsvFormatParser.toCsvData( incomeRecord.getYear() );
        csvDataArray[ 2 ] = CsvFormatParser.toCsvData( incomeRecord.getMonth() );
        csvDataArray[ 3 ] = CsvFormatParser.toCsvData( incomeRecord.getDay() );
        csvDataArray[ 4 ] = CsvFormatParser.toCsvData( incomeRecord.getHour() );
        csvDataArray[ 5 ] = CsvFormatParser.toCsvData( incomeRecord.getMinute() );
        csvDataArray[ 6 ] = CsvFormatParser.toCsvData( incomeRecord.getSecond() );
        csvDataArray[ 7 ] = CsvFormatParser.toCsvData( incomeRecord.getTotalAmount() );
        return CsvFormatParser.mergeCsvDataToATuple( csvDataArray );
    }
    
    public static TotalProperty copy( TotalProperty totalProperty ) {
        if( totalProperty == null ) {
            return null;
        } else {
            TotalProperty clone = new TotalProperty();
            clone.setId( totalProperty.getId() );
            clone.setYear( totalProperty.getYear() );
            clone.setMonth( totalProperty.getMonth() );
            clone.setDay( totalProperty.getDay() );
            clone.setHour( totalProperty.getHour() );
            clone.setMinute( totalProperty.getMinute() );
            clone.setSecond( totalProperty.getSecond() );
            clone.setTotalAmount( totalProperty.getTotalAmount() );
            return clone;
        }
    }
    
    public static boolean equals( TotalProperty data1, TotalProperty data2 ) {
        if( data1 == null && data2 == null ) {
            return true;
        } else if( data1 == null || data2 == null ) {
            return false;
        } else if( ComparingUtil.compare( data1.getId(), data2.getId() ) != 0 ) {
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
        } else if( ComparingUtil.compare( data1.getTotalAmount(), data2.getTotalAmount() ) != 0 ) {
            return false;
        } else {
            return true;
        }
    }
}
