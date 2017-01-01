package com.gourbi.photomaton.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gourbi.photomaton.listeners.ImageViewOnTouchListener;
import com.gourbi.photomaton.R;

import java.io.File;

/**
 * Created by Alex GOURBILIERE on 27/12/2016.
 */

public class PhotoPreviewActivity extends AppCompatActivity implements PhotomatonActivity {
    private ImageView imageViewPhotoPreview;
    private ImageView buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full Screen Application
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo_preview);

        // Displaying the preview of the picture thanks to a parameter in intent
        Intent myIntent = getIntent();
        imageViewPhotoPreview = (ImageView) findViewById(R.id.imageView_photoPreview);
        String imagePath = myIntent.getStringExtra(getString(R.string.parameter_fileName));
        imageViewPhotoPreview.setImageURI(Uri.fromFile(new File(imagePath)));

        // Adding our custom listener to the return icon
        buttonReturn = (ImageView) findViewById(R.id.imageView_return);
        buttonReturn.setOnTouchListener(new ImageViewOnTouchListener(this, R.drawable.return_icon, R.drawable.return_icon_hover));
    }

    @Override
    public void performActionOnClick() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
