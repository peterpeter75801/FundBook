package domain;

public class FundingStatus {

    private Integer id;             // ID
    private Character type;         // 儲存型態: C-現金, D-活存, T-定存
    private Integer year;           // 定存到期日-年
    private Integer month;          // 定存到期日-月
    private Integer day;            // 定存到期日-日
    private String bankCode;        // 銀行代碼
    private String bankName;        // 銀行名稱
    private String account;         // 帳號
    private String interestAccount; // 利息轉入帳號
    private Integer balance;        // 結餘

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

    public void setBankCode( String bankCode ) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankName( String bankName ) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setAccount( String account ) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setInterestAccount( String interestAccount ) {
        this.interestAccount = interestAccount;
    }

    public String getInterestAccount() {
        return interestAccount;
    }

    public void setBalance( Integer balance ) {
        this.balance = balance;
    }

    public Integer getBalance() {
        return balance;
    }
}
