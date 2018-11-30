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
import commonUtil.FundingStatusHistoryUtil;
import domain.FundingStatusHistory;
import repository.FundingStatusHistoryDAO;

public class FundingStatusHistoryDAOImpl implements FundingStatusHistoryDAO {
    
    @Override
    public boolean insert( FundingStatusHistory fundingStatusHistory ) throws Exception {
        FundingStatusHistory fundingStatusHistoryWithNewId = FundingStatusHistoryUtil.copy( fundingStatusHistory );
        
        // Get Funding Status History current sequence number (ID)
        if( !checkIfFileExists( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH ) ) {
            createSeqFile( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH );
        }
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH ) ),
                Contants.FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
            fundingStatusHistoryWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // Create new Funding Status History data
        String csvFilePath = Contants.FUNDING_STATUS_HISTORY_DATA_PATH + Contants.FUNDING_STATUS_HISTORY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( FundingStatusHistoryUtil.getCsvTupleStringFromFundingStatusHistory( fundingStatusHistoryWithNewId ) );
        writer.newLine();
        writer.close();
        
        // Update the current sequence number of Funding Status History data table
        writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH ), false ),
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
    public FundingStatusHistory findOne( int id ) throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_HISTORY_DATA_PATH + Contants.FUNDING_STATUS_HISTORY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return null;
        }
        
        String currentTuple = "";
        FundingStatusHistory currentFundingStatusHistory = new FundingStatusHistory();
        FundingStatusHistory searchResultFundingStatusHistory = null;
        try {
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( csvFilePath ) ),
                    Contants.FILE_CHARSET
                ));
            // read attribute titles
            bufReader.readLine();
            // search data
            while( (currentTuple = bufReader.readLine()) != null ) {
                currentFundingStatusHistory = FundingStatusHistoryUtil.getFundingStatusHistoryFromCsvTupleString( currentTuple );
                if( ComparingUtil.compare( currentFundingStatusHistory.getId(), id ) == 0 ) {
                    searchResultFundingStatusHistory = currentFundingStatusHistory;
                    break;
                }
            }
            bufReader.close();
        } catch ( FileNotFoundException e ) {
            return null;
        }
        
        return searchResultFundingStatusHistory;
    }

    @Override
    public List<FundingStatusHistory> findAll() throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_HISTORY_DATA_PATH + Contants.FUNDING_STATUS_HISTORY_FILENAME;
        ArrayList<FundingStatusHistory> fundingStatusHistoryList = new ArrayList<FundingStatusHistory>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return fundingStatusHistoryList;
        }
        
        String currentTuple = "";
        FundingStatusHistory currentFundingStatusHistory = new FundingStatusHistory();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatusHistory = FundingStatusHistoryUtil.getFundingStatusHistoryFromCsvTupleString( currentTuple );
            fundingStatusHistoryList.add( currentFundingStatusHistory );
        }
        bufReader.close();
        
        return fundingStatusHistoryList;
    }

    @Override
    public boolean delete( FundingStatusHistory fundingStatusHistory ) throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_HISTORY_DATA_PATH + Contants.FUNDING_STATUS_HISTORY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        FundingStatusHistory currentFundingStatusHistory = new FundingStatusHistory();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & delete data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatusHistory = FundingStatusHistoryUtil.getFundingStatusHistoryFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentFundingStatusHistory.getId(), fundingStatusHistory.getId() ) != 0 ) {
                // keep data that not match the deleting data
                fileContentBuffer.add( currentTuple );
            }
        }
        bufReader.close();
        
        // write file content buffer to FundingStatusHistory.csv
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
        if( !checkIfFileExists( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH ) ) {
            return Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
        }
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH ) ),
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
        File f = new File( Contants.FUNDING_STATUS_HISTORY_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.FUNDING_STATUS_HISTORY_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( Contants.FUNDING_STATUS_HISTORY_DATA_PATH );
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
