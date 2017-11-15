package service.impl;

import java.util.List;

import common.Contants;
import domain.FundingStatus;
import repository.FundingStatusDAO;
import service.FundingStatusService;

public class FundingStatusServiceImpl implements FundingStatusService {
    
    private FundingStatusDAO fundingStatusDAO;
    
    public FundingStatusServiceImpl( FundingStatusDAO fundingStatusDAO ) {
        this.fundingStatusDAO = fundingStatusDAO;
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
        } else {
            return Contants.SUCCESS;
        }
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
        } else {
            return Contants.SUCCESS;
        }
    }
}
