package service.impl;

import java.util.List;

import common.Contants;
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
        boolean returnCode;
        
        if( checkRecordDAO.getCurrentSeqNumber() >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        returnCode = checkRecordDAO.insert( checkRecord );
        if( !returnCode ) {
            return Contants.ERROR;
        }
                
        return Contants.SUCCESS;
    }

    @Override
    public CheckRecord findOne( Integer id ) throws Exception {
        return checkRecordDAO.findOne( id );
    }

    @Override
    public List<CheckRecord> findAll() throws Exception {
        return checkRecordDAO.findAll();
    }

    @Override
    public int update( CheckRecord checkRecord ) throws Exception {
        boolean returnCode;
        
        if( checkRecord.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( checkRecordDAO.findOne( checkRecord.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = checkRecordDAO.update( checkRecord );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

    @Override
    public int delete( CheckRecord checkRecord ) throws Exception {
        boolean returnCode;
        
        if( checkRecord.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( checkRecordDAO.findOne( checkRecord.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = checkRecordDAO.delete( checkRecord );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }
}
