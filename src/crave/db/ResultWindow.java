package crave.db;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class ResultWindow extends JFrame implements ActionListener {

	private CraveGUI crave;
	private SearchWindow window;
	private JTextArea textArea;
	private JScrollPane pane;
	
	public ResultWindow(CraveGUI c, SearchWindow w)
	{
		super();
		this.crave = c;
		this.window = w;
		
		this.textArea = new JTextArea();
		
		this.pane = new JScrollPane(this.textArea);
		this.pane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Search Result(s)"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		this.pane.getVerticalScrollBar().setUnitIncrement(16);
		this.pane.getHorizontalScrollBar().setUnitIncrement(16);
		this.pane.setPreferredSize(this.window.getSize());
		
		
		this.add(pane, "Center");
		
		this.setSize(this.window.getSize());
		
		//position the Frame NEXT to the CraveGUI window
		this.setLocation(this.window.getX() + (int)this.window.getSize().getWidth(), this.window.getY());
		
		this.pack();
		
		this.setVisible(true);
	}
	
	public CraveGUI getCraveGUI() { return this.crave; }
	
	public JScrollPane getScrollPane() { return this.pane; }
	
	public void setScrollPane(JScrollPane p) { this.pane = p; }
	
	public JTextArea getOutputText() { return this.textArea; }
	
	public void setOutputText(JTextArea p) { this.textArea = p; }
	
	public SearchWindow getSearchWindow() { return this.window; }
	
	public void setSearchWindow(SearchWindow w) { this.window = w; }
	
	public void update(ResultSet set)
	{
		try
		{
			this.getOutputText().setText(null);
			this.getOutputText().append("dish name \t rest. name \t rest. address \t price \t avg rating\n");
			this.getOutputText().append("------------------------------------------------------------------------" +
					"----------------------------------------------------------\n");
			
			//this contains useful information like the number of columns per tuple...things like that
			ResultSetMetaData metaData = set.getMetaData();
			
			System.out.println("Number of columns: " + metaData.getColumnCount());
			
			//iterate over every tuple
			while(set.next())
			{
				//iterate over every column in the tuple
				for(int i = 1; i <= metaData.getColumnCount(); i++)
				{
					
					//append column of tuple to field
					this.getOutputText().append(set.getString(i));
					
					if(i < metaData.getColumnCount()) { this.getOutputText().append("\t"); }
					
					//worry about clickable things later
				}
				this.getOutputText().append("\n");
			}
			//make sure window is legal and repaint
			//this.revalidate();
			//this.pack();
			//this.repaint();
		}
		catch(SQLException e)
		{
			System.err.println("Error updating ResultWindow with ResultSet");
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
