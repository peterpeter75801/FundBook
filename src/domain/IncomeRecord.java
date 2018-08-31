package domain;

public class IncomeRecord {

    private Integer id;             // ID
    private Integer year;           // 年
    private Integer month;          // 月
    private Integer day;            // 日
    private String item;            // 項目
    private Integer classNo;        // 類別代號
    private Integer amount;         // 金額
    private String description;     // 收支描述
    private Integer orderNo;        // 排序編號
    
    public IncomeRecord() {}
    
    public IncomeRecord( int id, int year, int month, int day, String item, 
            int classNo, int amount, String description, int orderNo ) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.item = item;
        this.classNo = classNo;
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

    public void setItem( String item ) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setClassNo( Integer classNo ) {
        this.classNo = classNo;
    }

    public Integer getClassNo() {
        return classNo;
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
