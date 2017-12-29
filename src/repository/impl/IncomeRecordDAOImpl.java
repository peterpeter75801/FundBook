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
import commonUtil.ComparingUtil;
import commonUtil.IncomeRecordUtil;
import domain.IncomeRecord;
import repository.IncomeRecordDAO;

public class IncomeRecordDAOImpl implements IncomeRecordDAO {
        
    @Override
    public boolean insert( IncomeRecord incomeRecord ) throws Exception {
        IncomeRecord incomeRecordWithNewId = IncomeRecordUtil.copy( incomeRecord );
        
        // 取得Income Record目前的流水號(ID)
        if( !checkIfFileExists( Contants.INCOME_RECORD_SEQ_FILE_PATH ) ) {
            createSeqFile( Contants.INCOME_RECORD_SEQ_FILE_PATH );
        }
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.INCOME_RECORD_SEQ_FILE_PATH ) ),
                Contants.FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
            incomeRecordWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // 新增Income Record資料
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + IncomeRecordUtil.getIncomeRecordCsvFileName( incomeRecordWithNewId );
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( incomeRecordWithNewId ) );
        writer.newLine();
        writer.close();
        
        // 更新Income Record的流水號(ID)
        writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( Contants.INCOME_RECORD_SEQ_FILE_PATH ), false ),
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
    public IncomeRecord findOne( int id, int indexYear, int indexMonth ) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
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
                    Contants.FILE_CHARSET
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
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        ArrayList<IncomeRecord> incomeRecordList = new ArrayList<IncomeRecord>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return incomeRecordList;
        }
        
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
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
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( indexYear, indexMonth );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
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
    public boolean delete( IncomeRecord incomeRecord, int indexYear, int indexMonth ) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( indexYear, indexMonth );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
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
        if( !checkIfFileExists( Contants.INCOME_RECORD_SEQ_FILE_PATH ) ) {
            return Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
        }
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.INCOME_RECORD_SEQ_FILE_PATH ) ),
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

    @Override
    public boolean refreshOrderNo( int year, int month ) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // read data & refresh order number
        int currentOrderNumber = 1;
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            currentIncomeRecord.setOrderNo( currentOrderNumber );
            fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( currentIncomeRecord ) );
            currentOrderNumber++;
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
        File f = new File( Contants.INCOME_RECORD_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.INCOME_RECORD_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( Contants.INCOME_RECORD_DATA_PATH );
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

    @Override
    public int getCount( int year, int month ) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        int count = 0;
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return count;
        }
        
        String currentTuple = "";
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // compute data count
        while( (currentTuple = bufReader.readLine()) != null ) {
            if( currentTuple.length() > 0 ) {
                count++;
            }
        }
        bufReader.close();
        
        return count;
    }

    @Override
    public boolean moveUp( int year, int month, int orderNo ) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        IncomeRecord swap = null;
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // read data & refresh order number
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            if( currentIncomeRecord.getOrderNo() == (orderNo - 1) ) {
                swap = currentIncomeRecord;
            } else if( swap != null && currentIncomeRecord.getOrderNo() == orderNo ) {
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( currentIncomeRecord ) );
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( swap ) );
                swap = null;
            } else {
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( currentIncomeRecord ) );
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
    public boolean moveDown(int year, int month, int orderNo) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        IncomeRecord swap = null;
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // read data & refresh order number
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            if( currentIncomeRecord.getOrderNo() == orderNo ) {
                swap = currentIncomeRecord;
            } else if( swap != null && currentIncomeRecord.getOrderNo() == (orderNo + 1) ) {
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( currentIncomeRecord ) );
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( swap ) );
                swap = null;
            } else {
                fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( currentIncomeRecord ) );
            }
        }
        if( swap != null ) {
            fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( swap ) );
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
    public boolean sort( int year, int month ) throws Exception {
        String csvFilePath = Contants.INCOME_RECORD_DATA_PATH + 
                IncomeRecordUtil.getIncomeRecordCsvFileName( year, month );
        List<IncomeRecord> incomeRecordList = new ArrayList<IncomeRecord>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return true;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        IncomeRecord currentIncomeRecord = new IncomeRecord();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentIncomeRecord = IncomeRecordUtil.getIncomeRecordFromCsvTupleString( currentTuple );
            incomeRecordList.add( currentIncomeRecord );
        }
        bufReader.close();
        
        // sort data ordered by (1)day (2)id
        incomeRecordList = IncomeRecordUtil.sortById( incomeRecordList );
        incomeRecordList = IncomeRecordUtil.sortByDay( incomeRecordList );
        
        // translate sorted data list to file content buffer
        
        for( int i = 0; i < incomeRecordList.size(); i++ ) {
            fileContentBuffer.add( IncomeRecordUtil.getCsvTupleStringFromIncomeRecord( incomeRecordList.get( i ) ) );
        }
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
}
