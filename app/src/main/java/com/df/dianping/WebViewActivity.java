package com.df.dianping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.df.DataBase.DataBaseHelp;
import com.df.DataBase.IndentDataBase;
import com.df.DataBase.OperateTable;


public class WebViewActivity extends Activity {

    private WebView webView;
    private String url, id, img, name, price, description;
    private ImageView collect, back;
    private DataBaseHelp help;
    private IndentDataBase idb;
    private OperateTable table;
    private Button Buy;
    private RelativeLayout rlBuy;
    private LinearLayout loadinglayout;
    private boolean isCollected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        name = getIntent().getStringExtra("shop_name");


        collect = (ImageView) findViewById(R.id.webview_collect);
        back = (ImageView) findViewById(R.id.webview_back);
        webView = (WebView) findViewById(R.id.webview);
        Buy = (Button) findViewById(R.id.btn_buy);
        rlBuy = (RelativeLayout) findViewById(R.id.rl_buy);
        loadinglayout = (LinearLayout) findViewById(R.id.webview_loadinglayout);

        help = new DataBaseHelp(this);
        idb = new IndentDataBase(this);
        table = new OperateTable(help.getWritableDatabase(), 1);
        if (table.queryName(name))
            collect.setImageResource(R.drawable.collect_before);
        else
            collect.setImageResource(R.drawable.collect_after);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shshouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //开始
                rlBuy.setVisibility(View.VISIBLE);
                loadinglayout.setVisibility(View.GONE);
                super.onPageFinished(view, url);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        url = getIntent().getStringExtra("url");
        //Log.i("url",url+" g");
        webView.loadUrl(url);

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = getIntent().getStringExtra("id");
                table = new OperateTable(help.getWritableDatabase(), 1);
                if (isCollected) {
                    isCollected = false;
                    collect.setImageResource(R.drawable.collect_before);
                    table.delete(id);
                } else {
                    isCollected = true;
                    img = getIntent().getStringExtra("tiny_image");
                    price = getIntent().getStringExtra("current_price");
                    description = getIntent().getStringExtra("description");
                    table.insert(id, name, img, description, price, url);
                    collect.setImageResource(R.drawable.collect_after);
                }

            }
        });

        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = getIntent().getStringExtra("shop_name");
                img = getIntent().getStringExtra("tiny_image");
                price = getIntent().getStringExtra("current_price");
                description = getIntent().getStringExtra("description");
                id = getIntent().getStringExtra("id");
                table = new OperateTable(idb.getWritableDatabase(), 2);
                if (table.queryid(id)) {
                    table = new OperateTable(idb.getWritableDatabase(), 2);
                    table.insert(id, name, img, description, price, url, 0);
                    Toast.makeText(WebViewActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(WebViewActivity.this, "你已下单", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
