package Server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver implements Runnable {

	public static final int port = 1800;
	public Socket receiver;
	
	File myFile = new File("Users/amrapoprzanovic/Documents");
	

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
			byte[] buffer = new byte[1024];
			FileOutputStream fos = new FileOutputStream(myFile);
			BufferedOutputStream  bos = new BufferedOutputStream(fos);
			int bytesRead = in.read(buffer, 0, buffer.length);
			bos.write(buffer, 0, bytesRead);
			bos.close();
			receiver.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
