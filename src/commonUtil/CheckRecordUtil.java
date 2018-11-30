package commonUtil;

import domain.CheckRecord;

public class CheckRecordUtil {
    
    private final static int ATTRIBUTE_NUMBER = 11;
    
    public static CheckRecord getCheckRecordFromCsvTupleString( String tuple ) throws Exception {
        String[] csvDataArray = CsvFormatParser.parseFromTuple( tuple );
        CheckRecord checkRecord = new CheckRecord();
        checkRecord.setId( Integer.parseInt( csvDataArray[ 0 ] ) );
        checkRecord.setYear( Integer.parseInt( csvDataArray[ 1 ] ) );
        checkRecord.setMonth( Integer.parseInt( csvDataArray[ 2 ] ) );
        checkRecord.setDay( Integer.parseInt( csvDataArray[ 3 ] ) );
        checkRecord.setHour( Integer.parseInt( csvDataArray[ 4 ] ) );
        checkRecord.setMinute( Integer.parseInt( csvDataArray[ 5 ] ) );
        checkRecord.setSecond( Integer.parseInt( csvDataArray[ 6 ] ) );
        checkRecord.setDifference( Integer.parseInt( csvDataArray[ 7 ] ) );
        checkRecord.setBookAmount( Integer.parseInt( csvDataArray[ 8 ] ) );
        checkRecord.setActualAmount( Integer.parseInt( csvDataArray[ 9 ] ) );
        checkRecord.setDescription( csvDataArray[ 10 ] );
        return checkRecord;
    }
    
    public static String getCsvTupleStringFromCheckRecord( CheckRecord checkRecord ) {
        String[] csvDataArray = new String[ ATTRIBUTE_NUMBER ];
        csvDataArray[ 0 ] = CsvFormatParser.toCsvData( checkRecord.getId() );
        csvDataArray[ 1 ] = CsvFormatParser.toCsvData( checkRecord.getYear() );
        csvDataArray[ 2 ] = CsvFormatParser.toCsvData( checkRecord.getMonth() );
        csvDataArray[ 3 ] = CsvFormatParser.toCsvData( checkRecord.getDay() );
        csvDataArray[ 4 ] = CsvFormatParser.toCsvData( checkRecord.getHour() );
        csvDataArray[ 5 ] = CsvFormatParser.toCsvData( checkRecord.getMinute() );
        csvDataArray[ 6 ] = CsvFormatParser.toCsvData( checkRecord.getSecond() );
        csvDataArray[ 7 ] = CsvFormatParser.toCsvData( checkRecord.getDifference() );
        csvDataArray[ 8 ] = CsvFormatParser.toCsvData( checkRecord.getBookAmount() );
        csvDataArray[ 9 ] = CsvFormatParser.toCsvData( checkRecord.getActualAmount() );
        csvDataArray[ 10 ] = CsvFormatParser.toCsvData( checkRecord.getDescription() );
        return CsvFormatParser.mergeCsvDataToATuple( csvDataArray );
    }
    
    public static CheckRecord copy( CheckRecord checkRecord ) {
        if( checkRecord == null ) {
            return null;
        } else {
            CheckRecord clone = new CheckRecord();
            clone.setId( checkRecord.getId() );
            clone.setYear( checkRecord.getYear() );
            clone.setMonth( checkRecord.getMonth() );
            clone.setDay( checkRecord.getDay() );
            clone.setHour( checkRecord.getHour() );
            clone.setMinute( checkRecord.getMinute() );
            clone.setSecond( checkRecord.getSecond() );
            clone.setDifference( checkRecord.getDifference() );
            clone.setBookAmount( checkRecord.getBookAmount() );
            clone.setActualAmount( checkRecord.getActualAmount() );
            clone.setDescription( checkRecord.getDescription() );
            return clone;
        }
    }
    
    public static boolean equals( CheckRecord data1, CheckRecord data2 ) {
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
        } else if( ComparingUtil.compare( data1.getDifference(), data2.getDifference() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getBookAmount(), data2.getBookAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getActualAmount(), data2.getActualAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDescription(), data2.getDescription() ) != 0 ) {
            return false;
        } else {
            return true;
        }
    }
}
