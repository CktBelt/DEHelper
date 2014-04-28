package com.example.dehelper;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static final String TAG = "myTag";

	private static final String URL = "http://preprod.y-sphere.com/ysphere-mosvc/service/y-box/application";
	private static final int MSSAGE_ID_LOG_IN = 0x01;

	public static final String PREFS_USER_INFO = "note.de.userinfo";

	private String Author;

	private EditText et_name;
	private EditText et_psw;
	private EditText et_imei;
	private Button btn_login;

	private String username;
	private String password;
	private String imei;

	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "MainActivity");

		et_name = (EditText) findViewById(R.id.main_ev_name);
		et_psw = (EditText) findViewById(R.id.main_ev_psd);
		et_imei = (EditText) findViewById(R.id.main_ev_imei);
		btn_login = (Button) findViewById(R.id.main_btn_login);
		
		SharedPreferences userinfo = getSharedPreferences(PREFS_USER_INFO, 0);
		String prefs_name = userinfo.getString("name", "");
		String prefs_password = userinfo.getString("password", "");
		String prefs_imei = userinfo.getString("imei", "");
		
		et_name.setText(prefs_name);
		et_psw.setText(prefs_password);
		et_imei.setText(prefs_imei);
		
		System.out.println(prefs_name);
		System.out.println(prefs_password);
		System.out.println(prefs_imei);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dMsg = null;

				username = et_name.getText().toString().trim();
				password = et_psw.getText().toString().trim();
				imei = et_imei.getText().toString().trim();

				if (username.length() < 6) {
					dMsg = "User name must be greater than six";
				} else if (password.length() < 6) {
					dMsg = "Password must be greater than six";
				} else if (imei.length() == 0) {
					dMsg = "imei can't be empty";
				}
				if (dMsg != null && dMsg.length() > 0) {
					Builder dialog = new AlertDialog.Builder(MainActivity.this);
					dialog.setTitle(R.string.login_error);
					dialog.setMessage(dMsg);
					dialog.setPositiveButton("OK", null);
					dialog.show();
				} else {
					Author = new String(username + ":" + password);
					pDialog = ProgressDialog.show(MainActivity.this, "",
							"Logging in...");
					new Thread(new LoginThread()).start();
				}
			}

		});
	}

	public class LoginThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String Base64_Str = android.util.Base64.encodeToString(
					Author.getBytes(), Base64.DEFAULT);
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			httpGet.addHeader("Accept", "ybox/2.2");
			httpGet.addHeader("X-YBox-Service", "doro580");
			httpGet.addHeader("x-ybox-hardwareid", imei);
			httpGet.addHeader("X-YBox-Phonemodel", "Doro 580");
			httpGet.addHeader("Authorization", "Basic "
					+ Base64_Str.trim().toString());
			HttpResponse response = null;

			try {
				response = client.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				Message msg = Message.obtain();

				msg.what = MSSAGE_ID_LOG_IN;
				msg.arg1 = statusCode;

				loginHandler.sendMessage(msg);

				Log.d(TAG, String.valueOf(statusCode));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@SuppressLint("HandlerLeak")
	private Handler loginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSSAGE_ID_LOG_IN:
				if (msg.arg1 == 200) {
					SharedPreferences userinfo = getSharedPreferences(PREFS_USER_INFO, 0);
					SharedPreferences.Editor editor = userinfo.edit();
					editor.putString("name", username);
					editor.putString("password", password);
					editor.putString("imei", imei);
					editor.commit();
					
					pDialog.dismiss();
					startActivity(new Intent(MainActivity.this,
							MainMenu580.class));
					
					MainActivity.this.finish();
				} else {
					String dMsg;

					pDialog.dismiss();
					if (msg.arg1 == 401) {
						dMsg = getResources().getString(R.string.login_error_1);
					} else if (msg.arg1 == 412) {
						dMsg = getResources().getString(R.string.login_error_2);
					} else if (msg.arg1 == 500) {
						dMsg = getResources().getString(R.string.login_error_3);
					} else {
						dMsg = "Unkown error";
					}

					Builder dialog = new AlertDialog.Builder(MainActivity.this);
					dialog.setTitle(R.string.login_error);
					dialog.setMessage(dMsg);
					dialog.setPositiveButton("OK", null);
					dialog.show();
				}
				break;
			}
			super.handleMessage(msg);
		}

	};

}
