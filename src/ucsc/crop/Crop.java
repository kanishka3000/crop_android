package ucsc.crop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ucsc.crop.util.CProperties;

import android.util.Log;

public class Crop {
	public static final int GET_FROM_CROP = 1;
	public static final int GET_FROM_LOCATION = 0;
	String URL ;
	public String Location;
	public String Price;
	public String Name;
	public String Id;

	public Crop() {
		URL = CProperties.getInstance().getProperty("server");
	}

	public ArrayList<Crop> getCropList() throws Exception {
		HttpConnect connection = new HttpConnect();
		InputStream in = connection.getString(URL += "service=list&id=crop");
		KXmlParser parse = new KXmlParser();
		ArrayList<Crop> croplist = new ArrayList<Crop>();
		parse.setInput(new InputStreamReader(in));
		parse.nextTag();
		parse.require(XmlPullParser.START_TAG, null, "list");
		while (parse.nextTag() == XmlPullParser.START_TAG) {
			Crop crop = new Crop();
			parse.require(XmlPullParser.START_TAG, null, "crop");
			parse.nextTag();
			parse.require(XmlPullParser.START_TAG, null, "id");
			String id = parse.nextText();
			crop.Id = id;
			parse.nextTag();
			String name = parse.nextText();
			crop.Name = name;
			croplist.add(crop);
			parse.nextTag();
		}
		parse.require(XmlPullParser.END_TAG, null, "list");
		return croplist;
	}

	public List getCrops(int type, String value) throws Exception {
		if (type == GET_FROM_CROP) {
			URL += "service=crop&id=" + value;
		} else if (type == GET_FROM_LOCATION) {
			URL += "service=location&id=" + value;
		}
		System.out.println(URL);
		HttpConnect conn = new HttpConnect();
		InputStream cropString = conn.getString(URL);
		List<Crop> croplist = parseXml(cropString);
		return croplist;
	}

	private List<Crop> parseXml(InputStream cropString)
			throws XmlPullParserException, IOException {
		KXmlParser parse = new KXmlParser();
		List<Crop> croplist = new ArrayList<Crop>();
		parse.setInput(new BufferedReader(new InputStreamReader(cropString)));
		parse.nextTag();
		parse.require(XmlPullParser.START_TAG, null, "crops");
		while (parse.nextTag() == XmlPullParser.START_TAG) {
			Crop crop = new Crop();
			parse.require(XmlPullParser.START_TAG, null, "crop");
			parse.nextTag();
			String location = parse.nextText();
			crop.Location = location;
			parse.nextTag();
			String price = parse.nextText();
			crop.Price = price;
			parse.nextTag();
			String name = parse.nextText();
			crop.Name = name;
			parse.nextTag();
			croplist.add(crop);
			parse.require(XmlPullParser.END_TAG, null, "crop");
		}
		return croplist;
	}

	public String toString() {
		return this.Name;

	}

}
