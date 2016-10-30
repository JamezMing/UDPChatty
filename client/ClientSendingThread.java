package client;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import global.GlobalVariables;

public class ClientSendingThread extends Thread{
	private DatagramSocket sendSoc;
	private int hostSoc;
	private InetAddress hostAddr;
	private ClientManager myManager;
	
	
	public ClientSendingThread(DatagramSocket send, int dest, InetAddress addr, ClientManager manager){
		hostSoc = dest;
		sendSoc = send;
		hostAddr = addr;
		myManager = manager;
	}


	public void run(){
		try{
		
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while(hostAddr == null){
				System.out.println("Host Address Undefined, please enter a valid host address to continue");
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				String conaddr = sc.nextLine();
				hostAddr = InetAddress.getByName(conaddr);
			}
			while(true){
				String msg;
				while ((msg = reader.readLine()) == null) {
					System.out.println("Preparing Reader...");
					Thread.sleep(100);
		        }
		        if(msg.equals("EXIT")){
		        	break;
		        }
		        //Temp Solution 
		        if( msg.substring(0, 5).equals(GlobalVariables.REGISTER_ACTION)){
		        	String keyStr = new String(GlobalVariables.delimiter + DatatypeConverter.printHexBinary(myManager.getPublicKey()));
		        	msg = msg.concat(keyStr);
		        }
		        if(myManager.getRegStat() == true){
		        	msg = msg.concat(GlobalVariables.delimiter + DatatypeConverter.printHexBinary(myManager.getSecretKey()));
		        }
		        //Temp Solution
		        DatagramPacket sendPac = new DatagramPacket(msg.getBytes(), msg.length(), hostAddr, hostSoc);
		        sendSoc.send(sendPac);
			}
			sendSoc.close();
			System.out.println("The client has closed its connection");
		}catch(IOException e){
			e.printStackTrace();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
}
