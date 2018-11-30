package commonUtil;

import domain.DigitalWallet;

public class DigitalWalletUtil {
    
    private final static int ATTRIBUTE_NUMBER = 8;
    
    public static DigitalWallet getDigitalWalletFromCsvTupleString( String tuple ) throws Exception {
        String[] csvDataArray = CsvFormatParser.parseFromTuple( tuple );
        DigitalWallet digitalWallet = new DigitalWallet();
        digitalWallet.setId( Integer.parseInt( csvDataArray[ 0 ] ) );
        digitalWallet.setName( csvDataArray[ 1 ] );
        digitalWallet.setYear( Integer.parseInt( csvDataArray[ 2 ] ) );
        digitalWallet.setMonth( Integer.parseInt( csvDataArray[ 3 ] ) );
        digitalWallet.setDay( Integer.parseInt( csvDataArray[ 4 ] ) );
        digitalWallet.setIssuer( csvDataArray[ 5 ] );
        digitalWallet.setAmount( Integer.parseInt( csvDataArray[ 6 ] ) );
        digitalWallet.setDescription( csvDataArray[ 7 ] );
        return digitalWallet;
    }
    
    public static String getCsvTupleStringFromDigitalWallet( DigitalWallet digitalWallet ) {
        String[] csvDataArray = new String[ ATTRIBUTE_NUMBER ];
        csvDataArray[ 0 ] = CsvFormatParser.toCsvData( digitalWallet.getId() );
        csvDataArray[ 1 ] = CsvFormatParser.toCsvData( digitalWallet.getName() );
        csvDataArray[ 2 ] = CsvFormatParser.toCsvData( digitalWallet.getYear() );
        csvDataArray[ 3 ] = CsvFormatParser.toCsvData( digitalWallet.getMonth() );
        csvDataArray[ 4 ] = CsvFormatParser.toCsvData( digitalWallet.getDay() );
        csvDataArray[ 5 ] = CsvFormatParser.toCsvData( digitalWallet.getIssuer() );
        csvDataArray[ 6 ] = CsvFormatParser.toCsvData( digitalWallet.getAmount() );
        csvDataArray[ 7 ] = CsvFormatParser.toCsvData( digitalWallet.getDescription() );
        return CsvFormatParser.mergeCsvDataToATuple( csvDataArray );
    }
    
    public static DigitalWallet copy( DigitalWallet digitalWallet ) {
        if( digitalWallet == null ) {
            return null;
        } else {
            DigitalWallet clone = new DigitalWallet();
            clone.setId( digitalWallet.getId() );
            clone.setName( digitalWallet.getName() );
            clone.setYear( digitalWallet.getYear() );
            clone.setMonth( digitalWallet.getMonth() );
            clone.setDay( digitalWallet.getDay() );
            clone.setIssuer( digitalWallet.getIssuer() );
            clone.setAmount( digitalWallet.getAmount() );
            clone.setDescription( digitalWallet.getDescription() );
            return clone;
        }
    }
    
    public static boolean equals( DigitalWallet data1, DigitalWallet data2 ) {
        if( data1 == null && data2 == null ) {
            return true;
        } else if( data1 == null || data2 == null ) {
            return false;
        } else if( ComparingUtil.compare( data1.getId(), data2.getId() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getName(), data2.getName() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getYear(), data2.getYear() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getMonth(), data2.getMonth() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDay(), data2.getDay() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getIssuer(), data2.getIssuer() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getAmount(), data2.getAmount() ) != 0 ) {
            return false;
        } else if( ComparingUtil.compare( data1.getDescription(), data2.getDescription() ) != 0 ) {
            return false;
        } else {
            return true;
        }
    }
}
