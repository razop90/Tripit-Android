package com.example.tripit_android.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseModel {

    private DatabaseReference ref;
    //FirebaseStorage storage = FirebaseStorage.getInstance("gs://tripit-65d75.appspot.com");
    //private StorageReference storageRef = storage.getReference();

    public FirebaseModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

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
