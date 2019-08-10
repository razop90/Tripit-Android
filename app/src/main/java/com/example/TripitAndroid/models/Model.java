package com.example.TripitAndroid.models;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.TripitApplication;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.Consts;
import com.google.firebase.auth.FirebaseUser;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public OnUserInfoUpdated onPostGetListener;
    public OnAddPostCompleteListener onAddPostCompleteListener;
    public SaveImageListener saveImageListener;
    public GetImageListener getImageListener;



    public interface OnAddPostCompleteListener {
        void onComplete(boolean success);
    }

    public interface SaveImageListener{
        void onComplete(String url);
        void fail();
    }

    public interface GetImageListener{
        void onComplete(String url);
        void fail();
    }

    public interface GetImageBitMapListener{
        void onComplete(Bitmap bitMap);
        void fail();
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

    public interface OnPostGetListener {
        void OnPostGetListener(String postId);
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
        lastUpdated = 0;

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
        lastUpdated = 0;
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

    public void getPost(String postId, FirebaseModel.OnGetPostCompletedListener callback) {
        firebaseModel.getPost(postId, callback);
    }

    public void addPost(Post post, FirebaseModel.OnAddPostCompleteListener callback) {
        firebaseModel.addPost(post, callback);
    }

    public void updatePost(Post post, boolean isImageUpdated, FirebaseModel.OnAddPostCompleteListener callback) {
        firebaseModel.updatePost(post, isImageUpdated, callback);
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
                    lastUpdated = 0;

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

    //funcs for getImage & saveImage
    private String getLocalImageFileName(String url) {
        String name = URLUtil.guessFileName(url, null, null);
        return name;
    }

    private void addPicureToGallery(File imageFile){
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        TripitApplication.getContext().sendBroadcast(mediaScanIntent);
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    //End funcs for get & save Image

    public void saveImage(final Bitmap imageBitmap, final SaveImageListener listener) {
        //1. save the image remotly
        firebaseModel.saveImage(imageBitmap, new SaveImageListener() {
            @Override
            public void onComplete(String url) {
                // 2. saving the file localy
                String localName = getLocalImageFileName(url);
                Log.d("TAG","cach image: " + localName);
                saveImageToFile(imageBitmap,localName); // synchronously save image locally
                //listener.oncomplete(url);
                listener.onComplete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });

    }

    public void getImage(final String url, final GetImageListener listener) {
        //1. first try to find the image on the device
        String localFileName = getLocalImageFileName(url);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) { //if image not found - try downloading it from parse
            firebaseModel.getImage(url, new GetImageListener() {
                @Override
                public void onComplete(String url) {
                    //2. save the image localy
                    String localFileName = getLocalImageFileName(url);
                    Log.d("TAG","save image to cache: " + localFileName);
                    saveImageToFile(image,localFileName);
                    //3. return the image using the listener
                    listener.onComplete(image.toString());
                }

                @Override
                public void fail() {
                    listener.fail();
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onComplete(image.toString());
        }}

    public void getImageBitMap(final String url, final GetImageBitMapListener listener) {
        //1. first try to find the image on the device
        String localFileName = getLocalImageFileName(url);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) { //if image not found - try downloading it from parse
            firebaseModel.getImageBitMap(url, new GetImageBitMapListener() {
                @Override
                public void onComplete(Bitmap bitMap) {
                    //2. save the image localy
                    String localFileName = getLocalImageFileName(url);
                    Log.d("TAG","save image to cache: " + localFileName);
                    saveImageToFile(image,localFileName);
                    //3. return the image using the listener
                    listener.onComplete(image);
                }

                @Override
                public void fail()  {
                    listener.fail();
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onComplete(image);
            }}


//    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
//        firebaseModel.saveImage(imageBitmap, listener);
//    }

//    public void getImage(String url, GetImageListener listener) {
//        firebaseModel.getImage(url, listener);
//    }

}