package com.example.tripit_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripit_android.PostsListAdapter;
import com.example.tripit_android.R;
import com.example.tripit_android.models.FirebaseModel;
import com.example.tripit_android.models.Model;
import com.google.firebase.auth.FirebaseUser;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLyoutManager;
    PostsListAdapter mAdapter;
    Vector<String> mData = new Vector<String>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    OnSignOutTapped();
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region  tmp initialize

        mRecyclerView = findViewById(R.id.main_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLyoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLyoutManager);

        for (int i=0;i<100;i++){
            mData.add("st" + i);
        }

        mAdapter = new PostsListAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PostsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int index) {
                Log.d("TAG","item click: " + index);
            }
        });

        //endregion

        mTextMessage = (TextView) findViewById(R.id.message);

        FirebaseUser user = Model.instance.currentUser();
        if (user != null) {
            String email = user.getEmail();
            Toast.makeText(MainActivity.this, email, Toast.LENGTH_LONG).show();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
