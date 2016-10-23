package global;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import global.GlobalVariables;
import requestsParser.Request;

public class User {
	private static int usercount = 0;
	private String name;
	private InetAddress addr;
	private int recevingPort;
	private HashMap<Integer, Request> history = new HashMap<Integer, Request>();
	private boolean isRegistered = false;
	private boolean isInitiated = false;
	private boolean isAvaliable = true;
	private ArrayList<User> allowedListofUser = new ArrayList<User>();
	//private int sendingPort;
	@Deprecated
	//The constructor is used for test only. 
	public User(InetAddress address){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = GlobalVariables.HANDSHAKING_PORT;
		isInitiated = false;
	}
	
	public User(InetAddress address, int recPortNum){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = recPortNum;
		isInitiated = true;
	}
	@Deprecated
	public User(String username, InetAddress address){
		usercount++;
		name = username;
		addr = address;
		recevingPort = GlobalVariables.HANDSHAKING_PORT;
		isInitiated = false;
		
	}
	
	public User(String username, InetAddress address, int recPortNum){
		usercount++;
		name = username;
		addr = address;
		recevingPort = recPortNum;
		isInitiated = true;
	}
	
	public void logHistoryRequest(Request req, Integer index){
		history.put(index, req);
	}
	
	public Request retrieveHistoryItem(Integer index){
		return history.get(index);
	}
	
	public int getHistorySize(){
		return history.size();
	}
	
	public InetAddress getAddr(){
		return addr;
	}
	
	public int getRecevingPort(){
		return recevingPort;
	}	
	
	public String getName(){
		return name;
	}
	
	public boolean getIsInitiated(){
		return isInitiated; 
	}
	
	public void setAvaliability(boolean ava){
		isAvaliable = ava;
	}
	
	public boolean returnAvaliability(){
		return isAvaliable;
	}
	
	public boolean isRegistered(){
		return isRegistered;
	}
	
	public void register() throws HasRegisteredException{
		if(isRegistered == true){
			throw new HasRegisteredException();
		}else{
			isRegistered = true;
		}
	}
	
	public void makeFriend(User user){
		allowedListofUser.add(user);
	}
	
	public void setReceivePort(int port){
		recevingPort = port;
	}
	
	
	

}
