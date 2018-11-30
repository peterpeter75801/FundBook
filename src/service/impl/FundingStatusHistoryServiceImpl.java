package service.impl;

import java.util.List;

import common.Contants;
import domain.FundingStatusHistory;
import repository.FundingStatusHistoryDAO;
import service.FundingStatusHistoryService;

public class FundingStatusHistoryServiceImpl implements FundingStatusHistoryService {
    
    private FundingStatusHistoryDAO fundingStatusHistoryDAO;
    
    public FundingStatusHistoryServiceImpl( FundingStatusHistoryDAO fundingStatusHistoryDAO ) {
        this.fundingStatusHistoryDAO = fundingStatusHistoryDAO;
    }

    @Override
    public int insert( FundingStatusHistory fundingStatusHistory ) throws Exception {
        boolean returnCode;
        
        if( fundingStatusHistoryDAO.getCurrentSeqNumber() >= Integer.MAX_VALUE ) {
            return Contants.ERROR_EXCEED_UPPER_LIMIT;
        }
        
        returnCode = fundingStatusHistoryDAO.insert( fundingStatusHistory );
        if( !returnCode ) {
            return Contants.ERROR;
        }
        
        return Contants.SUCCESS;
    }

    @Override
    public FundingStatusHistory findOne( Integer id ) throws Exception {
        return fundingStatusHistoryDAO.findOne( id );
    }

    @Override
    public List<FundingStatusHistory> findAll() throws Exception {
        return fundingStatusHistoryDAO.findAll();
    }

    @Override
    public int delete( FundingStatusHistory fundingStatusHistory ) throws Exception {
        boolean returnCode;
        
        if( fundingStatusHistory.getId() == null ) {
            return Contants.ERROR_EMPTY_NECESSARY_PARAMETER;
        }
        
        if( fundingStatusHistoryDAO.findOne( fundingStatusHistory.getId() ) == null ) {
            return Contants.ERROR_NOT_EXIST;
        }
        
        returnCode = fundingStatusHistoryDAO.delete( fundingStatusHistory );
        if( !returnCode ) {
            return Contants.ERROR;
        } else {
            return Contants.SUCCESS;
        }
    }
}
