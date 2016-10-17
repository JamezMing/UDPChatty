import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainClient {
	public static void main(String[] args) throws SocketException, UnknownHostException{
		System.out.println("Please enter your option: 1 for Server and 2 for Client");
		Scanner sc = new Scanner(System.in);
		int op = sc.nextInt();
		while(op!=1 && op!=2){
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

	}

}
