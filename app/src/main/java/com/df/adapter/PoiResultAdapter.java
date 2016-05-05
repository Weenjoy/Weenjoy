package com.df.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.df.dianping.R;
import com.df.widget.PoiListItem;

import java.util.List;
import java.util.Map;

/**
 * Created by asus88 on 2016/4/15.
 */
public class PoiResultAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, Object>> filterData;
    private Context context;

    public PoiResultAdapter(Context context, List<Map<String, Object>> filterData) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.filterData = filterData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filterData.size();
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


        if (convertView == null) {
            Log.v("is NULL", "DF2" + position);
        }

        Log.v("ListViewLog", "DF" + position);

        convertView = mInflater.inflate(R.layout.resultitem, null);

        PoiListItem item = (PoiListItem) convertView;

        Map map = filterData.get(position);

        item.setPoiData(map.get("name").toString(), map.get("price")
                        .toString(), map.get("addr").toString(), ((Integer) map
                        .get("star")).intValue(), ((Boolean) map.get("tuan"))
                        .booleanValue(), ((Boolean) map.get("card")).booleanValue(),
                ((Boolean) map.get("promo")).booleanValue(),
                ((Boolean) map.get("checkin")).booleanValue());

        item.setDistanceText(map.get("distance").toString());

        return convertView;
    }
}
