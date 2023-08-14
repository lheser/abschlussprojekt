package klassen.emailService;

//Beispiel:
// 2 euro pro email
// 0,2 euro pro tag pro buch

public class Penalty extends Notification {

	public Penalty() {
		super("penalty", ".//.//messageTypes//penalty.txt");
	}
}
