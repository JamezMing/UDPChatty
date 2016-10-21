package server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import global.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
@Deprecated
public class ServerSendingThread extends Thread{
	private DatagramSocket dataSendPort;
	private ServerManager myManager;
	private User targetUser;

	
	public ServerSendingThread(DatagramSocket send, User target, ServerManager father) throws SocketException{
		dataSendPort = send;
		targetUser = target;
		myManager = father;
	}
	
	

	public void run(){
		try {
			InputStream input = System.in;
			InputStreamReader sysin = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(sysin);
	        System.out.println("server is on£¬waiting for client to send data......");  
			while(true){
				String msg;
				while ((msg = reader.readLine()) == null) {
					System.out.println("Preparing Reader...");
					Thread.sleep(100);
		        }
	             if(msg.equals("EXIT")){
	            	 break;
	             }
	             DatagramPacket sendPac = new DatagramPacket(msg.getBytes(), msg.length(),targetUser.getAddr() , targetUser.getRecevingPort());
			     dataSendPort.send(sendPac);
			}
			dataSendPort.close();
			System.out.println("The Server has closed all its connections");
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
