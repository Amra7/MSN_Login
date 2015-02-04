package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectionListener extends Thread {

	private InputStream is;
	private String sender;

	public ConnectionListener(InputStream is, String sender) {
		this.is = is;
		this.sender = sender;

	}

	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		try {
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!line.equals("")) {
					Message incoming = new Message(line, sender);
					System.out.println(incoming.getSender() + ":" + incoming.getContent());
					System.out.println("Listener: " + Message.hasNext());
				}
			}
			ConnectionWriter.connections.remove(this.sender);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
