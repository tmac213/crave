package crave.db;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;

import javax.swing.*;

public class CraveGUI {
	
	public Connection conn;			//the database connection
	public DBAccess dbAccess;		//database access class
	public QueryManager qman;		//a query generator given user input
	
	public CraveGUI() throws SQLException{
		
		/* Create a query manager */
		qman = new QueryManager();
		try
		{
			this.qman.addTemplate(new QueryTemplate("templates/master_query.txt"));
		}
		catch(Exception e)
		{
			System.err.println("Error loading query template.");
			return;
		}
		
    	/* Connect to the database */
    	DBAccess db = new DBAccess();
    	dbAccess = db;
    		
    	//System.out.println("Connecting to database...\n");
    	
    	try
    	{
    		conn = db.getConnection();
    	}
    	catch(SQLException e)
    	{
    		System.err.println("Error connecting to database.");
    		e.printStackTrace();
    	}
    	
    	/* Bring up GUI */
    	showLogin();

    	/* Disconnect from database */
    	//db.cleanup(conn);
    	
	}
	
	public void showLogin() {
		new LoginWindow(this);
	}
	
	public void reshowLogin(JFrame frame) {
		frame.dispose();
		new LoginWindow(this);
	}
	
	public void registerUser(LoginWindow log) {
		log.dispose();
		new RegWindow(this);
	}
	
	public void loginSuccess(LoginWindow log) {
		log.dispose();
		new SearchWindow(this);
	}
	
	public void centerFrame(JFrame frame) {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		Point newLocation = new Point(middle.x - (frame.getWidth() / 2), 
		                              middle.y - (frame.getHeight() / 2));
		frame.setLocation(newLocation);
    }
	
	public QueryManager getManager() { return this.qman; }
	
	public Connection getConnection() { return this.conn; }
	
	public DBAccess getDatabaseAccess() { return this.dbAccess; }
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
            		CraveGUI crave = new CraveGUI();
            	} catch (SQLException e) {
            		System.out.println("ERROR: Could not connect to the database");
            		e.printStackTrace();
            		System.exit(-1);
            	}
            }
        });
    }
}
