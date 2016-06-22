package com.df.dianping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.df.GetDataFromNet.Getdata;
import com.df.Search.SearchHistory;
import com.df.adapter.GridViewAdapter;
import com.df.adapter.ShopListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus88 on 2016/6/11.
 */
public class MainFragment extends Fragment {
    private View view;
    private ListView listView;
    private List<Map<String, Object>> shopList, mshopList;
    private GridView mGridView;
    private List<CategoryInfo> mList;
    private GridViewAdapter adapter;
    private String shopStr, mshop;
    private ShopListAdapter shopAdapter;
    private String[] name = {"生活服务", "休闲娱乐", "美食", "网购", "酒店旅游", "上门服务", "演出赛事", "充值缴费", "汽车后服务", "其他"};
    private int[] imageId = {R.drawable.life, R.drawable.happy, R.drawable.food,
            R.drawable.shopping, R.drawable.hotel, R.drawable.beauty,
            R.drawable.sports, R.drawable.wedding, R.drawable.baby, R.drawable.other};
    private int[] catId = {316, 320, 326, 330, 377, 963, 970, 990, 1010, 323};
    private LinearLayout loadingLayout, loadLayout;

    private EditText searchText;

    private View footerView;
    public static String CITYCODE;
    private int pageCount;
    private static List<Map<String, String>> cityList;
    private static String Distract;
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    CITYCODE = getCityCode(amapLocation.getCity());//城市ID
                    Distract = amapLocation.getDistrict();//省
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
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
                    loadLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    loadingLayout.setVisibility(View.GONE);
                    shopAdapter = new ShopListAdapter(view.getContext(), shopList);
                    listView.setAdapter(shopAdapter);
                    break;
                case 3:
                    footerView.setVisibility(View.GONE);
                    shopAdapter.setList(shopList);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageCount = 2;
        if (view == null) {
            view = inflater.inflate(R.layout.main_fragment, null);

            footerView = View.inflate(view.getContext(), R.layout.shop_footer, null);
            footerView.setVisibility(View.GONE);
            searchText= (EditText) view.findViewById(R.id.main_fragment_search);

            CITYCODE = "";
            mshop = "";
            mshopList = new ArrayList<>();
            listView = (ListView) view.findViewById(R.id.main_fragment_listView);
            listView.addFooterView(footerView);

            mGridView = (GridView) view.findViewById(R.id.main_fragment_gridView);
            loadingLayout = (LinearLayout) view.findViewById(R.id.main_fragment_loadingLayout);
            loadLayout = (LinearLayout) view.findViewById(R.id.main_load_layout);
            mList = new ArrayList<>();
            for (int i = 0; i < name.length; i++) {
                CategoryInfo info = new CategoryInfo(name[i], imageId[i], catId[i]);
                mList.add(info);
            }
            adapter = new GridViewAdapter(view.getContext(), mList);
            mGridView.setAdapter(adapter);

            cityList = new ArrayList<>();
            shopList = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cityList = Getdata.getcityfromjson(Getdata.cityrequest("http://apis.baidu.com/baidunuomi/openapi/cities", ""));
                    initlocation();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (CITYCODE.equals("")) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    shopStr = Getdata.todayShopList("http://apis.baidu.com/baidunuomi/openapi/searchdeals",
                            "city_id=" + CITYCODE+"&page_size=5");
                    shopList = Getdata.getGoodsFromJson(shopStr);
                    Message message = Message.obtain();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }).start();


        }
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView, int i, long l) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                intent.putExtra("id", shopList.get(i).get("shop_id").toString());
                intent.putExtra("url", shopList.get(i).get("deal_murl").toString());
                intent.putExtra("tiny_image", shopList.get(i).get("image_url").toString());
                intent.putExtra("shop_name", shopList.get(i).get("shop_name").toString());
                intent.putExtra("description", shopList.get(i).get("description").toString());
                intent.putExtra("current_price", shopList.get(i).get("current_price").toString());
                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        footerView.setVisibility(View.VISIBLE);
                        loadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("NAME", mList.get(i).getName());
                intent.putExtra("ID", i);
                intent.putExtra("CATID", mList.get(i).getCatId() + "");
                startActivity(intent);
            }
        });

        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Intent intent=new Intent(view.getContext(), SearchHistory.class);
                    startActivity(intent);
                    searchText.clearFocus();
                }
            }
        });

    }

    private void loadMore() {
        mshop = "";
        mshopList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mshop = Getdata.todayShopList("http://apis.baidu.com/baidunuomi/openapi/searchdeals",
                        "city_id=" + CITYCODE + "&page=" + pageCount + "&page_size=5");
                if (!mshop.equals("")) {
                    mshopList = Getdata.getGoodsFromJson(mshop);
                    for (int i = 0; i < mshopList.size(); i++) {
                        shopList.add(mshopList.get(i));
                    }
                    pageCount++;
                    Message mMessage = Message.obtain();
                    mMessage.what = 3;
                    handler.sendMessage(mMessage);
                } else {
                    Toast.makeText(view.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    private void initlocation() {
        mLocationClient = new AMapLocationClient(view.getContext());
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

    public static String getCityCode(String str) {
        if (!cityList.isEmpty()) {
            for (int i = 0; i < cityList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                map = cityList.get(i);
                if (map.get("city_name").equals(str))
                    return map.get("city_id");
            }
        }
        return null;
    }

    public static String GetDistract() {
        return Distract;
    }
}
