package ucsc.crop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ucsc.crop.util.CProperties;
import ucsc.crop.util.HttpConnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class Crop {
	public static final int GET_FROM_CROP = 1;
	public static final int GET_FROM_LOCATION = 0;

	public static final String CROP_VALUES = "crop";
	public static final String LOCATION_VALUES = "location";
	public static final String PRICE_VALUES = "price";
	public static final String PRICE_TYPE = "pricetype";

	public static final String LIST_SERVICE = "service";
	
	public Bitmap cropimage=null;
	String URLIMG=null;

	public static final int SORTBY_CROP = 1;
	public static final int SORTBY_LOCATION = 2;

	String URL;
	public String Location;
	public String Price;
	public String Name;
	public String Id;

	public Crop() {
		URL = CProperties.getInstance().getProperty("server")+"/getCropService?";
		URLIMG=CProperties.getInstance().getProperty("server")+"/pics/";
	}

	public static Map<String, ArrayList<Crop>> sort(int method,
			ArrayList<Crop> crops) {
		Map<String, ArrayList<Crop>> map = new HashMap<String, ArrayList<Crop>>();
		for (Crop crop : crops) {
			if (method == SORTBY_CROP) {
				String cropname = crop.Name;
				System.out.println(cropname);
				fillcropsort(map, crop, cropname);
			} else {
				String croplocation = crop.Location;
				System.out.println(croplocation);
				fillcropsort(map, crop, croplocation);
			}
		}

		return map;
	}

	private static void fillcropsort(Map<String, ArrayList<Crop>> map,
			Crop crop, String cropname) {
		if (!map.containsKey(cropname)) {
			map.put(cropname, new ArrayList<Crop>());
		}
		map.get(cropname).add(crop);
	}

	public ArrayList<Crop> getCropList() throws Exception {
		URL += LIST_SERVICE + "=" + "list&id=crop";
		HttpConnect connection = new HttpConnect();
		InputStream in = connection.getString(URL);
		ArrayList<Crop> croplist = parseXMLforList(in);
		System.out.println(croplist);
		return croplist;
	}

	public ArrayList<Crop> getCropValue(String crop_list[],
			String[] location_list, String price, String price_type)
			throws Exception {
		HttpConnect connection = new HttpConnect();
		boolean first = true;
		if (crop_list != null && crop_list.length > 0) {
			first = false;
			URL += (CROP_VALUES + "=");
			filURL(crop_list);
		}
		if (location_list != null && location_list.length > 0) {
			if (!first)
				URL += "&";
			URL += (LOCATION_VALUES + "=");
			filURL(location_list);
		}
		if (price != null) {
			if (!first)
				URL += "&";
			URL += PRICE_VALUES + "=" + price + "&" + PRICE_TYPE + "="
					+ price_type;
		}
		System.out.println(URL);
		InputStream in = connection.getString(URL);

		ArrayList<Crop> crops = parseXml(in);

		// ArrayList<Crop> croplist = parseXMLforList(in);
		return crops;
	}

	private ArrayList<Crop> parseXMLforList(InputStream in)
			throws XmlPullParserException, IOException {
		KXmlParser parse = new KXmlParser();
		ArrayList<Crop> croplist = new ArrayList<Crop>();
		Crop cr = new Crop();
		cr.Id = "NA";
		cr.Name = "NA";
		cr.Location = "NA";
		cr.Price = "NA";
		croplist.add(cr);

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

	private void filURL(String[] crop_list) {
		int i = 0;
		for (String s : crop_list) {
			URL += s.trim();
			if (i != crop_list.length - 1) {
				URL += ",";
			}
		}
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

	private ArrayList<Crop> parseXml(InputStream cropString)
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
			String id = parse.nextText();
			crop.Id = id;		
			parse.nextTag();
			String name = parse.nextText();
			crop.Name = name;
			parse.nextTag();
			fixImage(crop);
			
			croplist.add(crop);
			parse.require(XmlPullParser.END_TAG, null, "crop");
		}
		
		return (ArrayList<Crop>) croplist;
	}

	private void fixImage(Crop crop) throws ClientProtocolException,
			IOException {
		System.out.println(URLIMG+crop.Id+".png");
		crop.cropimage=BitmapFactory.decodeStream(new HttpConnect().getString(URLIMG+crop.Id+".png"));
		
//		File fi=new File("croidm");
//		FileOutputStream ou= new FileOutputStream(fi);
//		crop.cropimage.compress(CompressFormat.PNG, 75, ou);
//		if(!fi.exists()){
//			ou.flush();
//			ou.close();
//		}
		
	}

	public String toString() {
		return this.Name;

	}

}
