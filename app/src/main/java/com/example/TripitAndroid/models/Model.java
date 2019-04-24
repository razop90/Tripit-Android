package com.example.TripitAndroid.models;

import android.media.Image;

import com.example.TripitAndroid.Classes.Post;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Model {
    public static Model instance = new Model();

    private FirebaseModel firebaseModel = new FirebaseModel();
    //private SqlModel sqlModel = new SqlModel();

    private Model() {
//       firebaseModel.getAllPostsFromDate(0, new FirebaseModel.OnGetPostsCompleteListener() {
//           @Override
//           public void onGetPostsComplete(ArrayList<Post> data) {
//
//           }
//       });
    }
    public void getAllPostsFromUser(String uid, FirebaseModel.OnGetUserPostsCompleteListener callback) {
        firebaseModel.getAllPostsFromUser(uid, callback);
    }

    public void getAllPostsFromDate(long from, FirebaseModel.OnGetPostsCompleteListener callback) {
        firebaseModel.getAllPostsFromDate(from, callback);
    }

    public void addPost(Post post, Image image, FirebaseModel.OnAddPostCompleteListener callback) {
        firebaseModel.addPost(post, image, callback);
    }

    public void updatePost(Post post, Image image, boolean isImageUpdated, FirebaseModel.OnAddPostCompleteListener callback) {
        firebaseModel.updatePost(post, image, isImageUpdated, callback);
    }

    public void setPostAsDeleted(String postId) {
        firebaseModel.setPostAsDeleted(postId);
    }

    public void getUserInfo(String uid, FirebaseModel.OnGetUserInfoCompletedListener callback) {
        firebaseModel.getUserInfo(uid, callback);
    }

    public void signUp(String email, String password, FirebaseModel.OnSignUpCompleteListener callback) {
        firebaseModel.signUp(email, password, callback);
    }

    public void login(String email, String password, FirebaseModel.OnLoginCompleteListener callback) {
        firebaseModel.signIn(email, password, callback);
    }

    public void signOut(FirebaseModel.OnSignOutCompleteListener callback) {
        firebaseModel.signOut(callback);
    }

    public FirebaseUser currentUser() {
        return firebaseModel.currentUser();
    }
}
