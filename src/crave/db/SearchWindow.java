package crave.db;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.*;

public class SearchWindow extends JFrame implements ActionListener {
	
	public CraveGUI crave;
	private JTextArea resultText;
	private JScrollPane scrollPane;
	private HashMap<Component, String> componentMap;
	private JPanel masterButtonPanel;
	private GridBagConstraints constraints;
	private String titles[] = {"Dish Name", "Rest. Name", "Rest. Address", "Price", "Avg. Rating"};
	private int maxValueLengths[] = { 15, 15, 20, 6, 4 };
	
	public SearchWindow(CraveGUI gui) {
		crave = gui;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.componentMap = new HashMap<Component, String>(4);
		
		addComponentsToPane();
		setSize(700,300);
		pack();
		crave.centerFrame(this);
		
		setVisible(true);
	}
	
	public JScrollPane getScrollPane() { return this.scrollPane; }
	
	private void setScrollPane(JScrollPane p) { this.scrollPane = p; }
	
	public HashMap<Component, String> getComponentMap() { return this.componentMap; }
	
	private void setComponentMap(HashMap<Component, String> map) { this.componentMap = map; }
	
	public JPanel getMasterPanel() { return this.masterButtonPanel; }
	
	private void setMasterPanel(JPanel p) { this.masterButtonPanel = p; }
	
	public GridBagConstraints getConstraints() { return this.constraints; }
	
	private void setConstraints(GridBagConstraints g) { this.constraints = g; }
	
	/**
	 * Adds labels, text fields, buttons, panels to frame.
	 */
	private void addComponentsToPane() {
		String[] typeVals = {"None", "Any", "Burger", "Pizza", "Noodles", "Sushi"};
		String[] originsVals = {"None", "Any", "Italian", "Chinese", "American", "Thai", "Indian"};
		String[] orderByVals = {"Price", "Rating"};
		
		/* Create the components of the search window */
		JTextArea results = new JTextArea();
		results.setEditable(false);
		this.resultText = results;
		
		this.masterButtonPanel = new JPanel(new GridLayout(2, 5));
		this.constraints = new GridBagConstraints();
		this.constraints.fill = GridBagConstraints.HORIZONTAL;
		this.constraints.anchor = GridBagConstraints.LINE_START;
		resetResults();
		
        JTextField dishText = new JTextField(15);
        this.getComponentMap().put(dishText, "Dish Name");
        
        JComboBox types = new JComboBox(typeVals);
        this.getComponentMap().put(types, "Type");
        
        JComboBox origins = new JComboBox(originsVals);
        this.getComponentMap().put(origins, "Origin");
        
        JComboBox orderBy = new JComboBox(orderByVals);
        this.getComponentMap().put(orderBy, "Order");
        
        JLabel titleLabel = new JLabel("Crave");
        JLabel detailLabel = new JLabel("Search our database to find the food you're craving!");
        JLabel dishLabel = new JLabel("Search keywords?");
        JLabel typeLabel = new JLabel("The type of dish?");
        JLabel originsLabel = new JLabel("Origin of the dish?");
        JLabel orderByLabel = new JLabel("Sort the results by...");
        
        JButton search = new JButton("Search");
        
        /* Adjust labels */
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        dishLabel.setLabelFor(dishText);
        typeLabel.setLabelFor(types);
        originsLabel.setLabelFor(origins);
        orderByLabel.setLabelFor(orderBy);
        
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dishLabel.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(50, 25, 0, 25));
        originsLabel.setBorder(BorderFactory.createEmptyBorder(50, 25, 0, 25));
        orderByLabel.setBorder(BorderFactory.createEmptyBorder(50, 25, 25, 25));
        
        /* Adjust text fields */
        Font medFont = dishText.getFont().deriveFont(Font.PLAIN, 15f);
        dishText.setFont(medFont);
        dishText.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));
        
        /* Adjust combo boxes */
        types.setBorder(BorderFactory.createEmptyBorder(50, 25, 0, 25));
        origins.setBorder(BorderFactory.createEmptyBorder(50, 25, 0, 25));
        orderBy.setBorder(BorderFactory.createEmptyBorder(50, 25, 0, 25));
        
        /* Adjust button */
        search.addActionListener(this);			//set button responder to this window
        
        /* Create panels */
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(25,25,13,25));
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(25,25,13,25));
        
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        
        JScrollPane resultPane = new JScrollPane(results);
//        JScrollPane resultPane = new JScrollPane(this.getMasterPanel());
        resultPane.setBackground(Color.WHITE);
        resultPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Search Result(s)"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        resultPane.getVerticalScrollBar().setUnitIncrement(16);
        resultPane.getHorizontalScrollBar().setUnitIncrement(16);
        resultPane.setSize(this.getSize());
