package domain;

public class FundingStatusHistory {

    private Integer id;                 // ID
    private Integer fundingStatusId;    // 經費儲存狀況id
    private Integer year;               // 更動日期-年
    private Integer month;              // 更動日期-月
    private Integer day;                // 更動日期-日
    private Integer hour;               // 更動日期-時
    private Integer minute;             // 更動日期-分
    private Integer second;             // 更動日期-秒
    private Character action;           // 更動種類: C-新增, U-修改, D-刪除, M-搬動
    private Integer originAmount;       // 更動前金額
    private Integer modifiedAmount;     // 更動後金額
    private Integer difference;         // 差額
    private String description;         // 描述
    
    public FundingStatusHistory() {}
    
    public FundingStatusHistory( int id, int fundingStatusId, int year, int month, int day, int hour, int minute, 
            int second, char action, int originAmount, int modifiedAmount, int difference, String description ) {
        this.id = id;
        this.fundingStatusId = fundingStatusId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.action = action;
        this.originAmount = originAmount;
        this.modifiedAmount = modifiedAmount;
        this.difference = difference;
        this.description = description;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setFundingStatusId( Integer fundingStatusId ) {
        this.fundingStatusId = fundingStatusId;
    }

    public Integer getFundingStatusId() {
        return fundingStatusId;
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

    public void setHour( Integer hour ) {
        this.hour = hour;
    }

    public Integer getHour() {
        return hour;
    }

    public void setMinute( Integer minute ) {
        this.minute = minute;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setSecond( Integer second ) {
        this.second = second;
    }

    public Integer getSecond() {
        return second;
    }

    public void setAction( Character action ) {
        this.action = action;
    }

    public Character getAction() {
        return action;
    }

    public void setOriginAmount( Integer originAmount ) {
        this.originAmount = originAmount;
    }

    public Integer getOriginAmount() {
        return originAmount;
    }

    public void setModifiedAmount( Integer modifiedAmount ) {
        this.modifiedAmount = modifiedAmount;
    }

    public Integer getModifiedAmount() {
        return modifiedAmount;
    }

    public void setDifference( Integer difference ) {
        this.difference = difference;
    }

    public Integer getDifference() {
        return difference;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
