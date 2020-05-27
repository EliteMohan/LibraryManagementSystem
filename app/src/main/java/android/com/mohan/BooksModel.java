package android.com.mohan;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
class BooksModel implements Serializable {
    private String bookname;
    private String bookabbr;
    private String bookauthor;
    private String bookedition;
    private String bookisbncode;
    private int bookcopies;
    private String imageurl;

    String getImageurl() {
        return imageurl;
    }

    void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    BooksModel() {
    }


    BooksModel(String bookname, String bookauthor, String imageurl) {
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.imageurl = imageurl;
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

    String getBookname() {
        return bookname;
    }

    void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
