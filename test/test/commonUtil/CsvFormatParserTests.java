package test.commonUtil;

import commonUtil.CsvFormatParser;

import junit.framework.TestCase;

public class CsvFormatParserTests extends TestCase {

    public void testParseFromTuple() {
        String input = "123,test,\"hello world\",\"double-quote\"\"\",,\",comma\"";
        String[] output = CsvFormatParser.parseFromTuple( input );
        assertEquals( 6, output.length );
        try {
            assertEquals( "123", output[ 0 ] );
            assertEquals( "test", output[ 1 ] );
            assertEquals( "hello world", output[ 2 ] );
            assertEquals( "double-quote\"", output[ 3 ] );
            assertEquals( "", output[ 4 ] );
            assertEquals( ",comma", output[ 5 ] );
        } catch( ArrayIndexOutOfBoundsException e ) {
            assertTrue( e.getMessage(), false );
        }
    }

    public void testToCsvDataString() {
        String input1 = "test";
        String input2 = "test, test";
        String input3 = "test\"123\"";
        String output1 = CsvFormatParser.toCsvData( input1 );
        String output2 = CsvFormatParser.toCsvData( input2 );
        String output3 = CsvFormatParser.toCsvData( input3 );
        assertEquals( "\"test\"", output1 );
        assertEquals( "\"test, test\"", output2 );
        assertEquals( "\"test\"\"123\"\"\"", output3 );
    }
    
    public void testMergeCsvDataToATuple() {
        String[] input = { "\"test\"", "123", "C", "", "\"test\"\"123\"\"\"" };
        String output = CsvFormatParser.mergeCsvDataToATuple( input );
        assertEquals( "\"test\",123,C,,\"test\"\"123\"\"\"", output );
    }
}
