package server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import global.HasRegisteredException;
import global.GlobalVariables;
import global.User;
import requestsParser.Request;
import requestsParser.RequestExecutor;
import server.ServerListenThread;

public class ServerManager {
	ArrayList<User> userList = new ArrayList<User>();
	private global.GlobalVariables GV;
	InetAddress nextServerAddr;
	int nextServerPort;
	private RequestExecutor exec;
	DatagramSocket serverListenSoc;
	DatagramSocket serverSendingSoc;
	
	
	public ServerManager(int sendingPort, int recevingPort, InetAddress addr, int port){
		try {
			serverListenSoc = new DatagramSocket(recevingPort);
			serverSendingSoc = new DatagramSocket(sendingPort);
			nextServerAddr = addr;
			nextServerPort = port;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ServerManager(int sendingPort, int recevingPort) throws UnknownHostException{
		try {
			serverListenSoc = new DatagramSocket(recevingPort);
			serverSendingSoc = new DatagramSocket(sendingPort);
			nextServerAddr = InetAddress.getLocalHost();
			nextServerPort = recevingPort;;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void init(){
		ServerListenThread lisTh = new ServerListenThread(serverListenSoc, this);
		lisTh.start();
		ServerSystemListenThread sysTh = new ServerSystemListenThread(this);
		sysTh.start();
	}
	
	
	public void processRequest(Request req) throws HasRegisteredException{
		exec = new RequestExecutor(req);
		exec.start();
		String[] args = exec.returnArgs();
		String type = args[0];
		Integer index;
		switch(type){
		case GlobalVariables.REGISTER_ACTION:
			index = new Integer(args[1]);
			User newUser = new User(args[2], req.getSenderAddr() , (int)new Integer(args[4]));
			if(!hasUser(req.getSenderAddr(),args[2]) && (userList.size() < 5)){	
				newUser.logHistoryRequest(req, index);
				registerClient(newUser);
				String responce = new String(GlobalVariables.REGISTER_SUCCESS + GlobalVariables.delimiter + index.toString()); 
				new ServerSendingThread(serverSendingSoc, newUser, responce).start();
			}
			else if((userList.size() >= 5)){
				String responce = new String(GlobalVariables.REGISTER_DENIED + GlobalVariables.delimiter + index.toString() + GlobalVariables.delimiter + 
						nextServerAddr.toString() + GlobalVariables.delimiter + new Integer(nextServerPort).toString());
				new ServerSendingThread(serverSendingSoc, newUser, responce).start();
			}
			else{
				String responce = new String(GlobalVariables.REGISTER_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(serverSendingSoc, newUser, responce).start();
			}
			break;
		case GlobalVariables.PUBLISH_ACTION:
			index = new Integer(args[1]);
			int tar = getUserInList(args[2], req.getSenderAddr());
			boolean status = (Boolean) null;
			if(args[4].equalsIgnoreCase("on")){
				status = true;
			}
			else if(args[4].equalsIgnoreCase("off")){
				status = false;
			}
			else{
				String responce = new String(GlobalVariables.PUBLISH_DENIED + GlobalVariables.delimiter + index.toString());
				new ServerSendingThread(serverSendingSoc, userList.get(tar), responce).start();
			}
			
			if(tar != -1){
				userList.get(tar).setReceivePort(new Integer(args[3]));
				userList.get(tar).setAvaliability(status);
				ArrayList<User> listOfConnections = new ArrayList<User>();
				//TODO
			}
		}	
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
	
	
	public void authenticateUser(User self, User[] users){
		for (User u:users){
			u.makeFriend(self);
		}
	}
	

	public int registerClient(User user) throws HasRegisteredException{
		
		if(userList.size() >= 5){
			return 1;
		}
		user.register();
		userList.add(user);
		return 0;
	}
	
	public User[] findListOfUsers(String[] names){
		User[] list = new User[5];
		int resCount = 0;
		ArrayList<String> namelist = new ArrayList<String>(Arrays.asList(names));
		for (String name:namelist){
			for (User user: userList){
				if(user.getName().equals(name)){
					list[resCount] = user;
					resCount++;
					namelist.remove(name);
					continue;
				}
			}
		}
		return list;
	}
	
	//The function has 2 return states, 0 for register successful, 1 for full server, 2 for user has already registered
	public int registerClient(String name, String address, int portNum) throws UnknownHostException, HasRegisteredException{ 
		if(hasUser(InetAddress.getByName(address), name)){
			return 2;
		}
		if(userList.size() >= 5){
			return 1;
		}
		User newUser = new User(name, InetAddress.getByName(address), portNum);
		newUser.register();
		userList.add(newUser);
		return 0;
	}
	
	public int getUserInList(String name, InetAddress addr){
		for(User u:userList){
			if(u.getName().equals(name) && u.getAddr().equals(addr)){
				return userList.indexOf(u);
			}
		}
		return -1;
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
	
	protected void closeDown(){
		serverListenSoc.close();
		serverSendingSoc.close();
		System.out.println("All Sockets are closed");
	}
	
	

}
