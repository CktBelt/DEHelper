package com.dehelper.settingmanager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GeneralSetting {
	private static final String TAG = "GeneralSetting";
	
	private Context context;
	private BaseAdapter langAdapter;
	private View view;
	private OnItemSelectedListener langListener;
	
	private String[] strItem;

	public GeneralSetting(Context parent, View v, String[] item) {
		this.view = v;
		this.context = parent;
		this.strItem = item;

		langAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View contertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll = new LinearLayout(context);
				TextView tv = new TextView(context);
				tv.setText(strItem[position]);
				tv.setTextSize(20);
				tv.setPadding(20, 20, 20, 20);
				ll.addView(tv);
				return ll;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int postion) {
				// TODO Auto-generated method stub
				return strItem[postion];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return strItem.length;
			}
		};
		
		setLangAdapter();
	}

	private void setLangAdapter() {
		((Spinner) view).setAdapter(langAdapter);
	}

	public void setOnItemSelectedListener() {
		langListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LinearLayout ll = (LinearLayout) view;
				TextView tv = (TextView) ll.getChildAt(0);
				Log.i(TAG, tv.getText().toString());
				Toast.makeText(context, "click on item "+position, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		};
		
		((Spinner) view).setOnItemSelectedListener(langListener);
//		return langListener;
	}
}
