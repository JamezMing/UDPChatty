import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerManager {
	ArrayList<User> userList = new ArrayList<User>();
	HashMap<Integer, User> portUserMap = new HashMap<Integer, User>();
	DatagramSocket serverListenSoc;
	DatagramSocket serverSendingSoc;
	
	
	public ServerManager(int sendingPort, int recevingPort){
		try {
			serverListenSoc = new DatagramSocket(recevingPort);
			serverSendingSoc = new DatagramSocket(sendingPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addUser(User user){
		userList.add(user);
	}
	
	public boolean hasUser(InetAddress userAdd){
		boolean hasUserExist = false;
		for (User regUser: userList){
			if(regUser.getAddr().equals(userAdd)){
				hasUserExist = true;
				break;
			}
		}
		return hasUserExist;
	}
	
	public void broadCast(String message) throws IOException{
		for(User u:userList){
			DatagramPacket sendPac = new DatagramPacket(message.getBytes(), message.length(), u.getAddr(), u.getRecevingPort());
			serverSendingSoc.send(sendPac);
		}
	}
	
	public void broadCast(String message, InetAddress except_addr) throws IOException{
		for(User u:userList){
			if(!u.getAddr().equals(except_addr)){
				DatagramPacket sendPac = new DatagramPacket(message.getBytes(), message.length(), u.getAddr(), u.getRecevingPort());
				serverSendingSoc.send(sendPac);
			}
		}
	}
	
	public void init(){
		ServerListenThread lisTh = new ServerListenThread(serverListenSoc, this);
		lisTh.start();
	}

}
