package com.df.Circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.df.User.MyUser;
import com.df.dianping.MainPersonalFragment;
import com.df.dianping.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UploadActivity extends Activity {

    private Button cancel;
    private Button subbmit;
    private EditText nick;
    private EditText sex;
    private EditText age;
    private EditText constellation;
    private EditText profession;
    private EditText school;
    private EditText address;
    private EditText hometown;
    private EditText mailbox;
    private String account;
    private Button signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        nick = (EditText) findViewById(R.id.username);
        sex = (EditText) findViewById(R.id.sex);
        age = (EditText) findViewById(R.id.age);
        constellation = (EditText) findViewById(R.id.constellation);
        profession = (EditText) findViewById(R.id.profession);
        school = (EditText) findViewById(R.id.school);
        address = (EditText) findViewById(R.id.address);
        hometown = (EditText) findViewById(R.id.hometown);
        mailbox = (EditText) findViewById(R.id.mailbox);


        subbmit = (Button) super.findViewById(R.id.subbmit);
        cancel = (Button) super.findViewById(R.id.cancel);
        signOut = (Button) findViewById(R.id.upload_sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(UploadActivity.this).setTitle("确定要退出当前账号?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BmobUser.logOut(UploadActivity.this);   //清除缓存用户对象
                                Intent intent = new Intent();
                                setResult(1, intent);
                                finish();
                            }
                        }).setNegativeButton("取消", null);

            }
        });


        Intent i = super.getIntent();
        String account = i.getStringExtra("account");


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, MainPersonalFragment.class);
                startActivity(intent);
            }
        });

        subbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyUser newUser = new MyUser();
                newUser.setEmail(mailbox.getText().toString());
                newUser.setAddress(address.getText().toString());
                newUser.setNick(nick.getText().toString());
                newUser.setAge(age.getText().toString());
                newUser.setHometown(hometown.getText().toString());
                newUser.setSchool(school.getText().toString());
                newUser.setSex(sex.getText().toString());
                newUser.setConstellation(constellation.getText().toString());


                BmobUser bmobUser = BmobUser.getCurrentUser(UploadActivity.this);
                newUser.update(UploadActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Toast.makeText(UploadActivity.this, "更新用户信息成功:", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadActivity.this, MainPersonalFragment.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(UploadActivity.this, "更新用户信息失败:" + msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", account);
        query.findObjects(UploadActivity.this, new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> object) {
                // TODO Auto-generated method stub

                nick.setText(object.get(0).getNick());
                sex.setText(object.get(0).getSex());
                age.setText(object.get(0).getAge());
                constellation.setText(object.get(0).getConstellation());
                profession.setText(object.get(0).getProfession());
                school.setText(object.get(0).getSchool());
                hometown.setText(object.get(0).getHometown());
                address.setText(object.get(0).getAddress());
                mailbox.setText(object.get(0).getEmail());
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(UploadActivity.this, "查询用户失败：" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
