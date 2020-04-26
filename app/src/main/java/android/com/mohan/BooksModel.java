package android.com.mohan;

public class BooksModel {
    private String BookName;
    private String BookAbbr;
    private String BookAuthor;
    private String BookEdition;
    private String BookISBNCode;
    private String BookReqCode;

    BooksModel() {
    }

    public BooksModel(String bookName, String bookAbbr, String bookAuthor, String bookEdition, String bookISBNCode, String bookReqCode) {
        this.BookName = bookName;
        this.BookAbbr = bookAbbr;
        this.BookAuthor = bookAuthor;
        this.BookEdition = bookEdition;
        this.BookISBNCode = bookISBNCode;
        this.BookReqCode = bookReqCode;
    }

    String getBookISBNCode() {
        return BookISBNCode;
    }

    void setBookISBNCode(String bookISBNCode) {
        this.BookISBNCode = bookISBNCode;
    }

    public String getBookReqCode() {
        return BookReqCode;
    }

    public void setBookReqCode(String bookReqCode) {
        this.BookReqCode = bookReqCode;
    }

    String getBookEdition() {
        return BookEdition;
    }

    void setBookEdition(String bookEdition) {
        this.BookEdition = bookEdition;
    }

    String getBookAuthor() {
        return BookAuthor;
    }

    void setBookAuthor(String bookAuthor) {
        this.BookAuthor = bookAuthor;
    }

    String getBookAbbr() {
        return BookAbbr;
    }

    void setBookAbbr(String bookAbbr) {
        this.BookAbbr = bookAbbr;
    }

    String getBookName() {
        return BookName;
    }

    void setBookName(String bookName) {
        this.BookName = bookName;
    }
}
