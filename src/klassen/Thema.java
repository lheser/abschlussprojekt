package klassen;

public class Thema {
	
	private String bezeichnung;
	private int thema_ID;	
	
	
	public Thema(int thema_ID, String bezeichnung) {
		super();
		this.thema_ID = thema_ID;
		this.bezeichnung = bezeichnung;
		
	}
	
	public Thema(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public int getThema_ID() {
		return thema_ID;
	}
	public void setThema_ID(int thema_ID) {
		this.thema_ID = thema_ID;
	}
	@Override
	public String toString() {
		return bezeichnung;
	}
	
}
