package com.example.TripitAndroid.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.TripitAndroid.Activities.LoginActivity;
import com.example.TripitAndroid.Activities.MainActivity;
import com.example.TripitAndroid.Classes.Adapters.ProfileListAdapter;
import com.example.TripitAndroid.Classes.Comment;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.R;

import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.android.gms.flags.IFlagProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {

    String mPostId;
    Post mPost = null;
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

        descriptionPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descriptionPost.toString() != null){
                    descriptionPost.findViewById(R.id.description_on_post_txt);
                }
            }
        });


        if (mPostId != null) {
            String iurl = mPost.imageUrl;
            String desc = mPost.description;
            String loc = mPost.location;

            //postImageView.set
            //postImageView.setImageBitmap(); new Model.GetImageListener()

         Model.instance.getImageBitMap(iurl, new Model.GetImageBitMapListener() {
             @Override
             public void onComplete(Bitmap bitMap) {
                 postImageView.setImageBitmap(bitMap);
             }

             @Override
             public void fail() {

             }
         });

         descriptionPost.setText(desc);
         locationPost.setText(loc);
        }

        return view;
    }

    public void setPost(Post post, String postId){
        mPostId = postId;
        mPost = post;
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

        try {
            if(imageBitmap == null) imageBitmap = ((BitmapDrawable)postImageView.getDrawable()).getBitmap();
        }
        catch(Exception e) {
            Toast.makeText(getActivity(), "Error while trying to save post - plz try again", Toast.LENGTH_LONG).show();
            return;
        }

        if(imageBitmap == null) {
            Toast.makeText(getActivity(), "Post must have an image!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if(descriptionPost.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Post must have description!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if(locationPost.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Post must have location!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
            @Override
            public void onComplete(String url) {
                FirebaseUser user = Model.instance.currentUser();

                if (mPost == null) {
                    Post p = new Post(user.getUid(), locationPost.getText().toString(), descriptionPost.getText().toString(), url);
                    // save Post
                    Model.instance.addPost(p, new FirebaseModel.OnAddPostCompleteListener() {
                        @Override
                        public void onAddPostsComplete() {
                            fragment.getActivity().onBackPressed();
                        }
                    });
                } else {
                    mPost.description = descriptionPost.getText().toString();
                    mPost.location = locationPost.getText().toString();
                    mPost.imageUrl = url;

                    Model.instance.updatePost(mPost, false, new FirebaseModel.OnAddPostCompleteListener() {
                        @Override
                        public void onAddPostsComplete() {
                            fragment.getActivity().onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void fail() {
            }
        });
    }
}