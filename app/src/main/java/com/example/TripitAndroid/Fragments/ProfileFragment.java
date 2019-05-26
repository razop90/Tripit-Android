package com.example.TripitAndroid.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.TripitAndroid.Classes.Adapters.ProfileListAdapter;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;

import java.util.ArrayList;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView mTextMessage;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLyoutManager;
    ProfileListAdapter mAdapter;
    Vector<String> mData = new Vector<String>();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.Profile_recyclerview);



        String uid = Model.instance.currentUser().getUid();

        Model.instance.getAllPostsFromUser(uid, new Model.OnPostUpdatedListener() {
            @Override
            public void onPostUpdated(ArrayList<Post> posts) {
                mAdapter = new ProfileListAdapter(posts);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        return view;
    }

}
