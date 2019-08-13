package service;

import java.util.List;

import domain.FundingStatus;

public interface FundingStatusService {
    
    public int initialDefault() throws Exception;
    
    public int insert( FundingStatus fundingStatus ) throws Exception;
    
    public FundingStatus findOne( Integer id ) throws Exception;
    
    public List<FundingStatus> findAll() throws Exception;
    
    public int update( FundingStatus fundingStatus ) throws Exception;
    
    public int updateAmount( Integer id, Integer amount, String comment ) throws Exception;
    
    public int updateStoredPlaceOrInstitution( Integer id, String storedPlaceOrInstitution ) throws Exception;
    
    public int updateProperty( Integer id, Character type, String storedPlaceOrInstitution, String description ) throws Exception;
    
    public int moveAmount( Integer sourceId, Integer destinationId, Integer amount ) throws Exception;
    
    public int delete( FundingStatus fundingStatus ) throws Exception;
    
    public int disable( Integer id ) throws Exception;
    
    public int getCount() throws Exception;
    
    public int moveUp( int orderNo ) throws Exception;
    
    public int moveDown( int orderNo ) throws Exception;
}
