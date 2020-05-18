package android.com.mohan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {
    private EditText inputEmail;
    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        inputEmail = findViewById(R.id.emailReset);
        Button btnReset = findViewById(R.id.btn_reset_password);
        login = findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance();
        try {
            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = inputEmail.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplication(), "Enter your mail address", Toast.LENGTH_SHORT).show();
                        inputEmail.setError("Enter your Email");
                        return;
                    }

                    try {
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ResetPassActivity.this, "We send you an e-mail", Toast.LENGTH_SHORT).show();
                                            login.performClick();

                                        } else {
                                            Toast.makeText(ResetPassActivity.this, "Please enter correct mail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ResetPassActivity.this, LoginAndSignUpActivity.class));
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
