package android.com.mohan;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
class BookReqRecModel implements Serializable {
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

    String getImageurl() {
        return imageurl;
    }

    void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    BookReqRecModel(String rollno, String bookname, String reccode,String imageurl) {
        this.rollno = rollno.toUpperCase();
        this.bookname = bookname;
        this.reccode = reccode;
        this.imageurl = imageurl;
    }


    BookReqRecModel(String retDate, String bookname,String imageurl) {
        this.retdate = retDate;
        this.bookname = bookname;
        this.imageurl = imageurl;
    }

    String getRetdate() {
        return retdate;
    }

    void setRetdate(String retdate) {
        this.retdate = retdate;
    }

    int getCopies() {
        return copies;
    }

    void setCopies(Integer copies) {
        this.copies = copies;
    }

    BookReqRecModel(){}

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

    String getBookauthor() {
        return bookauthor;
    }

    void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    String getBookedition() {
        return bookedition;
    }

    void setBookedition(String bookedition) {
        this.bookedition = bookedition;
    }

    void setBookreqrecdate(String bookreqrecdate) {
        this.bookreqrecdate = bookreqrecdate;
    }

    String getReccode() {
        return reccode;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getRollno() {
        return rollno;
    }

    void setRollno(String rollno) {
        this.rollno = rollno;
    }

}
