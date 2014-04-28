//package com.dehelper.syncManger;
//
//import org.apache.http.Header;
//import org.apache.http.message.BasicHeader;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.util.Base64;
//import android.util.Log;
//
//import com.example.dehelper.MainActivity;
//
//public class SyncDataThread implements Runnable {
//	private static final String TAG = "SyncDataThread";
//
//	public static final int GET = 1;
//	public static final int POST = 2;
//	public static final int PUT = 3;
//
//	private String prefs_name;
//	private String prefs_password;
//	private String prefs_imei;
//	private String Author;
//	private String base64Author;
//	private int httpMethod;
//	private String httpUrl;
//	private int handlerMsg;
//	private Handler httpHandler;
//	private HttpMethod httpMethod;
//	
//	public SyncDataThread(Context parent, int method, String url, int msg,
//			Handler handler) {
//		this.httpMethod = method;
//		this.httpUrl = url;
//		this.handlerMsg = msg;
//		this.httpHandler = handler;
//
//		SharedPreferences userinfo = parent.getSharedPreferences(
//				MainActivity.PREFS_USER_INFO, 0);
//		prefs_name = userinfo.getString("name", "");
//		prefs_password = userinfo.getString("password", "");
//		prefs_imei = userinfo.getString("imei", "");
//		
//		Author = new String(prefs_name + ":" + prefs_password);
//		Log.d(TAG, "syncdatathread-->"+Author);
//		base64Author = android.util.Base64.encodeToString(Author.getBytes(),
//				Base64.DEFAULT);
//		HttpMethod httpMethod = new HttpMethod(handlerMsg, httpHandler);
//	}
//
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "syncdatathread run()-->httpMethod="+httpMethod);
//		switch (httpMethod) {
//		case GET:
//			Header header[] = new Header[5];
//			header[0] = new BasicHeader("Accept", "ybox/2.2");
//			header[1] = new BasicHeader("X-YBox-Service", "doro580");
//			header[2] = new BasicHeader("x-ybox-hardwareid", prefs_imei);
//			header[3] = new BasicHeader("X-YBox-Phonemodel", "Doro 580");
//			header[4] = new BasicHeader("Authorization", "Basic " + base64Author.trim().toString());
//			httpMethod.httpGet(httpUrl, header);
//			break;
//		case POST:
//			break;
//		case PUT:
//			break;
//		default:
//			break;
//		}
//		
//		while(true){
//			
//		}
//
//	}
//
////	private String doGet(String URL, int handlerMsg, Handler handler) {
////		String getResult = null;
////		Log.d(TAG, "syncdatathread doGet()URL="+URL);
////		Log.d(TAG, "syncdatathread doGet()x-ybox-hardwareid="+prefs_imei);
////		Log.d(TAG, "syncdatathread doGet()base64Author="+base64Author.toString());
////		
////		HttpClient client = new DefaultHttpClient();
////		HttpGet httpGet = new HttpGet(URL);
////		httpGet.addHeader("Accept", "ybox/2.2");
////		httpGet.addHeader("X-YBox-Service", "doro580");
////		httpGet.addHeader("x-ybox-hardwareid", prefs_imei);
////		httpGet.addHeader("X-YBox-Phonemodel", "Doro 580");
////		httpGet.addHeader("Authorization", "Basic " + base64Author.trim().toString());
////		HttpResponse response = null;
////
////		try {
////			Log.d(TAG, "syncdatathread execute start");
////			response = client.execute(httpGet);
////			Log.d(TAG, "syncdatathread execute end");
////			int statusCode = response.getStatusLine().getStatusCode();
////			Log.d(TAG, "syncdatathread execute statusCode="+statusCode);
////			if (statusCode == 200) {
////				getResult = EntityUtils.toString(response.getEntity(), "utf-8");
////			} else {
////				getResult = null;
////			}
////			
////			Message msg = Message.obtain();
////			msg.what = handlerMsg;
////			msg.arg1 = statusCode;
////			msg.obj = getResult;
////			
////			Log.d(TAG, "syncdatathread execute getResult="+getResult);
////			handler.sendMessage(msg);
////		} catch (ClientProtocolException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		return getResult;
////	}
//}
