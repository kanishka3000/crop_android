package ucsc.crop;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class crop {
public crop() {
	HttpConnect conn=new HttpConnect();
	try {
		String res= conn.getCropString(HttpConnect.CROP_CROP, "a");
		
		System.out.println(res);
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
