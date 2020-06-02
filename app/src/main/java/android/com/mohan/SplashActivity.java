package android.com.mohan;


import android.com.mohan.Modules.AuthenticationModule.AuthenticationActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SplashActivity extends AppCompatActivity{
    private String ADMINCODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser()!=null){
            if(mAuth.getCurrentUser().isEmailVerified()){
                String id = mAuth.getCurrentUser().getUid();
                mFirestore.collection("users").document(id)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Toast.makeText(getApplicationContext(),"Please Check your Internet Connection",Toast.LENGTH_SHORT).show();
                                    Log.w("Error", "Listen failed.", e);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    ADMINCODE = snapshot.getString("ADMINCODE");
                                } else {
                                    Log.d("Error", "Current data: null");
                                }
                            }
                        });
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, AuthenticationActivity.class);
                if(ADMINCODE!=null){
                    i.putExtra("ADMINCODE",ADMINCODE);
                }
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
