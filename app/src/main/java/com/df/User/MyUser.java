package com.df.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/25.
 */
public class MyUser extends BmobUser {
    private String nick;
    private String sex;
    private String age;
    private String constellation;
    private String profession;
    private String school;
    private String address;
    private String hometown;

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getConstellation() {
        return constellation;
    }

    public String getHometown() {
        return hometown;
    }

    public String getNick() {
        return nick;
    }

    public String getProfession() {
        return profession;
    }

    public String getSchool() {
        return school;
    }

    public String getSex() {
        return sex;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
