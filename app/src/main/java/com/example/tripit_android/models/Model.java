package com.example.tripit_android.models;

import com.google.firebase.auth.FirebaseUser;

public class Model {
    public static Model instance = new Model();

    private FirebaseModel firebaseModel = new FirebaseModel();
    //private SqlModel sqlModel = new SqlModel();

    private Model() {
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
