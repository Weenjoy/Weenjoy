package com.df.Collect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.df.DataBase.DataBaseHelp;
import com.df.DataBase.OperateTable;
import com.df.adapter.CollectionAdapter;
import com.df.dianping.R;
import com.df.dianping.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Mycollection extends Activity implements CollectionAdapter.OnListSelectedChangeListener {

    private ListView list;
    private List<Map<String, Object>> dataList;
    private CollectionAdapter adapter;
    private boolean flag = false;
    private int num;
    private ImageView back;
    private DataBaseHelp help;
    private TextView delete;
    private OperateTable table;
    private LinearLayout loadingLayout;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter = new CollectionAdapter(Mycollection.this, dataList);
                    adapter.setOnListSelectedChangeListener(Mycollection.this);
                    list.setAdapter(adapter);
                    loadingLayout.setVisibility(View.GONE);
                    if (dataList.size() == 0)
                        Toast.makeText(Mycollection.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollection);
        init();
    }

    private void init() {
        num = 0;

        back = (ImageView) findViewById(R.id.collect_back);
        list = (ListView) findViewById(R.id.collect_list);
        delete = (TextView) findViewById(R.id.collect_delete);
        loadingLayout = (LinearLayout) findViewById(R.id.collect_loadinglayout);
        help = new DataBaseHelp(this);
        table = new OperateTable(help.getWritableDatabase(), 1);
        dataList = new ArrayList<>();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!flag) {
                    Intent intent = new Intent(Mycollection.this, WebViewActivity.class);
                    intent.putExtra("id", dataList.get(i).get("shop_id").toString());
                    intent.putExtra("url", dataList.get(i).get("deal_murl").toString());
                    intent.putExtra("tiny_image", dataList.get(i).get("image_url").toString());
                    intent.putExtra("shop_name", dataList.get(i).get("shop_name").toString());
                    intent.putExtra("description", dataList.get(i).get("description").toString());
                    intent.putExtra("current_price", dataList.get(i).get("current_price").toString());
                    startActivity(intent);
                } else {
                    boolean f = (boolean) dataList.get(i).get("selected");
                    dataList.get(i).put("selected", !f);
                    adapter.setList(dataList);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = dataList.size() - 1; i >= 0; i--) {
                    if ((boolean) dataList.get(i).get("selected")) {
                        table = new OperateTable(help.getWritableDatabase(), 1);
                        table.delete(dataList.get(i).get("shop_id").toString());
                        dataList.remove(i);
                    }
                }
                num = 0;
                delete.setVisibility(View.GONE);
                adapter.setList(dataList);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                dataList = table.find();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }


    @Override
    public void selectedNum(int n) {
        num = num + n;
        if (num > 0) {
            flag = true;
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            flag = false;
        }
    }
}
