package holbert.adam.csc280.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SocketIOManager {
	
	public Map<String, String> readHttpRequestHeaders(InputStream in) throws IOException {
		String rawHeaders = readToTerminalSymbol(in, String.format("%n%n"));
		Map<String, String> headers = new HashMap<>();
		String[] headersArray = rawHeaders.split(String.format("%n"));
		
		headers.put("CMD", headersArray[0]);
		for(int i = 1; i < headersArray.length; i++) {
			String[] kvArr = headersArray[i].split(":");
			if(kvArr.length > 1) {
				headers.put(kvArr[0].trim(), kvArr[1].trim());
			} else {
				headers.put(kvArr[0].trim(), "");
			}
		}
		return headers;
	}
	
	public byte[] readHttpRequestBody(InputStream in, String value) {
		int size = 0;
		if(value.matches("\\d+")) {
			size = Integer.parseInt(value);
		}
		byte[] container = new byte[size];
		try {
			in.read(container);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return container;
	}
	
	public void writeHttpResponceHeaders(OutputStream out, Map<String, String> headers) throws IOException {
		String body = headers.get("CMD") + "\n";
		headers.remove("CMD");
		for(String h : headers.keySet()) {
			body += h + ": " + headers.get(h) + "\n";
		}
		body += "\n";
		out.write(body.getBytes());
	}
	
	public void writeHttpResponceBody(OutputStream out, byte[] body) throws IOException {
		if(body != null) {			
			out.write(body);
		}
	}
	
	public String readToTerminalSymbol(InputStream in, String ts) throws IOException {
		String returnString = "";
		int b;
		while(returnString.length() == returnString.replace(ts, "").length() && (b = in.read()) != -1) {
			returnString += (char) b;
		}
		return returnString;
	}
	
	public String readKnownLength(InputStream in, int size) {
		
		return null;
	}
	
	public String valueInSocket(InputStream in) throws IOException{
		byte[] value = new byte[in.available()];
		in.read(value);
		return new String(value);
	}
	
	public static String makeHTML(String text) {
		return "<html><body><h1>" + text + "</h1></body></html>";
	}
}
