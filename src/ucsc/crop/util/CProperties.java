package ucsc.crop.util;

import java.util.ResourceBundle;

public class CProperties {
	ResourceBundle bunn;
	static CProperties prop=null;
private CProperties() {
	 bunn=ResourceBundle.getBundle("android");	
}
public static CProperties getInstance(){
	if(prop==null){
		prop=new CProperties();
	}
	return prop;
}
public String getProperty(String key){
	return bunn.getString(key);
}

}
