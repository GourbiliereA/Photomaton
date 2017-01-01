package com.gourbi.photomaton.listeners;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.gourbi.photomaton.R;
import com.gourbi.photomaton.activities.PhotomatonActivity;

/**
 * Created by Alex GOURBILIERE on 01/01/2017.
 */

public class ImageViewOnTouchListener implements View.OnTouchListener {
    // Boolean to know if the user is still clicking on the icon when
    // he moves his finger from the screen
    private boolean stillOnIcon = false;

    // Activity containing the icon
    private PhotomatonActivity activity;

    // Default image of the icon
    private int default_icon;

    // Image of the icon when clicked
    private int clicked_icon;

    public ImageViewOnTouchListener(PhotomatonActivity act, int d_icon, int c_icon) {
        activity = act;
        default_icon = d_icon;
        clicked_icon = c_icon;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());    // Variable rect to hold the bounds of the view
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                ((ImageView) v).setImageResource(clicked_icon);
                stillOnIcon = true;
                break;
            case MotionEvent.ACTION_UP:
                ((ImageView) v).setImageResource(default_icon);
                if (stillOnIcon) {
                    activity.performActionOnClick();
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
}
