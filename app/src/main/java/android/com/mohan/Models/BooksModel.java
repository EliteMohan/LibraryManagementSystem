package android.com.mohan.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class BooksModel implements Serializable {
    private String bookname;
    private String bookabbr;
    private String bookauthor;
    private String bookedition;
    private String bookisbncode;
    private int bookcopies;
    private String imageurl;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public BooksModel() {
    }


    public BooksModel(String bookname, String bookauthor, String imageurl) {
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.imageurl = imageurl;
    }


    public int getBookcopies() {
        return bookcopies;
    }

    public void setBookcopies(Integer bookcopies) {
        this.bookcopies = bookcopies;
    }

    String getBookisbncode() {
        return bookisbncode;
    }

    public String getBookedition() {
        return bookedition;
    }

    public void setBookedition(String bookedition) {
        this.bookedition = bookedition;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookabbr() {
        return bookabbr;
    }

    public void setBookabbr(String bookabbr) {
        this.bookabbr = bookabbr;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
