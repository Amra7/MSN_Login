package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Server {

	public static final int port = 1717;

	public static void serverStart() throws IOException {
		ServerSocket server = new ServerSocket(port);
		ConnectionWriter cw = new ConnectionWriter();
		cw.start();

		while (true) {
			String str = "waiting for connection";
			System.out.println(str);
			Socket client;
			try {
				client = server.accept();
				String clientName = handShake(client.getInputStream());

				if (clientName != null) {
					while (ConnectionWriter.connections.containsKey(clientName)) {
						clientName += new Random().nextInt(1000);
					}
					ConnectionWriter.connections.put(clientName,
							client.getOutputStream());
					ConnectionListener cl = new ConnectionListener(
							client.getInputStream(), clientName);
					cl.start();
					new Message("join%"+ clientName, "%server%");
					client.getOutputStream().write(0);

				} else {
					client.getOutputStream().write(-1);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private static String handShake(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String name = br.readLine();
		name = name.replace("%", ""); // sanela je ostavla ""
		String password = br.readLine();
		int num = XMLConnection.userLogin(name, password);
		if (num !=0){
			return null;
		}
		return name;

	}

	public static void main(String[] args) {
		try {
			new XMLConnection();
			
		} catch (SAXException e1) {
			e1.printStackTrace();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		try {
			serverStart();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
