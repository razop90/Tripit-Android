
package com.example.TripitAndroid.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.net.Uri;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.Consts;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.FirebaseFirestoreSettings;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FirebaseModel {

    private DatabaseReference ref;
    private FirebaseAuth auth;
    //FirebaseStorage storage = FirebaseStorage.getInstance("gs://tripit-65d75.appspot.com");
    //private StorageReference storageRef = storage.getReference();

    //region Callbacks
    OnGetPostsCompleteListener postsGetCompleteListener;
    OnGetUserPostsCompleteListener userPostsGetCompleteListener;
    OnAddPostCompleteListener postAddCompleteListener;
    OnLoginCompleteListener loginCompleteListener;
    OnSignUpCompleteListener signUpCompleteListener;
    OnAddUserInfoCompletedListener userInfoAddCompleteListener;
    OnGetUserInfoCompletedListener userInfoGetCompleteListener;

    public interface OnGetPostsCompleteListener {
        void onGetPostsComplete(ArrayList<Post> data);
    }

    public interface OnGetUserPostsCompleteListener {
        void OnGetUserPostsComplete(ArrayList<Post> data);
    }

    public interface OnAddPostCompleteListener {
        void onAddPostsComplete();
    }

    public interface OnLoginCompleteListener {
        void onLoginComplete(FirebaseUser user);
    }

    public interface OnSignUpCompleteListener {
        void onSignUpComplete(Boolean result);
    }

    public interface OnSignOutCompleteListener {
        void onSignOutComplete();
    }

    public interface OnAddUserInfoCompletedListener {
        void onUserInfoAddComplete(Boolean result);
    }

    public interface OnGetUserInfoCompletedListener {
        void onUserInfoGetComplete(UserInfo userInfo);
    }
    //endregion

    public FirebaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        auth = FirebaseAuth.getInstance();

//        getAllPostsFromUser("fiNmpX6PbxRxAAZnrVfEWETFSY53", new OnGetUserPostsCompleteListener() {
//            @Override
//            public void OnGetUserPostsComplete(ArrayList<Post> data) {
//                if (data != null) {
//
//                }
//            }
//        });
    }


    public void getAllPostsFromUser(String uid, OnGetUserPostsCompleteListener callback){
        userPostsGetCompleteListener = callback;

        DatabaseReference stRef = ref.child(Consts.Tables.PostsTableName);
        Query fbQuery = stRef.orderByChild("userID").equalTo(uid);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifyDataChanged(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void notifyDataChanged(DataSnapshot dataSnapshot) {
                ArrayList<Post> posts = new ArrayList<Post>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = new Post(snapshot);
                    posts.add(post);
                }
                userPostsGetCompleteListener.OnGetUserPostsComplete(posts);
            }
        };

        fbQuery.addValueEventListener(valueEventListener);
    }

    public void getAllPostsFromDate(long from, OnGetPostsCompleteListener callback) {
        postsGetCompleteListener = callback;


        DatabaseReference stRef = ref.child(Consts.Tables.PostsTableName);
        Query fbQuery = stRef.orderByChild("lastUpdate").startAt(from);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifyDataChanged(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void notifyDataChanged(DataSnapshot dataSnapshot) {
                ArrayList<Post> posts = new ArrayList<Post>();

                //GenericTypeIndicator<List<Post>> t = new GenericTypeIndicator<List<Post>>();
                // List<Post> messages = dataSnapshot.getValue(t);

                //Post map = dataSnapshot.getValue(Post.class);
                //String key = (String) dataSnapshot.getKey();
                //Object name = map.get("userID");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = new Post(snapshot);
                    posts.add(post);
                }

                postsGetCompleteListener.onGetPostsComplete(posts);
            }
        };

        ChildEventListener dataListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                notifyDataChanged(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                notifyDataChanged(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                notifyDataChanged(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                notifyDataChanged(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

            public void notifyDataChanged(DataSnapshot dataSnapshot) {
                ArrayList<Post> posts = new ArrayList<Post>();

                GenericTypeIndicator<List<Post>> t = new GenericTypeIndicator<List<Post>>() {};
                List<Post> messages = dataSnapshot.getValue(t);

                Post map = dataSnapshot.getValue(Post.class);
                String key = (String)dataSnapshot.getKey();
                //Object name = map.get("userID");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //String key = (String)snapshot.getKey();
                    Map values = (Map)snapshot.getValue();
                    Post post = new Post();

                    //post.id = key;
                    post.userID = (String)values.get("userID");

                    posts.add(post);
                }

                postsGetCompleteListener.onGetPostsComplete(posts);
            }
        };

        //fbQuery.addChildEventListener(dataListener);
        fbQuery.addValueEventListener(valueEventListener);
    }

    public void addPost(Post post, OnAddPostCompleteListener callback) {
        postAddCompleteListener = callback;

        ref.child(Consts.Tables.PostsTableName).push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    postAddCompleteListener.onAddPostsComplete();
                }
            }
        });
    }

    public void updatePost(Post post, boolean isImageUpdated, OnAddPostCompleteListener callback) {
        postAddCompleteListener = callback;

        if (isImageUpdated) { //uploading an image
            //ToDo - add image uploading and then post uploading.
        } else if (post.id != "") { //no need of uploading an image and it's not a new post
            updatePostParameters(post, false, null);

            postAddCompleteListener.onAddPostsComplete();
        } else { //nothing to add or update
            postAddCompleteListener.onAddPostsComplete();
        }
    }



    private void updatePostParameters(Post post, boolean saveImage, String newImageUrl) {
        ref.child(Consts.Tables.PostsTableName).child(post.id).child("location").setValue(post.location);
        ref.child(Consts.Tables.PostsTableName).child(post.id).child("description").setValue(post.description);
        if (saveImage && post.imageUrl != null && newImageUrl != null) {
            deleteImage(post.imageUrl);
            ref.child(Consts.Tables.PostsTableName).child(post.id).child("imageUrl").setValue(newImageUrl);
        }

        ref.child(Consts.Tables.PostsTableName).child(post.id).child("lastUpdate").setValue(ServerValue.TIMESTAMP);
    }

    private void deleteImage(String imageUrl) {
        //ToDo
    }

    public void setPostAsDeleted(String postId) {
        ref.child(Consts.Tables.PostsTableName).child(postId).child("isDeleted").setValue(1);
        ref.child(Consts.Tables.PostsTableName).child(postId).child("lastUpdate").setValue(ServerValue.TIMESTAMP);
    }

    public void addUserInfo(UserInfo userInfo, ImageView image, OnAddUserInfoCompletedListener callback) {
        userInfoAddCompleteListener = callback;

        if (image != null) {
//            saveImage(folderName:Consts.Posts.ProfileImagesFolderName, image:image !){
//                (url:String ?)in
//                if url != nil {
//                    userInfo.profileImageUrl = url !
//                }
//
//                self.ref !.
//                child(Consts.Posts.UserInfoTableName).child(userInfo.uid).setValue(userInfo.toJson())
//                completionBlock(true)
//            }
        } else {
            ref.child(Consts.Tables.UserInfoTableName).child(userInfo.uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        userInfoAddCompleteListener.onUserInfoAddComplete(true);
                    } else {
                        userInfoAddCompleteListener.onUserInfoAddComplete(false);
                    }
                }
            });
        }
    }

    public void getUserInfo(final String uid, OnGetUserInfoCompletedListener callback) {
        userInfoGetCompleteListener = callback;

        ref.child(Consts.Tables.UserInfoTableName).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo user = dataSnapshot.getValue(UserInfo.class);

                if (user != null)
                    user.uid = uid;

                if (userInfoGetCompleteListener != null)
                    userInfoGetCompleteListener.onUserInfoGetComplete(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (userInfoGetCompleteListener != null)
                    userInfoGetCompleteListener.onUserInfoGetComplete(null);
            }
        });
    }

    public void signUp(String email, String password, OnSignUpCompleteListener callback) {
        signUpCompleteListener = callback;

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = task.getResult().getUser().getUid();
                            final String email = task.getResult().getUser().getEmail();
                            String display = email.split("@")[0];

                            UserInfo userInfo = new UserInfo(uid, display, email, "", null);
                            addUserInfo(userInfo, null, new OnAddUserInfoCompletedListener() {
                                @Override
                                public void onUserInfoAddComplete(Boolean result) {
                                    if (result) {
                                        if (signUpCompleteListener != null)
                                            signUpCompleteListener.onSignUpComplete(true);
                                    } else {
                                        if (signUpCompleteListener != null)
                                            signUpCompleteListener.onSignUpComplete(false);
                                    }
                                }
                            });
                        } else {
                            if (signUpCompleteListener != null)
                                signUpCompleteListener.onSignUpComplete(false);
                        }
                    }
                });
    }

    public void signIn(String email, String password, OnLoginCompleteListener callback) {
        loginCompleteListener = callback;

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = currentUser();
                            if (user != null && loginCompleteListener != null)
                                loginCompleteListener.onLoginComplete(user);
                        } else {
                            if (loginCompleteListener != null)
                                loginCompleteListener.onLoginComplete(null);
                        }
                    }
                });
    }

    public void signOut(OnSignOutCompleteListener callback) {
        try {
            auth.signOut();
            if (callback != null)
                callback.onSignOutComplete();
        } catch (Exception e) {
            Log.d("TAG", "Error while signing out!");
        }
    }

    public FirebaseUser currentUser() {
        return auth.getCurrentUser();
    }

    public Bitmap getImage(String url) {
//        StorageReference islandRef = storageRef.child(url);
//
//        final long ONE_MEGABYTE = 1024 * 1024;
//        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//              Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 1024,bytes.length);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

        return null;
    }

    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        Date d = new Date();
        // Create a reference to "mountains.jpg"
        final StorageReference imageStorageRef = storageRef.child("image_" + d.getTime() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onComplete(null);
            }
        });
    }

// old func of saveImage

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        // Create a storage reference from our app
//        StorageReference storageRef = storage.getReference();
//
//        Date d = new Date();
//        // Create a reference to "mountains.jpg"
//        final StorageReference imageStorageRef = storageRef.child("image_" + d.getTime() + ".jpg");
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imageStorageRef.putBytes(data);
//
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return imageStorageRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    listener.onComplete(downloadUri.toString());
//                } else {
//                    listener.onComplete(null);
//                }
//            }
//        });
//    }

    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onComplete(image.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                listener.fail();

            }
        });
    }
}