package global;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.crypto.ShortBufferException;
import deprecated.ServerSignatureGen;
import requestsParser.Request;

public class User {
	private static int usercount = 0;
	private String name;
	private InetAddress addr;
	private int recevingPort;
	private HashMap<Integer, Request> history = new HashMap<Integer, Request>();
	private boolean isRegistered = false;
	private boolean isAvaliable = true;
	private ArrayList<User> allowedListofUser = new ArrayList<User>();
	private byte[] userFingerPrint; 
	private byte[] userPubKey;
	private byte[] userPriKey;

	
	public User(InetAddress address, int recPortNum, byte[] key){
		usercount++;
		name = new String("User" + usercount);
		addr = address;
		recevingPort = recPortNum;
		ServerSignatureGen sigGen = new ServerSignatureGen();
		try {
			sigGen.init(key);
			userPubKey = sigGen.getPublicKey();
			userPriKey = sigGen.getPrivateKey();
			userFingerPrint = sigGen.getSecret();
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidParameterSpecException
				| InvalidAlgorithmParameterException | InvalidKeySpecException | IllegalStateException
				| ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public User(String username, InetAddress address, int recPortNum, byte[] key){
		usercount++;
		name = username;
		addr = address;
		recevingPort = recPortNum;
		ServerSignatureGen sigGen = new ServerSignatureGen();
		try {
			sigGen.init(key);
			userPubKey = sigGen.getPublicKey();
			userPriKey = sigGen.getPrivateKey();
			userFingerPrint = sigGen.getSecret();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidParameterSpecException
				| InvalidAlgorithmParameterException | InvalidKeySpecException | IllegalStateException
				| ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean logHistoryRequest(Request req, Integer index){
		if(history.containsKey(index)){
			return false;
		}
		else{
			history.put(index, req);
			return true;
		}
	}
	
	public Request retrieveHistoryItem(Integer index){
		return history.get(index);
	}
	
	public boolean hasIndexLogged(Integer index){
		if(history.containsKey(index)){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public byte[] getPublicKey(){
		return userPubKey;
	}
	
	protected byte[] getPrivateKey(){
		return userPriKey;
	}
	
	public byte[] getSecret(){
		return userFingerPrint;
	}
	
	public void setListOfAllowedUsers(ArrayList<User> list){
		this.allowedListofUser = list;
	}
	
	public int getHistorySize(){
		return history.size();
	}
	
	public InetAddress getAddr(){
		return addr;
	}
	
	public int getRecevingPort(){
		return recevingPort;
	}	
	
	public String getName(){
		return name;
	}

	
	public void setAvaliability(boolean ava){
		isAvaliable = ava;
	}
	
	public boolean returnAvaliability(){
		return isAvaliable;
	}
	
	public boolean isRegistered(){
		return isRegistered;
	}
	
	public void register() throws HasRegisteredException{
		if(isRegistered == true){
			throw new HasRegisteredException();
		}else{
			isRegistered = true;
		}
	}
	
	public void makeFriend(User user){
		allowedListofUser.add(user);
	}
	
	public void setReceivePort(int port){
		recevingPort = port;
	}
	
	public boolean isFriend(InetAddress addr, String name){
		for (User u: allowedListofUser){
			if(u.getAddr().equals(addr) && u.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isFriend(User tar){
		for (User u: allowedListofUser){
			if(u.getAddr().equals(tar.getAddr()) && u.getName().equals(tar.getName())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isFriendByKey(User tar){
		for (User u: allowedListofUser){
			if(Arrays.equals(u.getSecret(), tar.getSecret())){
				return true;
			}
		}
		return false;
	}
	

	
	

}
