package cn.fanfan.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/*
 * GetInformation(String url)get方式获取网络图片，参数自己加入url
 * 
 * 
 * PostInformation(String url, Map<String, String> map)方式获取网络图片
 * 
 * 判断返回值是否为null，若为null则可以直接告诉用户访问数据出错
 * NetLoad netLoad = new NetLoad();
 * 如：String result = netLoad.GetInformation(url);
 * if(result == null){
 *     System.out.println("访问数据出错");
 * }else{
 *     处理返回结果
 * }
 * 
 */
public class NetLoad {
	public static CookieStore cookieStore;

	public String GetInformation(String url) throws ClientProtocolException,
			IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		if (status == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
			return null;
		} else {
			return null;
		}
	}

	public String PostInformation(String url, Map<String, String> map)
			throws ClientProtocolException, IOException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Set<Map.Entry<String, String>> set = map.entrySet();
		Iterator<Map.Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
					.next();
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		cookieStore = new BasicCookieStore();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse = client.execute(post);
		int status = httpResponse.getStatusLine().getStatusCode();
		if (status == 200) {
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
			return null;
		} else {
			return null;
		}
	}
}
