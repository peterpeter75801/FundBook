package service.impl;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TotalProperty> findAll() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMainTotalAmount() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int setMainTotalAmount() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update( TotalProperty totalProperty ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete( TotalProperty totalProperty ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

}
