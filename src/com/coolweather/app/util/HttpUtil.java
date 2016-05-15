package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Build;

public class HttpUtil {

	/**
	 * 
	 * @param address
	 * @param listener
	 */
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.setProperty("http.keepAlive", "true");
				HttpURLConnection connectioin = null;
				InputStream in = null;
				BufferedReader reader = null;
				URL url = null;
				try {
					url = new URL(address);
					connectioin = (HttpURLConnection)url.openConnection();
					connectioin.setRequestMethod("GET");
					//connectioin.setDoOutput(true);
					//connectioin.setUseCaches(false);
					//connectioin.setRequestProperty("Content-type", "application/x-java-serialized-object");
					connectioin.setConnectTimeout(8000);
					connectioin.setReadTimeout(8000);
					if (Build.VERSION.SDK_INT > 13) {
						connectioin.setRequestProperty("Connection", "close");
					}
					connectioin.connect();
					in = connectioin.getInputStream();
					reader = new BufferedReader(new InputStreamReader(in));
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
					e.printStackTrace();
					if(listener != null)
					{
						listener.onError(e);
					}
				}finally{
					if(connectioin != null)
					{
						connectioin.disconnect();
					}
					if(in != null)
					{
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(reader != null)
					{
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
