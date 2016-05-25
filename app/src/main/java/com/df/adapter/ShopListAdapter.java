package com.df.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.df.dianping.R;

import java.util.List;
import java.util.Map;

/**
 * Created by asus88 on 2016/4/17.
 */
public class ShopListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater minflater;


    public ShopListAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
        minflater = LayoutInflater.from(context);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = minflater.inflate(R.layout.listadapter, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.list_image);
            viewHolder.name = (TextView) view.findViewById(R.id.list_shop_name);
            viewHolder.description = (TextView) view.findViewById(R.id.list_shop_description);
            viewHolder.price = (TextView) view.findViewById(R.id.list_shop_price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mImageView.setImageBitmap((Bitmap) list.get(i).get("tiny_image"));
        viewHolder.name.setText(list.get(i).get("shop_name").toString());
        viewHolder.description.setText(list.get(i).get("description").toString());
        viewHolder.price.setText("¥" + String.valueOf(Integer.parseInt(list.get(i).get("current_price").toString()) / 100));

        return view;
    }


    private static class ViewHolder {
        ImageView mImageView;
        TextView name;
        TextView price;
        TextView description;

    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}
