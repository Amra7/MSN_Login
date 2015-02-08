package Client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import GUI.ChatGui;

/**
 * Class Login that creates Login window where user can enter password and username.
 * @author amrapoprzanovic
 *
 */
public class Login {
	public JFrame window ;
	public JTextField usernameField;
	public JPasswordField passwordField;
	private String host;
	private int PORT;
	
	/**
	 * Constructor for Login class.
	 * Receives two parameters.
	 * @param host - host address.
	 * @param PORT - server port.
	 */
	public Login(String host, int PORT){
		this.host=host;
		this.PORT=PORT;
		
	    window = new JFrame("Login");
		window.setLocation(700, 500);
		window.setSize(400, 200);
		JPanel panel = new JPanel();
		
		
		JLabel labelUser =  new JLabel("Enter username: ");
		usernameField =  new JTextField();
		usernameField.setPreferredSize(new Dimension(250,30));
		usernameField.setVisible(true);
		
		
		JLabel labelPass =  new JLabel("Enter password: ");
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(250,30));
		passwordField.setVisible(true);
		
		// private class for buttonLog
		JButton buttonLog =  new JButton("Login");
		buttonLog.addActionListener( new ButtonHandler());
		
		//Anonymous class for  buttonQuit
		JButton buttonQuit =  new JButton("Quit");
		buttonQuit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
			
		});
		
		
		panel.add(labelUser);
		panel.add(usernameField);
		panel.add(labelPass);
		panel.add(passwordField);
		panel.add(buttonLog);
		panel.add(buttonQuit);
		window.setContentPane(panel);
		
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
	}
	
	/**
	 * Class ButtonHandler that extends KeyAdapter implements ActionListener. 
	 * This class takes password and username from client.
	 * @author amrapoprzanovic
	 *
	 */
	public class ButtonHandler  extends KeyAdapter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
		
			
			String username = usernameField.getText();
			String password =  new String (passwordField.getPassword());
			
			// If somebody enter space it would be replaced with non space
			username = username.replaceAll(" ", "");
			password = password.replaceAll(" ", "");
			
			System.out.println(username);
			System.out.println(password);
			
			if ( username.equals("") || password.equals("")){
				showError("Please enter your password and username!");
				return;
			}
			
			Socket client;
			try {
				client = new Socket(host, PORT);
				OutputStream os = client.getOutputStream();
				InputStream is = client.getInputStream();
				os.write((username + "\n").getBytes());
		//		os.write((password + "\n").getBytes());
				String hashPassword = hashPassword(password);
				os.write((hashPassword + "\n").getBytes());
				
				int serverRespons = is.read();
				if (serverRespons == 0){
					ChatGui gui = new ChatGui(client);
					new Thread(gui).start();
				} else if( serverRespons == -1){
					showError("Username and password are not good!");
				} else if( serverRespons == -3){
					showError("Connection is not good please try again!");
				} else {
					showError("Username and password are not good!");
				}
				
				window.dispose(); // close Login window after entering usename and password
				
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * Private class that appears when the user enters the wrong password and username.
	 * @param message
	 */
	private  void showError( String message){
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.WARNING_MESSAGE);
	}
	
	// Samosalni dio  - koji ne treba bit u ovom dijelu pa je prebacen u klasu XML connection
	/**
	 * Method that converts password to hash.
	 * @param password - String password that we forwarded.
	 * @return new String of hash password.
	 */
	private String hashPassword( String password){
		String generatedPassword = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD%");
			md.update(password.getBytes());
			byte [] byteArr = md.digest();
			StringBuilder sb = new StringBuilder();
			for ( int i =0 ; i<byteArr.length; i++){
				sb.append(Integer.toString((byteArr[i] & 0xff) + 0x100, 16).substring(1));
				generatedPassword = sb.toString();
				System.out.println(generatedPassword);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatedPassword;
	
	}
}
