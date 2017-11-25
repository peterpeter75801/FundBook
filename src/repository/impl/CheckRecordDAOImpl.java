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

import common.Contants;
import commonUtil.CheckRecordUtil;
import commonUtil.ComparingUtil;
import domain.CheckRecord;
import repository.CheckRecordDAO;

public class CheckRecordDAOImpl implements CheckRecordDAO {
    
    @Override
    public boolean insert( CheckRecord checkRecord ) throws Exception {
        CheckRecord checkRecordWithNewId = CheckRecordUtil.copy( checkRecord );
        
        // 取得Check Record目前的流水號(ID)
        if( !checkIfFileExists( Contants.CHECK_RECORD_SEQ_FILE_PATH ) ) {
            createSeqFile( Contants.CHECK_RECORD_SEQ_FILE_PATH );
        }
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.CHECK_RECORD_SEQ_FILE_PATH ) ),
                Contants.FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
            checkRecordWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // 新增Check Record資料
        String csvFilePath = Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( CheckRecordUtil.getCsvTupleStringFromCheckRecord( checkRecordWithNewId ) );
        writer.newLine();
        writer.close();
        
        // 更新Check Record的流水號(ID)
        writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( Contants.CHECK_RECORD_SEQ_FILE_PATH ), false ),
                    Contants.FILE_CHARSET
                )
            );
        currentSeqNumber++;
        writer.write( currentSeqNumber.toString() );
        writer.newLine();
        writer.close();
        
        return true;
    }

    @Override
    public CheckRecord findOne( int id ) throws Exception {
        String csvFilePath = Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return null;
        }
        
        String currentTuple = "";
        CheckRecord currentCheckRecord = new CheckRecord();
        CheckRecord searchResultCheckRecord = null;
        try {
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( csvFilePath ) ),
                    Contants.FILE_CHARSET
                ));
            // read attribute titles
            bufReader.readLine();
            // search data
            while( (currentTuple = bufReader.readLine()) != null ) {
                currentCheckRecord = CheckRecordUtil.getCheckRecordFromCsvTupleString( currentTuple );
                if( ComparingUtil.compare( currentCheckRecord.getId(), id ) == 0 ) {
                    searchResultCheckRecord = currentCheckRecord;
                    break;
                }
            }
            bufReader.close();
        } catch( FileNotFoundException e ) {
            return null;
        }
        
        return searchResultCheckRecord;
    }

    @Override
    public List<CheckRecord> findAll() throws Exception {
        String csvFilePath = Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
        ArrayList<CheckRecord> checkRecordList = new ArrayList<CheckRecord>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return checkRecordList;
        }
        
        String currentTuple = "";
        CheckRecord currentCheckRecord = new CheckRecord();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentCheckRecord = CheckRecordUtil.getCheckRecordFromCsvTupleString( currentTuple );
            checkRecordList.add( currentCheckRecord );
        }
        bufReader.close();
        
        return checkRecordList;
    }

    @Override
    public boolean update( CheckRecord checkRecord ) throws Exception {
        String csvFilePath = Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        CheckRecord currentCheckRecord = new CheckRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & modify data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentCheckRecord = CheckRecordUtil.getCheckRecordFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentCheckRecord.getId(), checkRecord.getId() ) == 0 ) {
                // modify data
                fileContentBuffer.add( CheckRecordUtil.getCsvTupleStringFromCheckRecord( checkRecord ) );
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
                    Contants.FILE_CHARSET
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
    public boolean delete( CheckRecord checkRecord ) throws Exception {
        String csvFilePath = Contants.CHECK_RECORD_DATA_PATH + Contants.CHECK_RECORD_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        CheckRecord currentCheckRecord = new CheckRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & delete data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentCheckRecord = CheckRecordUtil.getCheckRecordFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentCheckRecord.getId(), checkRecord.getId() ) != 0 ) {
                // keep data that not match the deleting data
                fileContentBuffer.add( currentTuple );
            }
        }
        bufReader.close();
        
        // write file content buffer to Item/yyyy.mm.dd.csv
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), false ),
                    Contants.FILE_CHARSET
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
        if( !checkIfFileExists( Contants.CHECK_RECORD_SEQ_FILE_PATH ) ) {
            return Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
        }
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.CHECK_RECORD_SEQ_FILE_PATH ) ),
                Contants.FILE_CHARSET
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
        File f = new File( Contants.CHECK_RECORD_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.CHECK_RECORD_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( Contants.CHECK_RECORD_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.INITIAL_SEQ_NUMBER );
        bufWriter.newLine();
        bufWriter.close();
    }
}
