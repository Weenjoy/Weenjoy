package com.df.GetDataFromNet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus88 on 2016/4/6.
 */
public class Getdata {

    private String city_httpUrl = "http://apis.baidu.com/baidunuomi/openapi/cities";
    private String city_httpArg = "";

    private String area_httpUrl = "http://apis.baidu.com/baidunuomi/openapi/districts";
    private String area_httpArg = "city_id=100010000";

    private String areahttpUrl = "http://apis.baidu.com/baidunuomi/openapi/shopinfo";
    private String areahttpArg = "shop_id=1745896";

    private String type_httpUrl = "http://apis.baidu.com/baidunuomi/openapi/categories";
    private String type_httpArg = "";

    private String httpUrl = "http://apis.baidu.com/baidunuomi/openapi/searchshops";
    private String httpArg = "city_id=100010000&cat_ids=326&subcat_ids=962%2C994&district_ids=394%2C395&bizarea_ids=1322%2C1328&location=116.418993%2C39.915597&radius=3000&page=1&page_size=10&deals_per_shop=10";

    /**
     * 获取城市列表
     *
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String cityrequest(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "30d9544095b66da7c2db36776ef56f0d");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //解析城市数据
    public static List<Map<String, String>> getcityfromjson(String str) {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(str).getJSONArray("cities");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                if (!object.getString("short_name").equals("其他")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("city_id", object.getInt("city_id") + "");
                    map.put("short_name", object.getString("short_name"));
                    map.put("city_name", object.getString("city_name"));
                    list.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取地区信息
     *
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String arearequest(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "30d9544095b66da7c2db36776ef56f0d");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析地区数据
     */
    public static List<Map<String, Object>> getareaformjson(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(str).getJSONArray("districts");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                if (!object.getString("district_name").equals("其他")) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("district_id", object.getInt("district_id"));
                    map.put("district_name", object.getString("district_name"));
                    map.put("selected", false);
                    JSONArray array1 = object.getJSONArray("biz_areas");
                    if (array1.length() > 0) {
                        List<Map<String, Object>> list1 = new ArrayList<>();
                        for (int j = 0; j < array1.length(); j++) {
                            JSONObject object1 = (JSONObject) array1.get(j);
                            if (!object1.getString("biz_area_name").equals("其他")) {
                                Map<String, Object> map1 = new HashMap<>();
                                map1.put("selected", false);
                                map1.put("biz_area_name", object1.getString("biz_area_name"));
                                map1.put("biz_area_id", object1.getInt("biz_area_id") + "");
                                list1.add(map1);
                            }
                        }
                        map.put("list", list1);
                    } else map.put("list", "");
                    list.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取商户信息
     *
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String shopmsgrequest(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "30d9544095b66da7c2db36776ef56f0d");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析商户信息
     */
    public static Map<String, String> getshopfromjson(String str) {
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject object = new JSONObject(str).getJSONObject("shop");
            map.put("shop_name", object.getString("shop_name"));
            map.put("address", object.getString("address"));
            map.put("phone", object.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 返回店铺分类
     *
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String shoptyperequest(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "30d9544095b66da7c2db36776ef56f0d");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析店铺分类
     */

    public static List<Map<String, Object>> getshoptypefromjson(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(str).getJSONArray("categories");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("cat_name", object.getString("cat_name"));
                map.put("cat_id", object.getInt("cat_id"));
                JSONArray array1 = object.getJSONArray("subcategories");
                if (array1.length() > 0) {
                    List<Map<String, String>> list1 = new ArrayList<>();
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject object1 = (JSONObject) array1.get(j);
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("subcat_name", object1.getString("subcat_name"));
                        map1.put("subcat_id", object1.getInt("subcat_id") + "");
                        list1.add(map1);
                    }
                    map.put("list", list1);
                } else map.put("list", "");
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 返回商户列表
     *
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    public static String shoplistrequest(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "30d9544095b66da7c2db36776ef56f0d");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析商户列表
     */
    public static List<Map<String, Object>> getshoplistformjson(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(str).getJSONObject("data");
            JSONArray array = object.getJSONArray("shops");
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject object1 = (JSONObject) array.get(i);
                map.put("shop_id", object1.getInt("shop_id"));
                map.put("shop_name", object1.getString("shop_name"));
                JSONObject detail = (JSONObject) object1.getJSONArray("deals").get(0);
                try {
                    map.put("tiny_image", getBitmap(detail.getString("image")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                map.put("image_url", detail.getString("image"));
                map.put("description", detail.getString("description"));
                map.put("current_price", detail.getInt("current_price"));
                map.put("deal_murl", detail.getString("deal_murl"));
                list.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String todayShopList(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "30d9544095b66da7c2db36776ef56f0d");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, Object>> getGoodsFromJson(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(str).getJSONObject("data");
            JSONArray array = object.getJSONArray("deals");
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject object1 = (JSONObject) array.get(i);
                map.put("shop_id", object1.getString("deal_id"));
                map.put("shop_name", object1.getString("title"));
                map.put("description", object1.getString("description"));
                map.put("current_price", object1.getString("promotion_price"));
                map.put("deal_murl", object1.getString("deal_murl"));
                map.put("image_url", object1.getString("image"));
                try {
                    map.put("tiny_image", getBitmap(object1.getString("image")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 从url返回图像
     */
    private static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;

    }
}
