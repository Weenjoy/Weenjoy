package com.df.dianping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.df.DataBase.DataBaseHelp;
import com.df.DataBase.OperateTable;


public class WebViewActivity extends Activity {

    private WebView webView;
    private String url, id, img, name, price, description;
    private ImageView collect, back;
    private DataBaseHelp help;
    private OperateTable table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        help = new DataBaseHelp(this);
        collect = (ImageView) findViewById(R.id.webview_collect);
        back = (ImageView) findViewById(R.id.webview_back);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shshouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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
        webView.loadUrl(url);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = getIntent().getStringExtra("shop_name");
                img = getIntent().getStringExtra("tiny_image");
                price = getIntent().getStringExtra("current_price");
                description = getIntent().getStringExtra("description");
                id = getIntent().getStringExtra("id");
                table = new OperateTable(help.getWritableDatabase());
                if (table.queryid(id))
                    table.insert(id, name, img, description, price, url);
                else
                    Toast.makeText(WebViewActivity.this, "你已收藏该店铺", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
