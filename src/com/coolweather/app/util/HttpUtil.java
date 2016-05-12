package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class HttpUtil {

	/**
	 * ÇëÇóÊý¾Ý
	 * @param address
	 * @param listener
	 */
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connectioin = null;
				try {
					URL url = new URL(address);
					connectioin = (HttpURLConnection)url.openConnection();
					connectioin.setRequestMethod("GET");
					connectioin.setConnectTimeout(8000);
					connectioin.setReadTimeout(8000);
					InputStream in = connectioin.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					String line;
					StringBuilder response = new StringBuilder();
					while((line=reader.readLine()) != null)
					{
						response.append(line);
					}
					if(listener != null)
					{
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if(listener != null)
					{
						listener.onError(e);
					}
				}finally{
					if(connectioin != null)
					{
						connectioin.disconnect();
					}
				}
			}
		}).start();
	}
}
