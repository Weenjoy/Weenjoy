package com.df.dianping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.eftimoff.androipathview.PathView;

public class SplashActivity extends Activity {

    private PathView pathView;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.splash_screen_view);
        pathView= (PathView) findViewById(R.id.pathView);
        pathView.getPathAnimator().delay(100).duration(2000).listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
            @Override
            public void onAnimationEnd() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}