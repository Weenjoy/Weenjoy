package com.df.dianping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.df.GetDataFromNet.Getdata;
import com.df.adapter.AreaAdapter;
import com.df.adapter.CateAdapter;
import com.df.adapter.ShopListAdapter;
import com.df.adapter.SortAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultActivity extends Activity implements OnClickListener {
    private ListView list;
    private AreaAdapter areaAdapter = null;
    private CateAdapter cateAdapter = null;
    private SortAdapter sortAdapter = null;
    private int ID;
    private List<Map<String, Object>> mlist;
    private List<Map<String, Object>> arealist;
    private List<Map<String, Object>> typelist, shoplist;
    private Button btnArea;
    private Button btnType;
    private ImageButton btnSort;
    private String typename, shop;
    private int selected, before, aselected; //底层选中的下标
    public static boolean flag; //底层第一个是否选中
    private ShopListAdapter shoplistadapter;
    private RelativeLayout layout;
    private String DISTRACTID, SUBCATID, CATID, bizarea_id;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //刷新中
                case 1:
                    layout.setVisibility(View.VISIBLE);
                    break;
                //第一次刷新完成
                case 2:
                    layout.setVisibility(View.GONE);
                    if (shoplist.size() == 0)
                        Toast.makeText(ResultActivity.this, "找不到相关服务的店铺", Toast.LENGTH_SHORT).show();
                    else
                        list.setAdapter(shoplistadapter); //在主线程更新数据
                    setButton(true);
                    break;
                //改变地点、种类刷新
                case 3:
                    layout.setVisibility(View.GONE);
                    setButton(true);
                    shoplistadapter.setList(shoplist);
                    break;

            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.searchresult);
        DISTRACTID = "";
        SUBCATID = "";
        bizarea_id = "";
        list = (ListView) findViewById(R.id.resultlist);

        btnArea = (Button) findViewById(R.id.id_area);
        btnArea.setOnClickListener(this);

        btnType = (Button) findViewById(R.id.id_type);
        btnType.setOnClickListener(this);

        btnSort = (ImageButton) findViewById(R.id.id_sort);
        btnSort.setOnClickListener(this);

        layout = (RelativeLayout) findViewById(R.id.refreshlayout);
        ID = getIntent().getIntExtra("ID", 0);
        typename = getIntent().getStringExtra("NAME");
        CATID = getIntent().getStringExtra("CATID");
        btnType.setText(typename);


        new Thread(new Runnable() {
            @Override
            public void run() {
                setButton(false);
                //设置地点为所在地区
                //开线程加载数据
                btnArea.setText(MainActivity.GetDistract());
                arealist = Getdata.getareaformjson(Getdata.arearequest("http://apis.baidu.com/baidunuomi/openapi/districts", "city_id=" + MainActivity.CITYCODE));
                areaAdapter = new AreaAdapter(ResultActivity.this, arealist);

                for (int i = 0; i < arealist.size(); i++) {
                    if (arealist.get(i).get("district_name").equals(MainActivity.GetDistract())) {
                        DISTRACTID = arealist.get(i).get("district_id").toString();
                        before = i + 1;
                        flag = true;
                        areaAdapter.setTypeIndex(i + 1);        //设置选中下标
                    }
                }

                String str = Getdata.shoptyperequest("http://apis.baidu.com/baidunuomi/openapi/categories", "");
                mlist = Getdata.getshoptypefromjson(str);
                typelist = getList((List<Map<String, Object>>) mlist.get(ID - 1).get("list"));
                cateAdapter = new CateAdapter(ResultActivity.this, typelist);


                shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&distract_ids=" + DISTRACTID + "&radius=3000&page=1&page_size=10&deals_per_shop=10");
                shoplist = Getdata.getshoplistformjson(shop);
                shoplistadapter = new ShopListAdapter(ResultActivity.this, shoplist);

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();

        selected = 0;
        aselected = -1;

        mlist = new ArrayList<>();
        arealist = new ArrayList<>();
        typelist = new ArrayList<>();
        shoplist = new ArrayList<>();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ResultActivity.this, WebViewActivity.class);
                intent.putExtra("id", shoplist.get(i).get("shop_id").toString());
                intent.putExtra("url", shoplist.get(i).get("deal_murl").toString());
                intent.putExtra("tiny_image", shoplist.get(i).get("image_url").toString());
                intent.putExtra("shop_name", shoplist.get(i).get("shop_name").toString());
                intent.putExtra("description", shoplist.get(i).get("description").toString());
                intent.putExtra("current_price", shoplist.get(i).get("current_price").toString());
                startActivity(intent);
            }
        });

    }


    /**
     * 各个点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_area: {
                showDialogPopup(R.id.id_area);
                break;
            }
            case R.id.id_type: {
                showDialogPopup(R.id.id_type);
                break;
            }
            case R.id.id_sort: {
                showDialogPopup(R.id.id_sort);
                break;
            }
        }

    }

    /**
     * 弹出选择框
     *
     * @param viewId
     */
    protected void showDialogPopup(int viewId) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);

        switch (viewId) {
            case R.id.id_area: {
                localBuilder.setAdapter(areaAdapter, new areaPopupListener(areaAdapter));
                break;
            }
            case R.id.id_type: {
                localBuilder.setAdapter(cateAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        if (which != selected) {
                            btnType.setText(typelist.get(which).get("subcat_name").toString());
                            typelist.get(which).put("checked", true);
                            typelist.get(selected).put("checked", false);
                            selected = which;
                            cateAdapter.notifyDataSetChanged();
                            layout.setVisibility(View.VISIBLE);
                            setButton(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    typechange(which);
                                }
                            }).start();
                        }
                    }
                });
                break;
            }

            case R.id.id_sort: {
                if (sortAdapter == null) {
                    sortAdapter = new SortAdapter(this);
                }
                localBuilder.setAdapter(sortAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            }

        }

        AlertDialog localAlertDialog = localBuilder.create();
        localAlertDialog.show();
    }

    /**
     * type change
     */
    private void typechange(int which) {
        if (which == 0) {
            SUBCATID = "";
            shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&district_ids=" + DISTRACTID + "&bizarea_ids=" + bizarea_id + "&radius=3000&page=1&page_size=20&deals_per_shop=10");
        } else {
            SUBCATID = typelist.get(which).get("subcat_id").toString();
            shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&subcat_ids=" + SUBCATID + "&district_ids=" + DISTRACTID + "&bizarea_ids=" + bizarea_id + "&radius=3000&page=1&page_size=20&deals_per_shop=10");
        }
        shoplist = Getdata.getshoplistformjson(shop);
        Message message = new Message();
        message.what = 3;
        handler.sendMessage(message);
    }

    /**
     * 返回cateadapter的数据列表
     *
     * @return
     */
    public List<Map<String, Object>> getList(List<Map<String, Object>> list) {
        List<Map<String, Object>> mlist = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("subcat_name", typename);
        map1.put("subcat_id", CATID);
        map1.put("checked", true);
        mlist.add(map1);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("subcat_name", list.get(i).get("subcat_name"));
            map.put("subcat_id", list.get(i).get("subcat_id"));
            map.put("checked", false);
            mlist.add(map);
        }
        return mlist;
    }

    class areaPopupListener implements DialogInterface.OnClickListener {
        AreaAdapter mAdapter;

        public areaPopupListener(ListAdapter adapter) {
            mAdapter = (AreaAdapter) adapter;
        }

        @Override
        public void onClick(DialogInterface dialog, final int which) {
            layout.setVisibility(View.VISIBLE);
            setButton(false);
            if (mAdapter.isTopLevel()) {              //顶层
                before = which;
                flag = true;
                mAdapter.setTypeIndex(which);        //设置选中下标
                final String strSelect = mAdapter.getSelect();
                btnArea.setText(strSelect);
                DISTRACTID = arealist.get(which).get("district_id").toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        areachange(which, 1);
                    }
                }).start();
            } else {                //不是顶层
                if (which == 0) {
                    mAdapter.setTypeIndex(which);
                    if (aselected != -1) {
                        List<Map<String, Object>> alist = (List<Map<String, Object>>) arealist.get(before - 1).get("list");
                        alist.get(aselected).put("selected", false);
                    }
                    btnArea.setText(mAdapter.getSelect());
                } else {
                    btnArea.setText(mAdapter.getData().get(before).get(which));
                    List<Map<String, Object>> alist = (List<Map<String, Object>>) arealist.get(before - 1).get("list");
                    if (which == 1) {
                        flag = true;
                        if (aselected != -1)
                            alist.get(aselected).put("selected", false);
                        aselected = -1;
                    } else {
                        flag = false;
                        alist.get(which - 2).put("selected", true);
                        if (aselected != -1)
                            alist.get(aselected).put("selected", false);
                        aselected = which - 2;
                    }

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        areachange(which, 0);
                    }
                }).start();
            }
            mAdapter.setlist(arealist);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void areachange(int which, int istop) {
        if (istop == 1) {
            //顶层
            if (which == 0) {
                DISTRACTID = "";
                shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&subcat_ids=" + SUBCATID + "&radius=3000&page=1&page_size=10&deals_per_shop=10");
            } else {
                DISTRACTID = arealist.get(which - 1).get("district_id").toString();
                shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&subcat_ids=" + SUBCATID + "&district_ids=" + DISTRACTID + "&radius=3000&page=1&page_size=10&deals_per_shop=10");
            }
        } else {
            //底层
            if (which == 0) {
                DISTRACTID = "";
                bizarea_id = "";
                shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&subcat_ids=" + SUBCATID + "&radius=3000&page=1&page_size=10&deals_per_shop=10");
            } else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) arealist.get(before - 1).get("list");
                bizarea_id = list.get(which - 2).get("biz_area_id").toString();
                shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops", "city_id=" + MainActivity.CITYCODE + "&cat_ids=" + CATID + "&subcat_ids=" + SUBCATID + "&district_ids=" + DISTRACTID + "&bizarea_ids=" + bizarea_id + "&radius=3000&page=1&page_size=10&deals_per_shop=10");
            }
        }
        shoplist = Getdata.getshoplistformjson(shop);
        Message message = new Message();
        message.what = 3;
        handler.sendMessage(message);
    }

    private void setButton(Boolean t) {
        btnArea.setEnabled(t);
        btnType.setEnabled(t);
        btnSort.setEnabled(t);
    }
}