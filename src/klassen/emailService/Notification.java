package klassen.emailService;

/**
 * 
 * @author Lucas Henao
 *
 *Die Klassen die diese Klasse erweitern mussen die Nachricht für den email vorbereiten.
 * 
 */
public abstract class Notification {

	private String messageDescription;
	private String messageTypeLocation;
	
	public Notification(String messageDescriton, String messageTypeLocation) {
		super();
		this.messageTypeLocation = messageTypeLocation;
		this.messageDescription = messageDescriton;
	}


	public String getMessageTypeLocation() {
		return messageTypeLocation;
	}

	public String getMessageDescription() {
		return messageDescription;
	}
	
}
