package com.example.TripitAndroid.Activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.Model;
import com.example.TripitAndroid.Classes.Post;

public class NewPostActivity extends AppCompatActivity {
    ImageView avatarImgView;
    ProgressBar progressBar;
    EditText idEt;
    EditText nameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        progressBar = findViewById(R.id.ns_progree_pb);
        progressBar.setVisibility(View.INVISIBLE);

        Button takePicBtn = findViewById(R.id.ns_take_pic_btn);
        Button saveBtn = findViewById(R.id.ns_save_btn);

        idEt = findViewById(R.id.ns_id_et);
        nameEt = findViewById(R.id.ns_name_et);

        avatarImgView = findViewById(R.id.ns_image_img);

        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;

    private void takePic() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    Bitmap imageBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            avatarImgView.setImageBitmap(imageBitmap);
        }
    }


    private void save() {
        // activate progress bar
        progressBar.setVisibility(View.VISIBLE);

        // save image
        Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
            @Override
            public void onComplete(String url) {
                // create student
                Student student = new Student(idEt.getText().toString(),
                        nameEt.getText().toString(),
                        url);

                // save student
                Model.instance.addStudent(student, new Model.AddStudentListener() {
                    @Override
                    public void onComplete(boolean success) {
                        //stop progress bar
                        progressBar.setVisibility(View.INVISIBLE);

                        // close activity
                        finish();
                    }
                });

            }
        });



    }

}



