import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerListenThread extends Thread{
	private DatagramSocket recSoc;
	private ServerManager myManager;
	boolean isRunning = true; 
	
	public ServerListenThread(DatagramSocket port, ServerManager father){
		recSoc = port;
		myManager = father;
	}
	
	public void run(){
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket recPac = new DatagramPacket(buffer, 1024);
			while(isRunning){
				recSoc.receive(recPac);
				if(!myManager.hasUser(recPac.getAddress())){
					User newUser = new User(recPac.getAddress());
					myManager.addUser(newUser);
				}
				String str_receive = new String(recPac.getData(),0,recPac.getLength()) +   
	                    " from " + recPac.getAddress().getHostAddress() + ":" + recPac.getPort(); 
				myManager.broadCast(str_receive,recPac.getAddress());
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