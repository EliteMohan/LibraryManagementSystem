package android.com.mohan.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class UsersModel implements Serializable {
    private String username,email,phone,rollno,gender,college,ADMINCODE;

    public UsersModel(String username, String rollno) {
        this.username = username;
        this.rollno = rollno;
    }

    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRollno() {
        return rollno;
    }

    void setRollno(String rollno) {
        this.rollno = rollno;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    String getCollege() {
        return college;
    }

    void setCollege(String college) {
        this.college = college;
    }

    String getADMINCODE() {
        return ADMINCODE;
    }

    void setADMINCODE(String ADMINCODE) {
        this.ADMINCODE = ADMINCODE;
    }
}
