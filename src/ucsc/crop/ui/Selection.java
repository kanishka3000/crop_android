package ucsc.crop.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ucsc.crop.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Selection extends Activity {
	Spinner locationselect;
	Spinner cropselect;
	EditText pricetext;
	RadioButton lessthan;
	RadioButton greaterthan;
	RadioGroup gp;
	Button okbutton;
	ListView cropview;
	ListView locationview;
	ProgressDialog progressDialog;

	ArrayList listoflocations = null;
	ArrayList listofcrops = null;
	Activity sele;
	Set<String> selectedCrops;
	Set<String> selectedLocations;

	String pricetype = "lt";
	String[] selectedcroparray = null;
	String[] selectedlocationarray = null;
	String price = null;
	Set<Crop> listofcropsforview;
	Crop crops;
	Activity context;

	static ArrayList<Crop> CropResult;

	public Selection() {
		selectedCrops = new HashSet();
		selectedLocations = new HashSet();
		listofcropsforview = new HashSet<Crop>();
		crops = new Crop();
		context = this;
	}

	public boolean selectev = true;

	public void onResume() {
		super.onResume();
		selectedCrops.clear();
		selectedLocations.clear();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuselection, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		int id=item.getItemId();
		switch (id){
		case R.id.exitmenu:
			exit();
		return true;
		
		}
		return true;
	}
	private void exit() {
		onOkClick(okbutton);
	}


	private void onListViewLocationClick(AdapterView<?> arg0, int arg2) {
		selectedLocations.remove((String) arg0.getItemAtPosition(arg2));
		locationview.setAdapter(new ArrayAdapter<String>(this, R.xml.ress,
				new ArrayList(selectedLocations)));
	}

	private void onViewCropListSelected(AdapterView<?> arg0, int arg2) {
		Crop cr = (Crop) arg0.getItemAtPosition(arg2);
		selectedCrops.remove(cr.Id);
		listofcropsforview.remove(cr);
		cropview.setAdapter(new ArrayAdapter<Crop>(this, R.xml.ress,
				new ArrayList(listofcropsforview)));
	}

	private void onLocationSelected(AdapterView<?> arg0, int arg2) {
		String location = (String) arg0.getItemAtPosition(arg2);
		System.out.println(location + "locationselect");
		if (!location.equals("NA")) {
			selectedLocations.add(location);
			locationview.setAdapter(new ArrayAdapter<String>(this, R.xml.ress,
					new ArrayList(selectedLocations)));
		}
	}

	private void onCropSelected(AdapterView<?> arg0, int arg2) {
		Crop cr = (Crop) arg0.getItemAtPosition(arg2);
		System.out.println(cr.Id + "was selected" + cr.Name);
		if (cr.Id != "NA") {
			selectedCrops.add(cr.Id);
			listofcropsforview.add(cr);
			cropview.setAdapter(new ArrayAdapter<Crop>(this, R.xml.ress,
					new ArrayList(listofcropsforview)));
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		sele = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationselection);

		locationselect = (Spinner) findViewById(R.id.cropspinner);
		cropselect = (Spinner) findViewById(R.id.locationspinner);
		pricetext = (EditText) findViewById(R.id.pricetext);
		gp = (RadioGroup) findViewById(R.id.RadioGroup01);
		lessthan = (RadioButton) findViewById(R.id.lessthan);
		greaterthan = (RadioButton) findViewById(R.id.greaterthan);
		cropview = (ListView) findViewById(R.id.croplister);
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
		cropview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onViewCropListSelected(arg0, arg2);

			}
		});
		locationview = (ListView) findViewById(R.id.locationlister);
		locationview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				onListViewLocationClick(arg0, arg2);
			}

		});

		okbutton = (Button) findViewById(R.id.selectbutton);
		okbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onOkClick(arg0);

			}

		});

		cropselect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				onCropSelected(arg0, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		locationselect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				onLocationSelected(arg0, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		final Handler fetchAndDisplayLocationData = new Handler() {
			public void handleMessage(Message message) {
				System.out.println("hi" + listoflocations);
				ArrayAdapter adapter = new ArrayAdapter(sele, R.xml.ress,
						listoflocations);
				locationselect.setAdapter(adapter);
				progressDialog.setMessage("Loaded Locations");
				progressDialog.setProgress(50);

			}
		};
		final Handler fetchAndDisplayCropData = new Handler() {
			public void handleMessage(Message message) {

				ArrayAdapter adapter = new ArrayAdapter(sele, R.xml.ress,
						listofcrops);
				cropselect.setAdapter(adapter);
				progressDialog.setMessage("Loaded Crops");
				progressDialog.setProgress(100);
				progressDialog.dismiss();
			}
		};

		AsyncTask<Object, Object, Object> fetchLocationData = new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... arg0) {

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

				Crop cr = new Crop();
				ArrayList<Crop> list2 = null;
				try {
					list2 = cr.getCropList();
					listofcrops = list2;
					fetchAndDisplayCropData.sendMessage(fetchAndDisplayCropData
							.obtainMessage());
				} catch (Exception e) {

					e.printStackTrace();
				}

				return null;
			}

		};
		fetchLocationData.execute(null, null, null);

	}

	final Handler displayandProgress = new Handler() {
		public void handleMessage(Message message) {
			Intent nextin = new Intent(context, CropInformation.class);
			startActivityForResult(nextin, 0);
		}

	};

	private void onOkClick(View arg0) {

		if (greaterthan.isChecked()) {
			pricetype = "gt";
		} else {
			pricetype = "lt";
		}
		if (!pricetext.getText().toString().equals("0")) {
			price = pricetext.getText().toString();
		}

		if (selectedCrops.size() > 0) {
			selectedcroparray = selectedCrops.toArray(new String[1]);
		}
		if (selectedLocations.size() > 0) {
			selectedlocationarray = selectedLocations.toArray(new String[1]);
		}
		if (selectedcroparray == null && selectedlocationarray == null
				&& price == null) {
			System.out.println("not selected");
			return;
		}
		progressDialog.show();
		progressDialog.setProgress(30);
		try {
			AsyncTask<Object, Object, Object> fetchCropPrice = new AsyncTask<Object, Object, Object>() {
				@Override
				protected Object doInBackground(Object... arg0) {
					ArrayList<Crop> cp = null;
					try {
						cp = crops.getCropValue(selectedcroparray,
								selectedlocationarray, price, pricetype);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(cp);
					progressDialog.setProgress(100);
					progressDialog.dismiss();
					CropResult = cp;
					displayandProgress.sendMessage(displayandProgress
							.obtainMessage());
					return null;
				}

			};
			fetchCropPrice.execute(null, null, null);

		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
