package klassen.emailService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import klassen.Ausleihe;

public class EmailWriterUtility {

	private static ArrayList<String> verspaetungMeldungen = new ArrayList<>();
	//private static ArrayList<String> erinnerungMeldungen = new ArrayList<>();
	private static Penalty penaltyTemplate = new Penalty(null, null);


	/**
	 * Reads message Type from a txt file
	 * @return A String containing the messageType from the messageTypes folder
	 */
	private static String readMessageType(Notification message) {
		StringBuilder sb = new StringBuilder("");			
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(message.getMessageTypeLocation()), "UTF-8"));
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
	 * Parsing von 
	 * @param ueberfaelligeAusleihen
	 */
	public static void writeVerspaetungMeldungfürJeBesucher(ArrayList<Ausleihe> ueberfaelligeAusleihen) {
		String message = readMessageType(penaltyTemplate);
		for (Ausleihe ausleihe : ueberfaelligeAusleihen) {
			StringBuffer besucherVorUndNachname = new StringBuffer(ausleihe.getBesucher().getVorname());
			besucherVorUndNachname.append(" ").append(ausleihe.getBesucher().getNachname());

			message = message.replace("<besucher_name>", besucherVorUndNachname.toString());
			message = message.replace("<buch_titel>", ausleihe.getBuch().getTitel());
			message = message.replace("<ausleihe_bis>", ausleihe.getBis().toString());
			verspaetungMeldungen.add(message);
		}
	}
	
	public static List<MimeMessage> verspaetungEmails(){
		
		
		return null;
		
	}
	
	
	




}
