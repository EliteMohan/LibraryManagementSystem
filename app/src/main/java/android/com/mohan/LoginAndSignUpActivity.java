package android.com.mohan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginAndSignUpActivity extends AppCompatActivity {
    private EditText getPassword, getEmail,getUser,getConfirmPass,getPhone,getRollNo;
    private EditText email, password;
    private Button login, forgot_pass, linkToSignUp, signUp, linkToLogin;
    private TextView title,passTitle,emailTitle,title1;
    private ImageView clgLoginLogo,clgSignUpLogo;
    private RadioGroup genderRadioGroup,collegeRadioGroup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginandsignup);
        //Text Fields
        password = findViewById(R.id.passwordLoginID);
        getPassword = findViewById(R.id.passwordSignUpID);
        getEmail = findViewById(R.id.emailSignUpID);
        email = findViewById(R.id.emailLoginID);
        getConfirmPass = findViewById(R.id.confirmPassID);
        getPhone = findViewById(R.id.phoneID);
        getUser = findViewById(R.id.UserNameSignUpID);
        getRollNo = findViewById(R.id.rollNoID);

        //Text Views
        emailTitle = findViewById(R.id.emailTitle);
        passTitle = findViewById(R.id.passwordTitle);
        title1 = findViewById(R.id.title_1);
        title = findViewById(R.id.title);

        //ImageView
        clgLoginLogo = findViewById(R.id.loginImageView);
        clgSignUpLogo = findViewById(R.id.signUpImageView);

        //RadioGroup
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        collegeRadioGroup = findViewById(R.id.clg_radio_group);

        //Buttons
        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.SignUpID);
        forgot_pass = findViewById(R.id.ForgotPassButton);
        linkToLogin = findViewById(R.id.linkToLoginID);
        linkToSignUp = findViewById(R.id.linkToSignupID);

        firebaseAuth = FirebaseAuth.getInstance();//FirebaseAuth Instance
        firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance

        //ProgressBar
        progressBar = findViewById(R.id.progressBar);

        //Making Sign-up fields invisible
        getPassword.setVisibility(View.GONE);
        getEmail.setVisibility(View.GONE);
        signUp.setVisibility(View.GONE);
        linkToLogin.setVisibility(View.GONE);
        getUser.setVisibility(View.GONE);
        getPhone.setVisibility(View.GONE);
        getRollNo.setVisibility(View.GONE);
        getConfirmPass.setVisibility(View.GONE);
        clgSignUpLogo.setVisibility(View.GONE);
        title1.setVisibility(View.GONE);
        genderRadioGroup.setVisibility(View.GONE);
        collegeRadioGroup.setVisibility(View.GONE);

        //hide progress bar
        progressBar.setVisibility(View.GONE);

        //check user logged in or not
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),UserActivity.class));
            finish();
        }

        //Login Listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Pass = password.getText().toString();
                boolean isLoginFieldsEmpty = TextUtils.isEmpty(Email) || TextUtils.isEmpty(Pass);
                if (isLoginFieldsEmpty) {
                    Log.i("Info", "Please enter your username and password");
                    if(TextUtils.isEmpty(Email))email.setError("email is missing");
                    if(TextUtils.isEmpty(Pass))password.setError("password is missing");
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    //method to sign-in into user account
                    firebaseAuth.signInWithEmailAndPassword(Email,Pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginAndSignUpActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginAndSignUpActivity.this,UserActivity.class));
                                        finish();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginAndSignUpActivity.this,"User doesn't Exist".concat(Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage())),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    Log.i("Info", "Hello ".concat(Email));
                    Log.i("Info", "Your Password is ".concat(Pass));
                }
            }
        });
        //LinkToSignUp Listener
        linkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hiding login UI when going to sign-up UI
                title.setVisibility(View.GONE);
                emailTitle.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                passTitle.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                linkToSignUp.setVisibility(View.GONE);
                forgot_pass.setVisibility(View.GONE);
                clgLoginLogo.setVisibility(View.GONE);
                //un-Hiding sign-up UI
                genderRadioGroup.setVisibility(View.VISIBLE);
                collegeRadioGroup.setVisibility(View.VISIBLE);
                getPassword.setVisibility(View.VISIBLE);
                getEmail.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
                linkToLogin.setVisibility(View.VISIBLE);
                getRollNo.setVisibility(View.VISIBLE);
                getUser.setVisibility(View.VISIBLE);
                getPhone.setVisibility(View.VISIBLE);
                getConfirmPass.setVisibility(View.VISIBLE);
                title1.setVisibility(View.VISIBLE);
                clgSignUpLogo.setVisibility(View.VISIBLE);
                clgSignUpLogo.setAlpha(0.5f);
            }
        });
        //LinkToLogin Listener
        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding SignUp UI when going to login UI
                genderRadioGroup.setVisibility(View.GONE);
                collegeRadioGroup.setVisibility(View.GONE);
                getPassword.setVisibility(View.GONE);
                getEmail.setVisibility(View.GONE);
                signUp.setVisibility(View.GONE);
                linkToLogin.setVisibility(View.GONE);
                getRollNo.setVisibility(View.GONE);
                getUser.setVisibility(View.GONE);
                getPhone.setVisibility(View.GONE);
                getConfirmPass.setVisibility(View.GONE);
                title1.setVisibility(View.GONE);
                clgSignUpLogo.setVisibility(View.GONE);
                //UnHiding Login UI
                title.setVisibility(View.VISIBLE);
                emailTitle.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                passTitle.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                linkToSignUp.setVisibility(View.VISIBLE);
                forgot_pass.setVisibility(View.VISIBLE);
                clgLoginLogo.setVisibility(View.VISIBLE);
            }
        });
        //SignUp Listener
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user,pass,Email,confirmPass,phone,rollno,gender,college;
                pass = getPassword.getText().toString();
                Email = getEmail.getText().toString();
                user = getUser.getText().toString();
                confirmPass = getConfirmPass.getText().toString();
                phone = getPhone.getText().toString();
                rollno = getRollNo.getText().toString();

                int selectedGenderID = genderRadioGroup.getCheckedRadioButtonId();
                int selectedCollegeID = collegeRadioGroup.getCheckedRadioButtonId();


                if(selectedGenderID == R.id.female_radio_btn){
                    gender = "Female";
                }else {
                    gender = "Male";
                }

                if(selectedCollegeID == R.id.sietk_radio_btn){
                    college = "SIETK";
                }else{
                    college = "SISTK";
                }

                boolean isSignUpFieldsEmpty = TextUtils.isEmpty(Email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)
                        || TextUtils.isEmpty(user) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(rollno);
                if (isSignUpFieldsEmpty) {
                    Log.i("Info", "Empty Fields are not allowed");
                    if(TextUtils.isEmpty(pass))getPassword.setError("Password is required");
                    if(TextUtils.isEmpty(Email))getEmail.setError("Email is required");
                    if(TextUtils.isEmpty(user))getUser.setError("Username is required");
                    if(TextUtils.isEmpty(confirmPass))getConfirmPass.setError("Confirm Password is required");
                    if(TextUtils.isEmpty(phone))getPhone.setError("Phone number is required");
                    if(TextUtils.isEmpty(rollno))getRollNo.setError("Roll Number is required");
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }else if(pass.length()<8) {
                    Log.i("Info","Password Length should be greater than 6");
                    getPassword.setError("Password less than 8");
                    Toast.makeText(getApplicationContext(),"Password Length should be greater than 6",Toast.LENGTH_SHORT).show();
                }else if(!(pass.equals(confirmPass))){
                    getConfirmPass.setError("Password not matching");
                    Toast.makeText(LoginAndSignUpActivity.this,"Password not matching",Toast.LENGTH_SHORT).show();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    //method to signup/create/register new user
                    firebaseAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                userID = firebaseAuth.getCurrentUser().getUid();//getting current userID required to create user profile
                                //creating collection(database) users with document(tables) userID
                                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                                //Creating and Storing data as key/value pairs
                                Map<String,Object> User = new HashMap<>();
                                User.put("username",user);
                                User.put("email",Email);
                                User.put("phone",phone);
                                User.put("rollno",rollno);
                                User.put("gender",gender);
                                User.put("college",college);
                                //success and failure listeners
                                documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("userInfo","User data Successfully added to user ".concat(userID));
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "SignUp Successful", Toast.LENGTH_SHORT).show();
                                        linkToLogin.performClick();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("ErrorInfo","onFailure ".concat(e.toString()));
                                    }
                                });
                                //startActivity(new Intent(LoginAndSignUpActivity.this,UserActivity.class));
                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Error: ".concat(Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage())),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        //Forgot Password Listener
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Info", "Hello ".concat(email.getText().toString()));
                Log.i("Info", "Forgot your password?");
                startActivity(new Intent(LoginAndSignUpActivity.this,ResetPassActivity.class));
                Toast.makeText(getApplicationContext(), "Forgot Your Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
