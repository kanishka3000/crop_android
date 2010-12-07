package ucsc.crop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CropInformation extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Crop crop=new Crop();
        try{
        crop.getCrops(Crop.GET_FROM_LOCATION, "Dambulla");
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}