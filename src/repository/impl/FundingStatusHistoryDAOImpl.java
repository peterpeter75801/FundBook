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
import common.SystemInfo;
import commonUtil.ComparingUtil;
import commonUtil.FundingStatusHistoryUtil;
import domain.FundingStatusHistory;
import repository.FundingStatusHistoryDAO;

public class FundingStatusHistoryDAOImpl implements FundingStatusHistoryDAO {
    
    private SystemInfo systemInfo;
    
    public FundingStatusHistoryDAOImpl() {
        systemInfo = new SystemInfo( "." );
    }
    
    public FundingStatusHistoryDAOImpl( SystemInfo systemInfo ) {
        this.systemInfo = systemInfo;
    }
    
    @Override
    public boolean insert( FundingStatusHistory fundingStatusHistory ) throws Exception {
        FundingStatusHistory fundingStatusHistoryWithNewId = FundingStatusHistoryUtil.copy( fundingStatusHistory );
        
        // Get Funding Status History current sequence number (ID)
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH;
        if( !checkIfFileExists( seqFilePath ) ) {
            createSeqFile( seqFilePath );
        }
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( seqFilePath ) ),
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
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH + 
                FundingStatusHistoryUtil.getFundingStatusHistoryCsvFileName( fundingStatusHistoryWithNewId );
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
                    new FileOutputStream( new File( seqFilePath ), false ),
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
        String csvFileNames[] = (new File( systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH )).list();
        int fundingStatusIds[] = new int[ csvFileNames.length ];
        
        FundingStatusHistory searchResultFundingStatusHistory = null;
        for( int i = 0; i < fundingStatusIds.length; i++ ) {
            try {
                fundingStatusIds[ i ] = Integer.parseInt( csvFileNames[ i ].replaceAll( ".csv", "" ) );
            } catch( NumberFormatException e ) {
                fundingStatusIds[ i ] = -1;
            }
            
            searchResultFundingStatusHistory = findOne( id, fundingStatusIds[ i ] );
            if( searchResultFundingStatusHistory != null ) {
                break;
            }
        }
        
        return searchResultFundingStatusHistory;
    }

    @Override
    public FundingStatusHistory findOne( int id, int fundingStatusId ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH + 
            FundingStatusHistoryUtil.getFundingStatusHistoryCsvFileName( fundingStatusId );
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
        String csvFileNames[] = (new File( systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH )).list();
        int fundingStatusIds[] = new int[ csvFileNames.length ];
        
        List<FundingStatusHistory> fundingStatusHistoryList = new ArrayList<FundingStatusHistory>();
        for( int i = 0; i < fundingStatusIds.length; i++ ) {
            try {
                fundingStatusIds[ i ] = Integer.parseInt( csvFileNames[ i ].replaceAll( ".csv", "" ) );
            } catch( NumberFormatException e ) {
                fundingStatusIds[ i ] = -1;
            }
            
            List<FundingStatusHistory> fundingStatusHistoryListOfOneFile = findAll( fundingStatusIds[ i ] );
            for( FundingStatusHistory fundingStatusHistory : fundingStatusHistoryListOfOneFile ) {
                fundingStatusHistoryList.add( fundingStatusHistory );
            }
        }
        fundingStatusHistoryList = FundingStatusHistoryUtil.sortById( fundingStatusHistoryList );
        
        return fundingStatusHistoryList;
    }
    


    @Override
    public List<FundingStatusHistory> findAll( int fundingStatusId ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH + 
            FundingStatusHistoryUtil.getFundingStatusHistoryCsvFileName( fundingStatusId );
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
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH + 
            FundingStatusHistoryUtil.getFundingStatusHistoryCsvFileName( fundingStatusHistory );
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
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_SEQ_FILE_PATH;
        if( !checkIfFileExists( seqFilePath ) ) {
            return Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
        }
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( seqFilePath ) ),
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
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH );
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
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_HISTORY_DATA_PATH );
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
