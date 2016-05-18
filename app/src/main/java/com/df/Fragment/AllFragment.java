package com.df.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.df.DataBase.IndentDataBase;
import com.df.DataBase.OperateTable;
import com.df.adapter.ShopListAdapter;
import com.df.dianping.R;
import com.df.dianping.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 2016/5/17.
 */
public class AllFragment extends Fragment {
    private  View view;
    private ListView lvAll;
    private List<Map<String, Object>> datalist;
    private ShopListAdapter adapter;
    private LinearLayout loadinglayout;
    private ImageView ivAll;
    private IndentDataBase idb;
    private OperateTable table;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.setList(datalist);
                    loadinglayout.setVisibility(View.GONE);
                    if(datalist.size()==0)
                        ivAll.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_tab_all, container, false);
        lvAll=(ListView)view.findViewById(R.id.lv_all);
        ivAll=(ImageView)view.findViewById(R.id.iv_all);
        loadinglayout=(LinearLayout)view.findViewById(R.id.all_loadinglayout);
        idb=new IndentDataBase(view.getContext());
        table = new OperateTable(idb.getWritableDatabase(),2);
        datalist = new ArrayList<>();
        adapter = new ShopListAdapter(view.getContext(), datalist);
        lvAll.setAdapter(adapter);
        lvAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                intent.putExtra("id", datalist.get(i).get("shop_id").toString());
                intent.putExtra("url", datalist.get(i).get("deal_murl").toString());
                intent.putExtra("tiny_image", datalist.get(i).get("image_url").toString());
                intent.putExtra("shop_name", datalist.get(i).get("shop_name").toString());
                intent.putExtra("description", datalist.get(i).get("description").toString());
                intent.putExtra("current_price", datalist.get(i).get("current_price").toString());
                startActivity(intent);
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
        return view;
    }
}
