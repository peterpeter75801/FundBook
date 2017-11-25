package service;

import java.util.List;

import domain.CheckRecord;

public interface CheckRecordService {
    
    public int insert( CheckRecord checkRecord ) throws Exception;
    
    public CheckRecord findOne( Integer id ) throws Exception;
    
    public List<CheckRecord> findAll() throws Exception;
    
    public int update( CheckRecord checkRecord ) throws Exception;
    
    public int delete( CheckRecord checkRecord ) throws Exception;
}
