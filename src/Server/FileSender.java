package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileSender {
	public static final int port = 1717;
	private static final String host = "localhost";
	
	public static void main(String[] args) {
		
		try {
			Socket sender =new Socket(host, port);
			OutputStream out = sender.getOutputStream();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
