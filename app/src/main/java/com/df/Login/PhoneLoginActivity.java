package com.df.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by Administrator on 2016/5/25.
 */
public class PhoneLoginActivity extends CheckBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        but_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_check.getText().toString().isEmpty()){
                    showToast("请输入验证码");
                    return;
                }
                BmobUser.loginByAccount(PhoneLoginActivity.this, et_num.getText().toString(), et_check.getText().toString(), new LogInListener<BmobUser>() {

                    @Override
                    public void done(BmobUser user, BmobException e) {
                        // TODO Auto-generated method stub`
                        if (user != null) {
                            Log.i("smile", "用户登陆成功");
                        }
                    }
                });
            }
        });
    }
}
