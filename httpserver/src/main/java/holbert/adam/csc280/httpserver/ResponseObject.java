package holbert.adam.csc280.httpserver;

import java.time.LocalDateTime;

public class ResponseObject {
	public final LocalDateTime time;
	public final long respondingThread;
	public final String requestHeader;
	
	public ResponseObject(long l, String requestHeader) {
		this.time = LocalDateTime.now();
		this.respondingThread = l;
		this.requestHeader = requestHeader;
	}
}
