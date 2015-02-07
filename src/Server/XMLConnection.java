package Server;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Class XMLConnection that creates xml documents and write username and password every time when new user connects.
 * @author amrapoprzanovic
 *
 */
public class XMLConnection {

	private static Document xmldoc;
	private static DocumentBuilder docReader;
	private static XPath xPath;

	/**
	 * Constructor for XMLConnection
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public XMLConnection() throws SAXException, IOException,
			ParserConfigurationException {
		docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		xmldoc = docReader.parse(new File("./USERXML/user.xml"));

		xPath = XPathFactory.newInstance().newXPath();

	}

	public static int userLogin(String username, String password) {

		String hashPassword = hashPassword(password);
		String expersion = "//user[@username=\"" + username
				+ "\" and @password=\"" + password + "\"]";
		System.out.println(expersion);

		try {
			// <---koju return zelimo node(XPathConstants.NODE) ili nodelist
			// (XPathConstants.NODESET)
			Node user = (Node) xPath.compile(expersion).evaluate(xmldoc,
					XPathConstants.NODE);

			if (user == null) {
				String expersion2 = "//user[@username=\"" + username + "\"]";
				Node user2 = (Node) xPath.compile(expersion2).evaluate(xmldoc,
						XPathConstants.NODE);

				// if user does not exist we will do registration (make new
				// user).
				if (user2 == null) {

					Element newUsername = xmldoc.createElement("user");
					newUsername.setAttribute("username", username);
//					newUsername.setAttribute("password", password);
					
					//Changed password with hashPassword
					newUsername.setAttribute("password", hashPassword);
					
					xmldoc.getElementsByTagName("users").item(0)
							.appendChild(newUsername);
					System.out.println(xmldoc.toString());

					StreamResult file = new StreamResult(new File(
							"./USERXML/user.xml"));
					Transformer transformer;

					try {
						transformer = TransformerFactory.newInstance()
								.newTransformer();
						DOMSource source = new DOMSource(xmldoc);
						transformer.transform(source, file);

					} catch (TransformerConfigurationException e) {
						e.printStackTrace();
						return -4; 

					} catch (TransformerFactoryConfigurationError e) {
						e.printStackTrace();
						return -4; // try again we did not not succeed to
									// register in xml

					} catch (TransformerException e) {
						e.printStackTrace();
						return -4; 
					}

					return -1; // password is not good
				}

			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return -3; // if xml doc is not good
		}

		return 0; // everything is ok,
	}
	
	// Zadaca
	
	/**
	 * Method that converts password to hash.
	 * @param password - String password that we forwarded.
	 * @return new String of hash password.
	 */
	private static String hashPassword( String password){
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
				
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public static void main(String[] args) {
		try {
			XMLConnection test = new XMLConnection();
			int user1 = XMLConnection.userLogin("Amra", "P");

			int user2 = XMLConnection.userLogin("Amraa", "P");
			XMLConnection.userLogin("Hiko", "Kiko");

			System.out.println(user2);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
