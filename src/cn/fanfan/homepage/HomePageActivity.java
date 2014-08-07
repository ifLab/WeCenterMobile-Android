package cn.fanfan.homepage;

import java.util.ArrayList;
import java.util.List;

import cn.fanfan.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class HomePageActivity extends Activity {
	private ListView listView;
	private List<HomePageItemModel> itemDataList = new ArrayList<HomePageItemModel>();
	private HomePageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_homepage);
		listView = (ListView) findViewById(R.id.lvHomeListView);
		initItemData();
		adapter = new HomePageAdapter(HomePageActivity.this,
				R.layout.listitem_homepage, itemDataList);
		listView = (ListView) findViewById(R.id.lvHomeListView);
		listView.setAdapter(adapter);
	}

	private void initItemData() {
		// TODO Auto-generated method stub
		HomePageItemModel item1 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"提莫",
				"赞同该回答",
				"漳州有哪些不该错过的美食？",
				"漳州卤面也称为鲁面，久负盛名。采用肉丝、笋丝、蛋丝、香菇、鱿鱼、虾干、黄花菜等配料在热锅里炒熟后，加上猪骨汤煮开，然后放入适量的味精、白糖、精盐和番薯粉等，调成卤料。进餐时，在面条上放些韭菜、豆芽、浇上卤料，再配上胡椒粉、油炸蒜丁、油炸扁鱼丝、香菜等佐料。其特点：色泽鲜艳、质嫩爽滑、晕润香醇、甘美可口。",
				"78");
		itemDataList.add(item1);
		HomePageItemModel item2 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_SIMPLE, "庞荣", "关注该问题",
				"厦门蚵仔煎怎么做？", "没有数据", "1");
		itemDataList.add(item2);
		HomePageItemModel item3 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"hcjcch",
				"赞同该回答",
				"如何给美食拍照?",
				"曾经给一些商家拍过美食广告单，有一些小小的心得，与大家分享一下。首先，说一下自己对@闻佳照片的感觉，不是说闻佳的答案不好，只是说一下自己的意见（卧槽，第一次在饭饭@别人，好紧张，第一次啊",
				"100");
		itemDataList.add(item3);
		HomePageItemModel item4 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_SIMPLE, "黄伟", "关注该回答",
				"女生经期不能吃什么？", " ", "0");
		itemDataList.add(item4);
		HomePageItemModel item5 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"嗨Hwei",
				"赞同该回答",
				"漳州有哪些不该错过的美食？",
				"漳州卤面也称为鲁面，久负盛名。采用肉丝、笋丝、蛋丝、香菇、鱿鱼、虾干、黄花菜等配料在热锅里炒熟后，加上猪骨汤煮开，然后放入适量的味精、白糖、精盐和番薯粉等，调成卤料。进餐时，在面条上放些韭菜、豆芽、浇上卤料，再配上胡椒粉、油炸蒜丁、油炸扁鱼丝、香菜等佐料。其特点：色泽鲜艳、质嫩爽滑、晕润香醇、甘美可口。",
				"1k");
		itemDataList.add(item5);
		HomePageItemModel item6 = new HomePageItemModel(
				R.drawable.ic_avatar_default,
				HomePageItemModel.LAYOUT_TYPE_COMPLEX,
				"提莫",
				"赞同该回答",
				"台湾有哪些美食值得一试？",
				"只说自己印象深刻的：1. 豪大大鸡排基本上是半只鸡啦，而且号称“绝对不切开卖”，一枚55NT，超值。一般的女孩子3-4个人分吃不成问题。虽然吃到最后会有些“为什么要吃这么多鸡肉啊！”的感觉，但值得尝试。",
				"256");
		itemDataList.add(item6);
	}
}
