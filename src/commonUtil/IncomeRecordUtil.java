package commonUtil;

import java.util.List;

import domain.IncomeRecord;

public class IncomeRecordUtil {
    
    private final static int ATTRIBUTE_NUMBER = 9;
    
    public static IncomeRecord getIncomeRecordFromCsvTupleString( String tuple ) throws Exception {
        String[] csvDataArray = CsvFormatParser.parseFromTuple( tuple );
        IncomeRecord incomeRecord = new IncomeRecord();
        incomeRecord.setId( Integer.parseInt( csvDataArray[ 0 ] ) );
        incomeRecord.setYear( Integer.parseInt( csvDataArray[ 1 ] ) );
        incomeRecord.setMonth( Integer.parseInt( csvDataArray[ 2 ] ) );
        incomeRecord.setDay( Integer.parseInt( csvDataArray[ 3 ] ) );
        incomeRecord.setItem( csvDataArray[ 4 ] );
        incomeRecord.setClassNo( Integer.parseInt( csvDataArray[ 5 ] ) );
        incomeRecord.setAmount( Integer.parseInt( csvDataArray[ 6 ] ) );
        incomeRecord.setDescription( csvDataArray[ 7 ] );
        incomeRecord.setOrderNo( Integer.parseInt( csvDataArray[ 8 ] ) );
        return incomeRecord;
    }
    
    public static String getCsvTupleStringFromIncomeRecord( IncomeRecord incomeRecord ) {
        String[] csvDataArray = new String[ ATTRIBUTE_NUMBER ];
        csvDataArray[ 0 ] = CsvFormatParser.toCsvData( incomeRecord.getId() );
        csvDataArray[ 1 ] = CsvFormatParser.toCsvData( incomeRecord.getYear() );
        csvDataArray[ 2 ] = CsvFormatParser.toCsvData( incomeRecord.getMonth() );
        csvDataArray[ 3 ] = CsvFormatParser.toCsvData( incomeRecord.getDay() );
        csvDataArray[ 4 ] = CsvFormatParser.toCsvData( incomeRecord.getItem() );
        csvDataArray[ 5 ] = CsvFormatParser.toCsvData( incomeRecord.getClassNo() );
        csvDataArray[ 6 ] = CsvFormatParser.toCsvData( incomeRecord.getAmount() );
        csvDataArray[ 7 ] = CsvFormatParser.toCsvData( incomeRecord.getDescription() );
        csvDataArray[ 8 ] = CsvFormatParser.toCsvData( incomeRecord.getOrderNo() );
        return CsvFormatParser.mergeCsvDataToATuple( csvDataArray );
    }
    
    public static String getIncomeRecordCsvFileName( IncomeRecord incomeRecord ) {
        return String.format( "%04d.%02d.csv", incomeRecord.getYear(), incomeRecord.getMonth() );
    }
    
    public static String getIncomeRecordCsvFileName( int year, int month ) {
        return String.format( "%04d.%02d.csv", year, month );
    }
    
    public static List<IncomeRecord> sortById( List<IncomeRecord> incomeRecordList ) {
        if( incomeRecordList == null ) {
            return incomeRecordList;
        }
        for( int i = 1; i < incomeRecordList.size(); i++ ) {
            for( int j = 0; j < incomeRecordList.size() - 1; j++ ) {
                if( incomeRecordList.get( j ).getId() > incomeRecordList.get( j + 1 ).getId() ) {
                    IncomeRecord swap = incomeRecordList.get( j );
                    incomeRecordList.set( j, incomeRecordList.get( j + 1 ) );
                    incomeRecordList.set( j + 1, swap );
                }
            }
        }
        return incomeRecordList;
    }
    
    public static List<IncomeRecord> sortByDay( List<IncomeRecord> incomeRecordList ) {
        if( incomeRecordList == null ) {
            return incomeRecordList;
        }
        for( int i = 1; i < incomeRecordList.size(); i++ ) {
            for( int j = 0; j < incomeRecordList.size() - 1; j++ ) {
                if( incomeRecordList.get( j ).getDay() > incomeRecordList.get( j + 1 ).getDay() ) {
                    IncomeRecord swap = incomeRecordList.get( j );
                    incomeRecordList.set( j, incomeRecordList.get( j + 1 ) );
                    incomeRecordList.set( j + 1, swap );
                }
            }
        }
        return incomeRecordList;
    }
    
    public static int compareYearMonth( IncomeRecord incomeRecord1, IncomeRecord incomeRecord2 ) {
        int compareValue = 0;
         
        if( incomeRecord1 == null && incomeRecord2 == null ) {
            return 0;
        } else if( incomeRecord1 != null && incomeRecord2 == null ) {
            return 1;
        } else if( incomeRecord1 == null && incomeRecord2 != null ) {
            return -1;
        }
        
        if( (compareValue = ComparingUtil.compare( incomeRecord1.getYear(), incomeRecord2.getYear() )) != 0 ) {
            return compareValue;
        }
        if( (compareValue = ComparingUtil.compare( incomeRecord1.getMonth(), incomeRecord2.getMonth() )) != 0 ) {
            return compareValue;
        }
        
        return 0;
    }
    
    public static IncomeRecord copy( IncomeRecord incomeRecord ) {
        if( incomeRecord == null ) {
            return null;
        } else {
            IncomeRecord clone = new IncomeRecord();
            clone.setId( incomeRecord.getId() );
            clone.setYear( incomeRecord.getYear() );
            clone.setMonth( incomeRecord.getMonth() );
            clone.setDay( incomeRecord.getDay() );
            clone.setItem( incomeRecord.getItem() );
            clone.setClassNo( incomeRecord.getClassNo() );
            clone.setAmount( incomeRecord.getAmount() );
            clone.setDescription( incomeRecord.getDescription() );
            clone.setOrderNo( incomeRecord.getOrderNo() );
            return clone;
        }
    }
    
    public static boolean equals( IncomeRecord data1, IncomeRecord data2 ) {
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
        } else if( ComparingUtil.compare( data1.getItem(), data2.getItem() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getClassNo(), data2.getClassNo() ) != 0 ) {
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
