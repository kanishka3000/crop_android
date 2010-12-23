package ucsc.crop.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ucsc.crop.*;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LocationSelection extends Activity {
	Spinner locationselect;
	ArrayList listoflocations = null;
	Activity sele;
	public boolean selectev = true;

	public void onCreate(Bundle savedInstanceState) {
		sele = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationselection);
		locationselect = (Spinner) findViewById(R.id.locationspinner);
		final Handler fetchAndDisplayLocationData = new Handler() {
			public void handleMessage(Message message) {
				System.out.println("hi" + listoflocations);
				ArrayAdapter adapter = new ArrayAdapter(sele, R.xml.ress,
						listoflocations);
				locationselect.setAdapter(adapter);
			}
		};
		AsyncTask<Object, Object, Object> fetchLocationData = new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... arg0) {
				if (!selectev) {
					Location lo = new Location();
					ArrayList list = null;
					try {
						list = lo.getLocation();
						listoflocations = list;
						fetchAndDisplayLocationData
								.sendMessage(fetchAndDisplayLocationData
										.obtainMessage());

					} catch (Exception e) {

						e.printStackTrace();
					}
				} else {

					Crop cr = new Crop();
					ArrayList<Crop> list = null;
					try {
						list = cr.getCropList();
						listoflocations = list;
						fetchAndDisplayLocationData
								.sendMessage(fetchAndDisplayLocationData
										.obtainMessage());
					} catch (Exception e) {
						
						e.printStackTrace();
					}

				}

				return null;
			}

		};
		fetchLocationData.execute(null, null, null);

	}
}
