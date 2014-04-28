package com.example.dehelper;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DEMainMenu extends Activity {

	private ExpandableListView lv_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.de_mainmenu);

		final ExpandableListAdapter adapter = new ExpandableListAdapter() {

			// 主视图图片
			int[] logos = new int[] { R.drawable.mydevice,
					R.drawable.myapplication };

			// 主视图文字
			String[] generalType = new String[] { "My device",
					"My applications" };

			// 子视图文字
			String[][] general = new String[][] { { "Settings", "Assistance" },
					{ "Contacts", "Gallery", "Calendar", "Daily reminder" } };

			// 子视图图片
			int[][] generalLogo = new int[][] {
					{ R.drawable.myapplication, R.drawable.assistance },
					{ R.drawable.contacts, R.drawable.gallery,
							R.drawable.calender, R.drawable.reminder } };

			// 获得文字信息的方法
			TextView getTextView() {
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 64);
				TextView testView = new TextView(DEMainMenu.this);
				testView.setLayoutParams(lp);
				testView.setGravity(Gravity.CENTER_VERTICAL);
				// testView.setPadding(36, 0, 0, 0);
				testView.setTextSize(20);

				return testView;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll = new LinearLayout(DEMainMenu.this);
				ll.setOrientation(0);

				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(64,
						ViewGroup.LayoutParams.MATCH_PARENT);
				ImageView logo = new ImageView(DEMainMenu.this);
				logo.setImageResource(logos[groupPosition]);
				logo.setLayoutParams(lp);
				logo.setPadding(50, 0, 0, 0);
				// ll.addView(logo);
				TextView textView = getTextView();
				textView.setPadding(50, 0, 0, 0);
				textView.setText(getGroup(groupPosition).toString());
				ll.addView(textView);

				return ll;
			}

			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}

			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return generalType.length;
			}

			@Override
			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return generalType[groupPosition];
			}

			@Override
			public long getCombinedGroupId(long groupId) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getCombinedChildId(long groupId, long childId) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return general[groupPosition].length;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll = new LinearLayout(DEMainMenu.this);
				ll.setOrientation(0);

				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(64,
						ViewGroup.LayoutParams.MATCH_PARENT);
				ImageView logo = new ImageView(DEMainMenu.this);
				logo.setImageResource(generalLogo[groupPosition][childPosition]);
				logo.setLayoutParams(lp);
				logo.setPadding(13, 13, 13, 13);
				ll.addView(logo);
				TextView textView = getTextView();
				textView.setText(getChild(groupPosition, childPosition)
						.toString());
				textView.setTextSize(16);
				// textView.setPadding(left, top, right, bottom)
				ll.addView(textView);

				return ll;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return general[groupPosition][childPosition];
			}

			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onGroupCollapsed(int groupPosition) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGroupExpanded(int groupPosition) {
				// TODO Auto-generated method stub

			}

			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}
		};

		lv_main = (ExpandableListView) findViewById(R.id.lv_main);
		lv_main.setAdapter(adapter);
		int grpCnt = lv_main.getCount();
		for (int i=0; i < grpCnt; i++) {
			lv_main.expandGroup(i);
		}

		lv_main.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						DEMainMenu.this,
						"Click on "
								+ adapter
										.getChild(groupPosition, childPosition),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
}
