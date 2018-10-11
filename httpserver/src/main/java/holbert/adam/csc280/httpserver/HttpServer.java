package holbert.adam.csc280.httpserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class HttpServer {
	
	private static final int THREAD_POOL_SIZE = 5;
	private static final int PORT_NUMBER = 8080;
	
	public static void main(String[] args) throws IOException {
		ServerSocket servSoc = null;
		try {
			servSoc = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Starting server on localhost:" + PORT_NUMBER + "/index.html");
		
		String fileDir = System.getProperty("user.dir") + "\\src\\main\\resources\\project2";
		SocketIOManager sockIo = new SocketIOManager();
				
		ExecutorService svc = Executors.newFixedThreadPool(THREAD_POOL_SIZE, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("Thread #" + t.getId());
				return t;
			}
		});
		
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			svc.submit(new ServerThread(fileDir, servSoc, sockIo));
		}
//		server.run();
	}
}