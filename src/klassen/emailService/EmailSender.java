package klassen.emailService;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	
	private static String emailFrom = "";
	private static String password = "";
	
	
	public static void sendEmail(Notification notification){
		
		Properties properties = getProperties(); 
		Authenticator authenticator = getAuthenticator();		
		Session session = Session.getInstance(properties, authenticator);
		
		try
	    {
	      MimeMessage message = new MimeMessage(session);	      
	      message.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      message.addHeader("format", "flowed");
	      message.addHeader("Content-Transfer-Encoding", "8bit");	   
	      message.setFrom(new InternetAddress("tomasasturia@gmail.com"));	   
	      
	      String recipient = EmailWriterUtility.getRecipient(notification);
	      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
	      
	      message.setSubject(notification.getMessageDescription(), "UTF-8");
	      
	      String emailBody = EmailWriterUtility.buildEmailBody(notification);
	      message.setText(emailBody, "UTF-8");	     
	      
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
	 * @return Authenticator for a password in the 
	 */
	private static Authenticator getAuthenticator() {
		Authenticator authenticator = new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(emailFrom, password);
			}
		};
		return authenticator;
	}

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
