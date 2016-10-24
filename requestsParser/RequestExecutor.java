package requestsParser;

import java.net.InetAddress;
import java.util.ArrayList;

import global.GlobalVariables;
import global.HasRegisteredException;
import global.User;
import server.ServerManager;
import server.ServerSendingThread;

public class RequestExecutor extends Thread{
	private String rawMsg;
	private Request req; 
	private String[] fields; 
	private ServerManager myManager;
	
		
	
	public RequestExecutor(Request request, ServerManager manager){
		req = request;
		myManager = manager;
		rawMsg = req.getMessage();
	}
	
	public String[] returnArgs(){
		return fields;
	}
	
	public void processRequest() throws HasRegisteredException, InterruptedException{
		String type = new String();		
		String[] args = fields;
		type = args[0];
		Integer index;
		switch(type){
		case GlobalVariables.REGISTER_ACTION:
			index = new Integer(args[1]);
			User newUser = new User(args[2], req.getSenderAddr() , (int)new Integer(args[4]));
			if(!myManager.hasUser(req.getSenderAddr(),args[2]) && (myManager.getNumOfUser() < 5)){	
				newUser.logHistoryRequest(req, index);
				myManager.registerClient(newUser);
				String responce = new String(GlobalVariables.REGISTER_SUCCESS + GlobalVariables.delimiter + index.toString()); 
				new ServerSendingThread(myManager.getServerSendingPort(), newUser, responce).start();
			}
			else if((myManager.getNumOfUser() >= 5)){
				String responce = new String(GlobalVariables.REGISTER_DENIED + GlobalVariables.delimiter + index.toString() + GlobalVariables.delimiter + 
						myManager.getNextServerAddr().toString() + GlobalVariables.delimiter + new Integer(myManager.getNextServerPort()).toString());
				new ServerSendingThread(myManager.getServerSendingPort(), newUser, responce).start();
			}
			else{
				String responce = new String(GlobalVariables.REGISTER_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), newUser, responce).start();
			}
			break;
		case GlobalVariables.PUBLISH_ACTION:
			index = new Integer(args[1]);
			int tar = myManager.getUserInList(args[2], req.getSenderAddr());
			boolean status = (Boolean) null;
			if(args[4].equalsIgnoreCase("on")){
				status = true;
			}
			else if(args[4].equalsIgnoreCase("off")){
				status = false;
			}
			else{
				String responce = new String(GlobalVariables.PUBLISH_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar), responce).start();
			}
			
			if(tar != -1){
				myManager.getUserList().get(tar).setReceivePort(new Integer(args[3]));
				myManager.getUserList().get(tar).setAvaliability(status);
				ArrayList<User> listOfConnections = new ArrayList<User>();
				//TODO
			}
		}
	

	}


	

	
	public void run(){
		System.out.println("The Request has been received");
		System.out.println(rawMsg);
		fields = rawMsg.split(GlobalVariables.token);
		if(fields.length == 1){
			try {
				throw new Exception("Bad Input, no delimiter identified");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			processRequest();
		} catch (HasRegisteredException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
