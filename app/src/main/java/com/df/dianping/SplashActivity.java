package com.df.dianping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.eftimoff.androipathview.PathView;

public class SplashActivity extends Activity {
    private MyVideoView vvStart;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    /* 加载start.xml Layout */
        setContentView(R.layout.activity_splash);
    /* 设定VideoView */
        vvStart = (MyVideoView) findViewById(R.id.vv_start);
        Uri uri = Uri.parse
                (
                        "android.resource://"+getPackageName()+"/"+ R.raw.startvideo
                );
        vvStart.setVideoURI(uri);
        vvStart.requestFocus();
    /* 开始播放影片 */
        vvStart.start();

    /* 影片播放完后会运行的OnCompletionListener */
        vvStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer arg0)
            {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }

    /*private PathView pathView;

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
    }*/
}

/**
 * 重写videoview使全屏
 */
    class MyVideoView extends VideoView {
    public static int WIDTH;
    public static int HEIGHT;
    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(WIDTH, widthMeasureSpec);

        int height = getDefaultSize(HEIGHT, heightMeasureSpec);

        setMeasuredDimension(width,height);

    }
}