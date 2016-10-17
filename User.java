import java.net.InetAddress;

public class User {
	private static int usercount = 0;
	private String name;
	private InetAddress addr;
	private int recevingPort;
	//private int sendingPort;
	
	//The constructor is used for test only. 
	public User(InetAddress address){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = GlobalVariables.HANDSHAKING_PORT;
	}
	
	public User(InetAddress address, int recPortNum, int sendPortNum){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = recPortNum;
		//sendingPort = sendPortNum;
	}
	
	public User(String username, InetAddress address){
		usercount++;
		name = username;
		addr = address;
		recevingPort = GlobalVariables.HANDSHAKING_PORT;
		
	}
	
	public User(String username, InetAddress address, int recPortNum, int sendPortNum){
		usercount++;
		name = username;
		addr = address;
		recevingPort = recPortNum;
		//sendingPort = sendPortNum;
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
	
	
	

}
