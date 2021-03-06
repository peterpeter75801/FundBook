package service.impl;

import java.util.List;

import common.Contants;
import domain.IncomeRecord;
import repository.IncomeRecordDAO;
import service.IncomeRecordService;
import service.TotalPropertyService;

public class IncomeRecordServiceImpl implements IncomeRecordService {
    
    private IncomeRecordDAO incomeRecordDAO;
    private TotalPropertyService totalPropertyService;
    
    public IncomeRecordServiceImpl( IncomeRecordDAO incomeRecordDAO ) {
        this.incomeRecordDAO = incomeRecordDAO;
    }
    
    public void setTotalPropertyService( TotalPropertyService totalPropertyService ) {
        this.totalPropertyService = totalPropertyService;
    }

    @Override
    public int insert( IncomeRecord incomeRecord ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
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
        returnCode = incomeRecordDAO.refreshOrderNo( incomeRecord.getYear(), incomeRecord.getMonth() );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnStatus = totalPropertyService.addToMainTotalAmount( incomeRecord.getAmount() );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR_TOTAL_PROPERTY_INCONSISTENT;
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
    public int update( IncomeRecord incomeRecord, int originalYear, int originalMonth ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( incomeRecord.getId() == null || incomeRecord.getYear() == null || 
                incomeRecord.getMonth() == null || incomeRecord.getDay() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        IncomeRecord originalIncomeRecord = incomeRecordDAO.findOne( incomeRecord.getId(), originalYear, originalMonth );
        if( originalIncomeRecord == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        int originalAmount = originalIncomeRecord.getAmount();
        int difference = incomeRecord.getAmount() - originalAmount;
        if( incomeRecord.getYear() == originalYear && incomeRecord.getMonth() == originalMonth ) {
            returnCode = incomeRecordDAO.update( incomeRecord, incomeRecord.getYear(), incomeRecord.getMonth() );
            if( !returnCode ) {
                return Contants.ERROR;
            }
            returnCode = incomeRecordDAO.refreshOrderNo( incomeRecord.getYear(), incomeRecord.getMonth() );
            if( !returnCode ) {
                return Contants.ERROR;
            }
            returnStatus = totalPropertyService.addToMainTotalAmount( difference );
            if( returnStatus != Contants.SUCCESS ) {
                return Contants.ERROR_TOTAL_PROPERTY_INCONSISTENT;
            }
        } else {
            returnCode = incomeRecordDAO.updateDifferentMonth( incomeRecord, originalYear, originalMonth, 
                    incomeRecord.getYear(), incomeRecord.getMonth() );
            if( !returnCode ) {
                return Contants.ERROR;
            }
            returnCode = incomeRecordDAO.refreshOrderNo( incomeRecord.getYear(), incomeRecord.getMonth() );
            if( !returnCode ) {
                return Contants.ERROR;
            }
            returnCode = incomeRecordDAO.refreshOrderNo( originalYear, originalMonth );
            if( !returnCode ) {
                return Contants.ERROR;
            }
            returnStatus = totalPropertyService.addToMainTotalAmount( difference );
            if( returnStatus != Contants.SUCCESS ) {
                return Contants.ERROR_TOTAL_PROPERTY_INCONSISTENT;
            }
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int delete( IncomeRecord incomeRecord ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( incomeRecord.getId() == null || incomeRecord.getYear() == null || incomeRecord.getMonth() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        IncomeRecord originalIncomeRecord = incomeRecordDAO.findOne( incomeRecord.getId(), incomeRecord.getYear(), incomeRecord.getMonth() );
        if( originalIncomeRecord == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = incomeRecordDAO.delete( incomeRecord, incomeRecord.getYear(), incomeRecord.getMonth() );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = incomeRecordDAO.refreshOrderNo( incomeRecord.getYear(), incomeRecord.getMonth() );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnStatus = totalPropertyService.addToMainTotalAmount( originalIncomeRecord.getAmount() * (-1) );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR_TOTAL_PROPERTY_INCONSISTENT;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int moveUp( int year, int month, int orderNo ) throws Exception {
        int count = incomeRecordDAO.getCount( year, month );
        boolean returnCode;
        
        if( orderNo <= 1 || orderNo > count ) {
            return Contants.NO_DATA_MODIFIED;
        }
        
        returnCode = incomeRecordDAO.moveUp( year, month, orderNo );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = incomeRecordDAO.refreshOrderNo( year, month );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int moveDown( int year, int month, int orderNo ) throws Exception {
        int count = incomeRecordDAO.getCount( year, month );
        boolean returnCode;
        
        if( orderNo >= count ) {
            return Contants.NO_DATA_MODIFIED;
        }
        
        returnCode = incomeRecordDAO.moveDown( year, month, orderNo );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = incomeRecordDAO.refreshOrderNo( year, month );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int sort( int year, int month ) throws Exception {
        boolean returnCode;
        
        returnCode = incomeRecordDAO.sort( year, month );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = incomeRecordDAO.refreshOrderNo( year, month );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

}
