import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class MainInterface {
	public static void main(String[] args) throws IOException, Exception{
		System.out.println("Please enter 1 for Server Mode and 2 for Client Mode");
		InputStream sysin = System.in;
		Scanner sc = new Scanner(sysin);
		int choice = sc.nextInt();
		while(choice != 1 && choice != 2){
			System.out.println("Sorry you have entered an invalid option. Please enter again");
			choice = sc.nextInt();
		}
		if(choice == 1){
			ChattyServer ser = new ChattyServer(1234, 2345);
			ser.init();
		}
		else if(choice == 2){
			ChattyClient cli = new ChattyClient(3456, 4567, 1234, "192.168.2.222");
			cli.init();
		}
	}
}
