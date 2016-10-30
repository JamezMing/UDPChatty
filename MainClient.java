import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.DHParameterSpec;
import javax.xml.bind.DatatypeConverter;
import client.ClientManager;
import deprecated.ServerSignatureGen;
import deprecated.ClientSignatureGen;
import server.ServerManager;

public class MainClient {
	public static void main(String[] args) throws SocketException, UnknownHostException, ShortBufferException{
		System.out.println("Please enter your option: 1 for Server and 2 for Client");
		System.out.println("New Process Request");
		Scanner sc = new Scanner(System.in);
		int op = sc.nextInt();
		while(op!=1 && op!=2 && op!=3){
			System.out.println("False input. Please enter the correct option");
			op = sc.nextInt();
		}
		if(op == 1){
			ServerManager server = new ServerManager(1234, 2234);
			server.init();
		}
		if(op == 2){
			ClientManager client = new ClientManager(3234, 4234, "192.168.2.222", 2234);
			client.init();
		}
		if(op == 3){
			try {
				System.out.println("Test Mode");
				ClientSignatureGen gen1 = new ClientSignatureGen();
				gen1.init();
				byte[] pubK1 = gen1.getPublicKey();
				
				ServerSignatureGen gen2 = new ServerSignatureGen();
				gen2.init(pubK1);
				byte[] pubK2 = gen2.getPublicKey();
				System.out.println(pubK1);
				System.out.println(pubK2);
				gen1.decodeKey(pubK2);
				System.out.println(DatatypeConverter.printHexBinary(gen1.getSecret()));
				System.out.println(DatatypeConverter.printHexBinary(gen2.getSecret()));
				System.out.println(Arrays.equals(gen1.getSecret(), gen2.getSecret()));
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException
					| IllegalStateException | InvalidParameterSpecException | InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

			

		}

	}

}
