package com.example.TripitAndroid.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.Fragments.TimePickerDialogFragment;
import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    TextView userNameText;
    ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.main_navigation);

        NavigationUI.setupWithNavController(navigationView,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                getSupportActionBar().setTitle(destination.getLabel());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Getting user name to display in the menu bar
        userNameText = findViewById(R.id.userName);
        userImageView = findViewById(R.id.imageView);

        if(userNameText != null) {
            userNameText.setText("");
            FirebaseUser user = Model.instance.currentUser();
            if(user != null) {
                Model.instance.getUserInfo(user.getUid(), new Model.OnUserInfoUpdated() {
                    @Override
                    public void onUserInfoUpdated(UserInfo userInfo) {
                        if (userInfo != null) {
                            userNameText.setText("Hello " + userInfo.displayName);

                            if(userImageView != null) {
                                if(userInfo.profileImageUrl != null && userInfo.profileImageUrl.trim().length() != 0) {
                                    Picasso.get().setIndicatorsEnabled(true);

                                    Picasso.get().load(userInfo.profileImageUrl)
                                            .placeholder(R.drawable.default_profile)
                                            .into(userImageView);
                                }
                            }
                        }
                    }
                });
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            OnSignOutTapped();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnSignOutTapped() {
        Model.instance.signOut(new FirebaseModel.OnSignOutCompleteListener() {
            @Override
            public void onSignOutComplete() {
                gotoLoginView();
            }
        });
    }

    private void gotoLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
