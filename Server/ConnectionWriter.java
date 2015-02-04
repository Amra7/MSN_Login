package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ConnectionWriter extends Thread {

	public static HashMap<String, OutputStream> connections = new HashMap<String, OutputStream>();
	private Set<String> setClientName = connections.keySet();

	@Override
	public void run() {

		while (true) {
			if (Message.hasNext()) {
				System.out.println("Writer: " + Message.hasNext());
				Message msg = Message.next();
				byte[] msgByte = (msg.getSender() + ": " + msg.getContent())
						.getBytes();
				Iterator<String> iter = setClientName.iterator();

				while (iter.hasNext()) {
					String name = iter.next();
					if (!name.equals(msg.getSender())) {
						OutputStream os = connections.get(name);
						try {
							os.write(msgByte);
							os.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
