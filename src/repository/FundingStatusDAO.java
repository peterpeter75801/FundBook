package repository;

import java.util.List;

import domain.FundingStatus;

public interface FundingStatusDAO {
    
    public boolean insert( FundingStatus fundingStatus ) throws Exception;
    
    public FundingStatus findOne( int id ) throws Exception;
    
    public List<FundingStatus> findAll() throws Exception;
    
    public boolean update( FundingStatus fundingStatus ) throws Exception;
    
    public boolean delete( FundingStatus fundingStatus ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
}
