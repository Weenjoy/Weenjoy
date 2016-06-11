package com.df.dianping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.df.Collect.Mycollection;
import com.df.DataBase.OperateTable;
import com.df.DataBase.SearchHistoryDataBase;
import com.df.GetDataFromNet.Getdata;
import com.df.GetDataFromNet.MyApplication;
import com.df.Indent.MyIndentActivity;
import com.df.Search.Search;
import com.df.Search.SearchHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;

import static com.df.dianping.R.id.main_search_btn;

public class MainActivity extends Activity {
    public static final String AppKey="2cdc69b8257c5b4f65cc5b241a5a3e73";

    public static List<Map<String, Object>> mlist;
    public static String CITYCODE;
    private GridView grid;
    private DisplayMetrics localDisplayMetrics;
    private View view;
    private static List<Map<String, String>> list;
    private static String Distract;
    private EditText search_content;
    private Button button;
    private ImageView delete;
    private String content;
    private LinearLayout locatedLayout;
    private OperateTable table;
    private SearchHistoryDataBase mDataBase;
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    CITYCODE = getcitycode(amapLocation.getCity());//城市ID
                    Distract = amapLocation.getDistrict();//省
                    mLocationClient.stopLocation();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    locatedLayout.setVisibility(View.GONE);
            }
        }
    };

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bmob.initialize(MainActivity.this, AppKey);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = this.getLayoutInflater().inflate(R.layout.main, null);
        setContentView(view);
        button = (Button) findViewById(main_search_btn);
        search_content = (EditText) findViewById(R.id.main_search);
        delete = (ImageView) findViewById(R.id.main_search_delete);
        locatedLayout = (LinearLayout) findViewById(R.id.main_located_layout);

        localDisplayMetrics = getResources().getDisplayMetrics();
        list = new ArrayList<>();
        grid = (GridView) view.findViewById(R.id.my_grid);
        ListAdapter adapter = new GridAdapter(this);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(mOnClickListener);

        mDataBase = new SearchHistoryDataBase(this);

        //解析数据线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MyApplication();
                list = Getdata.getcityfromjson(Getdata.cityrequest("http://apis.baidu.com/baidunuomi/openapi/cities", ""));
                initlocation();
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
        initsearchdata();
    }

    /**
     * 初始化搜索
     */
    private void initsearchdata() {
        search_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    content = search_content.getText().toString();
                    if (!content.equals("")) {
                        Intent intent = new Intent(MainActivity.this, Search.class);
                        intent.putExtra("keyword", content);
                        startActivity(intent);
                        table = new OperateTable(mDataBase.getWritableDatabase(), 3);
                        table.insertSearchHistory(content);
                    } else
                        Toast.makeText(MainActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(""))
                    delete.setVisibility(View.VISIBLE);
                else
                    delete.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_content.setText("");
                delete.setVisibility(View.GONE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = search_content.getText().toString();
                if (!content.equals("")) {
                    Intent intent = new Intent(MainActivity.this, Search.class);
                    intent.putExtra("keyword", content);
                    startActivity(intent);
                    table = new OperateTable(mDataBase.getWritableDatabase(), 3);
                    table.insertSearchHistory(content);
                } else
                    Toast.makeText(MainActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static List<Map<String, Object>> getshoptype() {
        return mlist;
    }

    private void initlocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //定位一次
        mLocationOption.setOnceLocation(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public static String getcitycode(String str) {
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = new HashMap<>();
                map = list.get(i);
                if (map.get("city_name").equals(str))
                    return map.get("city_id");
            }
        }
        return null;
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.main_enter, R.anim.main_exit);
                    break;
                case 1:
                    Intent intent1 = new Intent(MainActivity.this, SearchHistory.class);
                    intent1.putExtra("keyword", "");
                    startActivity(intent1);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    Intent intent5 = new Intent(MainActivity.this, MyIndentActivity.class);
                    startActivity(intent5);
                    break;
                case 6:
                    Intent intent6 = new Intent(MainActivity.this, Mycollection.class);
                    startActivity(intent6);
                    break;
                case 7:
                    Intent intent7 = new Intent(MainActivity.this, MainpersonalActivity.class);
                    intent7.putExtra("account", "");
                    intent7.putExtra("isLogin", false);
                    startActivity(intent7);
                    break;
                case 8:
                    break;
            }

        }
    };

    public static String GetDistract() {
        return Distract;
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public final int getCount() {
            return 9;
        }

        public final Object getItem(int paramInt) {
            return null;
        }

        public final long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            paramView = inflater.inflate(R.layout.activity_label_item, null);
            TextView text = (TextView) paramView.findViewById(R.id.activity_name);

            switch (paramInt) {
                case 0: {
                    text.setText("附近");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_local);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 1: {
                    text.setText("搜索");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_search);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 2: {
                    text.setText("签到");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_checkin);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 3: {
                    text.setText("优惠券");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_promo);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 4: {
                    text.setText("今日团购");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_tuan);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 5: {
                    text.setText("我的订单");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_rank);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 6: {
                    text.setText("我的收藏");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_history);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }

                case 7: {
                    text.setText("个人中心");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_myzone);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }
                case 8: {
                    text.setText("更多");
                    Drawable draw = getResources().getDrawable(R.drawable.home_button_more);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                    text.setCompoundDrawables(null, draw, null, null);
                    break;
                }
            }

            paramView.setMinimumHeight((int) (96.0F * localDisplayMetrics.density));
            paramView.setMinimumWidth(((-12 + localDisplayMetrics.widthPixels) / 3));

            return paramView;
        }
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }

}