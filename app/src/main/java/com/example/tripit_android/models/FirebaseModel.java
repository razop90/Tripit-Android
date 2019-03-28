package com.example.tripit_android.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.tripit_android.Classes.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseModel {

    private DatabaseReference ref;
    private FirebaseAuth auth;
    //FirebaseStorage storage = FirebaseStorage.getInstance("gs://tripit-65d75.appspot.com");
    //private StorageReference storageRef = storage.getReference();

    //region Callbacks
    OnLoginCompleteListener loginCompleteListener;
    OnSignUpCompleteListener signUpCompleteListener;
    OnUserInfoCompletedListener userInfoCompleteListener;

    public interface OnLoginCompleteListener {
        void onLoginComplete(FirebaseUser user);
    }

    public interface OnSignUpCompleteListener {
        void onSignUpComplete(Boolean result);
    }

    public interface OnSignOutCompleteListener {
        void onSignOutComplete();
    }

    public interface OnUserInfoCompletedListener {
        void onUserInfoComplete(Boolean result);
    }
    //endregion

    public FirebaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        auth = FirebaseAuth.getInstance();

        //region Read/Write from the database
        //        getImage("https://firebasestorage.googleapis.com/v0/b/tripit-65d75.appspot.com/o/ImagesStorage%2F1545398805.264354.jpg?alt=media&token=33cbb110-c1c0-4963-8e1d-265ae6e02a76");
        //Image imageRef = storageRef.child("").child("");
//        ref.child("Users").child("me").setValue("Hello, World!");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                //Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                // Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
        //endregion
    }


    public void addUserInfo(UserInfo userInfo, ImageView image, OnUserInfoCompletedListener callback) {
        userInfoCompleteListener = callback;

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
            ref.child("Users").child(userInfo.uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        userInfoCompleteListener.onUserInfoComplete(true);
                    } else {
                        userInfoCompleteListener.onUserInfoComplete(false);
                    }
                }
            });
        }
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

                            UserInfo userInfo = new UserInfo(uid, display, email, "", 0l);
                            addUserInfo(userInfo, null, new OnUserInfoCompletedListener() {
                                @Override
                                public void onUserInfoComplete(Boolean result) {
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
}
