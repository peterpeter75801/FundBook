package service;

import java.util.List;

import domain.IncomeRecord;

public interface IncomeRecordService {
    
    public int insert( IncomeRecord incomeRecord ) throws Exception;
    
    public IncomeRecord findOne( Integer id, int indexYear, int indexMonth ) throws Exception;
    
    public List<IncomeRecord> findByMonth( int year, int month ) throws Exception;
    
    public int update( IncomeRecord incomeRecord ) throws Exception;
    
    public int delete( IncomeRecord incomeRecord ) throws Exception;
    
    public int moveUp( int year, int month, int orderNo ) throws Exception;
    
    public int moveDown( int year, int month, int orderNo ) throws Exception;
    
    public int sort( int year, int month ) throws Exception;
}
