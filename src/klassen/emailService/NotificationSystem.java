package klassen.emailService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import klassen.Ausleihe;
import klassen.Datenbank;

public class NotificationSystem {
	
	private static ArrayList<Ausleihe> ueberfaelligeAusleihen = new ArrayList<>();
	
	
	public void checkAusleihenStatus() {
		getAusleihenLaufenBald();
		getAusleihenUeberfaellig();
	}

	private void getAusleihenUeberfaellig() {
		try {
			ueberfaelligeAusleihen = Datenbank.readUeberfaellige();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private void getAusleihenLaufenBald() {
		
	}
	
	public void sendNotifications() {		
		if(!ueberfaelligeAusleihen.isEmpty()) {
			EmailService.writeVerspaetungMeldungfürJeBesucher(ueberfaelligeAusleihen);
		}
	}
}
