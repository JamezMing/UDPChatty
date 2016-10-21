package server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import global.*;

import global.HasRegisteredException;
import global.User;
import server.ServerListenThread;

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
	
	@Deprecated
	public boolean hasUser(InetAddress userAdd){
		boolean hasUserExist = false;
		for (User regUser: userList){
			if((regUser.getAddr().equals(userAdd))){
				hasUserExist = true;
				break;
			}
		}
		return hasUserExist;
	}
	
	public boolean hasUser(InetAddress userAdd, String userName){
		boolean hasUserExist = false;
		for (User regUser: userList){
			if((regUser.getAddr().equals(userAdd))&&(regUser.getName().equalsIgnoreCase(userName))){
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
	
	//The function has 2 return states, 0 for register successful, 1 for full server, 2 for user has already registered
	public int registerClient(String name, String address) throws UnknownHostException, HasRegisteredException{ 
		if(hasUser(InetAddress.getByName(address), name)){
			return 2;
		}
		if(userList.size() >= 5){
			return 1;
		}
		User newUser = new User(name, InetAddress.getByName(address));
		newUser.register();
		userList.add(newUser);
		return 0;
	}
	
	public boolean registerClientPort(User user, int recPortNum){
		if(userList.contains(user)){
			portUserMap.put(recPortNum, user);
			return true;
		}
		else{
			return false;
		}
	}
	
	public User[] findUserByName(String name){
		User[] results = new User[5];
		int resCount = 0;
		for(User u:userList){
			if(u.getName().equals(name)){
				results[resCount] = u;
				resCount++;
			}
		}
		return results;
	}
	
	

}
