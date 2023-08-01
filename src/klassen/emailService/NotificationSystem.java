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
	
	public void sendNotifications() {		
		if(!ueberfaelligeAusleihen.isEmpty()) {
			EmailWriterUtility.writeVerspaetungMeldungfürJeBesucher(ueberfaelligeAusleihen);
		}
	}

	private void getAusleihenLaufenBald() {
		// TODO Auto-generated method stub
		
	}

	private List<Ausleihe> getAusleihenUeberfaellig() {
		try {
			ueberfaelligeAusleihen = Datenbank.readUeberfaellige();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ueberfaelligeAusleihen;		
	}

}
