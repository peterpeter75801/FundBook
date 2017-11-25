package service.impl;

import java.util.List;

import domain.CheckRecord;
import repository.CheckRecordDAO;
import service.CheckRecordService;

public class CheckRecordServiceImpl implements CheckRecordService {
    
    private CheckRecordDAO checkRecordDAO;
    
    public CheckRecordServiceImpl( CheckRecordDAO checkRecordDAO ) {
        this.checkRecordDAO = checkRecordDAO;
    }
    
    @Override
    public int insert( CheckRecord checkRecord ) throws Exception {
        // TODO Auto-generated method stub
        
        return 0;
    }

    @Override
    public CheckRecord findOne( Integer id ) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CheckRecord> findAll() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update( CheckRecord checkRecord ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete( CheckRecord checkRecord ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }
}
