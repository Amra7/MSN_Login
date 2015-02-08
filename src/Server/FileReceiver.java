package Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver implements Runnable {

	public static final int port = 1717;
	public Socket receiver;

	public static void main(String[] args) {
		try {
			ServerSocket listener = new ServerSocket(port);
			
			while (true){
				FileReceiver fileRec = new FileReceiver();
				fileRec.receiver = listener.accept();
				new Thread(fileRec).start();
				
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
	
		try {
			InputStream in = receiver.getInputStream();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
