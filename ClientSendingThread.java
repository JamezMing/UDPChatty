import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSendingThread extends Thread{
	private DatagramSocket sendSoc;
	private int hostSoc;
	private InetAddress hostAddr;
	
	
	public ClientSendingThread(DatagramSocket send, int dest, InetAddress addr){
		hostSoc = dest;
		sendSoc = send;
		hostAddr = addr;
	}

	public void run(){
		try{
		
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while(hostAddr == null){
				System.out.println("Host Address Undefined, please enter a valid host address to continue");
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
