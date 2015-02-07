package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileAttach {
	private JFrame mainFrame;
	private JLabel statusLabel;
	
	public FileAttach(){
		prepareGUI();
	}

	public void FileAttach(){
		final JFileChooser fileChooser =  new JFileChooser();
		JButton showFileChooser = new JButton("Open File");
		showFileChooser.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnValue = fileChooser.showOpenDialog(mainFrame);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					statusLabel.setText("File selected" + file.getName());
				} else {
					statusLabel.setText("Open command cancelled by user!");
				}
				
			}
			
		});
		
	}
	
	public void prepareGUI(){
		JFrame mainFrame = new JFrame("File Dialog");
		mainFrame.setSize(400, 400);
		JPanel panel = new JPanel();
		
		JLabel statusLabel = new JLabel();
		FileAttach fileDialog = new FileAttach();
		panel.add(statusLabel);

		mainFrame.setContentPane(panel);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		
		FileAttach fileDialog = new FileAttach();
		fileDialog.FileAttach();
	}
}
