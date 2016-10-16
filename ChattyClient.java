import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class ClientListenThread extends Thread{
	private int recSocNum; 
	private boolean isRunning = true;
	
	public ClientListenThread(int soc){
		recSocNum = soc;
	}
	
	public void run(){
		try {
			DatagramSocket listSoc = new DatagramSocket(recSocNum);
			byte[] buffer = new byte[1024];
			DatagramPacket recPac = new DatagramPacket(buffer, 1024);
			while(isRunning){
				listSoc.receive(recPac);
				String str_receive = new String(recPac.getData(),0,recPac.getLength()) +   
	                    " from " + recPac.getAddress().getHostAddress() + ":" + recPac.getPort();  
				System.out.println(str_receive);
				recPac.setLength(1024);
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

public class ChattyClient {
	private int recSocNum = 1234;
	private int sendSocNum = 2345;
	private int destSocNum = 3456;
	private InetAddress hostAddr;
	
	public ChattyClient(){}
	public ChattyClient(int rece, int send, int dest, String addr){
		recSocNum = rece;
		destSocNum = dest;
		sendSocNum = send;
		try {
			hostAddr = InetAddress.getByName(addr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws IOException, InterruptedException{
		DatagramSocket sendSoc = new DatagramSocket(sendSocNum);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ClientListenThread listTh = new ClientListenThread(recSocNum);
		while(hostAddr == null){
			System.out.println("Host Address Undefined, please enter a valid host address to continue");
			Scanner sc = new Scanner(System.in);
			String conaddr = sc.nextLine();
			hostAddr = InetAddress.getByName(conaddr);
		}
		listTh.start();
		System.out.println("Client start!");
		while(true){
			String msg;
			while ((msg = reader.readLine()) == null) {
				System.out.println("Preparing Reader...");
				Thread.sleep(100);
	        }
	        if(msg.equals("EXIT")){
	        	break;
	        }
	        DatagramPacket sendPac = new DatagramPacket(msg.getBytes(), msg.length(), hostAddr, destSocNum);
	        sendSoc.send(sendPac);
		}
		sendSoc.close();
		System.out.println("The client has closed its connection");
		
	}
	
}
