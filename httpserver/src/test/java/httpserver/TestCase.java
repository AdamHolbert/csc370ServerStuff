package httpserver;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;

import holbert.adam.csc280.httpserver.HttpServer;
import holbert.adam.csc280.httpserver.ResponseObject;
import holbert.adam.csc280.httpserver.ServerThread;

public class TestCase {

	@Test
	public void REQUEST_COUNT_TEST() throws InterruptedException, ExecutionException {
		HttpServer server = new HttpServer(5, 8000);
		server.run();
		int REQUEST_COUNT = 200;
		
		ExecutorService svc = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("Thread #" + t.getId());
//				t.setDaemon(true);
				return t;
			}
		});
		Future[] futures = new Future[REQUEST_COUNT];
		
		//		ServerThread.ResponseStack = new Stack<ResponseObject>();
		for(int i = 0; i < REQUEST_COUNT; i++) {
			futures[i] = svc.submit(() -> {
				try {
				    URL index = new URL("http://localhost:" + server.PORT_NUMBER + "/index.html");
			        URLConnection indexConnection = index.openConnection();
			        BufferedReader in = new BufferedReader(
			                                new InputStreamReader(
			                                indexConnection.getInputStream()));
			        String inputLine;
			        
			        while ((inputLine = in.readLine()) != null) {
			        
			        }
			        in.close();
		        } catch (IOException e) {
		        	
		        }
			});
		}
		for(Future f : futures) {
			f.get();
		}
		server.shutdownNow();
		assertEquals(REQUEST_COUNT, server.ResponseStack.size());
	}

	@Test
	public void THREAD_COUNT_TEST() throws InterruptedException, ExecutionException {
		int THREAD_COUNT = 20;
		HttpServer server = new HttpServer(THREAD_COUNT , 8001);
		server.run();
		int REQUEST_COUNT = 2000;
		
		ExecutorService svc = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("Thread #" + t.getId());
//				t.setDaemon(true);
				return t;
			}
		});
		Future[] futures = new Future[REQUEST_COUNT];
		
		//		ServerThread.ResponseStack = new Stack<ResponseObject>();
		for(int i = 0; i < REQUEST_COUNT; i++) {
			futures[i] = svc.submit(() -> {
				try {
				    URL index = new URL("http://localhost:" + server.PORT_NUMBER + "/index.html");
			        URLConnection indexConnection = index.openConnection();
			        BufferedReader in = new BufferedReader(
			                                new InputStreamReader(
			                                indexConnection.getInputStream()));
			        String inputLine;
			        
			        while ((inputLine = in.readLine()) != null) {
			        
			        }
			        in.close();
		        } catch (IOException e) {
		        	
		        }
			});
		}
		for(Future f : futures) {
			f.get();
		}
		server.shutdownNow();
		HashMap<Long, Boolean> uniqueIDs = new HashMap<Long, Boolean>();
		for(ResponseObject r : server.ResponseStack) {
			uniqueIDs.put(r.respondingThread, true);
		}
		assertEquals(THREAD_COUNT, uniqueIDs.size());
	}
}
