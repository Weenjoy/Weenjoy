package com.df.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.df.User.MyUser;
import com.df.dianping.MainPersonalFragment;
import com.df.dianping.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by Administrator on 2016/5/25.
 */
public class PhoneLoginActivity extends BaseActivity {
    protected Button but_sure;
    private Button but_send;
    protected EditText et_num;
    protected EditText et_check;

    private TimeCount time;

    private ImageView back;
    private TextView passWordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_base);

       // back = (ImageView) findViewById(R.id.phone_login_in_back);
        passWordLogin = (TextView) findViewById(R.id.phone_login_in_to_password);

        et_check = (EditText) findViewById(R.id.et_check);
        et_num = (EditText) findViewById(R.id.et_phone_num);


        but_send = (Button) findViewById(R.id.bn_send);
        but_sure = (Button) findViewById(R.id.bn_sure);

        time = new TimeCount(60000, 1000);//构造CountDownTimer对象

        but_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_check.getText().toString().isEmpty()) {
                    showToast("请输入验证码");
                    return;
                }
                BmobUser.loginByAccount(PhoneLoginActivity.this, et_num.getText().toString(), et_check.getText().toString(), new LogInListener<MyUser>() {

                    @Override
                    public void done(MyUser user, BmobException e) {
                        // TODO Auto-generated method stub
                        if (user != null) {
                            Log.i("smile", "用户登陆成功");
                            Intent intent = new Intent(PhoneLoginActivity.this, MainPersonalFragment.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        but_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_num.getText().toString().isEmpty()) {
                    showToast("请输入手机号");
                    return;
                }
                BmobSMS.requestSMSCode(PhoneLoginActivity.this, et_num.getText().toString(), "weenjoy", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        // TODO Auto-generated method stub
                        if (ex == null) {//验证码发送成功
                            Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
                            time.start();//开始计时
                        }
                    }
                });
            }
        });


//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        passWordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            but_send.setText("重新验证");
            but_send.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            but_send.setClickable(false);
            but_send.setText(millisUntilFinished / 1000 + "秒");
        }
    }

}
