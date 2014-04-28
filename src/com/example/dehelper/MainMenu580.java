package com.example.dehelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainMenu580 extends Activity {

//	private static final String[] MENU_ITEM = { "My device", "Assistance",
//			"White list numbers", "Safety timer", "Auto answer", "GPS request" };
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_580);

		lv = (ListView) findViewById(R.id.lv);
		
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.lv_item, new String[] { "icon", "text", "icon_next" },
				new int[] { R.id.icon, R.id.text, R.id.icon_next });
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(MainMenu580.this,
						"Click on" + String.valueOf(position),
						Toast.LENGTH_SHORT).show();
				switch (position) {
				case 0:
					startActivity(new Intent(MainMenu580.this,
							MyDeviceActivity.class));
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				default:
					break;
				}
			}
		});
	}

	private List<Map<String, Object>> getData() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] text = { "My device", "Assistance", "White list numbers",
				"Safety timer", "Auto answer", "GPS request" };

		for (int i = 0; i < text.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("icon", null);
			map.put("text", text[i]);
			map.put("icon_next", R.drawable.icon_next);
			list.add(map);
		}
		return list;
	}

	private long first_time = 0;
	private long second_time = 0;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			second_time = System.currentTimeMillis();
			if (second_time - first_time > 2000) {
				Toast.makeText(this, "Press back again to exit",
						Toast.LENGTH_SHORT).show();
				first_time = second_time;
				return true;
			} else {
				System.exit(0);
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

}
