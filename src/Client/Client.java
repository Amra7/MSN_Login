package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import GUI.ChatGui;

/**
 * Class client that connect to the server.
 * @author amrapoprzanovic
 *
 */
public class Client {
	public static final int port = 1717;
	public static final String host = "127.0.0.1"; //"10.0.82.62"
	
	/**
	 * Main method that creates Login window where client can put his username and password.
	 * @param args
	 */
	public static void main(String[] args) {
		
		Login log = new Login(host, port);
		
//		try {
//			
////			Socket client = new Socket(host, port);
//			System.out.println("Unesi svoje ime: ");
//			Scanner scan =  new Scanner(System.in);
//			String name = scan.nextLine()+"\n";   // zato sto koristimo BufferedReader koji ceka da dodje do new Line --> "\n"
//			client.getOutputStream().write(name.getBytes());
////			ChatGui gui = new ChatGui(client);
//			new Thread(gui).start();
////			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	} // end of main method
} // end of class
