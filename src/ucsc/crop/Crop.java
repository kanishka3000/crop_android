package ucsc.crop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class Crop {
	public static final int GET_FROM_CROP = 1;
	public static final int GET_FROM_LOCATION = 0;
	String URL = "http://112.135.95.142:8080/myaxis/getCropService?";

	public Crop() {

	}

	public Crop[] getCrops(int type, String value) throws Exception {
		URL = "";
		if (type == GET_FROM_CROP) {
			URL += "service=crop&id=" + value;
		} else if (type == GET_FROM_LOCATION) {
			URL += "service=location&id=" + value;
		}
		HttpConnect conn = new HttpConnect();
		InputStream cropString = conn.getString(URL);
		KXmlParser parse = new KXmlParser();
		parse.setInput(new BufferedReader(new InputStreamReader(cropString)));
		parse.nextTag();
		parse.require(XmlPullParser.START_TAG, null, "crops");
		while (parse.nextTag() == XmlPullParser.START_TAG) {
			parse.require(XmlPullParser.START_TAG, null, "crop");
			parse.nextTag();
			String location = parse.nextText();
			System.out.println(location);
			parse.nextTag();
			String price = parse.nextText();
			System.out.println(price);
			parse.nextTag();
			String name = parse.nextText();
			System.out.println(name);
		}

		return null;
	}

}
