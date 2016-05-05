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
import com.df.dianping.ResultActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AreaAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ArrayList<String>> mData;
    private boolean isTopLevel = true; //是否为顶层
    private int typeIndex = 0;
    private ImageView img;
    private List<Map<String, Object>> arealist;
    private Context context;
    private List<List<String>> selected;//是否选中

    public AreaAdapter(Context context, List<Map<String, Object>> arealist) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.arealist = arealist;
        mData = getData();
    }

    public void setlist(List<Map<String,Object>> list){
        this.arealist=list;
    }
    public String getSelect() {
        return mData.get(typeIndex).get(1);
    }

    public boolean isTopLevel() {
        return isTopLevel;
    }

    public void setTypeIndex(int index) {
        typeIndex = index;
        if (index > 0) {
            isTopLevel = false;
        } else {
            isTopLevel = true;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (isTopLevel) {
            return mData.size();
        } else {
            return mData.get(typeIndex).size() - 1;
        }

    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
//        if (isTopLevel)
//            return mData.get(arg0);
//        else
//            return mData.get(typeIndex).get(arg0)
//;
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public List<Map<String, Object>> getArealist() {
        return arealist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.dialog_list_item, null);
        img = (ImageView) convertView.findViewById(R.id.ic_checked);
        if (isTopLevel) {
            View view = new View(context);
            LayoutParams param = new LayoutParams(30, 30);
            view.setLayoutParams(param);

            String area = mData.get(position).get(1);
            ((TextView) convertView.findViewById(R.id.id_area)).setText(area);

            if (position == 0) {
                convertView.findViewById(R.id.ic_checked).setVisibility(View.VISIBLE);
            } else {
                ((LinearLayout) convertView).addView(view, 0);
            }

        } else {
            String area = mData.get(typeIndex).get(position);
            ((TextView) convertView.findViewById(R.id.id_area)).setText(area);

            if (position == 1) {
                View view = new View(context);
                LayoutParams param = new LayoutParams(30, 30);
                view.setLayoutParams(param);
                if(ResultActivity.flag)
                    img.setVisibility(View.VISIBLE);
                else
                img.setVisibility(View.GONE);
                //convertView.findViewById(R.id.ic_checked).setVisibility(View.VISIBLE);
                ((LinearLayout) convertView).addView(view, 0);
            } else if (position > 1) {
                View view = new View(context);
                LayoutParams param = new LayoutParams(60, 30);
                view.setLayoutParams(param);
                ((LinearLayout) convertView).addView(view, 0);
                if (!arealist.get(typeIndex - 1).get("list").equals("")) {
                    List<Map<String, Object>> l = (List<Map<String, Object>>) arealist.get(typeIndex - 1).get("list");
                    if (l.get(position - 2).get("selected").equals(true))
                        img.setVisibility(View.VISIBLE);
                    else
                        img.setVisibility(View.GONE);
                }

            }

        }
        return convertView;
    }

    /**
     * 返回areaadapter的数据列表
     */
    public ArrayList<ArrayList<String>> getData() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> all = new ArrayList<>();
        all.add("全部地区");
        all.add("全部地区");
        data.add(all);
        for (int i = 0; i < arealist.size(); i++) {
            ArrayList<String> diqu = new ArrayList<>();
            diqu.add("全部地区");
            diqu.add(arealist.get(i).get("district_name").toString());
            if (!arealist.get(i).get("list").equals("")) {
                List<Map<String, Object>> mlist = (List<Map<String, Object>>) arealist.get(i).get("list");
                for (int j = 0; j < mlist.size(); j++) {
                    diqu.add(mlist.get(j).get("biz_area_name").toString());
                }
            }
            data.add(diqu);
        }
        return data;
    }

}
