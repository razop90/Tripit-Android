package com.example.TripitAndroid.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.TripitAndroid.Classes.Comment;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.R;

import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {

    ImageView postImageView;
    ProgressBar progressBar;
    EditText descriptionPost;
    EditText locationPost;
    Button takePicBtn ;
    Button uploadPostBtn;

    public AddPostFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_post, container, false);


        postImageView = (ImageView) view.findViewById(R.id.post_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        descriptionPost = (EditText) view.findViewById(R.id.description_on_post_txt);
        locationPost = (EditText) view.findViewById(R.id.location_on_post_txt);
        takePicBtn = (Button) view.findViewById(R.id.take_pic_btn);
        uploadPostBtn = (Button) view.findViewById(R.id.upload_post_btn);


        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });

        uploadPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        return view;
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;
    Bitmap imageBitmap;

    private void takePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            postImageView.setImageBitmap(imageBitmap);
        }
    }

    private AddPostFragment fragment;
    private void save() {
        fragment = this;
        // activate progress bar
        progressBar.setVisibility(View.VISIBLE);
        // save image
        //Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
        Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
            @Override
            public void onComplete(String url) {
                FirebaseUser user =  Model.instance.currentUser();

                Post p = new Post(user.getUid() ,locationPost.getText().toString(), descriptionPost.getText().toString(), url);
                // save Post
                Model.instance.addPost(p, new FirebaseModel.OnAddPostCompleteListener() {
                    @Override
                    public  void onAddPostsComplete() {
                        progressBar.setVisibility(View.INVISIBLE);
                        fragment.getActivity().onBackPressed();
                    }
                });
            }

            @Override
            public void fail() {
            }
        });
    }

}