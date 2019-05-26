package com.example.TripitAndroid.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TripitAndroid.Classes.Adapters.PostsListAdapter;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    private TextView mTextMessage;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLyoutManager;
    PostsListAdapter mAdapter;
    Vector<String> mData = new Vector<String>();

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_posts,container,false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.main_recyclerview);

        Model.instance.getAllPosts(new Model.OnPostUpdatedListener() {
            @Override
            public void onPostUpdated(ArrayList<Post> posts) {
                mAdapter = new PostsListAdapter(posts);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        //mTextMessage = view.findViewById(R.id.message);

        FirebaseUser user = Model.instance.currentUser();
        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();

            Model.instance.getUserInfo(uid, new FirebaseModel.OnGetUserInfoCompletedListener() {
                @Override
                public void onUserInfoGetComplete(UserInfo userInfo) {
                    if (userInfo != null) {

                    }
                }
            });

            Toast.makeText(getActivity(), email, Toast.LENGTH_LONG).show();
        }

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        // Inflate the layout for this fragment
        return view;
    }
}
