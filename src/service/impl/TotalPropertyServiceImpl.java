package service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import common.Contants;
import domain.TotalProperty;
import repository.TotalPropertyDAO;
import service.TotalPropertyService;

public class TotalPropertyServiceImpl implements TotalPropertyService {
    
    private TotalPropertyDAO totalPropertyDAO;
    
    public TotalPropertyServiceImpl( TotalPropertyDAO totalPropertyDAO ) {
        this.totalPropertyDAO = totalPropertyDAO;
    }
    
    @Override
    public int insert( TotalProperty totalProperty ) throws Exception {
        boolean returnCode;
        
        if( totalPropertyDAO.getCurrentSeqNumber() >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        returnCode = totalPropertyDAO.insert( totalProperty );
        if( !returnCode ) {
            return Contants.ERROR;
        }
                
        return Contants.SUCCESS;
    }

    @Override
    public TotalProperty findOne( Integer id ) throws Exception {
        return totalPropertyDAO.findOne( id );
    }

    @Override
    public List<TotalProperty> findAll() throws Exception {
        return totalPropertyDAO.findAll();
    }

    @Override
    public int getMainTotalAmount() throws Exception {
        final int MAIN_ID = 1;
        TotalProperty totalProperty = totalPropertyDAO.findOne( MAIN_ID );
        
        if( totalProperty == null || totalProperty.getTotalAmount() == null ) {
            return 0;
        } else {
            return totalProperty.getTotalAmount();
        }
    }

    @Override
    public int setMainTotalAmount( int totalAmount ) throws Exception {
        final int MAIN_ID = 1;
        boolean returnCode;
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        
        TotalProperty totalProperty = new TotalProperty();
        totalProperty.setId( MAIN_ID );
        totalProperty.setYear( calendar.get( Calendar.YEAR ) );
        totalProperty.setMonth( calendar.get( Calendar.MONTH ) + 1 );
        totalProperty.setDay( calendar.get( Calendar.DAY_OF_MONTH ) );
        totalProperty.setHour( calendar.get( Calendar.HOUR_OF_DAY ) );
        totalProperty.setMinute( calendar.get( Calendar.MINUTE ) );
        totalProperty.setSecond( calendar.get( Calendar.SECOND ) );
        totalProperty.setTotalAmount( totalAmount );
        
        if( totalPropertyDAO.findOne( MAIN_ID ) == null ) {
            returnCode = totalPropertyDAO.create( totalProperty );
        } else {
            returnCode = totalPropertyDAO.update( totalProperty );
        }
        
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

    @Override
    public int update( TotalProperty totalProperty ) throws Exception {
        boolean returnCode;
        
        if( totalProperty.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( totalPropertyDAO.findOne( totalProperty.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = totalPropertyDAO.update( totalProperty );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

    @Override
    public int delete( TotalProperty totalProperty ) throws Exception {
        boolean returnCode;
        
        if( totalProperty.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( totalPropertyDAO.findOne( totalProperty.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = totalPropertyDAO.delete( totalProperty );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }

}
