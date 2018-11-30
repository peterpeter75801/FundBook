package service;

import java.util.List;

import domain.FundingStatusHistory;

public interface FundingStatusHistoryService {
    
    public int insert( FundingStatusHistory fundingStatusHistory ) throws Exception;
    
    public FundingStatusHistory findOne( Integer id ) throws Exception;
    
    public List<FundingStatusHistory> findAll() throws Exception;
    
    public int delete( FundingStatusHistory fundingStatusHistory ) throws Exception;
}
