package klassen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class Datenbank {
	//DataBase Location und URL Verbindung
	private static final String DB_LOCATION = "";	
	private static final String CONNECTION_URL = "jdbc:derby:" + DB_LOCATION + ";create=true";	

	//TABELLEN: THEMA, BESUCHER, AUSLEIHE
	//Table für die Themen
	private static final String THEMEN_TABLE = "Themen";
	private static final String THEMA_ID = "ThemaID";
	private static final String THEMA_BEZEICHNUNG = "Bezeichnung";	

	//Table für die Bücher
	private static final String BUCH_TABLE = "Buecher";
	private static final String BUCH_ISBN = "BuecherISBN";
	private static final String BUCH_TITEL = "BuecherTitel";	
	private static final String BUCH_AUTOR = "BuecherAutor";
	private static final String BUCH_JAHR = "BuecherJahr";
	private static final String BUCH_THEMA = "BuecherThema";
	private static final String BUCH_VERLAG = "Verlag";	
	private static final String BUCH_PREIS = "BuecherPreis";
	private static final String BUCH_BESCHREIBUNG = "Beschreibung";
	private static final String BUCH_TITELBLATT = "PathTitelblatt";
	private static final String BUCH_ENTLEHNT = "BuchEntlehnt";
	private static final String BUCH_GELOESCHT = "BuchGeloescht";

	//Table für die Besucher
	private static final String BESUCHER_TABLE = "Besucher";
	private static final String BESUCHER_ID = "BesucherID";
	private static final String BESUCHER_VORNAME = "BesucherVorname";
	private static final String BESUCHER_NACHNAME = "BesucherNachname";
	private static final String BESUCHER_GEBURTSDATUM = "BesucherGeburtsdatum";
	private static final String BESUCHER_ADRESSE = "BesucherAdresse";
	private static final String BESUCHER_TELEFON = "BesucherTelefonnummer";
	private static final String BESUCHER_EMAIL = "BesucherEmail";
	private static final String BESUCHER_GELOESCHT = "BesucherGeloescht";

	//Table für die Ausleihen
	private static final String AUSLEIHE_TABLE = "Ausleihen";
	private static final String AUSLEIHE_ID = "AusleihenID";	
	private static final String AUSLEIHE_BUCH_ISBN = "AusleiheBuchISBN";
	private static final String AUSLEIHE_BESUCHER_ID = "AusleihenBesucherID";
	private static final String AUSLEIHE_VON = "AusleiheVon";
	private static final String AUSLEIHE_BIS = "AusleihenBis";	
	private static final String AUSLEIHE_BEMERKUNGEN = "AusleihenBemerkungen";
	private static final String AUSLEIHE_ERLEDIGT = "AusleiheErledigt";	

	//CreateTable methode für jede Tabelle
	//Themen Table Erstellung
	public static void createThemenTable() throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null,  null, THEMEN_TABLE.toUpperCase(), new String[]{"TABLE"});
			if(rs.next()) {
				return;
			}
			String create = "CREATE TABLE " + THEMEN_TABLE + "(" + 
					THEMA_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," + 
					THEMA_BEZEICHNUNG + " VARCHAR(50)," +
					"PRIMARY KEY(" + THEMA_ID + "))";
			stmt.executeUpdate(create);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}
	
	//Bücher Table Erstellung
	public static void createBuecherTable()throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, BUCH_TABLE.toUpperCase(), new String[]{"TABLE"});
			if(rs.next()) {
				return;				
			}
			String create = "CREATE TABLE " + BUCH_TABLE + "(" +
					BUCH_ISBN + " CHAR(13)," + 
					BUCH_TITEL + " VARCHAR(200)," + 
					BUCH_AUTOR + " VARCHAR(200)," + 
					BUCH_JAHR + " INTEGER," +
					BUCH_THEMA + " INTEGER," +
					BUCH_VERLAG + " VARCHAR(200)," +
					BUCH_PREIS + " DOUBLE," + 
					BUCH_BESCHREIBUNG + " VARCHAR(500)," +
					BUCH_TITELBLATT + " VARCHAR(500)," + 
					BUCH_ENTLEHNT + " BOOLEAN," + 
					BUCH_GELOESCHT + " BOOLEAN," +
					"PRIMARY KEY(" + BUCH_ISBN + ")," +
					"FOREIGN KEY(" + BUCH_THEMA + ") REFERENCES " + THEMEN_TABLE + "(" + THEMA_ID + "))";
			stmt.executeUpdate(create);
		}
		catch(SQLException e){
			throw e;

		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;				
			}
		}
	}
	
	//Besucher Table Erstellung
	public static void createBesucherTable() throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, BESUCHER_TABLE.toUpperCase(), new String[]{"TABLE"});
			if(rs.next()) {
				return;
			}
			String create = "CREATE TABLE " + BESUCHER_TABLE +"(" +
					BESUCHER_ID + " BIGINT," +
					BESUCHER_VORNAME + " VARCHAR(200)," + 
					BESUCHER_NACHNAME + " VARCHAR(200)," + 
					BESUCHER_GEBURTSDATUM + " DATE," +
					BESUCHER_ADRESSE + " VARCHAR(200)," +
					BESUCHER_TELEFON + " VARCHAR(20)," + 
					BESUCHER_EMAIL + " VARCHAR(50)," +
					BESUCHER_GELOESCHT + " BOOLEAN," +
					" PRIMARY KEY(" + BESUCHER_ID + "))";
			stmt.executeUpdate(create);
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}
	
	//Ausleihe Table Erstellung
	public static void createAusleihenTable() throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, AUSLEIHE_TABLE.toUpperCase(), new String[]{"TABLE"});
			if(rs.next()) {
				return;
			}
			String create = "CREATE TABLE " + AUSLEIHE_TABLE + "(" + 
					AUSLEIHE_ID + " INTEGER GENERATED ALWAYS AS IDENTITY," +
					AUSLEIHE_BESUCHER_ID + " BIGINT," + 
					AUSLEIHE_BUCH_ISBN + " CHAR(13)," + 
					AUSLEIHE_VON + " DATE," + 
					AUSLEIHE_BIS + " DATE," +
					AUSLEIHE_BEMERKUNGEN + " VARCHAR(200)," +
					AUSLEIHE_ERLEDIGT + " BOOLEAN," +										
					" PRIMARY KEY(" + AUSLEIHE_ID + ")," +
					" FOREIGN KEY(" + AUSLEIHE_BESUCHER_ID + ") REFERENCES " + BESUCHER_TABLE + "(" + BESUCHER_ID + ")," +
					" FOREIGN KEY(" + AUSLEIHE_BUCH_ISBN + ") REFERENCES " + BUCH_TABLE + "(" + BUCH_ISBN + "))";
			stmt.executeUpdate(create);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	//CRUD für Bücher
	//Insert ein Buch in die Datenbank
	public static void insertBuch(Buch buch) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;		
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String insert = "INSERT INTO " + BUCH_TABLE + " VALUES(" + 
					"?," + //ISBN
					"?," + //TITEL
					"?," + //AUTOR
					"?," + //JAHR
					"?," + //THEMA
					"?," + //VERLAG
					"?," + //PREIS
					"?," + //BESCHREIBUNG
					"?," + //TITELBLATT
					"?," + //ENTLEHNT
					"?)"; //GELOESCHT
			stmt = conn.prepareStatement(insert);
			stmt.setString(1, buch.getIsbn());			
			stmt.setString(2, buch.getTitel());
			stmt.setString(3,  buch.getAutor());
			stmt.setInt(4, buch.getJahr());
			stmt.setInt(5, buch.getThema().getThema_ID());
			stmt.setString(6, buch.getVerlag());
			stmt.setDouble(7, buch.getPreis());
			stmt.setString(8, buch.getBeschreibung());
			stmt.setString(9, buch.getTitelblatt());//??
			stmt.setBoolean(10, false);
			stmt.setBoolean(11, false);
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 *  Lies alle Bücher wo isGeloescht = false
	 * @return Eine ArrayList mit allen Bücher
	 * @throws SQLException
	 */
	public static ArrayList<Buch> readBuecher() throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList<Buch> alBuch = new ArrayList<>();
		String select = "SELECT * FROM " + BUCH_TABLE +	
				" INNER JOIN " + THEMEN_TABLE + " ON " + BUCH_TABLE + "." + BUCH_THEMA + "=" +
				THEMEN_TABLE + "." + THEMA_ID +		
				" WHERE " + BUCH_GELOESCHT + "=?";
		/*if(isbn != null)
			select += " AND " + BUCH_ISBN + "=?";*/
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);
			/*if(isbn != null)
				stmt.setString(2, isbn);*/
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBuch.add(new Buch(rs.getString(BUCH_ISBN),
						rs.getString(BUCH_TITEL),
						rs.getString(BUCH_AUTOR),
						rs.getInt(BUCH_JAHR),
						new Thema(rs.getInt(THEMA_ID), rs.getString(THEMA_BEZEICHNUNG)),
						rs.getString(BUCH_VERLAG),
						rs.getDouble(BUCH_PREIS),
						rs.getString(BUCH_BESCHREIBUNG),
						rs.getString(BUCH_TITELBLATT),
						rs.getBoolean(BUCH_ENTLEHNT),
						rs.getBoolean(BUCH_GELOESCHT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alBuch;
	}

	/**
	 * Lies alle Bücher für eine ISBN Nummer
	 * @param isbn
	 * @return Eine ArrayList mit allen Bücher
	 * @throws SQLException
	 */
	public static ArrayList<Buch> readIsbnBuecher(String isbn) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList<Buch> alBuch = new ArrayList<>();
		String select = "SELECT * FROM " + BUCH_TABLE +	
				" INNER JOIN " + THEMEN_TABLE + " ON " + BUCH_TABLE + "." + BUCH_THEMA + "=" +
				THEMEN_TABLE + "." + THEMA_ID +		
				" WHERE " + BUCH_GELOESCHT + "=? AND " + BUCH_ISBN + "=?";		
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);			
			stmt.setString(2, isbn);
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBuch.add(new Buch(rs.getString(BUCH_ISBN),
						rs.getString(BUCH_TITEL),
						rs.getString(BUCH_AUTOR),
						rs.getInt(BUCH_JAHR),
						new Thema(rs.getInt(THEMA_ID), rs.getString(THEMA_BEZEICHNUNG)),
						rs.getString(BUCH_VERLAG),
						rs.getDouble(BUCH_PREIS),
						rs.getString(BUCH_BESCHREIBUNG),
						rs.getString(BUCH_TITELBLATT),
						rs.getBoolean(BUCH_ENTLEHNT),
						rs.getBoolean(BUCH_GELOESCHT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alBuch;
	}

	/**
	 * Lies alle Bücher für einen Buchtitel wo isGeloescht = false
	 * @param titel
	 * @return Eine ArrayList mit Bücher fur eine String im Titel
	 * @throws SQLException
	 */
	public static ArrayList<Buch> readTitelBuecher(String titel) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String titelUpper = titel.toUpperCase();
		ArrayList<Buch> alBuch = new ArrayList<>();
		String select = "SELECT * FROM " + BUCH_TABLE +	
				" INNER JOIN " + THEMEN_TABLE + " ON " + BUCH_TABLE + "." + BUCH_THEMA + "=" + THEMEN_TABLE + "." + THEMA_ID +	
				" WHERE " + BUCH_GELOESCHT + "=? AND UPPER (" + BUCH_TITEL + ") LIKE ?";	
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);			
			stmt.setString(2, "%" + titelUpper + "%");
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBuch.add(new Buch(rs.getString(BUCH_ISBN),
						rs.getString(BUCH_TITEL),
						rs.getString(BUCH_AUTOR),
						rs.getInt(BUCH_JAHR),
						new Thema(rs.getInt(THEMA_ID), rs.getString(THEMA_BEZEICHNUNG)),
						rs.getString(BUCH_VERLAG),
						rs.getDouble(BUCH_PREIS),
						rs.getString(BUCH_BESCHREIBUNG),
						rs.getString(BUCH_TITELBLATT),
						rs.getBoolean(BUCH_ENTLEHNT),
						rs.getBoolean(BUCH_GELOESCHT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}		
		return alBuch;
	}

	/**
	 * Lies Alle Bücher für eine Autorname wo isGeloescht = false
	 * @param autor
	 * @return eine ArrayList mit Büchern für einen Autor
	 * @throws SQLException
	 */
	public static ArrayList<Buch> readAutorBuecher(String autor) throws SQLException{		
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		String autorUpper = autor.toUpperCase();
		ArrayList<Buch> alBuch = new ArrayList<>();		
		String select = "SELECT * FROM " + BUCH_TABLE +	
				" INNER JOIN " + THEMEN_TABLE + " ON " + BUCH_TABLE + "." + BUCH_THEMA + "=" + THEMEN_TABLE + "." + THEMA_ID +	
				" WHERE " + BUCH_GELOESCHT + "=? AND UPPER (" + BUCH_AUTOR + ") LIKE ?";	
		try {
			conn = DriverManager.getConnection(CONNECTION_URL); 
			stmt = conn.prepareStatement(select);	
			stmt.setBoolean(1, false);
			stmt.setString(2, "%" + autorUpper + "%");			
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBuch.add(new Buch(rs.getString(BUCH_ISBN),
						rs.getString(BUCH_TITEL),
						rs.getString(BUCH_AUTOR),
						rs.getInt(BUCH_JAHR),
						new Thema(rs.getInt(THEMA_ID), rs.getString(THEMA_BEZEICHNUNG)),
						rs.getString(BUCH_VERLAG),
						rs.getDouble(BUCH_PREIS),
						rs.getString(BUCH_BESCHREIBUNG),
						rs.getString(BUCH_TITELBLATT),
						rs.getBoolean(BUCH_ENTLEHNT),
						rs.getBoolean(BUCH_GELOESCHT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		return alBuch;
	}

	/**
	 * Lies alle Bücher für ein Thema ID wo isGeloescht = false
	 * @param thema ID
	 * @return Eine ArrayList mit allen Bücher für ein Thema
	 * @throws SQLException
	 */
	public static ArrayList<Buch> readThemaBuecher(int thema) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;	
		ResultSet rs = null;
		ArrayList<Buch> alBuch = new ArrayList<>();
		String select = "SELECT * FROM " + BUCH_TABLE +	
				" INNER JOIN " + THEMEN_TABLE + " ON " + BUCH_TABLE + "." + BUCH_THEMA + "=" +
				THEMEN_TABLE + "." + THEMA_ID +	" WHERE " + BUCH_GELOESCHT + "=? AND " + BUCH_THEMA + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);
			stmt.setInt(2, thema);
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBuch.add(new Buch(rs.getString(BUCH_ISBN),
						rs.getString(BUCH_TITEL),
						rs.getString(BUCH_AUTOR),
						rs.getInt(BUCH_JAHR),
						new Thema(rs.getInt(THEMA_ID), rs.getString(THEMA_BEZEICHNUNG)),
						rs.getString(BUCH_VERLAG),
						rs.getDouble(BUCH_PREIS),
						rs.getString(BUCH_BESCHREIBUNG),
						rs.getString(BUCH_TITELBLATT),
						rs.getBoolean(BUCH_ENTLEHNT),
						rs.getBoolean(BUCH_GELOESCHT)));

			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		return alBuch;
	}

	/**
	 * Update von Buchdaten in der Datenbank
	 * @param buch
	 * @throws SQLException
	 */
	public static void updateBuch(Buch buch) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String  update = "UPDATE " + BUCH_TABLE + " SET " +					 
					BUCH_TITEL + " =?, " + 
					BUCH_AUTOR + " =?, " + 
					BUCH_JAHR + " =?, " +
					BUCH_THEMA + " =?, " +
					BUCH_VERLAG + " =?, " +
					BUCH_PREIS + " =?, " + 
					BUCH_BESCHREIBUNG + " =?, " +
					BUCH_TITELBLATT + " =? WHERE " + BUCH_ISBN + "=?";
			stmt = conn.prepareStatement(update);
			stmt.setString(1, buch.getTitel());
			stmt.setString(2, buch.getAutor());
			stmt.setInt(3, buch.getJahr());
			stmt.setInt(4, buch.getThema().getThema_ID());
			stmt.setString(5, buch.getVerlag());
			stmt.setDouble(6, buch.getPreis());
			stmt.setString(7, buch.getBeschreibung());
			stmt.setString(8, buch.getTitelblatt());
			stmt.setString(9, buch.getIsbn());
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Set buch isEntlehnt auf true oder false
	 * @param buch
	 * @param bool
	 * @throws SQLException
	 * Update von Entlehnt Column vom Buch
	 */
	public static void setBuchEntlehnt(Buch buch, boolean bool) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + BUCH_TABLE + " SET " +
					BUCH_ENTLEHNT + "=? WHERE " + BUCH_ISBN + "=?";					
			stmt = conn.prepareStatement(update);
			stmt.setBoolean(1, bool);
			stmt.setString(2, buch.getIsbn());
			stmt.executeUpdate();			
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Set Buch variable isGeloescht auf true, somit das Buch nicht mehr aktiv ist
	 * @param buch
	 * @throws SQLException
	 */
	public static void deleteBuch(Buch buch) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + BUCH_TABLE + " SET " +
					BUCH_GELOESCHT + "=? WHERE " + BUCH_ISBN + "=?";					
			stmt = conn.prepareStatement(update);
			stmt.setBoolean(1, true);
			stmt.setString(2, buch.getIsbn());
			stmt.executeUpdate();			
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}		
	}	


	//CRUD für Themen
	/**
	 * Insert ein Thema in die Datenbank
	 * @param thema
	 * @throws SQLException
	 */
	public static void insertThema(Thema thema) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String insert = "INSERT INTO " + THEMEN_TABLE + " (" +
					THEMA_BEZEICHNUNG + ") VALUES(" +
					"?)";	//Thema Bezeichnung	
			stmt = conn.prepareStatement(insert);
			stmt.setString(1, thema.getBezeichnung());
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}	
	}

	/**
	 * Lies Alle Themen
	 * @return eine ArrayList von Themen
	 * @throws SQLException
	 */
	public static ArrayList<Thema> readThemen() throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Thema> alThemen = new ArrayList<>();
		String select = "SELECT * FROM " + THEMEN_TABLE + " ORDER BY " + THEMA_ID + " ASC";
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Thema t = new Thema(rs.getInt(THEMA_ID), rs.getString(THEMA_BEZEICHNUNG));
				alThemen.add(t);
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}		
		return alThemen;		
	}

	//CRUD für Besucher
	/**
	 * Insert einen Besucher in die Datenbank
	 * @param besucher
	 * @throws SQLException
	 */
	public static void insertBesucher(Besucher besucher) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String insert = "INSERT INTO " + BESUCHER_TABLE + 
					" VALUES (" +
					"?," + //BesucherID
					"?," + //Vorname
					"?," + //Nachname
					"?," + //Geburtsdatum
					"?," + //Adresse
					"?," + //Telefon
					"?," + //Email
					"?)"; //Geloescht
			stmt = conn.prepareStatement(insert);
			stmt.setLong(1, besucher.getBesucherId());
			stmt.setString(2, besucher.getVorname());
			stmt.setString(3, besucher.getNachname());	
			java.sql.Date geburtsdatum = java.sql.Date.valueOf(besucher.getGeburtsdatum());
			stmt.setDate(4, geburtsdatum);
			stmt.setString(5, besucher.getAdresse());
			stmt.setString(6, besucher.getTelefonnummer());
			stmt.setString(7,  besucher.getEmail());
			stmt.setBoolean(8, false);
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Lies alle Besucher wo isGeloescht = false
	 * @return ArrayList mit allen Besucher
	 * @throws SQLException
	 */
	public static ArrayList<Besucher> readBesucher() throws SQLException {
		return readBesucher(0);
	}

	/**
	 * Lies alle Besucher wo Besucher isGeloescht = false
	 * @param besucherId
	 * @return Eine Liste von Besucher
	 * @throws SQLException
	 */
	public static ArrayList<Besucher> readBesucher(long besucherId) throws SQLException {		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Besucher> alBesucher = new ArrayList<>();
		String select = "SELECT * FROM " + BESUCHER_TABLE + " WHERE " + BESUCHER_GELOESCHT + "=?";
		if(besucherId != 0) 
			select += " AND " + BESUCHER_ID + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);
			if(besucherId != 0)
				stmt.setLong(2, besucherId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBesucher.add(new Besucher(
						rs.getLong(BESUCHER_ID),
						rs.getString(BESUCHER_VORNAME),
						rs.getString(BESUCHER_NACHNAME),
						rs.getDate(BESUCHER_GEBURTSDATUM).toLocalDate(),
						rs.getString(BESUCHER_ADRESSE),						
						rs.getString(BESUCHER_TELEFON),
						rs.getString(BESUCHER_EMAIL),	
						rs.getBoolean(BESUCHER_GELOESCHT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}

		return alBesucher;
	}

	/**
	 * Lies ein Besucher für eine Besucher ID wo isGeloescht = false
	 * @param besucherId
	 * @return Ein Besucher mit der entsprechenden ID Nummmer
	 * @throws SQLException
	 */
	public static Besucher getSingleBesucher(long besucherId) throws SQLException {		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Besucher besucher = null;
		String select = "SELECT * FROM " + BESUCHER_TABLE + " WHERE " + BESUCHER_GELOESCHT + "=?" +
				" AND " + BESUCHER_ID + "=?";		
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);
			stmt.setLong(2, besucherId);			
			rs = stmt.executeQuery();
			while(rs.next()) {
				besucher = new Besucher(
						rs.getLong(BESUCHER_ID),
						rs.getString(BESUCHER_VORNAME),
						rs.getString(BESUCHER_NACHNAME),
						rs.getDate(BESUCHER_GEBURTSDATUM).toLocalDate(),
						rs.getString(BESUCHER_ADRESSE),						
						rs.getString(BESUCHER_TELEFON),
						rs.getString(BESUCHER_EMAIL),	
						rs.getBoolean(BESUCHER_GELOESCHT));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return besucher;
	}

	/**
	 * Lies alle Besucher für eine Nachname isGeloescht = false
	 * @param Nachname
	 * @return Eine ArrayList mit Besucher für eine Nachname
	 * @throws SQLException
	 */
	public static ArrayList<Besucher> readBesucher(String nachname) throws SQLException {		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String upperName = nachname.toUpperCase();
		ArrayList<Besucher> alBesucher = new ArrayList<>();
		String select = "SELECT * FROM " + BESUCHER_TABLE + 
				" WHERE " + BESUCHER_GELOESCHT + "=? AND UPPER (" + BESUCHER_NACHNAME + ") LIKE ? ";	
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);
			stmt.setString(2, "%" + upperName + "%");			
			rs = stmt.executeQuery();
			while(rs.next()) {
				alBesucher.add(new Besucher(
						rs.getLong(BESUCHER_ID),
						rs.getString(BESUCHER_VORNAME),
						rs.getString(BESUCHER_NACHNAME),
						rs.getDate(BESUCHER_GEBURTSDATUM).toLocalDate(),
						rs.getString(BESUCHER_ADRESSE),						
						rs.getString(BESUCHER_TELEFON),
						rs.getString(BESUCHER_EMAIL),	
						rs.getBoolean(BESUCHER_GELOESCHT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}

		return alBesucher;
	}

	/**
	 * Update von Besucherdaten in die Datenbank
	 * @param besucher
	 * @throws SQLException
	 */
	public static void updateBesucher(Besucher besucher) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + BESUCHER_TABLE + " SET " +				
					BESUCHER_VORNAME + "=?," + 
					BESUCHER_NACHNAME + "=?," + 				
					BESUCHER_GEBURTSDATUM + "=?," + 
					BESUCHER_ADRESSE + "=?," + 
					BESUCHER_TELEFON + "=?," + 
					BESUCHER_EMAIL + "=? WHERE " + BESUCHER_ID + "=?";
			stmt = conn.prepareStatement(update);
			stmt.setString(1, besucher.getVorname());
			stmt.setString(2, besucher.getNachname());
			java.sql.Date geburtsdatum = java.sql.Date.valueOf(besucher.getGeburtsdatum());
			stmt.setDate(3, geburtsdatum);
			stmt.setString(4, besucher.getAdresse());
			stmt.setString(5, besucher.getTelefonnummer());
			stmt.setString(6, besucher.getEmail());
			stmt.setLong(7, besucher.getBesucherId());
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Set Besucher entfernt variable auf true, so dass der Besucher nicht mehr aktive ist
	 * @param besucher
	 * @throws SQLException
	 */
	public static void deleteBesucher(Besucher besucher) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + BESUCHER_TABLE + " SET " +
					BESUCHER_GELOESCHT + "=? WHERE " + BESUCHER_ID + "=?";
			stmt = conn.prepareStatement(update);
			stmt.setBoolean(1, true);
			stmt.setLong(2, besucher.getBesucherId());
			stmt.executeUpdate();

		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	//CRUD für Ausleihen
	/**
	 * Insert eine Ausleihe in die Datenbank
	 * @param buch
	 * @param besucher
	 * @throws SQLException
	 */
	public static void insertAusleihe(Ausleihe ausleihe) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String instert = "INSERT INTO " + AUSLEIHE_TABLE + "(" +
					AUSLEIHE_BUCH_ISBN + "," + AUSLEIHE_BESUCHER_ID + "," + AUSLEIHE_VON + "," +
					AUSLEIHE_BIS + "," + AUSLEIHE_ERLEDIGT + ") VALUES(" + 
					"?," + //BUCH ISBN
					"?," + //BESUCHER ID
					"?," + //VON
					"?," + //BIS
					"?)"; //ERLEDIGT

			stmt = conn.prepareStatement(instert);
			stmt.setString(1, ausleihe.getBuch().getIsbn());
			stmt.setLong(2, ausleihe.getBesucher().getBesucherId());
			java.sql.Date vonDate = java.sql.Date.valueOf(ausleihe.getVon());
			stmt.setDate(3, vonDate);
			java.sql.Date bisDate = java.sql.Date.valueOf(ausleihe.getBis());
			stmt.setDate(4, bisDate);
			stmt.setBoolean(5, ausleihe.isErledigt());			
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}		
	}
	
	/**
	 * Lies alle Ausleihe eines Besuchers wo erledigt = false
	 * @param besucherID
	 * @return eine Liste von Gebuchten Ausleihen
	 * @throws SQLException
	 */
	public static ArrayList<Ausleihe> readBesucherAusleihen(long besucherID) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Ausleihe> alAusleihe = new ArrayList<>();
		String select = "SELECT * FROM " + AUSLEIHE_TABLE + 
				" INNER JOIN " + BUCH_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BUCH_ISBN + "=" +
				BUCH_TABLE + "." + BUCH_ISBN + 
				" INNER JOIN " + BESUCHER_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BESUCHER_ID + "=" 
				+ BESUCHER_TABLE + "." + BESUCHER_ID +
				" WHERE " + AUSLEIHE_BESUCHER_ID + "=" + besucherID + " AND " + AUSLEIHE_ERLEDIGT + "=" + false;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			rs = stmt.executeQuery();
			while(rs.next()) {
				alAusleihe.add(new Ausleihe(
						rs.getInt(AUSLEIHE_ID), 
						rs.getDate(AUSLEIHE_VON).toLocalDate(),
						rs.getDate(AUSLEIHE_BIS).toLocalDate(), 
						new Buch(rs.getString(AUSLEIHE_BUCH_ISBN),
								rs.getString(BUCH_TITEL),
								rs.getString(BUCH_AUTOR)),
						new Besucher(rs.getLong(AUSLEIHE_BESUCHER_ID),
								rs.getString(BESUCHER_VORNAME),
								rs.getString(BESUCHER_NACHNAME)),
						rs.getString(AUSLEIHE_BEMERKUNGEN),
						rs.getBoolean(AUSLEIHE_ERLEDIGT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alAusleihe;		
	}

	/**
	 * Lies eine Ausleihe nach Buch ISBN wo erledigt = false
	 * @param buchISBN
	 * @return Ausleihe Object für eine BUCH ISBN-Nummer
	 * @throws SQLException
	 */
	public static Ausleihe readSingleAusleihe(String buchISBN) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		Ausleihe ausleihe = null;
		String select = "SELECT * FROM " + AUSLEIHE_TABLE + 
				" INNER JOIN " + BUCH_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BUCH_ISBN + "=" +
				BUCH_TABLE + "." + BUCH_ISBN + 
				" INNER JOIN " + BESUCHER_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BESUCHER_ID + "=" 
				+ BESUCHER_TABLE + "." + BESUCHER_ID +
				" WHERE " + AUSLEIHE_BUCH_ISBN + "=? AND " + AUSLEIHE_ERLEDIGT + "=?";
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setString(1, buchISBN);
			stmt.setBoolean(2, false);
			rs = stmt.executeQuery();
			while(rs.next()) {				
				ausleihe = new Ausleihe(
						rs.getInt(AUSLEIHE_ID), 
						rs.getDate(AUSLEIHE_VON).toLocalDate(),
						rs.getDate(AUSLEIHE_BIS).toLocalDate(), 
						new Buch(rs.getString(AUSLEIHE_BUCH_ISBN),
								rs.getString(BUCH_TITEL),
								rs.getString(BUCH_AUTOR)),
						new Besucher(rs.getLong(AUSLEIHE_BESUCHER_ID),
								rs.getString(BESUCHER_VORNAME),
								rs.getString(BESUCHER_NACHNAME)),
						rs.getString(AUSLEIHE_BEMERKUNGEN),
						rs.getBoolean(AUSLEIHE_ERLEDIGT));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return ausleihe;		
	}

	/**
	 * Ausleihe erledigen aus dem Besucherdetailsfenster
	 * @param ausleiheID
	 * @throws SQLException
	 */
	public static void ausleiheErledigen(int ausleiheID) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + AUSLEIHE_TABLE + " SET " +
					AUSLEIHE_ERLEDIGT + "=? WHERE " + AUSLEIHE_ID + "=" + ausleiheID + 
					" AND " + AUSLEIHE_ERLEDIGT + "=?";
			stmt = conn.prepareStatement(update);
			stmt.setBoolean(1, true);
			stmt.setBoolean(2, false);
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Ausleihe erledigen aus dem Bücherkatalog Tab
	 * @param Buchisbn
	 * @throws SQLException
	 */
	public static void ausleiheErledigen(String isbn) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + AUSLEIHE_TABLE + " SET " +
					AUSLEIHE_ERLEDIGT + "=? WHERE " + AUSLEIHE_BUCH_ISBN + "=? AND " + AUSLEIHE_ERLEDIGT + "=?";			
			stmt = conn.prepareStatement(update);
			stmt.setBoolean(1, true);
			stmt.setString(2, isbn);
			stmt.setBoolean(3, false);
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Set Bemerkungen bei der Rückgabe von Bücher aus dem Bucherverwaltungs Tab
	 * @param isbn
	 * @throws SQLException
	 */
	public static void setAusleiheBemerkugen(String isbn, String bemerkungen) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + AUSLEIHE_TABLE + " SET " +
					AUSLEIHE_BEMERKUNGEN + "=? WHERE " + AUSLEIHE_ERLEDIGT + "=? AND " +
					AUSLEIHE_BUCH_ISBN + "=?";			
			stmt = conn.prepareStatement(update);
			stmt.setString(1, bemerkungen);
			stmt.setBoolean(2, false);
			stmt.setString(3, isbn);
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Set Bemerkungen bei der Rückgabe von Bücher aus dem Besucherdetails Fenster
	 * @param ausleiheID
	 * @param bemerkungen
	 * @throws SQLException
	 */
	public static void setAusleiheBemerkugen(int ausleiheID, String bemerkungen) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + AUSLEIHE_TABLE + " SET " +
					AUSLEIHE_BEMERKUNGEN + "=? WHERE " + AUSLEIHE_ERLEDIGT + "=? AND " +
					AUSLEIHE_ID + "=?";			
			stmt = conn.prepareStatement(update);
			stmt.setString(1, bemerkungen);
			stmt.setBoolean(2, false);
			stmt.setInt(3, ausleiheID);
			stmt.executeUpdate();			
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * Lies Alle Ausleihen wo erledigt = false
	 * @return ArrayList mit allen Aktiven Ausleihen
	 * @throws SQLException
	 */
	public static ArrayList<Ausleihe> readAktiveAusleihen() throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Ausleihe> alAusleihe = new ArrayList<>();
		String select = "SELECT * FROM " + AUSLEIHE_TABLE + 
				" INNER JOIN " + BUCH_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BUCH_ISBN + "=" +
				BUCH_TABLE + "." + BUCH_ISBN + 
				" INNER JOIN " + BESUCHER_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BESUCHER_ID + "=" +
				BESUCHER_TABLE + "." + BESUCHER_ID +
				" WHERE " + AUSLEIHE_ERLEDIGT + "=" + false;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			rs = stmt.executeQuery();
			while(rs.next()) {
				alAusleihe.add(new Ausleihe(
						rs.getInt(AUSLEIHE_ID), 
						rs.getDate(AUSLEIHE_VON).toLocalDate(),
						rs.getDate(AUSLEIHE_BIS).toLocalDate(), 
						new Buch(rs.getString(AUSLEIHE_BUCH_ISBN),
								rs.getString(BUCH_TITEL),
								rs.getString(BUCH_AUTOR)),
						new Besucher(rs.getLong(AUSLEIHE_BESUCHER_ID),
								rs.getString(BESUCHER_VORNAME),
								rs.getString(BESUCHER_NACHNAME)),
						rs.getString(AUSLEIHE_BEMERKUNGEN),
						rs.getBoolean(AUSLEIHE_ERLEDIGT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alAusleihe;
	}

	/**
	 * Lies Überfällige Ausleihen
	 * @return ArrayList mit überfällige Ausleihen
	 * @throws SQLException
	 */
	public static ArrayList<Ausleihe> readUeberfaellige() throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		ArrayList<Ausleihe> alAusleihe = new ArrayList<>();
		String select = "SELECT * FROM " + AUSLEIHE_TABLE + 
				" INNER JOIN " + BUCH_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BUCH_ISBN + "=" +
				BUCH_TABLE + "." + BUCH_ISBN + 
				" INNER JOIN " + BESUCHER_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BESUCHER_ID + "=" 
				+ BESUCHER_TABLE + "." + BESUCHER_ID +
				" WHERE " + AUSLEIHE_ERLEDIGT + " =? " +
				" AND " + AUSLEIHE_BIS  + "< ?";
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			java.sql.Date heute = java.sql.Date.valueOf(LocalDate.now());
			stmt.setBoolean(1, false);
			stmt.setDate(2, heute);			
			rs = stmt.executeQuery();
			while(rs.next()) {
				alAusleihe.add(new Ausleihe(
						rs.getInt(AUSLEIHE_ID), 
						rs.getDate(AUSLEIHE_VON).toLocalDate(),
						rs.getDate(AUSLEIHE_BIS).toLocalDate(), 
						new Buch(rs.getString(AUSLEIHE_BUCH_ISBN),
								rs.getString(BUCH_TITEL),
								rs.getString(BUCH_AUTOR)),
						new Besucher(rs.getLong(AUSLEIHE_BESUCHER_ID),
								rs.getString(BESUCHER_VORNAME),
								rs.getString(BESUCHER_NACHNAME)),
						rs.getString(AUSLEIHE_BEMERKUNGEN),
						rs.getBoolean(AUSLEIHE_ERLEDIGT)));
			}
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alAusleihe;			
	}

	/**
	 * Suche Alle Ausleihen
	 * @return Eine ArrayList mit allen Ausleihen (Aktive und Erledige)
	 * @throws SQLException
	 */
	public static ArrayList<Ausleihe> readAlleAusleihen() throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		ArrayList<Ausleihe> alAusleihe = new ArrayList<>();
		String select = "SELECT * FROM " + AUSLEIHE_TABLE + 
				" INNER JOIN " + BUCH_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BUCH_ISBN + "=" +
				BUCH_TABLE + "." + BUCH_ISBN + 
				" INNER JOIN " + BESUCHER_TABLE + " ON " + AUSLEIHE_TABLE + "." + AUSLEIHE_BESUCHER_ID + "=" 
				+ BESUCHER_TABLE + "." + BESUCHER_ID;	
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			rs = stmt.executeQuery();
			while(rs.next()) {
				alAusleihe.add(new Ausleihe(
						rs.getInt(AUSLEIHE_ID), 
						rs.getDate(AUSLEIHE_VON).toLocalDate(),
						rs.getDate(AUSLEIHE_BIS).toLocalDate(), 
						new Buch(rs.getString(AUSLEIHE_BUCH_ISBN),
								rs.getString(BUCH_TITEL),
								rs.getString(BUCH_AUTOR)),
						new Besucher(rs.getLong(AUSLEIHE_BESUCHER_ID),
								rs.getString(BESUCHER_VORNAME),
								rs.getString(BESUCHER_NACHNAME)),
						rs.getString(AUSLEIHE_BEMERKUNGEN),
						rs.getBoolean(AUSLEIHE_ERLEDIGT)));
			}			
			rs.close();
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
					if(conn != null) {
						conn.close();
					}
				}
			}
			catch(SQLException e) {
				throw e;
			}
		}
		return alAusleihe;
	}
}
