package android.com.mohan;

public class BookReqRecModel {
    private String bookName;
    private String bookAuthor;
    private String bookEdition;
    private String bookReqRecDate;
    private String bookAbbr;
    private String recCode;
    private String userName;
    private String rollNo;

    BookReqRecModel(String bookName, String bookAuthor, String bookEdition, String bookReqRecDate, String recCode, String userName, String rollNo, String bookAbbr) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookEdition = bookEdition;
        this.bookReqRecDate = bookReqRecDate;
        this.recCode = recCode;
        this.userName = userName;
        this.rollNo = rollNo;
        this.bookAbbr = bookAbbr;
    }
    BookReqRecModel(){}

    String getBookAbbr() {
        return bookAbbr;
    }

    void setBookAbbr(String bookAbbr) {
        this.bookAbbr = bookAbbr;
    }

    String getBookName() {
        return bookName;
    }

    void setBookName(String bookName) {
        this.bookName = bookName;
    }

    String getBookAuthor() {
        return bookAuthor;
    }

    void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    String getBookEdition() {
        return bookEdition;
    }

    void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    String getBookReqRecDate() {
        return bookReqRecDate;
    }

    void setBookReqRecDate(String bookReqRecDate) {
        this.bookReqRecDate = bookReqRecDate;
    }

    public String getRecCode() {
        return recCode;
    }

    public void setRecCode(String recCode) {
        this.recCode = recCode;
    }

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getRollNo() {
        return rollNo;
    }

    void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

}
