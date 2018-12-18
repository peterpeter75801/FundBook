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
import commonUtil.TotalPropertyUtil;
import domain.TotalProperty;
import repository.TotalPropertyDAO;

public class TotalPropertyDAOImpl implements TotalPropertyDAO {
    
    private SystemInfo systemInfo;
    
    public TotalPropertyDAOImpl() {
        systemInfo = new SystemInfo( "." );
    }
    
    public TotalPropertyDAOImpl( SystemInfo systemInfo ) {
        this.systemInfo = systemInfo;
    }
    
    @Override
    public boolean create( TotalProperty totalProperty ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        if( totalProperty.getId() == null || findOne( totalProperty.getId() ) != null ) {
            return false;
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( TotalPropertyUtil.getCsvTupleStringFromTotalProperty( totalProperty ) );
        writer.newLine();
        writer.close();
        
        return true;
    }

    @Override
    public boolean insert( TotalProperty totalProperty ) throws Exception {
        TotalProperty totalPropertyWithNewId = TotalPropertyUtil.copy( totalProperty );
        
        // 取得Total Property目前的流水號(ID)
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_SEQ_FILE_PATH;
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
            totalPropertyWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // 新增Total Property資料
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( TotalPropertyUtil.getCsvTupleStringFromTotalProperty( totalPropertyWithNewId ) );
        writer.newLine();
        writer.close();
        
        // 更新Total Property的流水號(ID)
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
    public TotalProperty findOne( int id ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return null;
        }
        
        String currentTuple = "";
        TotalProperty currentTotalProperty = new TotalProperty();
        TotalProperty searchResultTotalProperty = null;
        try {
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( csvFilePath ) ),
                    Contants.FILE_CHARSET
                ));
            // read attribute titles
            bufReader.readLine();
            // search data
            while( (currentTuple = bufReader.readLine()) != null ) {
                currentTotalProperty = TotalPropertyUtil.getTotalPropertyFromCsvTupleString( currentTuple );
                if( ComparingUtil.compare( currentTotalProperty.getId(), id ) == 0 ) {
                    searchResultTotalProperty = currentTotalProperty;
                    break;
                }
            }
            bufReader.close();
        } catch( FileNotFoundException e ) {
            return null;
        }
        
        return searchResultTotalProperty;
    }

    @Override
    public List<TotalProperty> findAll() throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
        ArrayList<TotalProperty> totalPropertyList = new ArrayList<TotalProperty>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return totalPropertyList;
        }
        
        String currentTuple = "";
        TotalProperty currentTotalProperty = new TotalProperty();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentTotalProperty = TotalPropertyUtil.getTotalPropertyFromCsvTupleString( currentTuple );
            totalPropertyList.add( currentTotalProperty );
        }
        bufReader.close();
        
        return totalPropertyList;
    }

    @Override
    public boolean update( TotalProperty totalProperty ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        TotalProperty currentTotalProperty = new TotalProperty();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & modify data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentTotalProperty = TotalPropertyUtil.getTotalPropertyFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentTotalProperty.getId(), totalProperty.getId() ) == 0 ) {
                // modify data
                fileContentBuffer.add( TotalPropertyUtil.getCsvTupleStringFromTotalProperty( totalProperty ) );
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
    public boolean delete( TotalProperty totalProperty ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH + Contants.TOTAL_PROPERTY_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        TotalProperty currentTotalProperty = new TotalProperty();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & delete data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentTotalProperty = TotalPropertyUtil.getTotalPropertyFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentTotalProperty.getId(), totalProperty.getId() ) != 0 ) {
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
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_SEQ_FILE_PATH;
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
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.TOTAL_PROPERTY_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.TOTAL_PROPERTY_DATA_PATH );
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
