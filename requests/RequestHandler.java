package requests;

public class RequestHandler {
	static final String delimiter = new String("(?<!%a)%_");
	String message; 
	String[] fields;
	
	public RequestHandler(String msg){
		message = msg;
		fields = message.split(delimiter);
	}
	
	public String[] processString() throws Exception{
		if(fields.length == 1){
			throw new Exception("Bad Input, no delimiter identified");
		}
		return fields;
	}
	
	public String getRequestType(){
		return fields[0];
	}
	
	public int getNumFields(){
		return fields.length;
	}
	
	public String returnRequestRaw(){
		return message;
	}
	
}
