package repository;

import java.util.List;

import domain.CheckRecord;

public interface CheckRecordDAO {
    
    public boolean insert( CheckRecord checkRecord ) throws Exception;
    
    public CheckRecord findOne( int id ) throws Exception;
    
    public List<CheckRecord> findAll() throws Exception;
    
    public boolean update( CheckRecord checkRecord ) throws Exception;
    
    public boolean delete( CheckRecord checkRecord ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
}
