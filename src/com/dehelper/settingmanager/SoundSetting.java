package com.dehelper.settingmanager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SoundSetting {
	private static final String TAG = "SoundSetting";

	private Context context;
	private BaseAdapter soundAdapter;
	private View view;
	private OnItemSelectedListener soundListener;
	
	private String[] strItem;

	public SoundSetting(Context parent, View view, String[] audioStr) {
		// TODO Auto-generated constructor stub
		this.view = view;
		this.context = parent;
		this.strItem = audioStr;
		
		soundAdapter = new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return strItem.length;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return strItem[arg0];
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				LinearLayout ll = new LinearLayout(context);
				TextView tv = new TextView(context);
				tv.setText(strItem[arg0]);
				tv.setTextSize(20);
				tv.setPadding(20, 20, 20, 20);
				ll.addView(tv);
				return ll;
			}
			
		};
		
		setSoundAdapter();
	}
	
	private void setSoundAdapter() {
		((Spinner) view).setAdapter(soundAdapter);
	}

	public void setOnItemSelectedListener() {
		soundListener = new OnItemSelectedListener() {

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
		
		((Spinner) view).setOnItemSelectedListener(soundListener);
//		return langListener;
	}
}
