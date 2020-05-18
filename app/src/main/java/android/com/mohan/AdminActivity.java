package android.com.mohan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private TextView headerUsername,headerRollNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_admin);
        MaterialToolbar materialToolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(materialToolbar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();//FirebaseAuth Instance
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance


        NavigationView navigationView = findViewById(R.id.nav_view_admin);//capture navigation view
        View headerview = navigationView.getHeaderView(0);//getting header view position using index
        headerUsername = headerview.findViewById(R.id.userNameHeader);
        headerRollNo = headerview.findViewById(R.id.rollNoHeader);
        drawerLayout = findViewById(R.id.admin_drawer_layout); // capture drawer layout
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,materialToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new FragmentLibraryAdmin()).commit();
            navigationView.setCheckedItem(R.id.nav_library_books_admin);
        }

        String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();//getting current userID required to fetch user profile data

        DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
        try {
            docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    assert documentSnapshot != null;
                    try {
                        headerUsername.setText(documentSnapshot.getString("username"));
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        headerRollNo.setText(documentSnapshot.getString("rollno"));
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.d("Info", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_account_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new FragmentAccount()).commit();
                break;
            case R.id.nav_library_books_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new FragmentLibraryAdmin()).commit();
                break;
            case R.id.nav_users_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new FragmentUsersAdmin()).commit();
                break;
            case R.id.nav_settings_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new FragmentSettings()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
