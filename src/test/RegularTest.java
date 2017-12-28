package test;

import org.json.JSONArray;
import org.json.JSONObject;

import service.NLPServices;

public class RegularTest {
	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Name", "Abhinav");
		jsonObject.put("Designation", "Software Engineer");
		jsonObject.put("Age", 26);
		jsonObject.put("Hobbies", "Music");
		System.out.println(jsonObject.toString());
	}
}
