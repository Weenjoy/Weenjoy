package com.df.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.df.dianping.CategoryInfo;
import com.df.dianping.R;

import java.util.List;

/**
 * Created by asus88 on 2016/6/11.
 */
public class GridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<CategoryInfo> list;

    public GridViewAdapter(Context context, List<CategoryInfo> list) {
        mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.gridview_adapter, null);
            viewHolder.textView = (TextView) view.findViewById(R.id.gridView_adapter_textView);
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.gridview_adapter_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(list.get(i).getName());
        viewHolder.mImageView.setImageResource(list.get(i).getImageId());
        return view;
    }

    public void setList(List<CategoryInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView textView;
        ImageView mImageView;
    }
}
