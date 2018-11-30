package domain;

public class DigitalWallet {

    private Integer id;         // ID
    private String name;        // 名稱
    private Integer year;       // 建立日期-年
    private Integer month;      // 建立日期-月
    private Integer day;        // 建立日期-日
    private String issuer;      // 發行機構
    private Integer amount;     // 金額
    private String description; // 描述
    
    public DigitalWallet() {}
    
    public DigitalWallet( int id, String name, int year, int month, int day, String issuer, int amount, String description ) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.issuer = issuer;
        this.amount = amount;
        this.description = description;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setIssuer( String issuer ) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
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
}
