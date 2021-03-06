package com.df.Login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.df.User.MyUser;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/5/25.
 */
public class RegisterActivity extends ResetPasswordActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void OnSure_ClickListene() {
        but_sure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (et_re_password.getText().toString().isEmpty() || et_agian_password.getText().toString().isEmpty())
                    return;
                if (!et_re_password.getText().toString().equals(et_agian_password.getText().toString())) {
                    showToast("密码不一致");
                    return;
                }
                MyUser user = new MyUser();
                user.setMobilePhoneNumber(et_num.getText().toString());//设置手机号码（必填）

                user.setPassword(et_re_password.getText().toString());                  //设置用户密码

                user.signOrLogin(RegisterActivity.this, et_check.getText().toString(), new SaveListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        toast("注册成功");
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        String info = et_num.getText().toString();
                        intent.putExtra("account", info);
                        String info1 = et_re_password.getText().toString();
                        intent.putExtra("pass", info1);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        toast("错误码：" + code + ",错误原因：" + msg);
                    }
                });
            }
        });
    }
}
