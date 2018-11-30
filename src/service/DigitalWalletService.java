package service;

import java.util.List;

import domain.DigitalWallet;

public interface DigitalWalletService {
    
    public int insert( DigitalWallet digitalWallet ) throws Exception;
    
    public DigitalWallet findOne( Integer id ) throws Exception;
    
    public List<DigitalWallet> findAll() throws Exception;
    
    public int update( DigitalWallet digitalWallet ) throws Exception;
    
    public int delete( DigitalWallet digitalWallet ) throws Exception;
}
