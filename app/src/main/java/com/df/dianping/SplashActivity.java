package com.df.dianping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.eftimoff.androipathview.PathView;

public class SplashActivity extends Activity {
    private MyVideoView vvStart;
    private Button btJump;
    private int time=5;
    //主线程
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if(msg.what==0){
                btJump.setText("跳过（"+time+"s)");
                time--;
                if(time==0){
                    btJump.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this,MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    });
                }
            }
        }
    };
    //倒数跳过
    private Thread thJump=new Thread(new Runnable() {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()&&time>=0) {
                mainHandler.obtainMessage(0).sendToTarget();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    });
        public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    /* 加载start.xml Layout */
        setContentView(R.layout.activity_splash);
    /* 设定VideoView */
        vvStart = (MyVideoView) findViewById(R.id.vv_start);
        btJump=(Button)findViewById(R.id.bt_jump);
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

        thJump.start();
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