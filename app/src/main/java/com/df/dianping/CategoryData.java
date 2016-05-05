package com.df.dianping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryData {
    public static List<Map<String, Object>> getData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();


		map.put("title", "生活服务");
		map.put("img", R.drawable.life);
		map.put("id","316");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "休闲娱乐");
		map.put("img", R.drawable.happy);
		map.put("id","320");
		list.add(map);

		map=new HashMap<>();
		map.put("title","其他");
		map.put("img",R.drawable.icon);
		map.put("id","323");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "美食");
		map.put("img", R.drawable.food);
		map.put("id","326");
		list.add(map);



		map = new HashMap<String, Object>();
		map.put("title", "网购");
		map.put("img", R.drawable.shopping);
		map.put("id","330");
		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "酒店旅游");
		map.put("img", R.drawable.hotel);
		map.put("id","377");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "上门服务");
		map.put("img", R.drawable.beauty);
		map.put("id","963");
		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "演出赛事");
		map.put("img", R.drawable.sports);
		map.put("id","970");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "充值缴费");
		map.put("img", R.drawable.wedding);
		map.put("id","990");
		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "汽车后服务");
		map.put("img", R.drawable.baby);
		map.put("id","1010");
		list.add(map);

		return list;
    }
}
