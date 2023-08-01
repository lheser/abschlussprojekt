package klassen.emailService;

//Beispiel:
// 2 euro pro email
// 0,2 euro pro tag pro buch

public class Penalty extends Notification {

	public Penalty(String messageDescription, String messageTypeLocation) {
		super("penalty", ".//.//messageTypes//penalty.txt");
	}
}
