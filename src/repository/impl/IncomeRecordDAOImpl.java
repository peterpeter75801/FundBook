package repository.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import commonUtil.ComparingUtil;
import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import repository.IncomeRecordDAO;

public class IncomeRecordDAOImpl implements IncomeRecordDAO {
    
    private final String INCOME_RECORD_CSV_FILE_ATTR_STRING = "id,year,month,day,item,subclass,amount,description";
    private final String INITIAL_SEQ_NUMBER = "1";
    
    private final String INCOME_RECORD_DATA_PATH = "data\\IncomeRecord\\";
    private final String INCOME_RECORD_SEQ_FILE_PATH = "data\\IncomeRecord\\IncomeRecordSeq.txt";
    private final String FILE_CHARSET = "big5";
    
    @Override
    public boolean insert( IncomeRecord incomeRecord ) throws Exception {
        IncomeRecord scheduledItemWithNewId = IncomeRecordUtil.copy( incomeRecord );
        
        // 取得Income Record目前的流水號(ID)
        if( !checkIfFileExists( INCOME_RECORD_SEQ_FILE_PATH ) ) {
            createSeqFile( INCOME_RECORD_SEQ_FILE_PATH );
        }
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( INCOME_RECORD_SEQ_FILE_PATH ) ),
                FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
            scheduledItemWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // 新增Income Record資料
        String csvFilePath = INCOME_RECORD_DATA_PATH + IncomeRecordUtil.getIncomeRecordCsvFileName( scheduledItemWithNewId );
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    FILE_CHARSET
                )
            );
        writer.write( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( scheduledItemWithNewId ) );
        writer.newLine();
        writer.close();
        
        // 更新Income Record的流水號(ID)
        writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( INCOME_RECORD_SEQ_FILE_PATH ), false ),
                    FILE_CHARSET
                )
            );
        currentSeqNumber++;
        writer.write( currentSeqNumber.toString() );
        writer.newLine();
        writer.close();
        
        return true;
    }

    @Override
    public IncomeRecord findOne( int id, int indexYear, int indexMonth ) throws Exception {
        String csvFilePath = INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( indexYear, indexMonth );
        if( !checkIfFileExists( csvFilePath ) ) {
            return null;
        }
        
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        IncomeRecord searchResultIncomeRecord = null;
        try {
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( csvFilePath ) ),
                    FILE_CHARSET
                ));
            // read attribute titles
            bufReader.readLine();
            // search data
            while( (currentTuple = bufReader.readLine()) != null ) {
                currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
                if( ComparingUtil.compare( currentIncomeRecord.getId(), id ) == 0 ) {
                    searchResultIncomeRecord = currentIncomeRecord;
                    break;
                }
            }
            bufReader.close();
        } catch( FileNotFoundException e ) {
            return null;
        }
        
        return searchResultIncomeRecord;
    }

    @Override
    public List<IncomeRecord> findByMonth( int year, int month ) throws Exception {
        String csvFilePath = INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        ArrayList<IncomeRecord> incomeRecordList = new ArrayList<IncomeRecord>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return incomeRecordList;
        }
        
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            incomeRecordList.add( currentIncomeRecord );
        }
        bufReader.close();
        
        return incomeRecordList;
    }

    @Override
    public boolean update( IncomeRecord incomeRecord, int indexYear, int indexMonth ) throws Exception {
        String csvFilePath = INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( indexYear, indexMonth );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & modify data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentIncomeRecord.getId(), incomeRecord.getId() ) == 0 ) {
                // modify data
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( incomeRecord ) );
            } else {
                // not modify data
                fileContentBuffer.add( currentTuple );
            }
        }
        bufReader.close();
        
        // write file content buffer to CSV data file
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), false ),
                    FILE_CHARSET
                )
            );
        for( String content : fileContentBuffer ) {
            writer.write( content );
            writer.newLine();
        }
        writer.close();
        
        return true;
    }

    @Override
    public boolean delete( IncomeRecord incomeRecord, int indexYear, int indexMonth ) throws Exception {
        String csvFilePath = INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( indexYear, indexMonth );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & delete data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentIncomeRecord.getId(), incomeRecord.getId() ) != 0 ) {
                // keep data that not match the deleting data
                fileContentBuffer.add( currentTuple );
            }
        }
        bufReader.close();
        
        // write file content buffer to Item/yyyy.mm.dd.csv
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), false ),
                    FILE_CHARSET
                )
            );
        for( String content : fileContentBuffer ) {
            writer.write( content );
            writer.newLine();
        }
        writer.close();
        
        return true;
    }

    @Override
    public int getCurrentSeqNumber() throws Exception {
        if( !checkIfFileExists( INCOME_RECORD_SEQ_FILE_PATH ) ) {
            return Integer.parseInt( INITIAL_SEQ_NUMBER ) - 1;
        }
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( INCOME_RECORD_SEQ_FILE_PATH ) ),
                FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
        } catch( NumberFormatException e ) {
            return Integer.MAX_VALUE;
        } finally {
            bufReader.close();
        }
        
        return currentSeqNumber;
    }
    
    private boolean checkIfFileExists( String fileName ) {
        File f = new File( fileName );
        if( !f.exists() ) {
            return false;
        } else if( f.isDirectory() || f.length() <= 0 ) {
            f.delete();
            return false;
        } else {
            return true;
        }
    }
    
    private void createCsvFile( String fileName ) throws IOException {
        File f = new File( INCOME_RECORD_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    FILE_CHARSET 
                ) 
            );
        bufWriter.write( INCOME_RECORD_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( INCOME_RECORD_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    FILE_CHARSET 
                ) 
            );
        bufWriter.write( INITIAL_SEQ_NUMBER );
        bufWriter.newLine();
        bufWriter.close();
    }
}