//        resultPane.add(this.getMasterPanel());
        resultPane.setVisible(true);
        
        /* Add components to panels */
        titlePanel.add(titleLabel);
        titlePanel.add(detailLabel);
        
        labelPanel.add(dishLabel);
        labelPanel.add(typeLabel);
        labelPanel.add(originsLabel);
        labelPanel.add(orderByLabel);
        
        settingsPanel.add(dishText);
        settingsPanel.add(types);
        settingsPanel.add(origins);
        settingsPanel.add(orderBy);
        
        centerPanel.add(labelPanel);
        centerPanel.add(settingsPanel);

        searchPanel.add(search);
        
        leftPanel.add(titlePanel);
        leftPanel.add(centerPanel);
        leftPanel.add(searchPanel);
        
        /* Add the panels to the top-level content pane */
        Container pane = getContentPane();		//Outermost frame's c-pane
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(leftPanel);
        pane.add(resultPane);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		System.out.println("[SEARCH WINDOW] args to parse out of input: " + this.crave.getManager().getRules());
		
		String arg = null;
		
		//crush fields down into a search string
		StringBuilder argString = new StringBuilder();
				
		//run through all components in this instance
		for(Component c : this.getComponentMap().keySet())
		{
			arg = this.getInputArgOfComponent(c);
			System.out.println("ARG: " + arg);
			if(arg == null)
			{
				System.err.println("Unexpected component type: " + c);
			}
			else
			{
				argString.append(arg);
			}
		}
		
		System.out.println("[SEARCH WINDOW] argument string: " + argString.toString().trim());
		
		DBAccess db = this.crave.getDatabaseAccess();
		
		//get the result of the query
		Pair<ResultSet, Statement> pair = db.generalQuery(this.crave.getConnection(), 
							argString.toString().trim(), this.crave.getManager());
		
		ResultSet rs = pair.getVal1();
		
		//update the result window with the query result.
		//this.getResultWindow().update(rs);
		update(rs);
		
		try { pair.getVal2().close(); }
		catch(SQLException error) { error.printStackTrace(); }
	}
	
	public String getInputArgOfComponent(Component c)
	{	
		String map = this.getComponentMap().get(c);
		String text = null;
		if(c instanceof JTextField)
		{
			return (text = ((JTextField)c).getText()).equals("") ? "" : " SIGNARG2 LIKE" + " WORDARG " + text;
		}
		else if(c instanceof JComboBox)
		{
			text = (String)((JComboBox)c).getSelectedItem();
			if(map.equals("Type")) 
			{ 
				if(text.equals("None") || text.equals("Any")) { return ""; }
				else { return " TYPEARG \"" + text + "\""; }
			}
			else if(map.equals("Origin")) 
			{ 
				if(text.equals("None") || text.equals("Any")) { return ""; }
				else { return " ORIGINARG \"" + text + "\""; }
			}
			else if(map.equals("Order")) 
			{ 
				if(text.equals("Price")) { return " ORDERARG serv.price"; } 
				else if(text.equals("Rating")) { return " ORDERARG d.avgRating"; }
				else { System.err.println("Unexpected ordering argument"); }
			}
			else { System.err.println("Unexpected container mapping: " + map); }
		}
		
		return null;
	}
	
	/**
	 * Updates the results pane with the latest search results
	 * @param set The ResultSet returned from the SQL query
	 */
	public void update(ResultSet set)
	{
		try
		{
			resetResults();
			
			//this contains useful information like the number of columns per tuple...things like that
			ResultSetMetaData metaData = set.getMetaData();
			
			System.out.println("Number of columns: " + metaData.getColumnCount());
			int count = 0;
			
			String value = null;
			//iterate over every tuple
			while(set.next())
			{
				//iterate over every column in the tuple
				for(int i = 1; i <= metaData.getColumnCount(); i++)
				{
					
					//append column of tuple to field
					
					value = set.getString(i);
					if(value.length() > this.maxValueLengths[i - 1])
					{
						resultText.append(value.substring(0, this.maxValueLengths[i - 1]));
					}
					else
					{
						resultText.append(value);
					}
					
					if(i < metaData.getColumnCount()) { resultText.append("\t"); }
					
					//worry about clickable things later
				}
				resultText.append("\n");
				count++;
			}
			
			if (count == 0) {
				resultText.append("No results found. Try expanding your search.");
				resultText.append("\n");
			}
			
			//make sure window is legal and repaint
			this.revalidate();
			this.repaint();
			this.pack();
		}
		catch(SQLException e)
		{
			System.err.println("Error updating ResultWindow with ResultSet");
			e.printStackTrace();
		}
	}
	
	private void resetResults()
	{
//		for(int i = 0; i < 5; i++)
//		{
//			this.getConstraints().gridx = i;
//			this.getConstraints().gridy = 0;
//			this.getMasterPanel().add(new JButton(this.titles[i]));
//		}
//		
//		for(int i = 0; i < 5; i++)
//		{
//			this.getConstraints().gridx = i;
//			this.getConstraints().gridy = 1;
//			this.getMasterPanel().add(new JButton("--------------------"));
//		}
		
		
		
		resultText.setText(null);
		resultText.append("dish name \t rest. name \t rest. address \t\t price \t avg rating\n");
		resultText.append("------------------------------------------------------------------------" +
				"----------------------------------------------------------\n");
	}
	
}