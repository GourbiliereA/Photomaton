package com.gourbi.photomaton;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex GOURBILIERE on 27/12/2016.
 */
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private int ACTION_TAKE_PICTURE = 1;

    private Camera camera;
    private Boolean isPreview;
    private FileOutputStream stream;
    private String currentPictureUri;

    private ImageView buttonTakePicture;
    private SurfaceView surfaceCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full Screen Application
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        isPreview = false;

        setContentView(R.layout.activity_main);

        surfaceCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);

        buttonTakePicture = (ImageView) findViewById(R.id.imageView_photo);
        buttonTakePicture.setOnTouchListener(new View.OnTouchListener() {
            // Boolean to know if the user is still clicking on the icon when
            // he moves his finger from the screen
            private boolean stillOnIcon = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());    // Variable rect to hold the bounds of the view

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        buttonTakePicture.setImageResource(R.drawable.camera_icon_hover);
                        stillOnIcon = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        buttonTakePicture.setImageResource(R.drawable.camera_icon);
                        if (stillOnIcon) {
                            SavePicture();
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

        InitializeCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (camera == null)
            camera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (isPreview) {
            camera.stopPreview();
        }

        try {
            camera.setPreviewDisplay(surfaceCamera.getHolder());
        } catch (IOException e) {
        }

        camera.startPreview();

        isPreview = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
            isPreview = false;
            camera.release();
        }
    }

    // App resumed
    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    // App paused
    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void SavePicture() {
        try {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy-MM-dd-HH.mm.ss");
            String fileName = "photo_" + timeStampFormat.format(new Date())
                    + ".jpg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image taken by the app created by Alex GOURBILIERE");
            values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            StringBuilder path = new StringBuilder();
            path.append(Environment.getExternalStorageDirectory());
            path.append("/Pictures/");
            path.append(fileName);
            File imageFile = new File(path.toString());

            Uri pictureTakenUri = Uri.fromFile(imageFile);

            stream = (FileOutputStream) getContentResolver().openOutputStream(
                    pictureTakenUri);

            currentPictureUri = pictureTakenUri.getPath();

            camera.takePicture(null, pictureCallback, pictureCallback);
        } catch (Exception e) {
            System.out.println("Exception in SavePicture method : \n" + e.getMessage());
        }

    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                try {
                    if (stream != null) {
                        stream.write(data);
                        stream.flush();
                        stream.close();
                    }

                    launchPhotoPreview();
                } catch (Exception e) {
                    System.out.println("Exception in Camera's callback's method : \n" + e.getMessage());
                }

//                camera.startPreview();
            }
        }
    };

    private void launchPhotoPreview() {
        Intent myIntent = new Intent(this, PhotoPreviewActivity.class);
        myIntent.putExtra(getString(R.string.parameter_fileName), currentPictureUri);
        startActivity(myIntent);
    }

    private void InitializeCamera() {
        surfaceCamera.getHolder().addCallback(this);
        surfaceCamera.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}
