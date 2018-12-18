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
import commonUtil.DigitalWalletUtil;
import domain.DigitalWallet;
import repository.DigitalWalletDAO;

public class DigitalWalletDAOImpl implements DigitalWalletDAO {
    
    private SystemInfo systemInfo;
    
    public DigitalWalletDAOImpl() {
        systemInfo = new SystemInfo( "." );
    }
    
    public DigitalWalletDAOImpl( SystemInfo systemInfo ) {
        this.systemInfo = systemInfo;
    }
    
    @Override
    public boolean insert( DigitalWallet digitalWallet ) throws Exception {
        DigitalWallet digitalWalletWithNewId = DigitalWalletUtil.copy( digitalWallet );
        
        // Get Digital Wallet current sequence number (ID)
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_SEQ_FILE_PATH;
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
            digitalWalletWithNewId.setId( currentSeqNumber );
        } catch( NumberFormatException e ) {
            return false;
        } finally {
            bufReader.close();
        }
        
        // Create new Digital Wallet data
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            createCsvFile( csvFilePath );
        }
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream( new File( csvFilePath ), true ),
                    Contants.FILE_CHARSET
                )
            );
        writer.write( DigitalWalletUtil.getCsvTupleStringFromDigitalWallet( digitalWalletWithNewId ) );
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
    public DigitalWallet findOne( int id ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return null;
        }
        
        String currentTuple = "";
        DigitalWallet currentDigitalWallet = new DigitalWallet();
        DigitalWallet searchResultDigitalWallet = null;
        try {
            BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                    new FileInputStream( new File( csvFilePath ) ),
                    Contants.FILE_CHARSET
                ));
            // read attribute titles
            bufReader.readLine();
            // search data
            while( (currentTuple = bufReader.readLine()) != null ) {
                currentDigitalWallet = DigitalWalletUtil.getDigitalWalletFromCsvTupleString( currentTuple );
                if( ComparingUtil.compare( currentDigitalWallet.getId(), id ) == 0 ) {
                    searchResultDigitalWallet = currentDigitalWallet;
                    break;
                }
            }
            bufReader.close();
        } catch( FileNotFoundException e ) {
            return null;
        }
        
        return searchResultDigitalWallet;
    }

    @Override
    public List<DigitalWallet> findAll() throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
        ArrayList<DigitalWallet> digitalWalletList = new ArrayList<DigitalWallet>();
        
        if( !checkIfFileExists( csvFilePath ) ) {
            return digitalWalletList;
        }
        
        String currentTuple = "";
        DigitalWallet currentDigitalWallet = new DigitalWallet();
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        bufReader.readLine();
        // fetch data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentDigitalWallet = DigitalWalletUtil.getDigitalWalletFromCsvTupleString( currentTuple );
            digitalWalletList.add( currentDigitalWallet );
        }
        bufReader.close();
        
        return digitalWalletList;
    }

    @Override
    public boolean update( DigitalWallet digitalWallet ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        DigitalWallet currentDigitalWallet = new DigitalWallet();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & modify data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentDigitalWallet = DigitalWalletUtil.getDigitalWalletFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentDigitalWallet.getId(), digitalWallet.getId() ) == 0 ) {
                // modify data
                fileContentBuffer.add( DigitalWalletUtil.getCsvTupleStringFromDigitalWallet( digitalWallet ) );
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
    public boolean delete( DigitalWallet digitalWallet ) throws Exception {
        String csvFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH + Contants.DIGITAL_WALLET_FILENAME;
        if( !checkIfFileExists( csvFilePath ) ) {
            return false;
        }
        
        ArrayList<String> fileContentBuffer = new ArrayList<String>();
        String currentTuple = "";
        DigitalWallet currentDigitalWallet = new DigitalWallet();
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader(
                new FileInputStream( new File( csvFilePath ) ),
                Contants.FILE_CHARSET
            )
        );
        // read attribute titles
        fileContentBuffer.add( bufReader.readLine() );
        // search data & delete data
        while( (currentTuple = bufReader.readLine()) != null ) {
            currentDigitalWallet = DigitalWalletUtil.getDigitalWalletFromCsvTupleString( currentTuple );
            if( ComparingUtil.compare( currentDigitalWallet.getId(), digitalWallet.getId() ) != 0 ) {
                // keep data that not match the deleting data
                fileContentBuffer.add( currentTuple );
            }
        }
        bufReader.close();
        
        // write file content buffer to DigitalWalletHistory.csv
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
        String seqFilePath = systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_SEQ_FILE_PATH;
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
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH );
        f.mkdirs();
        
        BufferedWriter bufWriter = new BufferedWriter( 
                new OutputStreamWriter( 
                    new FileOutputStream( new File( fileName ), false ), 
                    Contants.FILE_CHARSET 
                ) 
            );
        bufWriter.write( Contants.DIGITAL_WALLET_CSV_FILE_ATTR_STRING );
        bufWriter.newLine();
        bufWriter.close();
    }
    
    private void createSeqFile( String fileName ) throws IOException {
        File f = new File( systemInfo.getRootDirectory() + "/" + Contants.DIGITAL_WALLET_DATA_PATH );
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
