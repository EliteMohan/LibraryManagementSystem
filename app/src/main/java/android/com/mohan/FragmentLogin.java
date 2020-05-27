package android.com.mohan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentLogin extends Fragment {
    private AppCompatEditText email, password;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID, adminID;
    private Map<String,Object> User;
    private SharedPreferences sharedPref;
    private ContentLoadingProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_login, container, false);

        sharedPref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("pref",Context.MODE_PRIVATE);
        //textviews
        TextView linkToSignUp = view.findViewById(R.id.linkToSignupID);
        TextView forgot_pass = view.findViewById(R.id.ForgotPassButton);
        //input fields
        email = view.findViewById(R.id.emailLoginID);
        password = view.findViewById(R.id.passwordLoginID);
        //buttons
        Button login = view.findViewById(R.id.cirLoginButton);
        //firebase instances
        firebaseAuth = FirebaseAuth.getInstance();//FirebaseAuth Instance
        firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance
        //progress bar
        progressBar = view.findViewById(R.id.progressBarSend);
        progressBar.setVisibility(View.GONE);
        //check user logged in or not
        if (firebaseAuth.getCurrentUser() != null) {
            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                adminID = firebaseAuth.getCurrentUser().getUid();//getting adminID to log into admin profile
                if (adminID.equals("OAb5ZYnZVnPeL6KP3MsFLf9ch7j2")) {
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    startActivity(new Intent(getActivity(), UserActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        }

        linkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSignup fragmentSignup = new FragmentSignup();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_login, fragmentSignup);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentForgotPassword fragmentForgotPassword = new FragmentForgotPassword();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_login, fragmentForgotPassword);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = Objects.requireNonNull(email.getText()).toString();
                String Pass = Objects.requireNonNull(password.getText()).toString();
                boolean isLoginFieldsEmpty = TextUtils.isEmpty(Email) || TextUtils.isEmpty(Pass);
                if (isLoginFieldsEmpty) {
                    Log.i("Info", "Please enter your username and password");
                    if (TextUtils.isEmpty(Email)) email.setError("email is missing");
                    if (TextUtils.isEmpty(Pass)) password.setError("password is missing");
                    Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    //method to sign-in into user account
                    firebaseAuth.signInWithEmailAndPassword(Email, Pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();//getting current userID required to create user profile
                                            //Creating and Storing data as key/value pairs
                                            User = new HashMap<>();
                                            User.put("username", sharedPref.getString("username", "no name found"));
                                            User.put("email", sharedPref.getString("email", "no mail found"));
                                            User.put("phone", sharedPref.getString("phone", "no phone number found"));
                                            User.put("rollno", sharedPref.getString("rollno", "no rollno found"));
                                            User.put("gender", sharedPref.getString("gender", "not found"));
                                            User.put("college", sharedPref.getString("college", "not found"));
                                            //creating collection(database) users with document(tables) userID if doesnt exists
                                            final DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                                            try {
                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot doc = task.getResult();
                                                            assert doc != null;
                                                            if (doc.exists()) {
                                                                Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                                try {
                                                                    adminID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();//getting adminID to log into admin profile
                                                                } catch (NullPointerException n) {
                                                                    Log.d("Error:", Objects.requireNonNull(n.getMessage()));
                                                                }
//                                                                Log.d("Admin ID: ",adminID);
                                                                if (adminID.equals("OAb5ZYnZVnPeL6KP3MsFLf9ch7j2")) {
                                                                    startActivity(new Intent(getActivity(), AdminActivity.class));
                                                                    Objects.requireNonNull(getActivity()).finish();
                                                                } else {
                                                                    startActivity(new Intent(getActivity(), UserActivity.class));
                                                                    Objects.requireNonNull(getActivity()).finish();
                                                                }
                                                            } else {
                                                                //success and failure listeners
                                                                //adds user data
                                                                try {
                                                                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d("userInfo", "User data Successfully added to user ".concat(userID));
                                                                            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                                            adminID = firebaseAuth.getCurrentUser().getUid();//getting adminID to log into admin profile
                                                                            if (adminID.equals("aqXctFlLaKbQXzTVo08gchQ1E8I3")) {
                                                                                startActivity(new Intent(getActivity(), AdminActivity.class));
                                                                                Objects.requireNonNull(getActivity()).finish();
                                                                            } else {
                                                                                startActivity(new Intent(getActivity(), UserActivity.class));
                                                                                Objects.requireNonNull(getActivity()).finish();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.d("ErrorInfo", "onFailure ".concat(e.toString()));
                                                                        }
                                                                    });
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Please verify your Email", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "User doesn't Exist".concat(Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage())), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });
        return view;
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }
}
