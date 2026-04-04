package org.example.service.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http请求工具类
 */
public class HttpUtil {

    private static final String REQUEST_METHOD_GET = "GET";

	private static final Integer CONNECT_TIME_OUT = 120000;

	/**
	 * http get请求 返回输出流
	 * @param url 请求链接
	 * @param headers 请求头
	 * @param response 响应
	 */
	public static OutputStream get(String url,
								   Map<String, Object> headers,
								   HttpServletResponse response) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoInput(true);
		con.setRequestMethod(REQUEST_METHOD_GET);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		for(Entry<String, Object> entry : headers.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			con.setRequestProperty(key, value);
		}
		con.connect();
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
		OutputStream os = response.getOutputStream();
		int responseCode = con.getResponseCode();
		byte[] buffer = new byte[1024];
		if(responseCode >=200 && responseCode <300) {
			int i = bis.read(buffer);
			while (( i != -1)) {
				os.write(buffer,0,i);
				i = bis.read(buffer);
			}
			bis.close();
		}
		bis.close();
		con.disconnect();
		return os;
	}
}
