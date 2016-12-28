package com.gourbi.photomaton;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Alex GOURBILIERE on 27/12/2016.
 */

public class PhotoPreviewActivity extends AppCompatActivity {
    ImageView imageViewPhotoPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Entering Preview Activity");

        // Full Screen Application
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo_preview);

        Intent myIntent = getIntent();
        imageViewPhotoPreview = (ImageView) findViewById(R.id.imageView_photoPreview);

        String imagePath = myIntent.getStringExtra(getString(R.string.parameter_fileName));
        System.out.println("File Path : " + imagePath);

        imageViewPhotoPreview.setImageURI(Uri.fromFile(new File(imagePath)));
    }
}
