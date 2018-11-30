package repository;

import java.util.List;

import domain.DigitalWallet;

public interface DigitalWalletDAO {
    
    public boolean insert( DigitalWallet digitalWallet ) throws Exception;
    
    public DigitalWallet findOne( int id ) throws Exception;
    
    public List<DigitalWallet> findAll() throws Exception;
    
    public boolean update( DigitalWallet digitalWallet ) throws Exception;
    
    public boolean delete( DigitalWallet digitalWallet ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
}
