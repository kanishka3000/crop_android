package ucsc.crop.ui;

import java.util.ArrayList;

import ucsc.crop.Crop;
import ucsc.crop.CropViewSorter;
import ucsc.crop.R;
import ucsc.crop.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class CropInformation extends Activity {
	ArrayList<Crop> cropResult;
	CropViewSorter sorter;
	GridView display;
	TextView topic;

	Button nextButton;
	Button previousButton;
	private void nextButtonClick() {
		
		sorter.next();
		topic.setText(sorter.getCurrentTopic());
		display.setAdapter(sorter);
	}
	private void prevButtonClick() {
		sorter.previous();
		topic.setText(sorter.getCurrentTopic());
		display.setAdapter(sorter);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		topic = (TextView) findViewById(R.id.topic);
		nextButton = (Button) findViewById(R.id.nextbutton);
		
		cropResult = LocationSelection.CropResult;
		sorter = new CropViewSorter(Crop.sort(Crop.SORTBY_CROP, cropResult),
				this);
		topic.setText(sorter.getCurrentTopic());
		display = (GridView) findViewById(R.id.gridtext);
		display.setAdapter(sorter);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				nextButtonClick();
			}

			
		});
		previousButton = (Button) findViewById(R.id.prevbutton);
		previousButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				prevButtonClick();

			}

			
		});
	}
}