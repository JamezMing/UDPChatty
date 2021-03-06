package client;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import deprecated.ClientSignatureGen;


public class ClientManager {
	private DatagramSocket recevingPort;
	private DatagramSocket sendingPort;
	private InetAddress hostAddr;
	private int hostRecPort;
	private byte[] publicKey;
	private byte[] secretKey;
	private ClientSignatureGen sigGen; 
	private boolean isRegistered = false;
	
	public ClientManager(int recPortNum, int sendPortNum, String addr, int hostPortNum) throws SocketException, UnknownHostException{
		recevingPort = new DatagramSocket(recPortNum);
		sendingPort = new DatagramSocket(sendPortNum);
		hostAddr = InetAddress.getByName(addr);
		hostRecPort = hostPortNum;
		sigGen = new ClientSignatureGen();
		try {
			sigGen.init();
			publicKey = sigGen.getPublicKey();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidParameterSpecException
				| InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void decodeSecret(byte[] key){
		try {
			sigGen.decodeKey(key);
			secretKey = sigGen.getSecret();
			isRegistered = true;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init(){
		ClientSendingThread senTh = new ClientSendingThread(sendingPort, hostRecPort, hostAddr, this);
		senTh.start();
		ClientListenThread lisTh = new ClientListenThread(recevingPort, this);
		lisTh.start();
	}
	
	public boolean getRegStat(){
		return isRegistered;
	}
	

	public byte[] getPublicKey(){
		return publicKey;
	}
	
	protected byte[] getSecretKey(){
		return secretKey;
	}

	
}
