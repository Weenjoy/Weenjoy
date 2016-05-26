package com.df.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.df.User.MyUser;
import com.df.dianping.MainpersonalActivity;
import com.df.dianping.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by Administrator on 2016/5/25.
 */
public class LoginActivity extends BaseActivity {

    private TextView tv_register;
    private  TextView tv_fast_login;
    private TextView tv_return;

    private Button bt_login;

    private EditText et_accout;
    private EditText et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_accout=(EditText)findViewById(R.id.et_accout);
        et_password=(EditText)findViewById(R.id.et_password);

        tv_register=(TextView)findViewById(R.id.tv_register);
        tv_fast_login=(TextView)findViewById(R.id.tv_fast_login);
        tv_return=(TextView)findViewById(R.id.tv_return);

        Intent i=super.getIntent();
        et_accout.setText(i.getStringExtra("num"));
        et_password.setText(i.getStringExtra("pass"));


        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(it);
            }
        });
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                LoginActivity.this.startActivity(it);
            }
        });
        tv_fast_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, PhoneLoginActivity.class);
                LoginActivity.this.startActivity(it);
            }
        });

        bt_login=(Button)findViewById(R.id.bn_login);


        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.loginByAccount(LoginActivity.this, et_accout.getText().toString(), et_password.getText().toString(), new LogInListener<MyUser>() {

                    @Override
                    public void done(MyUser user, BmobException e) {
                        // TODO Auto-generated method stub15
                        if (user != null) {
                            showLog("用户登陆成功");
                            showToast("用户登录成功");
                            Intent intent=new Intent(LoginActivity.this, MainpersonalActivity.class);
                            String info=et_accout.getText().toString();
                            intent.putExtra("account",info);
                            intent.putExtra("isLogin", true);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

    }

}
