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
import commonUtil.FundingStatusUtil;
import domain.FundingStatus;
import repository.FundingStatusDAO;

public class FundingStatusDAOImpl implements FundingStatusDAO {
    
    private SystemInfo systemInfo;
    
    public FundingStatusDAOImpl() {
        systemInfo = new SystemInfo( "." );
    }
    
    public FundingStatusDAOImpl( SystemInfo systemInfo ) {
        this.systemInfo = systemInfo;
    }

    @Override
    public boolean createDefault( FundingStatus fundingStatus ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();    // skip attribute titles
        // search data & modify data
        while( (currentTuple = bufReader.readLine()) != null ) {
            fileContentBuffer.add( currentTuple );
        }
        bufReader.close();
        
        if( fundingStatus.getId() != Contants.FUNDING_STATUS_DEFAULT_ID || findOne( fundingStatus.getId() ) != null ) {
            return false;
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), false ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( Contants.FUNDING_STATUS_CSV_FILE_ATTR_STRING );
        writer.newLine();
        writer.write( FundingStatusUtil.getCsvTupleStringFromFundingStatus( fundingStatus ) );
        writer.newLine();
        for( String content : fileContentBuffer ) {
            writer.write( content );
            writer.newLine();
        }
        writer.close();
        
        // 更新Funding Status的流水號(ID)
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_SEQ_FILE_PATH;
        if( !checkIfFileExists( seqFilePath ) ) {
            createSeqFile( seqFilePath );
        }
        bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( seqFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        Integer currentSeqNumber = null;
        try {
            currentSeqNumber = Integer.parseInt( bufReader.readLine() );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        if( currentSeqNumber == Contants.FUNDING_STATUS_DEFAULT_ID ) {
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
        }
        
        return true;
    }
    
    @Override
    public boolean insert( FundingStatus fundingStatus ) throws Exception {
        FundingStatus fundingStatusWithNewId = FundingStatusUtil.copy( fundingStatus );
        
        // 取得Funding Status目前的流水號(ID)
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_SEQ_FILE_PATH;
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
            fundingStatusWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // 新增Funding Status資料
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
        
        // 將新增的Funding Status流水號(ID)記錄並回傳
        fundingStatus.setId( currentSeqNumber );
        
        // 更新Funding Status的流水號(ID)
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
    public FundingStatus findOne( int id ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_SEQ_FILE_PATH;
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

    @Override
    public boolean refreshOrderNo() throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
        // read data & refresh order number
        int currentOrderNumber = 1;
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            currentFundingStatus.setOrderNo( currentOrderNumber );
            fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( currentFundingStatus ) );
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

    @Override
    public int getCount() throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
    public int getActiveCount() throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
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
            FundingStatus currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            if( currentTuple.length() > 0 && currentFundingStatus.getDisabledFlag() == false ) {
                count++;
            }
        }
        bufReader.close();
        
        return count;
    }

    @Override
    public boolean moveUp( int orderNo ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        FundingStatus swap = null;
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // read data & refresh order number
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            if( currentFundingStatus.getOrderNo() == (orderNo - 1) ) {
                swap = currentFundingStatus;
            } else if( swap != null && currentFundingStatus.getOrderNo() == orderNo ) {
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( currentFundingStatus ) );
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( swap ) );
            } else {
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( currentFundingStatus ) );
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
    public boolean moveDown( int orderNo ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        FundingStatus swap = null;
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // read data & refresh order number
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            if( currentFundingStatus.getOrderNo() == orderNo ) {
                swap = currentFundingStatus;
            } else if( swap != null && currentFundingStatus.getOrderNo() == (orderNo + 1) ) {
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( currentFundingStatus ) );
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( swap ) );
                swap = null;
            } else {
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( currentFundingStatus ) );
            }
        }
        if( swap != null ) {
            fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( swap ) );
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
    public boolean moveToBottom( int orderNo ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH + Contants.FUNDING_STATUS_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        FundingStatus currentFundingStatus = new FundingStatus();
        FundingStatus swap = null;
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // read data & refresh order number
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentFundingStatus = FundingStatusUtil.getFundingStatusFromCsvTupleString( currentTuple );
            if( currentFundingStatus.getOrderNo() == orderNo ) {
                swap = currentFundingStatus;
            } else {
                fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( currentFundingStatus ) );
            }
        }
        if( swap != null ) {
            fileContentBuffer.add( FundingStatusUtil.getCsvTupleStringFromFundingStatus( swap ) );
        }
        bufReader.close();
        
        // No data changed
        if( swap == null ) {
            return false;
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
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH );
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
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.FUNDING_STATUS_DATA_PATH );
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
