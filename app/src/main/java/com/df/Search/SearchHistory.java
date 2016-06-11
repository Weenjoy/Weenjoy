package com.df.Search;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.df.DataBase.OperateTable;
import com.df.DataBase.SearchHistoryDataBase;
import com.df.dianping.R;

import java.util.ArrayList;
import java.util.List;


public class SearchHistory extends Activity {

    private EditText editText;
    private TextView textView;
    private ImageView delete;
    private ListView listView;
    private SearchHistoryAdapter adapter;
    private List<String> dataList;
    private String content;
    private OperateTable table;
    private ImageButton allDelete;
    private SearchHistoryDataBase mDataBase;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchHistory.this, Search.class);
                intent.putExtra("keyword", dataList.get(i));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(SearchHistory.this).setTitle("确定要删除该条记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                table = new OperateTable(mDataBase.getWritableDatabase(), 3);
                                table.deleteSearchHistory(dataList.get(position));
                                dataList.remove(position);
                                adapter.setList(dataList);
                                if (dataList.size() == 0) {
                                    allDelete.setVisibility(View.GONE);
                                }
                            }
                        }).setNegativeButton("取消", null).show();
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String content = editText.getText().toString();
                    if (!content.equals("")) {
                        table = new OperateTable(mDataBase.getWritableDatabase(), 3);
                        table.insertSearchHistory(content);
                        dataList.add(content);
                        adapter.setList(dataList);
                        Intent intent = new Intent(SearchHistory.this, Search.class);
                        intent.putExtra("keyword", content);
                        startActivity(intent);
                    } else
                        Toast.makeText(SearchHistory.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
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

        allDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SearchHistory.this).setTitle("确定要清除所有记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dataList.clear();
                                adapter.setList(dataList);
                                table = new OperateTable(mDataBase.getWritableDatabase(), 3);
                                table.deleteAllSearchHistory();
                                allDelete.setVisibility(View.GONE);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    private void init() {
        allDelete = (ImageButton) findViewById(R.id.history_all_delete);
        editText = (EditText) findViewById(R.id.search_history_edittext);
        textView = (TextView) findViewById(R.id.search_history_text);
        delete = (ImageView) findViewById(R.id.search_history_image);
        listView = (ListView) findViewById(R.id.search_history_list);
        mDataBase = new SearchHistoryDataBase(SearchHistory.this);
        table = new OperateTable(mDataBase.getWritableDatabase(), 3);
        dataList = new ArrayList<>();
        dataList = table.getSearchHistory();
        if (dataList.size() == 0) {
            allDelete.setVisibility(View.GONE);
        }
        adapter = new SearchHistoryAdapter(this, dataList);
        listView.setAdapter(adapter);
    }


}
