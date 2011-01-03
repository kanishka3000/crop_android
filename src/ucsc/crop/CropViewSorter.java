package ucsc.crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import ucsc.crop.util.CProperties;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CropViewSorter extends BaseAdapter {

	Map<String, ArrayList<Crop>> map;
	Map<Integer, String> counter = new HashMap();
	Context context;
	int currentlocation = 0;

	int sortoption = 0;
	public static int SORTED_BY_CROP = 0;
	public static int SORTED_BY_LOCATION = 1;
	public String URLIMG=null;

	public CropViewSorter(Map<String, ArrayList<Crop>> map, Context con,
			int sortedby) {
		URLIMG=CProperties.getInstance().getProperty("server")+"/pics/CDM.png";
		this.sortoption = sortedby;
		context = con;
		this.map = map;
		int i = 0;
		for (String s : map.keySet()) {
			counter.put(i, s);
			System.out.println(s);
			i++;
		}
	}

	public String getCurrentTopic() {
		if (counter.keySet().size() == 0) {
			return "No Applicable data";
		} else if (sortoption == SORTED_BY_CROP) {
			return map.get(counter.get(currentlocation)).get(0).Name;
		} else {
			return map.get(counter.get(currentlocation)).get(0).Location;
		}
	}

	public void next() {
		if (currentlocation < counter.size() - 1) {
			currentlocation++;
		}
	}

	public void previous() {
		if (currentlocation > 0) {
			currentlocation--;
		}
	}

	@Override
	public int getCount() {
		if (map.size() > 0) {
			return map.get(counter.get(currentlocation)).size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return map.get(counter.get(currentlocation)).get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return map.get(counter.get(currentlocation)).get(arg0).hashCode();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LinearLayout lay = new LinearLayout(context);
		lay.setOrientation(LinearLayout.HORIZONTAL);
		ImageView imview=new ImageView(context);
		imview.setImageBitmap(map.get(counter.get(currentlocation)).get(arg0).cropimage);
		lay.addView(imview);
		TextView view = new TextView(context);
		view.setTextColor(Color.BLACK);
		view.setWidth(150);
		
		view.setPadding(10, 10, 10, 10);
		if (this.sortoption == SORTED_BY_CROP) {
			view
					.setText(map.get(counter.get(currentlocation)).get(arg0).Location);
		} else {
			view.setText(map.get(counter.get(currentlocation)).get(arg0).Name);
		}
		lay.addView(view);
		TextView view2 = new TextView(context);
		view2.setTextColor(Color.BLACK);
		view2.setPadding(10, 10, 10, 10);
		view2.setText(map.get(counter.get(currentlocation)).get(arg0).Price);
		
		
		String ids=map.get(counter.get(currentlocation)).get(arg0).Id;
//		try {
//			FileInputStream fi=context.openFileInput(ids+".png");
//			imview.setImageBitmap(BitmapFactory.decodeStream(fi));
//			System.out.println("image was found in local system");
//		} catch (FileNotFoundException e) {
//			try {
//				Bitmap mp=BitmapFactory.decodeStream(new HttpConnect().getString(URLIMG));
//				imview.setImageBitmap(mp);
//				FileOutputStream fi2=context.openFileOutput(ids,Context.MODE_WORLD_READABLE);
//				mp.compress(CompressFormat.PNG, 70, fi2);
//				fi2.flush();
//				fi2.close();
//			} catch (ClientProtocolException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		
		
		
		
		
		lay.addView(view2);
		
		
		return lay;
	}

}
