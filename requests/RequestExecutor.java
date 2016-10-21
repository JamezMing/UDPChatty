package requests;
import global.GlobalVariables;

public class RequestExecutor implements Runnable{
	String requestStr; 
	RequestHandler handler;
	
	public RequestExecutor(String request){
		requestStr = request;
		handler = new RequestHandler(requestStr);
	}
		
	public void run(){
		String type = handler.getRequestType();
		switch (type) {
		case GlobalVariables.REGISTER_ACTION:
			
		 
		
		}
			 
		
	}
}
