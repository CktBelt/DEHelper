package com.example.dehelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dehelper.settingmanager.EmcNumberSetting;
import com.dehelper.settingmanager.GeneralSetting;
import com.dehelper.settingmanager.SoundSetting;
import com.dehelper.syncManger.HttpMethod;

public class MyDeviceActivity extends Activity {

	private static final String TAG = "MyDeviceActivity";
	private static final String host = "http://preprod.y-sphere.com";
	private static final String url = "http://preprod.y-sphere.com/ysphere-mosvc/service/y-box/settings";
	private static final int MESSAGE_ID_GET_SETTINGS = 0x01;
	private static final int MESSAGE_ID_GET_SETTINGS_ITEM = 0x02;
	private static final String PREFS_MY_DEV_SETTING_LIST = "note.de.my_dev_setting_list";
	private static final String PREFS_SETTING_ITEM = "note.de.setting_item";

	private Spinner sp_lang;
	private Spinner sp_as;
	private EditText et_ec;
	private TextView[] tv_sd = new TextView[8];

	private String[] myDeviceSetting = { "language", "sound.volume",
			"call.emergency.number", "call.speedial.a.name",
			"call.speedial.a.number", "call.speedial.b.name",
			"call.speedial.b.number", "call.speedial.c.name",
			"call.speedial.c.number", "call.speedial.d.name",
			"call.speedial.d.number" };

	// private String[] str = {"aaa", "bbb", "ccc"};
	private String[] langStr = { "Dansk", "Deutsch", "English", "Nederlands",
			"Norwegian Bokmål", "Español", "Français", "Italiano", "Str",
			"Suomi", "Svenska", "Русский", "עברית", "العربية" };
	private String[] langCode = { "da_DK", "de_DE", "en_GB", "nl_NL", "nb_NO",
			"es_ES", "fr_FR", "it_IT", "str_LANGUAGE", "fi_FI", "sv_SE",
			"ru_RU", "iw_IL", "ar_QA" };
	private String[] audioStr = { "Normal", "Medium", "High", "HAC mode" };
	private String emcStr;

	private String prefs_name;
	private String prefs_password;
	private String prefs_imei;
	private String Author;
	private String base64Author;

	private boolean isBusy;
	private boolean isSyncListInit = false;

	List<String> mSyncItemList = new ArrayList<String>();
	GeneralSetting generalSetting;
	SoundSetting soundSetting;
	EmcNumberSetting emcNumSetting;

	ProgressDialog pDialog;

	SharedPreferences pref_setting;

	// SyncDataThread syncDataThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mydevice);
		super.onCreate(savedInstanceState);

		// syncDataThread = new SyncDataThread(this, SyncDataThread.GET, url,
		// MESSAGE_ID_GET_SETTINGS, handler);
		SharedPreferences userinfo = getSharedPreferences(
				MainActivity.PREFS_USER_INFO, 0);
		prefs_name = userinfo.getString("name", "");
		prefs_password = userinfo.getString("password", "");
		prefs_imei = userinfo.getString("imei", "");

		Author = new String(prefs_name + ":" + prefs_password);
		Log.d(TAG, "syncdatathread-->" + Author);
		base64Author = android.util.Base64.encodeToString(Author.getBytes(),
				Base64.DEFAULT);

