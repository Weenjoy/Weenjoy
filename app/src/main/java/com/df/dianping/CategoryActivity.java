package com.df.dianping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.df.Search.Search;

import java.util.List;
import java.util.Map;

public class CategoryActivity extends Activity {

    private List<Map<String, Object>> mData;
    private View view;
    private EditText text;
    private ImageView delete;
    private Button btn;
    private String content;

    Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what == KeyEvent.KEYCODE_BACK) {
                finish();
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        view = this.getLayoutInflater().inflate(R.layout.category, null);

        setContentView(view);

        mData = CategoryData.getData();

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout header = (LinearLayout) inflater.inflate(R.layout.categoryheader, null);//搜素栏
        header.setBackgroundColor(0XC0C0C0);
        text = (EditText) header.findViewById(R.id.search_text);
        delete = (ImageView) header.findViewById(R.id.search_delete);
        btn = (Button) header.findViewById(R.id.search_back);
        ListView list = (ListView) findViewById(R.id.categorylist);
        list.addHeaderView(header);
        list.setOnItemClickListener(mOnClickListener);
        ListAdapter adapter = new MyAdapter(this);
        list.setAdapter(adapter);
        init();

    }

    private void init() {
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
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("");
                delete.setVisibility(View.GONE);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = text.getText().toString();
                if (!content.equals("")) {
                    Intent intent = new Intent(CategoryActivity.this, Search.class);
                    intent.putExtra("keyword", content);
                    startActivity(intent);
                }
            }
        });
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent intent = new Intent();
            intent.setClass(CategoryActivity.this, ResultActivity.class);
            intent.putExtra("NAME", mData.get(position - 1).get("title").toString());
            intent.putExtra("ID", position);
            intent.putExtra("CATID", mData.get(position - 1).get("id").toString());
            startActivity(intent);
        }
    };


    public final class ViewHolder {
        public ImageView img;
        public TextView title;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.categoryitem, null);
                convertView.setMinimumHeight(100);
                holder.img = (ImageView) convertView.findViewById(R.id.category_icon);
                holder.title = (TextView) convertView.findViewById(R.id.category_name);

                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
            holder.title.setText((String) mData.get(position).get("title"));

            return convertView;
        }

    }

    boolean isBack;

    public void onPause() {
        if (isBack) {
            isBack = false;
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        }
        super.onPause();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBack = true;

        }

        return super.onKeyUp(keyCode, event);
    }
}