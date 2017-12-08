package commonUtil;

public class StringUtil {
    
    public static boolean isNumber( String input ) {
        if( input == null || input.length() <= 0 ) {
            return false;
        }
        if( input.charAt( 0 ) != '-' && (input.charAt( 0 ) < '0' || input.charAt( 0 ) > '9') ) {
            return false;
        }
        for( int i = 1; i < input.length(); i++ ) {
            if( input.charAt( i ) < '0' || input.charAt( i ) > '9' ) {
                return false;
            }
        }
        return true;
    }
}
