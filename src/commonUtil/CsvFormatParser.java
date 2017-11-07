package commonUtil;

import java.util.ArrayList;

public class CsvFormatParser {
    
    private enum ParseStatus { NORMAL, LEFT_DOUBLE_QUOTE, ESCAPE_CHAR }

    /**
     * parse individual data from a tuple. For example:
     * if input is
     *   123,test,"hello world","double-quote""",,",comma"
     * then output will be
     *   {"123", "test", "hello world", "double-quote\"", "", ",comma"}
     */
    public static String[] parseFromTuple( String tuple ) {
        ParseStatus status = ParseStatus.NORMAL;
        ArrayList<String> csvDataList = new ArrayList<String>();
        StringBuffer csvDataBuf = new StringBuffer();
        for( int i = 0; i < tuple.length(); i++ ) {
            switch( status ) {
            case NORMAL:
                if( tuple.charAt( i ) == '"' ) {
                    status = ParseStatus.LEFT_DOUBLE_QUOTE;
                } else if( tuple.charAt( i ) == ',' ) {
                    csvDataList.add( csvDataBuf.toString() );
                    csvDataBuf = new StringBuffer();
                } else {
                    csvDataBuf.append( tuple.charAt( i ) );
                }
                break;
            case LEFT_DOUBLE_QUOTE:
                if( tuple.charAt( i ) == '"' ) {
                    status = ParseStatus.ESCAPE_CHAR;
                } else {
                    csvDataBuf.append( tuple.charAt( i ) );
                }
                break;
            case ESCAPE_CHAR:
                if( tuple.charAt( i ) == '"' ) {
                    csvDataBuf.append( '"' );
                    status = ParseStatus.LEFT_DOUBLE_QUOTE;
                } else if( tuple.charAt( i ) == ',' ) {
                    csvDataList.add( csvDataBuf.toString() );
                    csvDataBuf = new StringBuffer();
                    status = ParseStatus.NORMAL;
                } else {
                    csvDataBuf.append( tuple.charAt( i ) );
                    status = ParseStatus.NORMAL;
                }
                break;
            default:
                break;
            }
            if( i == tuple.length() - 1 ) {
                csvDataList.add( csvDataBuf.toString() );
                csvDataBuf = new StringBuffer();
            }
        }

        return csvDataList.toArray( new String[ csvDataList.size() ] );
    }
    
    /**
     * translate String type data to csv format data.
     */
    public static String toCsvData( String data ) {
        StringBuffer csvDataBuf = new  StringBuffer();
        if( data == null ) {
            return "\"\"";
        }
        csvDataBuf.append( '"' );
        for( int i = 0; i < data.length(); i++ ) {
            if( data.charAt( i ) == '"' ) {
                csvDataBuf.append( "\"\"" );
            } else {
                csvDataBuf.append( data.charAt( i ) );
            }
        }
        csvDataBuf.append( '"' );
         
        return csvDataBuf.toString();
    }

    /**
     * translate Integer type data to csv format data.
     */
    public static String toCsvData( Integer data ) { 
        if( data == null ) {
            return "";
        } else {
            return data.toString();
        }
    }

    /**
     * translate Character type data to csv format data.
     */
    public static String toCsvData( Character data ) {
        if( data == null || data == '\0' ) {
            return "";
        } else {
            return data.toString();
        }
    }

    /**
     * merge multiple CSV data to a tuple.
     */
    public static String mergeCsvDataToATuple( String[] csvData ) {
        StringBuffer tupleBuf = new StringBuffer();
        for( int i = 0; i < csvData.length; i++ ) {
            tupleBuf.append( csvData[ i ] );
            if( i < csvData.length - 1 ) {
                tupleBuf.append( ',' );
            }
        }
        return tupleBuf.toString();
    }
}
