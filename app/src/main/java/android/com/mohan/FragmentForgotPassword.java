package android.com.mohan;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FragmentForgotPassword extends Fragment {
    private AppCompatEditText inputEmail;
    private FirebaseAuth auth;
    private ContentLoadingProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_forgot_password, container, false);
        //textviews
        TextView linkToLogin = view.findViewById(R.id.linkToLoginID);
        //buttons
        Button send = view.findViewById(R.id.cirSendButton);
        //Edittext
        inputEmail = view.findViewById(R.id.emailLoginID);
        //progressbase
        progressBar = view.findViewById(R.id.progressBarSend);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();

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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(inputEmail.getText()).toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter your mail address", Toast.LENGTH_SHORT).show();
                    inputEmail.setError("Enter your Email");
                    return;
                }
                try {
                    hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.VISIBLE);

                try {
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "We send you an e-mail", Toast.LENGTH_SHORT).show();
                                        FragmentLogin fragmentLogin = new FragmentLogin();
                                        assert getFragmentManager() != null;
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_container_login, fragmentLogin);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Please enter correct mail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
