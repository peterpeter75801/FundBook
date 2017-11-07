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
}
