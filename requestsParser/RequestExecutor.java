package requestsParser;
import global.GlobalVariables;

public class RequestExecutor extends Thread{
	private String rawMsg;
	private Request req; 
	private String[] fields; 
		
	
	public RequestExecutor(Request request){
		req = request;
		rawMsg = req.getMessage();
	}
	
	public String[] returnArgs(){
		return fields;
	}
	
	
	public void run(){
		System.out.println("The Request has been received");
		System.out.println(rawMsg);
		fields = rawMsg.split(GlobalVariables.token);
		for(String s:fields){
			System.out.println(s);
		}
		if(fields.length == 1){
			try {
				throw new Exception("Bad Input, no delimiter identified");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
