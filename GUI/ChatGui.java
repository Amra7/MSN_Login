package GUI;

import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatGui implements Runnable {

	private JTextArea display;
	private TextField inputMsg;
	private Socket connection;
	private InputStream is;
	private OutputStream os;

	public ChatGui(final Socket connection) throws IOException {

		this.connection = connection;
		this.is = connection.getInputStream();
		this.os = connection.getOutputStream();

		JFrame window = new JFrame("MSN");
		JPanel content = new JPanel();
		JButton buttonSend = new JButton("SEND");
		display = new JTextArea();
		// display.setPreferredSize(new Dimension(300, 200));
		display.setEditable(false);
		inputMsg = new TextField();
		inputMsg.setPreferredSize(new Dimension(200, 20));

		buttonSend.addActionListener(new MessageHandler());
		inputMsg.addKeyListener(new MessageHandler());
		display.setLineWrap(true);

		JScrollPane areaScrollPane = new JScrollPane(display);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(390, 220));

		content.add(areaScrollPane);
		content.add(inputMsg);
		content.add(buttonSend);

		window.add(content);

		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				try {
					connection.shutdownOutput();
					connection.close();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		
		});
		window.setSize(400, 300);
		window.setVisible(true);

	}

	/**
	 * Listening messages.
	 * @throws IOException
	 */
	public void listenForNetwork() throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(is));

		String line = null;
		while ((line = input.readLine()) != null) {
			
			if (!line.equals("")) {
				String [] arrString = line.split(":");
				
				if( arrString[0].equals("%server%")){
					String [] secPartArrString = arrString[1].split("%");
					
					if (secPartArrString[0].equals(" join")){
						display.append(secPartArrString[1] + " joined the chat!\n");
						
					} else if(secPartArrString[0].equals(" left")){
						display.append(secPartArrString[1] + " had left the chat!\n");
					}
				} else{
					display.append(line + "\n");					
				}
				line = null;
			}
		}
	}

	/**
	 * Sending message.
	 * @author amrapoprzanovic
	 *
	 */
	private class MessageHandler extends KeyAdapter implements ActionListener {

		private void sendMessage() {
			String str = inputMsg.getText();
			if (!str.equals("")) {
				str += "\n";
				display.append("Me: " + str);
				try {
					os.write(str.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputMsg.setText(null);

			}

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			sendMessage();
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10)
				sendMessage();
		}
	}

	@Override
	public void run() {
		try {
			listenForNetwork();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
