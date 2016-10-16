import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerListenThread extends Thread{
	private DatagramSocket recSoc;
	boolean isRunning = true; 
	
	public ServerListenThread(DatagramSocket port, ServerSendingThread father){
		recSoc = port;
	}
	
	public void run(){
		try {
			byte[] buffer = new byte[1024];
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