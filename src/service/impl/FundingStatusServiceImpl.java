package service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.Contants;
import domain.FundingStatus;
import domain.FundingStatusHistory;
import repository.FundingStatusDAO;
import service.FundingStatusHistoryService;
import service.FundingStatusService;

public class FundingStatusServiceImpl implements FundingStatusService {
    
    private FundingStatusDAO fundingStatusDAO;
    private FundingStatusHistoryService fundingStatusHistoryService;
    
    public FundingStatusServiceImpl( FundingStatusDAO fundingStatusDAO ) {
        this.fundingStatusDAO = fundingStatusDAO;
    }
    
    public void setFundingStatusHistoryService( FundingStatusHistoryService fundingStatusHistoryService ) {
        this.fundingStatusHistoryService = fundingStatusHistoryService;
    }

    @Override
    public int initialDefault() throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( findOne( Contants.FUNDING_STATUS_DEFAULT_ID ) != null ) {
            return Contants.SUCCESS;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        int currentHour = calendar.get( Calendar.HOUR_OF_DAY );
        int currentMinute = calendar.get( Calendar.MINUTE );
        int currentSecond = calendar.get( Calendar.SECOND );
        
        FundingStatus defaultFundingStatus = new FundingStatus( Contants.FUNDING_STATUS_DEFAULT_ID, '0', 
                currentYear, currentMonth, currentDay, Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 0 );
        returnCode = fundingStatusDAO.createDefault( defaultFundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        FundingStatusHistory fundingStatusHistory = new FundingStatusHistory( 0, Contants.FUNDING_STATUS_DEFAULT_ID, 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'C', 0, 0, 0, 
                Contants.FUNDING_STATUS_DEFAULT_DATA_LOG_DESCRIPTION );
        returnStatus = fundingStatusHistoryService.insert( fundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }
    
    @Override
    public int insert( FundingStatus fundingStatus ) throws Exception {
        boolean returnCode;
        
        if( fundingStatusDAO.getCurrentSeqNumber() >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        returnCode = fundingStatusDAO.insert( fundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
                
        return Contants.SUCCESS;
    }

    @Override
    public FundingStatus findOne( Integer id ) throws Exception {
        return fundingStatusDAO.findOne( id );
    }

    @Override
    public List<FundingStatus> findAll() throws Exception {
        return fundingStatusDAO.findAll();
    }

    @Override
    public int update( FundingStatus fundingStatus ) throws Exception {
        boolean returnCode;
        
        if( fundingStatus.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( fundingStatusDAO.findOne( fundingStatus.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = fundingStatusDAO.update( fundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int delete( FundingStatus fundingStatus ) throws Exception {
        boolean returnCode;
        
        if( fundingStatus.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( fundingStatusDAO.findOne( fundingStatus.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = fundingStatusDAO.delete( fundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int getCount() throws Exception {
        return fundingStatusDAO.getCount();
    }

    @Override
    public int moveUp( int orderNo ) throws Exception {
        int count = fundingStatusDAO.getCount();
        boolean returnCode;
        
        if( orderNo <= 1 || orderNo > count ) {
            return Contants.NO_DATA_MODIFIED;
        }
        
        returnCode = fundingStatusDAO.moveUp( orderNo );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int moveDown( int orderNo ) throws Exception {
        int count = fundingStatusDAO.getCount();
        boolean returnCode;
        
        if( orderNo >= count ) {
            return Contants.NO_DATA_MODIFIED;
        }
        
        returnCode = fundingStatusDAO.moveDown( orderNo );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }
}
