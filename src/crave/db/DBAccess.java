package crave.db;

import java.sql.*;
import java.util.Properties;

public class DBAccess {
	
	/** The name of the MySQL account to use */
	private final String userName = "appUser";

	/** The password for the MySQL account */
	private final String password = "Food216";

	/** The name of the MySQL server */
	private final String serverName = "localhost";

	/** The port of the MySQL server */
	private final int portNumber = 3306;

	/** The name of the database */
	private final String dbName = "restaurantsApp";
	
	/**
	 * Get a new database connection
	 * @return A connection to the database
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		return DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
				connectionProps);
	}
	
	/**
	 * Queries the password associated with the given username in the database
	 * @param username The username who's password is being looked up
	 * @param conn The connection to the database
	 * @return A char array containing the password associated with the given username
	 */
	public char[] queryPassword(String username, Connection conn) {
		System.out.println("Validating login information...");
		Statement stmt = null;
		ResultSet rs = null;
		String pw = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(String.format("select U.password from users U where U.username = \"%s\";", username));
			while (rs.next()) {
				 pw = rs.getString("password");
			}
		} catch (SQLException e) {
			System.err.println("ERROR: Could not execute query");
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			System.out.print('\n');
		}
		return pw.toCharArray();
	}
	
	public void insertUser(String name, String username, char[] password, Connection conn) {
        String pwString = new String(password);
        System.out.printf("Inserting user %s with username %s and password %s\n", name, username, pwString);
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(String.format("insert into users(name, username, password) values (\"%s\", \"%s\", \"%s\");", name, username, pwString));
        } catch (SQLException e) {
            System.err.println("ERROR: Could not create user");
            e.printStackTrace();
        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            System.out.println('\n');
        }
    }
	
	public boolean usernameExists(String username, Connection conn) {
        System.out.printf("Checking for username %s\n", username);
        Statement statement = null;
        ResultSet resultSet = null;
        int result = -1;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(String.format("select count(*) from users where username = \"%s\";", username));
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Could not check username");
            e.printStackTrace();
        }
        return result > 0;
    }
	
	/** Executes a general query using the query generator and returns the result set */
	public Pair<ResultSet, Statement> generalQuery(Connection conn, String argString, QueryManager manager) {
		System.out.println("Querying database...");
		Statement stmt = null;
		ResultSet rs = null;
		
		//get the query according to the argString parameters (created in actionPerformed
		//method of SearchWindow
		Query q = manager.produceQuery(argString);
		manager.flush();
		
		System.out.println("[DATABASE ACCESS] Query: \n" + q);
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(q.getQuery());
		} catch (SQLException e) {
			System.err.println("ERROR: Could not execute query");
			e.printStackTrace();
		} 
//		finally {
//			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
//			System.out.print('\n');
//		}
		return new Pair<ResultSet, Statement>(rs, stmt);
	}
	
	/**
	 * Connect to MySQL and execute queries specified in homework
	 */
	public void cleanup(Connection conn) {
		try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		System.out.println("Database access complete");
	}
}