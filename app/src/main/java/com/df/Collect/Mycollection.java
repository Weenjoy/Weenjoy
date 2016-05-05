package com.df.Collect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.df.DataBase.DataBaseHelp;
import com.df.DataBase.OperateTable;
import com.df.adapter.ShopListAdapter;
import com.df.dianping.R;
import com.df.dianping.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Mycollection extends Activity {

    private ListView list;
    private List<Map<String, Object>> datalist;
    private ShopListAdapter adapter;
    private ImageView back;
    // private TextView edit;
    private DataBaseHelp help;
    private OperateTable table;
    private LinearLayout loadinglayout;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.setList(datalist);
                    loadinglayout.setVisibility(View.GONE);
                    if(datalist.size()==0)
                        Toast.makeText(Mycollection.this,"暂无数据",Toast.LENGTH_SHORT).show();
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
        back = (ImageView) findViewById(R.id.collect_back);
        //     edit = (TextView) findViewById(R.id.collect_edit);
        list = (ListView) findViewById(R.id.collect_list);
        loadinglayout = (LinearLayout) findViewById(R.id.collect_loadinglayout);
        help = new DataBaseHelp(this);
        table = new OperateTable(help.getWritableDatabase());
        datalist = new ArrayList<>();
        adapter = new ShopListAdapter(this, datalist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Mycollection.this, WebViewActivity.class);
                intent.putExtra("id", datalist.get(i).get("shop_id").toString());
                intent.putExtra("url", datalist.get(i).get("deal_murl").toString());
                intent.putExtra("tiny_image", datalist.get(i).get("image_url").toString());
                intent.putExtra("shop_name", datalist.get(i).get("shop_name").toString());
                intent.putExtra("description", datalist.get(i).get("description").toString());
                intent.putExtra("current_price", datalist.get(i).get("current_price").toString());
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(view.getContext()).setTitle("删除").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = datalist.get(position).get("shop_id").toString();
                                Log.i("info",id+"");
                                datalist.remove(position);
                                table = new OperateTable(help.getWritableDatabase());
                                table.delete(id);
                                adapter.setList(datalist);
                            }
                        }).setNegativeButton("取消", null).show();
                return false;
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
                datalist = table.find();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }


}
