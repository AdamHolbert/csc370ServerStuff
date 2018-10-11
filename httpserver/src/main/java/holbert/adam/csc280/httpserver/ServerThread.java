package holbert.adam.csc280.httpserver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ServerThread extends Thread {
	
	private String fileDir;
	private ServerSocket servSoc;
	private SocketIOManager sockIo;
	private static Object portLock = new Object();
	private static Object stackLock = new Object();
	public static Stack ResponseStack = new Stack();
	
	
	public ServerThread(String fileDir, ServerSocket servSoc, SocketIOManager sockIo) {
		this.fileDir = fileDir;
		this.servSoc = servSoc;
		this.sockIo = sockIo;
	}
	
	public void run() {
		while (true) {
			try(Socket socket = servSoc.accept()){
				
				// Read http request
				Map<String, String> headers = sockIo.readHttpRequestHeaders(socket.getInputStream());
				
				// Determine request type
				RequestType requestMethod = determineRequestType(headers);
				String CMDPath = headers.get("CMD").split("\\s+")[1];
				String fileDir = this.fileDir + CMDPath;
				System.out.println("Thread #"+this.getId() + "| " + LocalDateTime.now() + " request for file '" + CMDPath + "'.");
				ResponseObject ro = new ResponseObject(this.getId(), CMDPath);
				synchronized (stackLock) {
					ResponseStack.add(ro);
				}
				
				boolean successfulWrite = true;
				if(requestMethod == RequestType.POST) {
					byte[] requestBody = sockIo.readHttpRequestBody(socket.getInputStream(), headers.get("Content-Length"));
					successfulWrite = writeFile(requestBody, fileDir);
				}
				
				// Take action (load file, etc.)
				byte[] file = null;
				HashMap<String, String> responseHeaders = new HashMap<String, String>();
				responseHeaders.put("CMD", "HTTP/1.0 200 OK");
				switch(requestMethod) {
				case GET:
					if(CMDPath.matches("/calc/.*")) {
						HashMap<String, String> values = Calculator.parsePath(CMDPath);
						try {			
							double a = Double.parseDouble(values.get("operand1"));
							double b = Double.parseDouble(values.get("operand2"));
							
							switch (values.get("op")) {
							case "add":
								file = SocketIOManager.makeHTML(Double.toString(Calculator.add(a, b))).getBytes();
								break;
							case "subtract":
								file = SocketIOManager.makeHTML(Double.toString(Calculator.subtract(a, b))).getBytes();
								break;
							case "multiply":
								file = SocketIOManager.makeHTML(Double.toString(Calculator.multiply(a, b))).getBytes();
								break;
							case "divide":
								file = SocketIOManager.makeHTML(Double.toString(Calculator.divide(a, b))).getBytes();
								break;
							default:
								responseHeaders.put("CMD", "HTTP/1.0 404 Not Found");
								file = SocketIOManager.makeHTML("Operation not found").getBytes();
								break;
							}
						} catch (Exception e){
							e.printStackTrace(System.err);
							responseHeaders.put("CMD", "HTTP/1.0 500 Not Found");
							file = sockIo.makeHTML("Variables couldn't be parsed").getBytes();
						}
					} else {
						file = getFile(fileDir);
						responseHeaders.put("Content-type", findContentType(headers.get("CMD")));
						
						if(file == null) {
							responseHeaders.put("CMD", "HTTP/1.0 404 Not Found");
						}
					}
					break;
				case POST: // FILENAME
					if(!successfulWrite) {
						responseHeaders.put("CMD", "HTTP/1.0 404 Not Found");
					}
					break;
				default:
					break;
				}
				// Create response
				// Write response
				synchronized (portLock) {
					sockIo.writeHttpResponceHeaders(socket.getOutputStream(), responseHeaders);
					sockIo.writeHttpResponceBody(socket.getOutputStream(), file);	
				}
			}
			catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	private HashMap<String, Integer> parsePath(String cMDPAth) {
		// TODO Auto-generated method stub
		return null;
	}

	private String findContentType(String CMD) {
		String type;
		switch (CMD.split("\\s+")[1].replaceFirst(".*\\.", "")) {
		case "html":
			type = "text/html"; 
			break;
		case "css":
			type = "text/css"; 
			break;
		case "jpg":
			type = "image/jpeg"; 
			break;
		case "png":
			type = "image/png"; 
			break;
		case "js":
			type = "application/javascript";
			break;
		default:
			type = "application/bin";
			break;
		}
		return type;
	}

	private byte[] getFile(String fileDir) {
		byte[] content = null;
		File tempDir = new File(fileDir);
		if(tempDir.exists() && !tempDir.isDirectory()) {
			content = new byte[(int) tempDir.length()];
			try(FileInputStream fIn = new FileInputStream(fileDir)){
				fIn.read(content);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
		
	}

	private boolean writeFile(byte[] requestBody, String fileDir) {
		File tempFile = new File(fileDir);
		if(requestBody != null) {
			try {
				if(!tempFile.exists()) {
						tempFile.createNewFile();
				}
				try(FileOutputStream fout = new FileOutputStream(fileDir)){
						try(BufferedOutputStream br = new BufferedOutputStream(fout)){
						br.write(requestBody);
						return true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Post had a null body");
		}
		return false;
	}

	private RequestType determineRequestType(Map<String, String> headers) {
		String[] CMDArray = headers.get("CMD").split("\\s+");
		RequestType request = RequestType.valueOf(CMDArray[0]);
		return request;
	}

}
