package com.example.baoxie.tips;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedGifView extends View {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long movieRunDuration;
    private long lastTick;
    private long nowTick;
    private String action;

    private boolean repeat = true;
    private boolean running = true;

    public void setRepeat(boolean r) {
        repeat = r;
    }

    public void setRunning(boolean r) {
        running = r;
    }

    public AnimatedGifView(Context context, String action) {
        super(context);
        init(context, action);
    }

    public AnimatedGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, action);
    }

    public AnimatedGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, action);
    }

    private void init(Context context, String icon) {

        // Turn OFF hardware acceleration
        // API Level 11
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setFocusable(true);
        action = icon;

        switch(icon) {
            case "steam":
                gifInputStream = context.getResources().openRawResource(
                        R.raw.steam);
                break;
            case "stir":
                gifInputStream = context.getResources().openRawResource(
                        R.raw.stir);
                break;
//            case 'shake:
//                gifInputStream = context.getResources().openRawResource(
//                        R.raw.shake);
//                break;
            case "pour":
                gifInputStream = context.getResources().openRawResource(
                        R.raw.pour);
        }
        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(gifMovie == null){
            return;
        }

        nowTick = android.os.SystemClock.uptimeMillis();
        if (lastTick == 0) { // first time
            movieRunDuration = 0;
        }else{
            if(running){
                movieRunDuration += nowTick-lastTick;
                if(movieRunDuration > movieDuration){
                    if(repeat){
                        movieRunDuration = 0;
                    }else{
                        movieRunDuration = movieDuration;
                    }
                }
            }
        }

        gifMovie.setTime((int)movieRunDuration);
        gifMovie.draw(canvas, 20, 0);

        lastTick = nowTick;
        invalidate();

    }
}