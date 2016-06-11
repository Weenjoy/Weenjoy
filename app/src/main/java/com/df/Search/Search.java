package com.df.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.df.DataBase.OperateTable;
import com.df.DataBase.SearchHistoryDataBase;
import com.df.GetDataFromNet.Getdata;
import com.df.adapter.ShopListAdapter;
import com.df.dianping.MainActivity;
import com.df.dianping.R;
import com.df.dianping.WebViewActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search extends Activity {

    private ListView list;
    private ImageView back, delete;
    private Button search;
    private EditText text;
    private String content, shop;
    private List<Map<String, Object>> datalist;
    private ShopListAdapter adapter;
    private LinearLayout loadinglayout;
    private OperateTable table;
    private SearchHistoryDataBase mDataBase;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    loadinglayout.setVisibility(View.GONE);
                    if (datalist.size() == 0)
                        Toast.makeText(Search.this, "暂无搜索结果，请更换关键词", Toast.LENGTH_SHORT).show();
                    else
                        adapter.setList(datalist);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        initdata();
    }

    private void initdata() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Search.this, WebViewActivity.class);
                intent.putExtra("id", datalist.get(i).get("shop_id").toString());
                intent.putExtra("url", datalist.get(i).get("deal_murl").toString());
                intent.putExtra("tiny_image", datalist.get(i).get("image_url").toString());
                intent.putExtra("shop_name", datalist.get(i).get("shop_name").toString());
                intent.putExtra("description", datalist.get(i).get("description").toString());
                intent.putExtra("current_price", datalist.get(i).get("current_price").toString());
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("");
                delete.setVisibility(View.GONE);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedata();
            }
        });
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH)
                    updatedata();
                return true;
            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(""))
                    delete.setVisibility(View.VISIBLE);
                else
                    delete.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void init() {
        mDataBase = new SearchHistoryDataBase(this);
        list = (ListView) findViewById(R.id.search_list);
        back = (ImageView) findViewById(R.id.search_list_back);
        delete = (ImageView) findViewById(R.id.search_list_delete);
        search = (Button) findViewById(R.id.search_list_btn);
        text = (EditText) findViewById(R.id.search_list_text);
        loadinglayout = (LinearLayout) findViewById(R.id.search_loadinglayout);
        content = getIntent().getStringExtra("keyword");
        delete.setVisibility(View.VISIBLE);
        text.setText(content);
        datalist = new ArrayList<>();
        adapter = new ShopListAdapter(this, datalist);
        list.setAdapter(adapter);
        if (!content.equals(""))
            refresh();
        else {
            loadinglayout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
    }

    private void updatedata() {
        String c = text.getText().toString();
        if (c.equals(""))
            Toast.makeText(Search.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
        else if (!c.equals(content)) {
            content = c;
            loadinglayout.setVisibility(View.VISIBLE);
            table = new OperateTable(mDataBase.getWritableDatabase(), 3);
            table.insertSearchHistory(c);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String strUTF8 = null;
                    try {
                        strUTF8 = URLEncoder.encode(content, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops",
                            "city_id=" + MainActivity.CITYCODE + "&keyword=" + strUTF8 + "&page=1&page_size=10&deals_per_shop=10");
                    datalist = Getdata.getshoplistformjson(shop);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }

    /**
     * 搜索函数
     */
    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strUTF8 = null;
                try {
                    strUTF8 = URLEncoder.encode(content, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                shop = Getdata.shoplistrequest("http://apis.baidu.com/baidunuomi/openapi/searchshops",
                        "city_id=" + MainActivity.CITYCODE + "&keyword=" + strUTF8 + "&page=1&page_size=10&deals_per_shop=10");
                datalist = Getdata.getshoplistformjson(shop);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }


}