		pDialog = ProgressDialog.show(MyDeviceActivity.this, "", "Waiting...");
		new Thread(new syncDataThread()).start();
		initCommenView();
		applyListener();
	}

	private void initCommenView() {
		String speed_dial_text = null;

		sp_lang = (Spinner) findViewById(R.id.sp_lang);
		sp_as = (Spinner) findViewById(R.id.sp_as);
		et_ec = (EditText) findViewById(R.id.et_ec);
		tv_sd[0] = (TextView) findViewById(R.id.tv_sd_name_a);
		tv_sd[1] = (TextView) findViewById(R.id.tv_sd_number_a);
		tv_sd[2] = (TextView) findViewById(R.id.tv_sd_name_b);
		tv_sd[3] = (TextView) findViewById(R.id.tv_sd_number_b);
		tv_sd[4] = (TextView) findViewById(R.id.tv_sd_name_c);
		tv_sd[5] = (TextView) findViewById(R.id.tv_sd_number_c);
		tv_sd[6] = (TextView) findViewById(R.id.tv_sd_name_d);
		tv_sd[7] = (TextView) findViewById(R.id.tv_sd_number_d);

		generalSetting = new GeneralSetting(this, sp_lang, langStr);
		soundSetting = new SoundSetting(this, sp_as, audioStr);
		emcNumSetting = new EmcNumberSetting(this, et_ec, emcStr);

		pref_setting = getSharedPreferences(PREFS_SETTING_ITEM, 0);
		int language = pref_setting.getInt(myDeviceSetting[0], 0);
		sp_lang.setSelection(language, true);
		int sound = pref_setting.getInt(myDeviceSetting[1], 0);
		sp_as.setSelection(sound, true);
		String emergencyNum = pref_setting.getString(myDeviceSetting[2], "");
		et_ec.setText(emergencyNum);
		for (int i = 0; i < 8; i++) {
			speed_dial_text = pref_setting.getString(myDeviceSetting[i+3], "");
			tv_sd[i].setText(speed_dial_text);
		}
	}

	private void applyListener() {
		generalSetting.setOnItemSelectedListener();
		soundSetting.setOnItemSelectedListener();
	}

	public class syncDataThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpMethod httpMethod = new HttpMethod(handler);
			Header header[] = new Header[5];
			header[0] = new BasicHeader("Accept", "ybox/2.2");
			header[1] = new BasicHeader("X-YBox-Service", "doro580");
			header[2] = new BasicHeader("x-ybox-hardwareid", prefs_imei);
			header[3] = new BasicHeader("X-YBox-Phonemodel", "Doro 580");
			header[4] = new BasicHeader("Authorization", "Basic "
					+ base64Author.trim().toString());
			httpMethod.httpGet(url, header, MESSAGE_ID_GET_SETTINGS);
			setBusyFlag(true);

			while (true) {
				// Log.d("yf_tag", "run getBusyFlag=" + getBusyFlag());
				if (getBusyFlag() == false && isSyncListInit == true) {
					Log.d("yf_tag", "syncDataThread mSyncItemList.size = "
							+ String.valueOf(mSyncItemList.size()));
					if (mSyncItemList.size() == 0)
						break;
					String URL = new String(url + "/" + mSyncItemList.get(0));
					Log.d(TAG, "run URL=" + URL);
					Log.d("yf_tag", "run URL=" + URL);
					setBusyFlag(true);
					httpMethod.httpGet(URL, header,
							MESSAGE_ID_GET_SETTINGS_ITEM);
				}
			}
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.d(TAG, "handleMessage" + String.valueOf(msg.what));
			switch (msg.what) {
			case MESSAGE_ID_GET_SETTINGS:
				if (msg.arg1 == 200) {

					String res = (String) msg.obj;
					System.out.println(res);
					parseJsonSettings(res);
					Log.d(TAG, res);
				} else {
					Toast.makeText(MyDeviceActivity.this, "connect error",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case MESSAGE_ID_GET_SETTINGS_ITEM:
				if (msg.arg1 == 200) {
					pref_setting = getSharedPreferences(PREFS_SETTING_ITEM, 0);
					SharedPreferences.Editor pref_setting_editor = pref_setting
							.edit();

					Log.d(TAG, (String) msg.obj);
					Log.d("yf_tag", "MESSAGE_ID_GET_SETTINGS_ITEM remove "
							+ mSyncItemList.get(0));
					Log.d("yf_tag",
							"MESSAGE_ID_GET_SETTINGS_ITEM mSyncItemList.size = "
									+ String.valueOf(mSyncItemList.size()));

					/* 解析JSON数据，取出value字段和version字段 */
					String res = (String) msg.obj;
					String value = null;
					String mItemVer = null;
					try {
						JSONObject jObj = new JSONObject(res);
						double ver = jObj.getDouble("version");

						mItemVer = new BigDecimal(ver).toString();
						value = jObj.getString("value");
						Log.d("yf_tag", "value = " + value);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/* 将value对应的item存储在sharedpreferences中，并修改spinner的显示结果 */
					if (mSyncItemList.get(0).equals(myDeviceSetting[0])) {
						for (int i = 0; i < langCode.length; i++) {
							if (langCode[i].equals(value)) {
								Log.d("yf_tag", "i = " + String.valueOf(i)
										+ "langCode[i] = " + langCode[i]);
								sp_lang.setSelection(i, true);
								pref_setting_editor.putInt(myDeviceSetting[0],
										i);
								pref_setting_editor.commit();
							}
						}
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[1])) {
						int pos = Integer.parseInt(value);
						sp_as.setSelection(pos, true);
						pref_setting_editor.putInt(myDeviceSetting[1], pos);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[2])) {
						et_ec.setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[2], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[3])) {
						tv_sd[0].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[3], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[4])) {
						tv_sd[1].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[4], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[5])) {
						tv_sd[2].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[5], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[6])) {
						tv_sd[3].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[6], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[7])) {
						tv_sd[4].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[7], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[8])) {
						tv_sd[5].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[8], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[9])) {
						tv_sd[6].setText(value);
						pref_setting_editor
								.putString(myDeviceSetting[9], value);
						pref_setting_editor.commit();
					} else if (mSyncItemList.get(0).equals(myDeviceSetting[10])) {
						tv_sd[7].setText(value);
						pref_setting_editor.putString(myDeviceSetting[10],
								value);
						pref_setting_editor.commit();
					}

					/* 将version更新存储在sharedpreference中 */
					SharedPreferences settingList = getSharedPreferences(
							PREFS_MY_DEV_SETTING_LIST, 0);
					SharedPreferences.Editor editor = settingList.edit();
					editor.putString(mSyncItemList.get(0), mItemVer);// 将新的版本号更新进sharedpreferences
					editor.commit();

					mSyncItemList.remove(0);// 将同步List表中已同步过的数据记录去除
					if (mSyncItemList.size() == 0) {
						pDialog.dismiss();
					}
				}
				break;
			}
			setBusyFlag(false);
		}

		private void parseJsonSettings(String res) {
			// TODO Auto-generated method stub
			try {
				SharedPreferences settingList = getSharedPreferences(
						PREFS_MY_DEV_SETTING_LIST, 0);
				// SharedPreferences.Editor editor = settingList.edit();
				HashMap<String, String> hashMap = new HashMap<String, String>();

				JSONArray array = new JSONArray(res);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String id = obj.getString("id");
					double ver = obj.getDouble("version");

					hashMap.put(id, new BigDecimal(ver).toString());
				}

				for (int i = 0; i < myDeviceSetting.length; i++) {
					String mItemVer = hashMap.get(myDeviceSetting[i]);
					if (mItemVer != null) {
						if (settingList.contains(myDeviceSetting[i])) {
							if (mItemVer.equals(settingList.getString(
									myDeviceSetting[i], "")) == false) {
								mSyncItemList.add(myDeviceSetting[i]); // mSyncItemList记录需要同步的项
								// editor.putString(myDeviceSetting[i],
								// mItemVer);// 同时将新的版本号更新进sharedpreferences
							}
						} else {
							mSyncItemList.add(myDeviceSetting[i]);
							// editor.putString(myDeviceSetting[i], mItemVer);
						}
					}
				}
				Log.d(TAG, mSyncItemList.toString());
				Log.d("yf_tag",
						"mSyncItemList.size = "
								+ String.valueOf(mSyncItemList.size()));
				isSyncListInit = true;
				if (mSyncItemList.size() == 0) {
					pDialog.dismiss();
				}
				// editor.commit();
				System.out.println(mSyncItemList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	private void setBusyFlag(boolean isBusy) {
		Log.d("yf_tag", "setBusy = " + isBusy);
		this.isBusy = isBusy;
	}

	private boolean getBusyFlag() {
		return this.isBusy;
	}
}
