package klassen.emailService;

import java.time.LocalDate;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import klassen.Ausleihe;
import klassen.Besucher;
import klassen.Buch;

public class MessagingTest {

	public static void main(String[] args) {
		
		Buch buch = new Buch();
		buch.setTitel("Against the Day by Thomas Pinchon");
		Besucher besucher = new Besucher();
		besucher.setVorname("");
		besucher.setEmail("");
		Ausleihe ausleihe = new Ausleihe(0, LocalDate.now(), LocalDate.now().plusDays(3), buch, besucher, null, false);
		Penalty penalty = new Penalty(null, null);
		Reminder reminder = new Reminder(null, null);
		//System.out.println(MessageWriterUtility.buildEmailBody(reminder));
		
		
		System.out.println("Simple Email Start");
		
		String emailFrom = "";

		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");    
		props.put("mail.smtp.socketFactory.port", "465");    
		props.put("mail.smtp.socketFactory.class",    
				"javax.net.ssl.SSLSocketFactory");    
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465"); 

		Authenticator authenticator = new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(emailFrom,"");  
			}
		};
		
		Session session = Session.getInstance(props, authenticator);
		//EmailSender.sendEmail(session, reminder);
		EmailSender.sendEmail(reminder);
		
	}
}
