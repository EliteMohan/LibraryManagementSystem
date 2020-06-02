package android.com.mohan.Modules.AuthenticationModule;

import android.com.mohan.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FragmentSignup extends Fragment {
    private RadioGroup genderRadioGroup, collegeRadioGroup;
    private TextInputEditText password;
    private AppCompatEditText getEmail, getUser, getConfirmPass, getPhone, getRollNo;
    private ContentLoadingProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPref;
    private String user, pass, Email, confirmPass, Phone, rollno, gender, college;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_register, container, false);
        TextView linkToLogin = view.findViewById(R.id.linkToLoginID);
        Button register = view.findViewById(R.id.register);
        sharedPref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("pref", Context.MODE_PRIVATE);
        password = view.findViewById(R.id.passwordSignUpID);
        getEmail = view.findViewById(R.id.emailSignUpID);
        getConfirmPass = view.findViewById(R.id.confirmPassID);
        getPhone = view.findViewById(R.id.phoneID);
        getUser = view.findViewById(R.id.UserNameSignUpID);
        getRollNo = view.findViewById(R.id.rollNoID);
        //RadioGroup
        genderRadioGroup = view.findViewById(R.id.gender_radio_group);
        collegeRadioGroup = view.findViewById(R.id.clg_radio_group);
        //firebase instances
        firebaseAuth = FirebaseAuth.getInstance();//FirebaseAuth Instance

        progressBar = view.findViewById(R.id.progressBarSend);
        progressBar.setVisibility(View.GONE);

        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLogin fragmentLogin = new FragmentLogin();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_login, fragmentLogin);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = Objects.requireNonNull(password.getText()).toString();
                Email = Objects.requireNonNull(getEmail.getText()).toString();
                user = Objects.requireNonNull(getUser.getText()).toString();
                confirmPass = Objects.requireNonNull(getConfirmPass.getText()).toString();
                Phone = Objects.requireNonNull(getPhone.getText()).toString();
                rollno = Objects.requireNonNull(getRollNo.getText()).toString();

                int selectedGenderID = genderRadioGroup.getCheckedRadioButtonId();
                int selectedCollegeID = collegeRadioGroup.getCheckedRadioButtonId();


                if (selectedGenderID == R.id.female_radio_btn) {
                    gender = "Female";
                } else {
                    gender = "Male";
                }

                if (selectedCollegeID == R.id.sietk_radio_btn) {
                    college = "SIETK";
                } else {
                    college = "SISTK";
                }

                boolean isSignUpFieldsEmpty = TextUtils.isEmpty(Email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)
                        || TextUtils.isEmpty(user) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(rollno);
                if (isSignUpFieldsEmpty) {
                    Log.i("Info", "Empty Fields are not allowed");
                    if (TextUtils.isEmpty(pass)) password.setError("Password is required");
                    if (TextUtils.isEmpty(Email)) getEmail.setError("Email is required");
                    if (TextUtils.isEmpty(user)) getUser.setError("Username is required");
                    if (TextUtils.isEmpty(confirmPass))
                        getConfirmPass.setError("Confirm Password is required");
                    if (TextUtils.isEmpty(Phone)) getPhone.setError("Phone number is required");
                    if (TextUtils.isEmpty(rollno)) getRollNo.setError("Roll Number is required");
                    Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 8) {
                    Log.i("Info", "Password Length should be greater than 6");
                    password.setError("Password less than 8");
                    Toast.makeText(getActivity(), "Password Length should be greater than 6", Toast.LENGTH_SHORT).show();
                } else if (!(pass.equals(confirmPass))) {
                    getConfirmPass.setError("Password not matching");
                    Toast.makeText(getActivity(), "Password not matching", Toast.LENGTH_SHORT).show();
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    //method to signup/create/register new user
                    firebaseAuth.createUserWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Registration Successful, Please Check your Email", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("username", user);
                                                    editor.putString("email", Email);
                                                    editor.putString("phone", Phone);
                                                    editor.putString("rollno", rollno.toUpperCase());
                                                    editor.putString("gender", gender);
                                                    editor.putString("college", college);
                                                    editor.apply();
                                                    FragmentLogin fragmentLogin = new FragmentLogin();
                                                    assert getFragmentManager() != null;
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.fragment_container_login, fragmentLogin);
                                                    transaction.addToBackStack(null);
                                                    transaction.commit();
                                                }
                                            }
                                        });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Error: ".concat(Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage())), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}
