package repository;

import java.util.List;

import domain.TotalProperty;

public interface TotalPropertyDAO {
    
    public boolean create( TotalProperty totalProperty ) throws Exception;
    
    public boolean insert( TotalProperty totalProperty ) throws Exception;
    
    public TotalProperty findOne( int id ) throws Exception;
    
    public List<TotalProperty> findAll() throws Exception;
    
    public boolean update( TotalProperty totalProperty ) throws Exception;
    
    public boolean delete( TotalProperty totalProperty ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
}
