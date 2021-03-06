package ucsc.crop.ui;

import ucsc.crop.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class StartUp extends Activity{
	ImageButton SearchLink;
	ImageButton HelpLink;
	private void onSearchClick() {
		Intent toSearch=new Intent(this, Selection.class);
		startActivityForResult(toSearch, 0);
		
	}
	private void onHelpClick() {
		Intent toSearch=new Intent(this, Help.class);
		startActivityForResult(toSearch, 0);
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);
		SearchLink=(ImageButton)findViewById(R.id.widget71);
		SearchLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onSearchClick();
				
			}
		
		});
		HelpLink = (ImageButton)findViewById(R.id.widget70);
		HelpLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onHelpClick();
				
			}

		
		});
		
	}
}
