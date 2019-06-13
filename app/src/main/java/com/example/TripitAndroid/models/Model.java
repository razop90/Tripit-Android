package com.example.TripitAndroid.models;

import android.media.Image;

import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.Consts;
import com.google.firebase.auth.FirebaseUser;
import android.graphics.Bitmap;


import java.util.ArrayList;
import java.util.List;

public class Model {
    public static Model instance = new Model();

    private String userId = null;
    private String searchedUserInfo = null;
    private FirebaseModel firebaseModel = new FirebaseModel();
    private SqlModel sqlModel = new SqlModel();

    //region Callbacks
    public OnPostUpdatedListener onPostUpdatedListener;
    public OnPostUpdatedListener onUserPostUpdatedListener;
    public OnUserInfoUpdated onUserInfoUpdated;
    public OnAddPostCompleteListener onAddPostCompleteListener;
    public SaveImageListener saveImageListener;

    public interface OnAddPostCompleteListener {
        void onComplete(boolean success);
    }

    public interface SaveImageListener{
        void onComplete(String url);
    }

    private interface OnGetPostsCompleteListener {
        void onGetPostsComplete(boolean isUpdated, boolean curUserUpdated);
    }
    public interface OnPostUpdatedListener {
        void onPostUpdated(ArrayList<Post> posts);
    }
    public interface OnUserInfoUpdated {
        void onUserInfoUpdated(UserInfo userInfo);
    }
    //endregion

    private Model() {
    }

    public void getAllPostsFromUser(String uid, final OnPostUpdatedListener callback) {
        onUserPostUpdatedListener = callback;
        userId = uid;

        firebaseModel.getAllPostsFromUser(uid, new FirebaseModel.OnGetUserPostsCompleteListener() {
            @Override
            public void OnGetUserPostsComplete(ArrayList<Post> data) {
                sqlHandler(data, new OnGetPostsCompleteListener() {
                    @Override
                    public void onGetPostsComplete(boolean isUpdated, boolean curUserUpdated) {
                        getAllPostsFromLocalAndNotify(userId, onUserPostUpdatedListener);
                    }
                });
            }
        });

        getAllPostsFromLocalAndNotify(userId, onUserPostUpdatedListener);
    }

    public void getAllPosts(final OnPostUpdatedListener callback) {
        onPostUpdatedListener = callback;
        long lastUpdated = sqlModel.getLastUpdate(Consts.SQL.PostsTableName);
        lastUpdated += 1;

        firebaseModel.getAllPostsFromDate(lastUpdated, new FirebaseModel.OnGetPostsCompleteListener() {
            @Override
            public void onGetPostsComplete(ArrayList<Post> data) {
                sqlHandler(data, new OnGetPostsCompleteListener() {
                    @Override
                    public void onGetPostsComplete(boolean isUpdated, boolean curUserUpdated) {
                        if(isUpdated) {
                            String user = null;
                            if(curUserUpdated && userId != null) {
                                user = userId;
                            }

                            getAllPostsFromLocalAndNotify(user, onPostUpdatedListener);
                        }
                    }
                });
            }
        });

        getAllPostsFromLocalAndNotify(null, onPostUpdatedListener);
    }

    private void getAllPostsFromLocalAndNotify(String _userId, OnPostUpdatedListener callback){
        ArrayList<Post> postData = sqlModel.getAllPosts(_userId);
        callback.onPostUpdated(postData);
    }

    private void sqlHandler(List<Post> data, OnGetPostsCompleteListener callback) {
        long lastUpdated = sqlModel.getLastUpdate(Consts.SQL.PostsTableName);
        lastUpdated += 1;
        boolean isUpdated = false;
        boolean currUserUpdated = false;

        for (Post post:data) {
            //removing all likes
            //Post.removeAllLikes(database: self.sqlModel.database, postId: post.id)

            if(post.isDeleted == 1) {
                sqlModel.deletePost(post.id);
            } else {
                sqlModel.addPost(post);

                //updating the likes collection
                //for like in post.likes {
                //    Post.addNewLike(database: self.sqlModel.database, postId: post.id, userId: like)
                //}
            }

            if(post.lastUpdateLongFormat > lastUpdated) {
                lastUpdated = post.lastUpdateLongFormat;
                isUpdated = true;
            }

            if(this.userId != null && post.userID == userId) {
                currUserUpdated = true;
            }
        }

        if(isUpdated) {
            sqlModel.updateLastUpdate(Consts.SQL.PostsTableName, lastUpdated);
        }

        callback.onGetPostsComplete(isUpdated, currUserUpdated);
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

    public void getUserInfo(String uid, OnUserInfoUpdated callback) {
        onUserInfoUpdated = callback;
        searchedUserInfo = uid;

        firebaseModel.getUserInfo(uid, new FirebaseModel.OnGetUserInfoCompletedListener() {
            @Override
            public void onUserInfoGetComplete(UserInfo userInfo) {
                if(userInfo != null) {
                    long lastUpdated = sqlModel.getLastUpdate(Consts.SQL.UserInfoTableName);
                    lastUpdated += 1;

                    sqlModel.addUserInfo(userInfo);

                    if (userInfo.getLastUpdate() > lastUpdated){
                        lastUpdated = userInfo.getLastUpdate();
                        sqlModel.updateLastUpdate(Consts.SQL.UserInfoTableName, lastUpdated);
                        getUserInfoFromLocalAndNotify(searchedUserInfo, onUserInfoUpdated);
                    }
                }
            }
        });

        getUserInfoFromLocalAndNotify(uid, callback);
    }

    private void getUserInfoFromLocalAndNotify(String uid, OnUserInfoUpdated callback) {
        UserInfo info = sqlModel.getUserInfo(uid);
        if(info != null) {
            callback.onUserInfoUpdated(info);
        }
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


    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        firebaseModel.saveImage(imageBitmap, listener);
    }


}
