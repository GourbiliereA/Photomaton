package com.gourbi.photomaton;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Alex GOURBILIERE on 27/12/2016.
 */

public class PhotoPreviewActivity extends AppCompatActivity {
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

        Intent myIntent = getIntent();
        imageViewPhotoPreview = (ImageView) findViewById(R.id.imageView_photoPreview);
        String imagePath = myIntent.getStringExtra(getString(R.string.parameter_fileName));
        imageViewPhotoPreview.setImageURI(Uri.fromFile(new File(imagePath)));


        buttonReturn = (ImageView) findViewById(R.id.imageView_return);
        buttonReturn.setOnTouchListener(new View.OnTouchListener() {
            // Boolean to know if the user is still clicking on the icon when
            // he moves his finger from the screen
            private boolean stillOnIcon = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());    // Variable rect to hold the bounds of the view

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        buttonReturn.setImageResource(R.drawable.return_icon_hover);
                        stillOnIcon = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        buttonReturn.setImageResource(R.drawable.return_icon);
                        if (stillOnIcon) {
                            cancelPicture();
                            stillOnIcon = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                            // User moved outside bounds
                            stillOnIcon = false;
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void cancelPicture() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
