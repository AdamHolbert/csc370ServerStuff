package holbert.adam.csc280.httpserver;

import java.util.HashMap;

public class Calculator {
	public static double add(double a, double b) {
		return a + b;
	}
	
	public static double subtract(double a, double b) {
		return a - b;
	}
	
	public static double multiply(double a, double b) {
		return a * b;
	}
	
	public static double divide(double a, double b) {
		return a/b;
	}

	public static HashMap<String, String> parsePath(String CMDPath) {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("CMD", CMDPath);
		values.put("op", CMDPath.replace("/calc/", "").replaceFirst("\\?.*", ""));
		String[] operands = CMDPath.replaceFirst(".*\\?", "").split("&");
		for(String s : operands) {
			String[] kv = s.split("=");
			values.put(kv[0], kv[1]);
		}
		return values;
	}
}
