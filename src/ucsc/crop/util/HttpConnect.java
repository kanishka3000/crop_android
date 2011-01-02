package ucsc.crop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpConnect {
	private String URL = null;

	public InputStream getString(String ip)
			throws ClientProtocolException, IOException {
		URL=ip;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL);
		HttpResponse response = client.execute(get);
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		return in;
//		char[] chars = new char[20];
//		StringBuffer buff = new StringBuffer();
//		while (reader.read(chars) != -1) {
//			buff.append(new String(chars));
//		}
//		return buff.toString();
	}
}
