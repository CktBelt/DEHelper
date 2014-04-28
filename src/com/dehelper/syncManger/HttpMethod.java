package com.dehelper.syncManger;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpMethod {
	public static final String TAG = "HttpMethod";
	private Handler httpHandler;

	public HttpMethod(Handler handler) {
		this.httpHandler = handler;
	}

	private HttpClient createClient() {
		HttpClient client = new DefaultHttpClient();

		return client;
	}

	public String httpGet(String URL, Header[] header, int handlerMsg) {
		String getResult = null;

		Log.d(TAG, "httpGet"+URL+", handlerMsg="+String.valueOf(handlerMsg));
		HttpClient client = createClient();
		HttpGet httpGet = new HttpGet(URL);
		httpGet.setHeaders(header);
		HttpResponse response = null;

		try {
			Log.d(TAG, "syncdatathread execute start");
			response = client.execute(httpGet);
			Log.d(TAG, "syncdatathread execute end");
			int statusCode = response.getStatusLine().getStatusCode();
			Log.d(TAG, "syncdatathread execute statusCode=" + statusCode);
			if (statusCode == 200) {
				getResult = EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				getResult = null;
			}

			Message msg = Message.obtain();
			msg.what = handlerMsg;
			msg.arg1 = statusCode;
			msg.obj = getResult;

			Log.d(TAG, "syncdatathread execute getResult=" + getResult);
			httpHandler.sendMessage(msg);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getResult;
	}

	public int httpPost(String URL, Header[] header, String param) {
		return 0;
	}
	
	public int httpPut(String URL, Header[] header, String param){
		HttpPut httpPut;
		return 0;
	}
}
