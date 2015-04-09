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
		
    	/* Connect to the database */
    	DBAccess db = new DBAccess();
    	dbAccess = db;
    		
    	//System.out.println("Connecting to database...\n");
    	
    	//conn = db.getConnection();
    	
    	/* Bring up GUI */
    	createAndShowGUI(this);

    	/* Disconnect from database */
    	//db.cleanup(conn);
    	
	}
	
	public void createAndShowGUI(CraveGUI crave) {
		new LoginWindow(crave);
	}
	
	public void loginSuccess(LoginWindow log) {
		log.dispose();
		SearchWindow search = new SearchWindow();
	}
	
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
