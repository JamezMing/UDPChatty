import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;

class ServerListenThread extends Thread{
	int listenPort; 
	boolean isRunning = true; 
	
	public ServerListenThread(int port, ChattyServer father){
		listenPort = port;
		isRunning = true;
	}
	
	public void run(){
		try {
			byte[] buffer = new byte[1024];
			DatagramSocket recSoc = new DatagramSocket(listenPort);
			DatagramPacket recPac = new DatagramPacket(buffer, 1024);
			while(isRunning){
				recSoc.receive(recPac);
				String str_receive = new String(recPac.getData(),0,recPac.getLength()) +   
	                    " from " + recPac.getAddress().getHostAddress() + ":" + recPac.getPort(); 
				System.out.println(str_receive);
				recPac.setLength(1024);
			}
			recSoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}



public class ChattyServer {
	private int recevingPort = 1200;
	private int sendingPort = 1201;
	private DatagramSocket dataSendPort;

	
	public ChattyServer() throws SocketException{		
	}
	public ChattyServer(int rece, int send) throws SocketException{
		recevingPort = rece;
		sendingPort = send;
	}

	public void init(){
		
		try {
			DatagramSocket dataSendPort = new DatagramSocket(sendingPort);
			InputStream input = System.in;
			InputStreamReader sysin = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(sysin);
			ServerListenThread lisTh = new ServerListenThread(recevingPort, this);
			while (dataSendPort == null){}
			lisTh.start();
	        System.out.println("server is on£¬waiting for client to send data......");  
			while(true){
				String msg;
				while ((msg = reader.readLine()) == null) {
					System.out.println("Preparing Reader...");
					Thread.sleep(100);
		        }
	             //String msg = reader.readLine();
	             if(msg.equals("EXIT")){
	            	 break;
	             }
	             
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
