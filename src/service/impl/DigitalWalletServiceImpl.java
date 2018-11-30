package service.impl;

import java.util.List;

import common.Contants;
import domain.DigitalWallet;
import repository.DigitalWalletDAO;
import service.DigitalWalletService;

public class DigitalWalletServiceImpl implements DigitalWalletService {
    
    private DigitalWalletDAO digitalWalletDAO;
    
    public DigitalWalletServiceImpl( DigitalWalletDAO digitalWalletDAO ) {
        this.digitalWalletDAO = digitalWalletDAO;
    }

    @Override
    public int insert( DigitalWallet digitalWallet ) throws Exception {
        boolean returnCode;
        
        if( digitalWalletDAO.getCurrentSeqNumber() >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        returnCode = digitalWalletDAO.insert( digitalWallet );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public DigitalWallet findOne( Integer id ) throws Exception {
        return digitalWalletDAO.findOne( id );
    }

    @Override
    public List<DigitalWallet> findAll() throws Exception {
        return digitalWalletDAO.findAll();
    }

    @Override
    public int update( DigitalWallet digitalWallet ) throws Exception {
        boolean returnCode;
        
        if( digitalWallet.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( digitalWalletDAO.findOne( digitalWallet.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = digitalWalletDAO.update( digitalWallet );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

    @Override
    public int delete( DigitalWallet digitalWallet ) throws Exception {
        boolean returnCode;
        
        if( digitalWallet.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( digitalWalletDAO.findOne( digitalWallet.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = digitalWalletDAO.delete( digitalWallet );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }
}
