package utils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import serviceImpl.UserServiceImpl;

public class cafeUtils {
	private cafeUtils() {
		
	}

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	
	public static ResponseEntity<String> getResponseEntity(String responseMsg, HttpStatus httpstatus){
		return new ResponseEntity<String>("{\"error message\":\""+ responseMsg +"\"}",httpstatus);

	}
	
	public static String getUUID() {
		Date date= new Date();
		long time = date.getTime();
		return "Bill-"+time;	
		}
	
	public static JSONArray getJsonFromString(String data) throws JSONException{
		JSONArray jsonArray = new JSONArray(data);
		return jsonArray;
		
	}
	
	public static Map<String,Object> getMapFromJson(String data){
		if (!Strings.isNullOrEmpty(data)) {
			return new Gson().fromJson(data,new TypeToken<Map<String,Object>>(){
				
			}.getType());
				}
		return new HashMap<>();	
	}
	
	public static Boolean isFileExist(String path) {
		logger.info("inside isFileExist {}",path);
		try {
			File file = new File(path);
			return (file !=null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}
