package service;

import java.util.List;

import domain.TotalProperty;

public interface TotalPropertyService {
    
    public int insert( TotalProperty totalProperty ) throws Exception;
    
    public TotalProperty findOne( Integer id ) throws Exception;
    
    public List<TotalProperty> findAll() throws Exception;
    
    public int getMainTotalAmount() throws Exception;
    
    public int setMainTotalAmount( int totalAmount ) throws Exception;

    public int addToMainTotalAmount(int amount) throws Exception;
    
    public int update( TotalProperty totalProperty ) throws Exception;
    
    public int delete( TotalProperty totalProperty ) throws Exception;
}
