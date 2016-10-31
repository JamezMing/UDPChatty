package client;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import global.GlobalVariables;


public class ClientListenThread extends Thread{
	private DatagramSocket listSoc;
	private boolean isRunning = true;
	ClientManager myManager;
	
	public ClientListenThread(DatagramSocket soc, ClientManager manager){
		listSoc = soc;
		myManager = manager;
	}
	
	public void run(){
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket recPac = new DatagramPacket(buffer, 1024);
			while(isRunning){
				listSoc.receive(recPac);
				if(new String(recPac.getData()).startsWith(GlobalVariables.REGISTER_SUCCESS)){
					String strRec = new String(recPac.getData(),0,recPac.getLength());
					myManager.decodeSecret(new BigInteger(strRec.split(GlobalVariables.delimiter)[2], 16).toByteArray());
					System.out.println("Register Successful");
				}
				if(new String(recPac.getData()).startsWith(GlobalVariables.CHAT_ACTION)){
					String str_receive = new String(recPac.getData(),0,recPac.getLength()) +   
		                    " from " + recPac.getAddress().getHostAddress() + ":" + recPac.getPort();  
					System.out.println(str_receive);
					recPac.setLength(1024);
				}
			}
			listSoc.close();
			

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}