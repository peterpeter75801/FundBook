package service.impl;

import java.util.List;

import common.Contants;
import domain.IncomeRecord;
import repository.IncomeRecordDAO;
import service.IncomeRecordService;

public class IncomeRecordServiceImpl implements IncomeRecordService {
    
    private IncomeRecordDAO incomeRecordDAO;
    
    public IncomeRecordServiceImpl( IncomeRecordDAO incomeRecordDAO ) {
        this.incomeRecordDAO = incomeRecordDAO;
    }

    @Override
    public int insert( IncomeRecord incomeRecord ) throws Exception {
        boolean returnCode;
        
        if( incomeRecord.getYear() == null || incomeRecord.getMonth() == null || incomeRecord.getDay() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( incomeRecordDAO.getCurrentSeqNumber() >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        returnCode = incomeRecordDAO.insert( incomeRecord );
        if( !returnCode ) {
            return Contants.ERROR;
        }
                
        return Contants.SUCCESS;
    }

    @Override
    public IncomeRecord findOne( Integer id, int indexYear, int indexMonth ) throws Exception {
        return incomeRecordDAO.findOne( id, indexYear, indexMonth );
    }

    @Override
    public List<IncomeRecord> findByMonth( int year, int month ) throws Exception {
        return incomeRecordDAO.findByMonth( year, month );
    }

    @Override
    public int update( IncomeRecord incomeRecord ) throws Exception {
        boolean returnCode;
        
        if( incomeRecord.getId() == null || incomeRecord.getYear() == null || 
                incomeRecord.getMonth() == null || incomeRecord.getDay() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( incomeRecordDAO.findOne( incomeRecord.getId(), incomeRecord.getYear(), incomeRecord.getMonth() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = incomeRecordDAO.update( incomeRecord, incomeRecord.getYear(), incomeRecord.getMonth() );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

    @Override
    public int delete( IncomeRecord incomeRecord ) throws Exception {
        boolean returnCode;
        
        if( incomeRecord.getId() == null || incomeRecord.getYear() == null || incomeRecord.getMonth() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( incomeRecordDAO.findOne( incomeRecord.getId(), incomeRecord.getYear(), incomeRecord.getMonth() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = incomeRecordDAO.delete( incomeRecord, incomeRecord.getYear(), incomeRecord.getMonth() );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

}
