package com.example.tripit_android.models;

public class Model {
    public static Model instance = new Model();

    private FirebaseModel firebaseModel = new FirebaseModel();
    //private SqlModel sqlModel = new SqlModel();
    private String userId = null;

    private Model() {
    }

    public void Initialize() {
    }
}
