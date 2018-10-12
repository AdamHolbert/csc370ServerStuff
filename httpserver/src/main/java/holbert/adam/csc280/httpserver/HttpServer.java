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
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class HttpServer {
	
	public final int THREAD_POOL_SIZE;
	public final int PORT_NUMBER;
	private ExecutorService svc;
	private ServerSocket servSoc;
	public final Stack<ResponseObject> ResponseStack;
	
	public static void main(String[] args) throws IOException {
		(new HttpServer(5, 8080)).run();
	}
	
	public HttpServer() {
		this(5, 8080);
	}
	
	public HttpServer(int THREAD_POOL_SIZE, int PORT_NUMBER) {
		this.THREAD_POOL_SIZE = THREAD_POOL_SIZE;
		this.PORT_NUMBER = PORT_NUMBER;
		ResponseStack = new Stack<ResponseObject>();
	}
	
	public void run() {
		servSoc = null;
		try {
			servSoc = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Starting server on localhost:" + PORT_NUMBER + "/index.html");
		
		String fileDir = System.getProperty("user.dir") + "\\src\\main\\resources\\project2";
		SocketIOManager sockIo = new SocketIOManager();
				
		svc = Executors.newFixedThreadPool(THREAD_POOL_SIZE, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("Thread #" + t.getId());
				return t;
			}
		});
		
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			svc.submit(new ServerThread(ResponseStack, fileDir, servSoc, sockIo));
		}
//		server.run();
	}

	public void shutdownNow() {
		svc.shutdownNow();
		try {
			servSoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}