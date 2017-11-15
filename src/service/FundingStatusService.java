package service;

import java.util.List;

import domain.FundingStatus;

public interface FundingStatusService {
    
    public int insert( FundingStatus fundingStatus ) throws Exception;
    
    public FundingStatus findOne( Integer id ) throws Exception;
    
    public List<FundingStatus> findAll() throws Exception;
    
    public int update( FundingStatus fundingStatus ) throws Exception;
    
    public int delete( FundingStatus fundingStatus ) throws Exception;
}
