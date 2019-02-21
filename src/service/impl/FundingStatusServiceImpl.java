package service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.Contants;
import commonUtil.ComparingUtil;
import commonUtil.FundingStatusUtil;
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
                currentYear, currentMonth, currentDay, Contants.FUNDING_STATUS_DEFAULT_STORED_PLACE_OR_INSTITUTION, 0, "", 0, false );
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
        int returnStatus;
        
        int currentSeqNumber = fundingStatusDAO.getCurrentSeqNumber();
        if( currentSeqNumber >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        int currentHour = calendar.get( Calendar.HOUR_OF_DAY );
        int currentMinute = calendar.get( Calendar.MINUTE );
        int currentSecond = calendar.get( Calendar.SECOND );
        
        returnCode = fundingStatusDAO.insert( fundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        FundingStatusHistory fundingStatusHistory = new FundingStatusHistory( 0, currentSeqNumber, 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'C', 
                0, fundingStatus.getAmount(), fundingStatus.getAmount(), "" );
        returnStatus = fundingStatusHistoryService.insert( fundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
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
        int returnStatus;
        
        if( fundingStatus.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        FundingStatus originalFundingStatus = fundingStatusDAO.findOne( fundingStatus.getId() );
        if( originalFundingStatus == null ) {
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
        
        if( fundingStatus.getId() == Contants.FUNDING_STATUS_DEFAULT_ID ||
                ComparingUtil.compare( originalFundingStatus.getAmount(), fundingStatus.getAmount() ) == 0 ) {
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
        
        int difference = fundingStatus.getAmount() - originalFundingStatus.getAmount();
        FundingStatusHistory fundingStatusHistory = new FundingStatusHistory( 0, fundingStatus.getId(), 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'U', 
                originalFundingStatus.getAmount(), fundingStatus.getAmount(), difference, "" );
        returnStatus = fundingStatusHistoryService.insert( fundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }
    
    @Override
    public int updateAmount( Integer id, Integer amount ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( id == null || amount == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        FundingStatus originalFundingStatus = fundingStatusDAO.findOne( id );
        if( originalFundingStatus == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        FundingStatus modifiedFundingStatus = FundingStatusUtil.copy( originalFundingStatus );
        modifiedFundingStatus.setAmount( amount );
        returnCode = fundingStatusDAO.update( modifiedFundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        if( id == Contants.FUNDING_STATUS_DEFAULT_ID ||
                ComparingUtil.compare( originalFundingStatus.getAmount(), amount ) == 0 ) {
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
        
        int difference = amount - originalFundingStatus.getAmount();
        FundingStatusHistory fundingStatusHistory = new FundingStatusHistory( 0, id, 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'U', 
                originalFundingStatus.getAmount(), amount, difference, "" );
        returnStatus = fundingStatusHistoryService.insert( fundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int updateStoredPlaceOrInstitution( Integer id, String storedPlaceOrInstitution ) throws Exception {
        boolean returnCode;
        
        if( id == null || storedPlaceOrInstitution == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        FundingStatus originalFundingStatus = fundingStatusDAO.findOne( id );
        if( originalFundingStatus == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        FundingStatus modifiedFundingStatus = FundingStatusUtil.copy( originalFundingStatus );
        modifiedFundingStatus.setStoredPlaceOrInstitution( storedPlaceOrInstitution );
        returnCode = fundingStatusDAO.update( modifiedFundingStatus );
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
    public int moveAmount( Integer sourceId, Integer destinationId, Integer amount ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( sourceId == null || destinationId == null || amount == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        if( ComparingUtil.compare( sourceId, destinationId ) == 0 || amount.intValue() == 0 ) {
            return Contants.NO_DATA_MODIFIED;
        }
        
        FundingStatus sourceFundingStatus = fundingStatusDAO.findOne( sourceId );
        if( sourceFundingStatus == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        FundingStatus destinationFundingStatus = fundingStatusDAO.findOne( destinationId );
        if( destinationFundingStatus == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        sourceFundingStatus.setAmount( sourceFundingStatus.getAmount() - amount );
        destinationFundingStatus.setAmount( destinationFundingStatus.getAmount() + amount );
        returnCode = fundingStatusDAO.update( sourceFundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.update( destinationFundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        int currentHour = calendar.get( Calendar.HOUR_OF_DAY );
        int currentMinute = calendar.get( Calendar.MINUTE );
        int currentSecond = calendar.get( Calendar.SECOND );
        
        FundingStatusHistory sourceFundingStatusHistory = new FundingStatusHistory( 0, sourceId, 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'U', 
                sourceFundingStatus.getAmount() + amount, sourceFundingStatus.getAmount(), amount * (-1), 
                "轉出金額" + amount + "元至'" + destinationFundingStatus.getStoredPlaceOrInstitution() + "'" );
        returnStatus = fundingStatusHistoryService.insert( sourceFundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR;
        }
        
        FundingStatusHistory destinationFundingStatusHistory = new FundingStatusHistory( 0, destinationId, 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'U', 
                destinationFundingStatus.getAmount() - amount, destinationFundingStatus.getAmount(), amount, 
                "由'" + sourceFundingStatus.getStoredPlaceOrInstitution() + "'轉入金額" + amount + "元" );
        returnStatus = fundingStatusHistoryService.insert( destinationFundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int delete( FundingStatus fundingStatus ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( fundingStatus.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        FundingStatus originalFundingStatus = fundingStatusDAO.findOne( fundingStatus.getId() );
        if( originalFundingStatus == null ) {
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
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        int currentHour = calendar.get( Calendar.HOUR_OF_DAY );
        int currentMinute = calendar.get( Calendar.MINUTE );
        int currentSecond = calendar.get( Calendar.SECOND );
        
        FundingStatusHistory fundingStatusHistory = new FundingStatusHistory( 0, fundingStatus.getId(), 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'D', 
                originalFundingStatus.getAmount(), originalFundingStatus.getAmount(), 0, "永久刪除" );
        returnStatus = fundingStatusHistoryService.insert( fundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public int disable( Integer id ) throws Exception {
        boolean returnCode;
        int returnStatus;
        
        if( id == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        FundingStatus disabledFundingStatus = fundingStatusDAO.findOne( id );
        if( disabledFundingStatus == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        disabledFundingStatus.setDisabledFlag( true );
        returnCode = fundingStatusDAO.update( disabledFundingStatus );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.moveToBottom( disabledFundingStatus.getOrderNo() );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        returnCode = fundingStatusDAO.refreshOrderNo();
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        int currentYear = calendar.get( Calendar.YEAR );
        int currentMonth = calendar.get( Calendar.MONTH ) + 1;
        int currentDay = calendar.get( Calendar.DAY_OF_MONTH );
        int currentHour = calendar.get( Calendar.HOUR_OF_DAY );
        int currentMinute = calendar.get( Calendar.MINUTE );
        int currentSecond = calendar.get( Calendar.SECOND );
        
        FundingStatusHistory fundingStatusHistory = new FundingStatusHistory( 0, id, 
                currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond, 'D', 
                disabledFundingStatus.getAmount(), disabledFundingStatus.getAmount(), 0, "" );
        returnStatus = fundingStatusHistoryService.insert( fundingStatusHistory );
        if( returnStatus != Contants.SUCCESS ) {
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
        int activeCount = fundingStatusDAO.getActiveCount();
        boolean returnCode;
        
        if( orderNo <= 1 || orderNo > activeCount ) {
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
        int activeCount = fundingStatusDAO.getActiveCount();
        boolean returnCode;
        
        if( orderNo >= activeCount ) {
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
