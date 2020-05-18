package android.com.mohan;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class BooksModel implements Serializable {
    public String bookname;
    private String bookabbr;
    private String bookauthor;
    private String bookedition;
    private String bookisbncode;
    private int bookcopies;

    BooksModel() {
    }

    BooksModel(String bookname, String bookauthor){
        this.bookname = bookname;
        this.bookauthor = bookauthor;
    }

    int getBookcopies() {
        return bookcopies;
    }
    void setBookcopies(Integer bookcopies) {
        this.bookcopies = bookcopies;
    }

    String getBookisbncode() {
        return bookisbncode;
    }

    void setBookisbncode(String bookisbncode) {
        this.bookisbncode = bookisbncode;
    }

    String getBookedition() {
        return bookedition;
    }

    void setBookedition(String bookedition) {
        this.bookedition = bookedition;
    }

    String getBookauthor() {
        return bookauthor;
    }

    void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    String getBookabbr() {
        return bookabbr;
    }

    void setBookabbr(String bookabbr) {
        this.bookabbr = bookabbr;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
