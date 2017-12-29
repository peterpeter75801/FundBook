package repository;

import java.util.List;

import domain.IncomeRecord;

public interface IncomeRecordDAO {
    
    public boolean insert( IncomeRecord incomeRecord ) throws Exception;
    
    public IncomeRecord findOne( int id, int indexYear, int indexMonth ) throws Exception;
    
    public List<IncomeRecord> findByMonth( int year, int month ) throws Exception;
    
    public boolean update( IncomeRecord incomeRecord, int indexYear, int indexMonth ) throws Exception;
    
    public boolean delete( IncomeRecord incomeRecord, int indexYear, int indexMonth ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
    
    public boolean refreshOrderNo( int year, int month ) throws Exception;
    
    public int getCount( int year, int month ) throws Exception;
    
    public boolean moveUp( int year, int month, int orderNo ) throws Exception;
    
    public boolean moveDown( int year, int month, int orderNo ) throws Exception;
    
    public boolean sort( int year, int month ) throws Exception;
}
