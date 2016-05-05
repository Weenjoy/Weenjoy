package com.df.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.df.dianping.R;

public class Search_history extends Activity {

    private EditText editText;
    private TextView textView;
    private ImageView delete;
    private ListView list;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        init();
        initdata();
    }

    private void initdata() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                delete.setVisibility(View.GONE);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //搜索
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String content = editText.getText().toString();
                    if (!content.equals("")) {
                        Intent intent = new Intent(Search_history.this, Search.class);
                        intent.putExtra("keyword", content);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(Search_history.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                content = editText.getText().toString();
                if (!content.equals(""))
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
        editText = (EditText) findViewById(R.id.search_history_edittext);
        textView = (TextView) findViewById(R.id.search_history_text);
        delete = (ImageView) findViewById(R.id.search_history_image);
        list = (ListView) findViewById(R.id.search_history_list);
    }


}
