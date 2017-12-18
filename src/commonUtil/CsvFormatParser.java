package commonUtil;

import java.util.ArrayList;

public class CsvFormatParser {
    
    private enum ParseStatus { NORMAL, LEFT_DOUBLE_QUOTE, ESCAPE_CHAR }
    private enum HtmlFormatParseStatus { NORMAL, LINE_SEPARATOR, LITTLE_THAN_SYMBOL, GREATER_THAN_SYMBOL, AND_SYMBOL }

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
    
    /**
     * check whether the input data include lineSeparator, '<', '>', or '&' character or not
     */
    public static boolean checkSpecialCharacter( String data ) {
        if( data == null ) {
            return false;
        } else if( data.indexOf( System.lineSeparator() ) == -1 && data.indexOf( "\n" ) == -1 &&
                data.indexOf( "<" ) == -1 && data.indexOf( ">" ) == -1 && data.indexOf( "&" ) == -1 ) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * transfer special characters (lineSeparator, '<', '>', and '&') to ("<br />", "&lt;", "&gt;", and "&amp;")
     */
    public static String specialCharacterToHtmlFormat( String data ) {
        if( data == null || data.length() <= 0 ) {
            return "";
        }
        
        StringBuffer dataBuffer = new StringBuffer( data );
        int currentIndex = 0;
        
        String htmlLineSeparator = "<br />";
        String htmlLittleThan = "&lt;";
        String htmlGreaterThan = "&gt;";
        String htmlAndSymbol = "&amp;";
        
        while( currentIndex < dataBuffer.length() && 
                (currentIndex = dataBuffer.indexOf( "&", currentIndex )) != -1 ) {
            dataBuffer.replace( currentIndex, currentIndex + "&".length(), htmlAndSymbol );
            currentIndex++;
        }
        currentIndex = 0;
        while( currentIndex < dataBuffer.length() && 
                (currentIndex = dataBuffer.indexOf( "<", currentIndex )) != -1 ) {
            dataBuffer.replace( currentIndex, currentIndex + "<".length(), htmlLittleThan );
            currentIndex++;
        }
        currentIndex = 0;
        while( currentIndex < dataBuffer.length() && 
                (currentIndex = dataBuffer.indexOf( ">", currentIndex )) != -1 ) {
            dataBuffer.replace( currentIndex, currentIndex + ">".length(), htmlGreaterThan );
            currentIndex++;
        }
        currentIndex = 0;
        while( currentIndex < dataBuffer.length() && 
                (currentIndex = dataBuffer.indexOf( System.lineSeparator(), currentIndex )) != -1 ) {
            dataBuffer.replace( currentIndex, currentIndex + System.lineSeparator().length(), htmlLineSeparator );
            currentIndex++;
        }
        currentIndex = 0;
        while( currentIndex < dataBuffer.length() && 
                (currentIndex = dataBuffer.indexOf( "\n", currentIndex )) != -1 ) {
            dataBuffer.replace( currentIndex, currentIndex + "\n".length(), htmlLineSeparator );
            currentIndex++;
        }
        
        return dataBuffer.toString();
    }
    
    /**
     * restore special characters (lineSeparator, '<', '>', and '&') from ("<br />", "&lt;", "&gt;", and "&amp;")
     */
    public static String restoreCharacterFromHtmlFormat( String htmlFormatData ) {
        if( htmlFormatData == null || htmlFormatData.length() <= 0 ) {
            return "";
        }
        
        StringBuffer dataBuffer = new StringBuffer();
        StringBuffer keywordBuffer = new StringBuffer();
        HtmlFormatParseStatus status = HtmlFormatParseStatus.NORMAL;
        for( int i = 0; i < htmlFormatData.length(); i++ ) {
            switch( status ) {
            case NORMAL:
                if( htmlFormatData.charAt( i ) == '<' ) {
                    keywordBuffer.append( '<' );
                    status = HtmlFormatParseStatus.LINE_SEPARATOR;
                } else if( htmlFormatData.charAt( i ) == '&' ) {
                    keywordBuffer.append( '&' );
                    status = HtmlFormatParseStatus.AND_SYMBOL;
                } else {
                    dataBuffer.append( htmlFormatData.charAt( i ) );
                }
                break;
            case LINE_SEPARATOR:
                if( keywordBuffer.toString().equals( "<" ) && htmlFormatData.charAt( i ) == 'b' ) {
                    keywordBuffer.append( 'b' );
                } else if( keywordBuffer.toString().equals( "<b" ) && htmlFormatData.charAt( i ) == 'r' ) {
                    keywordBuffer.append( 'r' );
                } else if( keywordBuffer.toString().equals( "<br" ) && htmlFormatData.charAt( i ) == ' ' ) {
                    keywordBuffer.append( ' ' );
                } else if( keywordBuffer.toString().equals( "<br " ) && htmlFormatData.charAt( i ) == '/' ) {
                    keywordBuffer.append( '/' );
                } else if( keywordBuffer.toString().equals( "<br /" ) && htmlFormatData.charAt( i ) == '>' ) {
                    //dataBuffer.append( System.lineSeparator() );
                    dataBuffer.append( '\n' );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                } else {
                    dataBuffer.append( keywordBuffer.toString() );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                    i--;
                }
                break;
            case AND_SYMBOL:
                if( keywordBuffer.toString().equals( "&" ) && htmlFormatData.charAt( i ) == 'l' ) {
                    keywordBuffer.append( 'l' );
                    status = HtmlFormatParseStatus.LITTLE_THAN_SYMBOL;
                } else if( keywordBuffer.toString().equals( "&" ) && htmlFormatData.charAt( i ) == 'g' ) {
                    keywordBuffer.append( 'g' );
                    status = HtmlFormatParseStatus.GREATER_THAN_SYMBOL;
                } else if( keywordBuffer.toString().equals( "&" ) && htmlFormatData.charAt( i ) == 'a' ) {
                    keywordBuffer.append( 'a' );
                } else if( keywordBuffer.toString().equals( "&a" ) && htmlFormatData.charAt( i ) == 'm' ) {
                    keywordBuffer.append( 'm' );
                } else if( keywordBuffer.toString().equals( "&am" ) && htmlFormatData.charAt( i ) == 'p' ) {
                    keywordBuffer.append( 'p' );
                } else if( keywordBuffer.toString().equals( "&amp" ) && htmlFormatData.charAt( i ) == ';' ) {
                    dataBuffer.append( '&' );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                } else {
                    dataBuffer.append( keywordBuffer.toString() );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                    i--;
                }
                break;
            case LITTLE_THAN_SYMBOL:
                if( keywordBuffer.toString().equals( "&l" ) && htmlFormatData.charAt( i ) == 't' ) {
                    keywordBuffer.append( 't' );
                } else if( keywordBuffer.toString().equals( "&lt" ) && htmlFormatData.charAt( i ) == ';' ) {
                    dataBuffer.append( '<' );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                } else {
                    dataBuffer.append( keywordBuffer.toString() );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                    i--;
                }
                break;
            case GREATER_THAN_SYMBOL:
                if( keywordBuffer.toString().equals( "&g" ) && htmlFormatData.charAt( i ) == 't' ) {
                    keywordBuffer.append( 't' );
                } else if( keywordBuffer.toString().equals( "&gt" ) && htmlFormatData.charAt( i ) == ';' ) {
                    dataBuffer.append( '>' );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                } else {
                    dataBuffer.append( keywordBuffer.toString() );
                    keywordBuffer = new StringBuffer();
                    status = HtmlFormatParseStatus.NORMAL;
                    i--;
                }
                break;
            default:
                break;
            }
        }
        if( keywordBuffer.length() > 0 ) {
            dataBuffer.append( keywordBuffer.toString() );
        }
        
        return dataBuffer.toString();
    }
}
