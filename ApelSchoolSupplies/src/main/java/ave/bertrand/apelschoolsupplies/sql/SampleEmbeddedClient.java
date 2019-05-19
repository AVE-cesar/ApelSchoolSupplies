package ave.bertrand.apelschoolsupplies.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ave.bertrand.apelschoolsupplies.Consts;
import ave.bertrand.apelschoolsupplies.model.Request;

public class SampleEmbeddedClient {

	private static Connection conn = null;;

	public SampleEmbeddedClient() throws Exception {

		Class.forName(Consts.JDBC_DRIVER);
		createConnection();
	}

	private Connection getValidConnection() {
		System.out.println("On doit tester la connexion db");
		try {
			if (conn != null && conn.isValid(30 * 1000)) {
				System.out.println("La connexion db était toujours valide.");
				return conn;
			} else {
				// refresh it
				closeConnection();
				conn = null;
				createConnection();
				System.out.println("La connexion db était invalide, on l'a refaite.");
				return conn;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void createConnection() throws SQLException {
		if (conn == null) {
			conn = DriverManager.getConnection(Consts.DATABASE_JDBC_URL, // filenames
					Consts.DATABASE_LOGIN, // username
					Consts.DATABASE_PASSWORD);
		}
	}

	public void closeConnection() {
		if (conn == null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// use for SQL command SELECT
	public void queryAndDump(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = getValidConnection().createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		// do something with the result set.
		dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.
	}

	public List<Request> queryRequests(String expression) throws SQLException {
		List<Request> data = new ArrayList();
		Statement st = null;
		ResultSet rs = null;

		st = getValidConnection().createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		while (rs.next()) {

			int mainId = rs.getInt("ID");
			String classNumber = rs.getString("CLASSNUMBER");
			String studentName = rs.getString("STUDENTNAME");
			String gender = rs.getString("GENDER");
			String kitType = rs.getString("KITTYPE");
			String amount = rs.getString("AMOUNT");
			String receivedDate = rs.getString("RECEIVEDDATE");
			String replyTos = rs.getString("REPLYTOS");
			String messageId = rs.getString("MESSAGEID");
			boolean payed = rs.getBoolean("PAYED");
			boolean sent = rs.getBoolean("SENT");
			String note = rs.getString("NOTE");
			boolean enabled = rs.getBoolean("ENABLED");

			Request request = new Request(mainId, 0, classNumber, studentName, gender, kitType, amount, receivedDate,
					replyTos, messageId, note, enabled);
			request.setPayed(payed);
			request.setSent(sent);

			data.add(request);
		}
		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.

		return data;
	}

	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	public void update(String expression) throws SQLException {

		Statement st = null;

		st = getValidConnection().createStatement(); // statements

		int i = st.executeUpdate(expression); // run the query

		if (i == -1) {
			System.out.println("db error : " + expression);
		}

		st.close();
	} // void update()

	public int count(String expression) throws SQLException {
		int row = 0;
		Statement st = null;

		st = getValidConnection().createStatement(); // statements

		ResultSet rs = st.executeQuery(expression); // run the query

		while (rs.next()) {
			int nb = rs.getInt("NB");
			row = nb;
		}

		st.close();

		return row;
	} // void update()

	public void createObjectsOnlyOnce(String expression) {
		Statement st = null;
		try {
			st = getValidConnection().createStatement(); // statements

			int i = st.executeUpdate(expression); // run the query

			if (i == -1) {
				System.out.println("db error : " + expression);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	} // void createObjectsOnlyOnce()

	public static void dump(ResultSet rs) throws SQLException {

		// the order of the rows in a cursor
		// are implementation dependent unless you use the SQL ORDER statement
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;

		// the result set is a cursor into the data. You can only
		// point to one row at a time
		// assume we are pointing to BEFORE the first row
		// rs.next() points to next row and returns true
		// or false if there is no next row, which breaks the loop
		for (; rs.next();) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is indexed

				// with 1 not 0
				if (o != null) {
					System.out.print(o.toString() + " ");
				} else {
					System.out.print("null" + " ");
				}
			}

			System.out.println(" ");
		}
	} // void dump( ResultSet rs )

	public static List<Request> saveDataToDB(List<Request> data, boolean isDemoMode) {
		SampleEmbeddedClient db = null;

		try {
			db = new SampleEmbeddedClient();
		} catch (Exception ex1) {
			ex1.printStackTrace(); // could not start db

			return null; // bye bye
		}

		try {
			int nbInsert = 0;
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					Request request = data.get(i);

					// FIXME ne pas ajouter si déjà présent
					String sqlSSelect = "SELECT count(*) as NB FROM sample_table where messageId = '"
							+ request.getMessageID() + "'";
					int row = db.count(sqlSSelect);

					if (row == 0) {
						String sql = "INSERT INTO sample_table(messageId, classNumber, studentName, gender, kitType, amount, receivedDate, replyTos, enabled) VALUES('"
								+ request.getMessageID() + "', '" + request.getClassNumber() + "', '"
								+ request.getStudentName().replaceAll("'", "''") + "', '" + request.getGender() + "', '"
								+ request.getKitType() + "', '" + request.getAmount() + "', '"
								+ request.getReceivedDate() + "', '" + request.getReplyTos() + "', "
								+ request.isEnabled() + ")";
						System.out.println(sql);
						db.update(sql);

						sql = "INSERT INTO statut_table(messageId) VALUES('" + request.getMessageID() + "')";
						System.out.println(sql);
						db.update(sql);

						nbInsert++;
					}
				}
				System.out.println("On a ajouté: " + nbInsert);
			}

			data = db.reloadDataFromDB();
		} catch (SQLException ex3) {
			ex3.printStackTrace();
		} finally {
			db.closeConnection();
		}
		System.out.println("Fin de l'ajout en DB");
		return data;
	}

	public List<Request> reloadDataFromDB() throws SQLException {
		return this.queryRequests(
				"SELECT a.id as ID, a.messageId as MESSAGEID, a.classNumber as CLASSNUMBER, a.studentName as STUDENTNAME, a.gender as GENDER, a.kitType as KITTYPE, a.amount as AMOUNT, a.receivedDate as RECEIVEDDATE, a.replyTos as REPLYTOS, a.enabled as ENABLED, b.payed as PAYED, b.sent as SENT, a.note as NOTE FROM sample_table a, statut_table b WHERE a.messageId = b.messageId");
	}
} // class Testdb
