package com.df.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.df.dianping.R;

import java.util.List;
import java.util.Map;

/**
 * Created by asus88 on 2016/5/25.
 */
public class CollectionAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater minflater;
    private OnListSelectedChangeListener mOnListSelectedChangeListener;


    public CollectionAdapter(Context context, List<Map<String, Object>> list) {
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
            view = minflater.inflate(R.layout.collectionadapter, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.collection_image);
            viewHolder.name = (TextView) view.findViewById(R.id.collection_shop_name);
            viewHolder.description = (TextView) view.findViewById(R.id.collection_shop_description);
            viewHolder.price = (TextView) view.findViewById(R.id.collection_shop_price);
            viewHolder.btn = (RadioButton) view.findViewById(R.id.collection_btn);

            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = (boolean) list.get(i).get("selected");
                    if (flag) {
                        if (mOnListSelectedChangeListener != null) {
                            mOnListSelectedChangeListener.selectedNum(-1);
                        }
                    } else {
                        if (mOnListSelectedChangeListener != null) {
                            mOnListSelectedChangeListener.selectedNum(1);
                        }
                    }
                    list.get(i).put("selected", !flag);
                    notifyDataSetChanged();
                }

            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mImageView.setImageBitmap((Bitmap) list.get(i).get("tiny_image"));
        viewHolder.name.setText(list.get(i).get("shop_name").toString());
        viewHolder.description.setText(list.get(i).get("description").toString());
        viewHolder.price.setText("¥" + String.valueOf(Integer.parseInt(list.get(i).get("current_price").toString()) / 100));
        viewHolder.btn.setChecked((Boolean) list.get(i).get("selected"));
        return view;
    }

    public void setOnListSelectedChangeListener(OnListSelectedChangeListener onListSelectedChangeListener) {
        mOnListSelectedChangeListener = onListSelectedChangeListener;
    }

    private static class ViewHolder {
        ImageView mImageView;
        TextView name;
        TextView price;
        TextView description;
        RadioButton btn;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnListSelectedChangeListener {
        void selectedNum(int n);
    }
}
