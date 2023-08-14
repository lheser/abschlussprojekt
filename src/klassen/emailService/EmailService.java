package klassen.emailService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import klassen.Ausleihe;
import klassen.config.EmailConfiguration;

public class EmailService {

	private static final String emailFrom = EmailConfiguration.getEmailAdress() ;
	private static final String password = EmailConfiguration.getPassword();
	private static Penalty penaltyTemplate = new Penalty();
	private static Map<String, String> verspaetungMeldungen = new HashMap<>();
	private static List<MimeMessage> emailListVerspaetungen = new ArrayList<>();


	/**
	 * Reads message Type from a txt file
	 * @return A String containing the messageType from the messageTypes folder
	 */
	private static String readNotification(Notification notification) {
		StringBuilder sb = new StringBuilder("");			
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(notification.getMessageTypeLocation()), "UTF-8"));
			String line;
			while((line = br.readLine())!= null) {
				line = line.trim();
				if(!line.isEmpty()) {
					sb.append(line);
				}				
				sb.append("\n");				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return sb.toString();
	}

	/**
	 * 
	 * @param ueberfaelligeAusleihen
	 */
	public static void writeVerspaetungMeldungfürJeBesucher(ArrayList<Ausleihe> ueberfaelligeAusleihen) {
		String penalty = readNotification(penaltyTemplate);
		for (Ausleihe ausleihe : ueberfaelligeAusleihen) {
			String message = penalty;
			StringBuffer besucherVorUndNachname = new StringBuffer(ausleihe.getBesucher().getVorname());
			besucherVorUndNachname.append(" ").append(ausleihe.getBesucher().getNachname());
			message = message.replace("<besucher_name>", besucherVorUndNachname.toString());
			message = message.replace("<buch_titel>", ausleihe.getBuch().getTitel());
			message = message.replace("<ausleihe_bis>", ausleihe.getBis().toString());
			verspaetungMeldungen.put(ausleihe.getBesucher().getEmail(), message);
		}
	}

	public static void sendEmails(HashMap<String, String> emails){
		Properties properties = getProperties(); 
		Authenticator authenticator = getAuthenticator();		
		Session session = Session.getInstance(properties, authenticator);
		MimeMessage message = new MimeMessage(session);	
		try
		{
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");	   
			message.setFrom(new InternetAddress(emailFrom));	

			for(String key : emails.keySet()) {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(key, false));
				message.setSubject("Notification", "UTF-8");
				message.setText(emails.get(key));
				message.setSentDate(new Date());
				System.out.println("Message for " + key + " ready");
				Transport.send(message);
				System.out.println("Email sent to " + key);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 	REFERENCE
	 * @param notification 
	 */
	public static void sendEmail(Notification notification){		
		Properties properties = getProperties(); 
		Authenticator authenticator = getAuthenticator();		
		Session session = Session.getInstance(properties, authenticator);
		MimeMessage message = new MimeMessage(session);	


		try
		{
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");	   
			message.setFrom(new InternetAddress(emailFrom));	   

			//String recipient = EmailWriterUtility.getRecipient(notification);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient", false));

			message.setSubject(notification.getMessageDescription(), "UTF-8");

			//String emailBody = EmailService.buildEmailBody(notification);
			//message.setText(emailBody, "UTF-8");	     

			message.setSentDate(new Date()); 

			System.out.println("Message ready");

			Transport.send(message);  

			System.out.println("Email Sent");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return Authenticator for a password and an email account
	 */
	private static Authenticator getAuthenticator() {
		Authenticator authenticator = new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(emailFrom, password);
			}
		};
		return authenticator;
	}

	/**
	 * 
	 * @return System properties for email sending
	 */
	private static Properties getProperties() {
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", "smtp.gmail.com");    
		properties.put("mail.smtp.socketFactory.port", "465");    
		properties.put("mail.smtp.socketFactory.class",    
				"javax.net.ssl.SSLSocketFactory");    
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		return properties;
	}
}
