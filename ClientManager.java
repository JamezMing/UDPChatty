import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ClientManager {
	public DatagramSocket recevingPort;
	public DatagramSocket sendingPort;
	public InetAddress hostAddr;
	public int hostRecPort;
	
	public ClientManager(int recPortNum, int sendPortNum, String addr, int hostPortNum) throws SocketException, UnknownHostException{
		recevingPort = new DatagramSocket(recPortNum);
		sendingPort = new DatagramSocket(sendPortNum);
		hostAddr = InetAddress.getByName(addr);
		hostRecPort = hostPortNum;
	}
	
	public void init(){
		ClientSendingThread senTh = new ClientSendingThread(sendingPort, hostRecPort, hostAddr);
		senTh.start();
		ClientListenThread lisTh = new ClientListenThread(recevingPort);
		lisTh.start();
	}
	

}
