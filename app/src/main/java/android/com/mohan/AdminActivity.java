package android.com.mohan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        MaterialToolbar materialToolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(materialToolbar);

        drawerLayout = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,materialToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new FragmentLibraryAdmin()).commit();
            navigationView.setCheckedItem(R.id.nav_library_books_admin);
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
