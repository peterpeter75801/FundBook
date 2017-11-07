package domain;

public class CheckRecord {

    private Integer id;             // ID
    private Integer year;           // 年
    private Integer month;          // 月
    private Integer day;            // 日
    private Integer hour;           // 時
    private Integer minute;         // 分
    private Integer second;         // 秒
    private Integer difference;     // 對帳差額, 實際-帳面
    private Integer bookAmount;     // 帳面金額
    private Integer actualAmount;   // 實際金額

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

    public void setDifference( Integer difference ) {
        this.difference = difference;
    }

    public Integer getDifference() {
        return difference;
    }

    public void setBookAmount( Integer bookAmount ) {
        this.bookAmount = bookAmount;
    }

    public Integer getBookAmount() {
        return bookAmount;
    }

    public void setActualAmount( Integer actualAmount ) {
        this.actualAmount = actualAmount;
    }

    public Integer getActualAmount() {
        return actualAmount;
    }
}
