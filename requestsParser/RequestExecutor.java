package requestsParser;

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
	
	public void processRequest() throws Exception{
		String type = new String();		
		String[] args = fields;
		type = args[0];
		Integer index;
		switch(type){
		case GlobalVariables.REGISTER_ACTION:
			index = new Integer(args[1]);
			User newUser = new User(args[2], req.getSenderAddr() , (int)new Integer(args[4]));
			if(newUser.logHistoryRequest(req, index) == false){
				String responce = new String(GlobalVariables.REGISTER_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), newUser, responce).start();
				break;
			}
			if(!myManager.hasUser(req.getSenderAddr(),args[2]) && (myManager.getNumOfUser() < 5)){	
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
			if (myManager.getUserList().isEmpty() || tar == -1){

				//TODO: need to consturct a user not registered error in order to make it work
				throw new Exception("The message is not sent from a validated user");
			}
			if(myManager.getUserList().get(tar).logHistoryRequest(req, index) == false){
				String responce = new String(GlobalVariables.PUBLISH_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar), responce).start();
				break;
			}

			int status = -1;
			if(args[4].equalsIgnoreCase("on")){
				status = 1;
			}
			else if(args[4].equalsIgnoreCase("off")){
				status = 0;
			}
			else{
				String responce = new String(GlobalVariables.PUBLISH_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar), responce).start();
			}
			
			if(tar > -1){
				boolean isOn;
				switch(status){
				case 1:
					isOn = true;
					break;
				case 0:
					isOn = false;
					break;
				default:
					throw new Exception("The server is in an invalid state");
				}
				myManager.getUserList().get(tar).setReceivePort(new Integer(args[3]));
				myManager.getUserList().get(tar).setAvaliability(isOn);
				String usernames = args[5];
				String[] userNameList = usernames.split(" ");
				for (String n:userNameList){
					User u = myManager.findUserByName_Single(n);
					if(u!=null){
						myManager.getUserList().get(tar).makeFriend(u);
					}
				}
				String msgNew  = rawMsg.replace(GlobalVariables.PUBLISH_ACTION, GlobalVariables.PUBLISH_SUCCESS);
				new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar), msgNew).start();
			}
			else{
				String responce = new String(GlobalVariables.PUBLISH_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar), responce).start();
			}
			break;
		case GlobalVariables.IMFORMATION_REQUEST_ACTION:
			index = new Integer(args[1]);
			int tar_2 = myManager.getUserInList(args[2], req.getSenderAddr());
			if (myManager.getUserList().isEmpty() || tar_2 == -1){
				//TODO: need to consturct a user not registered error in order to make it work
				throw new Exception("The message is not sent from a validated user");
			}
			if(myManager.getUserList().get(tar_2).hasIndexLogged(index) == false){
				String responce = new String(GlobalVariables.INFORMATION_REQUEST_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar_2), responce).start();
				break;
			}
			Request retrieved = myManager.getUserList().get(tar_2).retrieveHistoryItem(index);
			String msgNew = retrieved.getMessage();
			new ServerSendingThread(myManager.getServerSendingPort(), myManager.getUserList().get(tar_2), msgNew).start();
			break;
			
			
				
				//TODO
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
