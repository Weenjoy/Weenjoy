package com.df.DataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus88 on 2016/5/3.
 */
public class OperateTable {
    private static final String TABLENAME = "collection";
    private SQLiteDatabase db;

    public OperateTable(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(String id, String name, String img, String description, String price, String url) {
        String sql = "INSERT INTO " + TABLENAME + " (id,name,image,description,price,url)VALUES ('" + id + "','" +
                name + "','" + img + "','" +
                description + "','" + price + "','" + url + "')";
        db.execSQL(sql);
        db.close();
    }

    public void delete(String id) {
        String sql = "DELETE FROM " + TABLENAME + " WHERE id=" + id;
        db.execSQL(sql);
        db.close();
    }

    public boolean queryid(String id) {
        String sql = "SELECT id,name,image,description,price,url FROM " + TABLENAME + " WHERE id= " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0)
            return false;
        else
            return true;
    }

    public List<Map<String, Object>> find() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT id,name,image,description,price,url FROM collection";
        Cursor cursor = db.rawQuery(sql, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put("shop_id", cursor.getString(0));
            map.put("shop_name", cursor.getString(1));
            map.put("tiny_image", getBitmap(cursor.getString(2)));
            map.put("image_url",cursor.getString(2));
            map.put("description", cursor.getString(3));
            map.put("current_price", cursor.getString(4));
            map.put("deal_murl", cursor.getString(5));
            list.add(map);
        }
        db.close();
        return list;
    }

    private static Bitmap getBitmap(String path) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
