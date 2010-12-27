package ucsc.crop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CropViewSorter extends BaseAdapter {

	Map<String, ArrayList<Crop>> map;
	Map<Integer, String> counter = new HashMap();
	Context context;
	int currentlocation = 0;

	public CropViewSorter(Map<String, ArrayList<Crop>> map, Context con) {
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
		return map.get(counter.get(currentlocation)).get(0).Name;
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
		return map.get(counter.get(currentlocation)).size();
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
		TextView view = new TextView(context);
		view.setPadding(10, 10, 10, 3);
		view.setText(map.get(counter.get(currentlocation)).get(arg0).Location);
		lay.addView(view);
		TextView view2 = new TextView(context);
		view2.setText(map.get(counter.get(currentlocation)).get(arg0).Price);
		lay.addView(view2);
		return lay;
	}

}
