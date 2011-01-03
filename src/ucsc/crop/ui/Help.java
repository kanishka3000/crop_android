package ucsc.crop.ui;

import ucsc.crop.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends Activity{
	Button helpbutton;
	private void onBackClick() {
		Intent inten=new Intent(this, StartUp.class);
		startActivityForResult(inten, 0);
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		helpbutton=(Button)findViewById(R.id.helpback);
		helpbutton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				onBackClick();	
			}		
		});
		
		
		
	}
}
