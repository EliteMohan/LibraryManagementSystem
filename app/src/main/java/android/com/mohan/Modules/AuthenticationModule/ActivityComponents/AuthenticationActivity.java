package android.com.mohan.Modules.AuthenticationModule.ActivityComponents;

import android.annotation.SuppressLint;
import android.com.mohan.Modules.AuthenticationModule.FragmentComponents.FragmentLogin;
import android.com.mohan.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class AuthenticationActivity extends AppCompatActivity {
    private String ADMINCODE;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent splashIntent = getIntent();
        try {
            ADMINCODE = splashIntent.getStringExtra("ADMINCODE");
        } catch (NullPointerException e) {
            Log.d("Error","No ID got from splash activity");
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        if (savedInstanceState == null) {
            FragmentLogin fragmentLogin = new FragmentLogin();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(ADMINCODE!=null){
                transaction.replace(R.id.fragment_container_login, fragmentLogin,ADMINCODE);
            }else {
                transaction.replace(R.id.fragment_container_login, fragmentLogin);
            }
            transaction.commit();
        }

    }

}
