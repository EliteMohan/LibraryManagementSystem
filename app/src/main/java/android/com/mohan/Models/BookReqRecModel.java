package android.com.mohan.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class BookReqRecModel implements Serializable {
    private String bookname;
    private String bookauthor;
    private String bookedition;
    private String bookreqrecdate;
    private String bookabbr;
    private String reccode;
    private String username;
    private String rollno;
    private int copies;
    private String retdate;
    private String imageurl;

    public BookReqRecModel(String rollno, String bookname, String recCode, String imageurl) {
        this.rollno = rollno;
        this.bookname = bookname;
        this.reccode = recCode;
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    public BookReqRecModel(String retDate, String bookname, String imageurl) {
        this.retdate = retDate;
        this.bookname = bookname;
        this.imageurl = imageurl;
    }

    public String getRetdate() {
        return retdate;
    }

    void setRetdate(String retdate) {
        this.retdate = retdate;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public BookReqRecModel() {
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

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookedition() {
        return bookedition;
    }

    public void setBookedition(String bookedition) {
        this.bookedition = bookedition;
    }

    void setBookreqrecdate(String bookreqrecdate) {
        this.bookreqrecdate = bookreqrecdate;
    }

    public String getReccode() {
        return reccode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

}
