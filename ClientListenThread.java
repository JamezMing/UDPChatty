import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientListenThread extends Thread{
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