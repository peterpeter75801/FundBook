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
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import repository.FundingStatusDAO;

public class FundingStatusDAOImpl implements FundingStatusDAO {

    @Override
    public boolean insert( FundingStatus fundingStatus ) throws Exception {
        FundingStatus fundingStatusWithNewId = FundingStatusUtil.copy( fundingStatus );
        
        // 取得Funding Status目前的流水號(ID)
        if( !checkIfFileExists( Contants.FUNDING_STATUS_SEQ_FILE_PATH ) ) {
            createSeqFile( Contants.FUNDING_STATUS_SEQ_FILE_PATH );
        }
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.FUNDING_STATUS_SEQ_FILE_PATH ) ),
                Contants.FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
            fundingStatusWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // 新增Funding Status資料
        String csvFilePath = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( FundingStatusUtil.getCsvTupleStringFromFundingStatus( fundingStatusWithNewId ) );
        writer.newLine();
        writer.close();
        
        // 更新Funding Status的流水號(ID)
        writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( Contants.FUNDING_STATUS_SEQ_FILE_PATH ), false ),
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
    public FundingStatus findOne( int id ) throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return null;
        }
        
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        FundingStatus searchResultFundingStatus = null;
        try {
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( csvFilePath ) ),
                    Contants.FILE_CHARSET
                ));
            // read attribute titles
            bufReader.readLine();
            // search data
            while( (currentTuple = bufReader.readLine()) != null ) {
                currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
                if( ComparingUtil.compare( currentFundingStatus.getId(), id ) == 0 ) {
                    searchResultFundingStatus = currentFundingStatus;
                    break;
                }
            }
            bufReader.close();
        } catch( FileNotFoundException e ) {
            return null;
        }
        
        return searchResultFundingStatus;
    }

    @Override
    public List<FundingStatus> findAll() throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        ArrayList<FundingStatus> fundingStatusList = new ArrayList<FundingStatus>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return fundingStatusList;
        }
        
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            fundingStatusList.add( currentFundingStatus );
        }
        bufReader.close();
        
        return fundingStatusList;
    }

    @Override
    public boolean update( FundingStatus fundingStatus ) throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & modify data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentFundingStatus.getId(), fundingStatus.getId() ) == 0 ) {
                // modify data
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( fundingStatus ) );
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
    public boolean delete( FundingStatus fundingStatus ) throws Exception {
        String csvFilePath = Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & delete data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentFundingStatus.getId(), fundingStatus.getId() ) != 0 ) {
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
        if( !checkIfFileExists( Contants.FUNDING_STATUS_SEQ_FILE_PATH ) ) {
            return Integer.parseInt( Contants.INITIAL_SEQ_NUMBER ) - 1;
        }
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( Contants.FUNDING_STATUS_SEQ_FILE_PATH ) ),
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
        File f = new File( Contants.FUNDING_STATUS_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.FUNDING_STATUS_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( Contants.FUNDING_STATUS_DATA_PATH );
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
