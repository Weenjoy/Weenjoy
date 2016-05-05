package com.df.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.df.dianping.R;

import java.util.List;
import java.util.Map;

public class CateAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    Context mContext;
    private LayoutInflater mInflater;
    private ImageView img;

    public CateAdapter(Context context, List<Map<String, Object>> list) {
        mContext = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }



    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.dialog_list_item, null);
        String area = list.get(position).get("subcat_name").toString();
        ((TextView) convertView.findViewById(R.id.id_area)).setText(area);
        img = (ImageView) convertView.findViewById(R.id.ic_checked);
        if (list.get(position).get("checked").equals(true))
            img.setVisibility(View.VISIBLE);
        else
            img.setVisibility(View.GONE);
        View view = new View(mContext);
        LayoutParams param = new LayoutParams(30, 30);
        view.setLayoutParams(param);
        ((LinearLayout) convertView).addView(view, 0);

        return convertView;
    }

}