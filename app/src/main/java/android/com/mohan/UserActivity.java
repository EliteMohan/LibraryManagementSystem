package android.com.mohan;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private TextView headerUsername,headerRollNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_user);
        MaterialToolbar materialToolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(materialToolbar);
        materialToolbar.setTitleTextAppearance(getApplicationContext(),R.style.Toolbar_TitleText);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();//FirebaseAuth Instance
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();//Firebase Firestore Instance


        NavigationView navigationView = findViewById(R.id.nav_view);//capture navigation view
        View headerview = navigationView.getHeaderView(0);//getting header view position using index

        headerUsername = headerview.findViewById(R.id.userNameHeader);
        headerRollNo = headerview.findViewById(R.id.rollNoHeader);

        drawerLayout = findViewById(R.id.user_drawer_layout); // capture drawer layout
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,materialToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLibraryUsers()).commit();
            navigationView.setCheckedItem(R.id.nav_library_books);
        }

        String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();//getting current userID required to fetch user profile data

        DocumentReference docRef = firebaseFirestore.collection("users").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                try {
                    headerUsername.setText(documentSnapshot.getString("username"));
                    headerRollNo.setText(Objects.requireNonNull(documentSnapshot.getString("rollno")).toUpperCase());
                }catch (NullPointerException n){
                    Log.d("Error", Objects.requireNonNull(n.getMessage()));
                }
                }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_account:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentAccount()).commit();
                break;
            case R.id.nav_library_books:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentLibraryUsers()).commit();
                break;
            case R.id.nav_my_books:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentMyBooksUsers()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentSettingsUsers()).commit();
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
