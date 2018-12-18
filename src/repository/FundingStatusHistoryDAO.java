package repository;

import java.util.List;

import domain.FundingStatusHistory;

public interface FundingStatusHistoryDAO {
    
    public boolean insert( FundingStatusHistory fundingStatusHistory ) throws Exception;
    
    public FundingStatusHistory findOne( int id ) throws Exception;
    
    public FundingStatusHistory findOne( int id, int fundingStatusId ) throws Exception;
    
    public List<FundingStatusHistory> findAll() throws Exception;
    
    public List<FundingStatusHistory> findAll( int fundingStatusId ) throws Exception;
    
    public boolean delete( FundingStatusHistory fundingStatusHistory ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
}
