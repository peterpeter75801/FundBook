package commonUtil;

public class ComparingUtil {
    
    public static int compare( String s1, String s2 ) {
        if( s1 == null && s2 == null ) {
            return 0;
        } else if( s1 != null && s2 == null ) {
            return 1;
        } else if( s1 == null && s2 != null ) {
            return -1;
        } else {
            return s1.compareTo( s2 );
        }
    }
    
    public static int compare( Integer i1, Integer i2 ) {
        if( i1 == null && i2 == null ) {
            return 0;
        } else if( i1 != null && i2 == null ) {
            return 1;
        } else if( i1 == null && i2 != null ) {
            return -1;
        } else {
            return i1.compareTo( i2 );
        }
    }
    
    public static int compare( Character c1, Character c2 ) {
        if( c1 == null && c2 == null ) {
            return 0;
        } else if( c1 != null && c2 == null ) {
            return 1;
        } else if( c1 == null && c2 != null ) {
            return -1;
        } else {
            return c1.compareTo( c2 );
        }
    }
}
