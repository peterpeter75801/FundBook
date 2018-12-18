package domain;

public class FundingStatus {

    private Integer id;                         // ID
    private Character type;                     // 種類: C-現金, D-活存, T-定存, R-信用卡
    private Integer year;                       // 建立日期-年
    private Integer month;                      // 建立日期-月
    private Integer day;                        // 建立日期-日
    private String storedPlaceOrInstitution;    // 儲存地點/儲存機構
    private Integer amount;                     // 金額
    private String description;                 // 描述
    private Integer orderNo;                    // 排序編號
    
    public FundingStatus() {}
    
    public FundingStatus( int id, char type, int year, int month, int day, 
            String storedPlaceOrInstitution, int amount, String description, int orderNo ) {
        this.id = id;
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.storedPlaceOrInstitution = storedPlaceOrInstitution;
        this.amount = amount;
        this.description = description;
        this.orderNo = orderNo;
    }
    
    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setType( Character type ) {
        this.type = type;
    }

    public Character getType() {
        return type;
    }

    public void setYear( Integer year ) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setMonth( Integer month ) {
        this.month = month;
    }

    public Integer getMonth() {
        return month;
    }

    public void setDay( Integer day ) {
        this.day = day;
    }

    public Integer getDay() {
        return day;
    }
    
    public void setStoredPlaceOrInstitution( String storedPlaceOrInstitution ) {
        this.storedPlaceOrInstitution = storedPlaceOrInstitution;
    }
    
    public String getStoredPlaceOrInstitution() {
        return storedPlaceOrInstitution;
    }
    
    public void setAmount( Integer amount ) {
        this.amount = amount;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setDescription( String description ) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setOrderNo( Integer orderNo ) {
        this.orderNo = orderNo;
    }
    
    public Integer getOrderNo() {
        return orderNo;
    }
}
