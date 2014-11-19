package com.hp.samples.moveit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {

    ImageView pokeball1;
    ImageView pokeball2;
    private boolean exploded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokeball1 = (ImageView) findViewById(R.id.pokeball1);
        pokeball2 = (ImageView) findViewById(R.id.pokeball2);

        pokeball2.setX(pokeball2.getX() + 500);
        pokeball2.setY(pokeball2.getY() + 500);

        pokeball1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return updateViewPositionOnMove(view, event);
            }
        });

        pokeball2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return updateViewPositionOnMove(view, event);
            }
        });


    }

    private boolean areImagesIntersecting() {
        Rect rectangle1 = new Rect();
        pokeball1.getHitRect(rectangle1);
        Rect rectangle2 = new Rect();
        pokeball2.getHitRect(rectangle2);
        return rectangle1.intersect(rectangle2);
    }

    private float currentX = 0;
    private float currentY = 0;

    AnimatorSet animatorSet;

    private boolean updateViewPositionOnMove(View view, MotionEvent event) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float diffX = event.getRawX() - currentX;
                float diffY = event.getRawY() - currentY;
                params.topMargin += diffY;
                params.leftMargin += diffX;
                view.setLayoutParams(params);
                currentX = event.getRawX();
                currentY = event.getRawY();

                if (areImagesIntersecting() && !exploded) {
                    if (animatorSet == null || !animatorSet.isRunning()) {
                        exploded = true;
                        final ImageView otherView = view == pokeball1 ? pokeball2 : pokeball1;
                        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(otherView, View.SCALE_Y, 1f, 10f);
                        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(otherView, View.SCALE_X, 1f, 10f);
                        ObjectAnimator alpha = ObjectAnimator.ofFloat(otherView, View.ALPHA, 1f, 0f);
                        animatorSet = new AnimatorSet();
                        animatorSet.setDuration(500);
                        animatorSet.playTogether(xAnimator, yAnimator, alpha);
                        animatorSet.start();
                        alpha.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                otherView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_DOWN:
                currentX = event.getRawX();
                currentY = event.getRawY();
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
