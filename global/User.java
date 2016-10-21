package global;
import java.net.InetAddress;
import java.util.ArrayList;
import global.GlobalVariables;

public class User {
	private static int usercount = 0;
	private String name;
	private InetAddress addr;
	private int recevingPort;
	private boolean isRegistered = false;
	private boolean isInitiated = false;
	private ArrayList<User> allowedListofUser = new ArrayList<User>();
	private boolean status = true; //True means on and 
	//private int sendingPort;
	
	//The constructor is used for test only. 
	public User(InetAddress address){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = GlobalVariables.HANDSHAKING_PORT;
		isInitiated = false;
	}
	
	public User(InetAddress address, int recPortNum, int sendPortNum){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = recPortNum;
		isInitiated = true;
		//sendingPort = sendPortNum;
	}
	
	public User(String username, InetAddress address){
		usercount++;
		name = username;
		addr = address;
		recevingPort = GlobalVariables.HANDSHAKING_PORT;
		isInitiated = false;
		
	}
	
	public User(String username, InetAddress address, int recPortNum, int sendPortNum){
		usercount++;
		name = username;
		addr = address;
		recevingPort = recPortNum;
		isInitiated = true;
		//sendingPort = sendPortNum;
	}
	
	
	public void setRecevingPort(int portNum){
		recevingPort = portNum;
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
	
	
	

}
