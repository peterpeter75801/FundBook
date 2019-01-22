package repository;

import java.util.List;

import domain.FundingStatus;

public interface FundingStatusDAO {
    
    public boolean createDefault( FundingStatus fundingStatus ) throws Exception;
    
    public boolean insert( FundingStatus fundingStatus ) throws Exception;
    
    public FundingStatus findOne( int id ) throws Exception;
    
    public List<FundingStatus> findAll() throws Exception;
    
    public boolean update( FundingStatus fundingStatus ) throws Exception;
    
    public boolean delete( FundingStatus fundingStatus ) throws Exception;
    
    public int getCurrentSeqNumber() throws Exception;
    
    public boolean refreshOrderNo() throws Exception;
    
    public int getCount() throws Exception;
    
    public boolean moveUp( int orderNo ) throws Exception;
    
    public boolean moveDown( int orderNo ) throws Exception;
}
