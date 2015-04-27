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
	
	//this is a reference to the JScrollPane that the results of a query are added to...we need
	//to keep track of it so that we can update it.
	private JScrollPane scrollPane;
	
	//this is what does the argument parsing...it maps the JTextFields and whatnot to their intended
	//purpose as an argument for the QueryManager (or else we have to sift through every component
	//attached to the GUI...including the JScrollPane and JPanels and stuff...worth the extra memory
	private HashMap<Component, String> componentMap;
	
	//These actually are very important. The masterPanel is the overall panel that sits just under the
	//frame. It is responsible for splitting the frame in half between querying and results.
	
	//scrollPanePanel is VERY important, it is the framework that OutputTuple instances (fance buttons)
	//are stored in the JScrollPane.
	private JPanel masterPanel, scrollPanePanel;
	
	//we need a method to place components in different locations and whatnot in a frame or component (panel, ...)
	//this is done via a GridBagConstraints seeing as the layout of the components must be set out using either
	//GridLayout or GridBagLayout.
	//
	//Helpful tidbit is that in a GridLayout, all components are equally sized and distributed throughout the
	//component that is containing it. This is why buttons and stuff resize to cover ALL the space the component
	//has. GridBagLayout on the other hand does not do this...it allows elements to be of different size, so USE IT
	//whenever you can.
	private GridBagConstraints constraints;
	
	//the titles for the output attributes
	private String titles[] = {"Dish Name", "Rest. Name", "Rest. Address", "Price", "Avg. Rating"};
	
	//the maximum number of characters an output attribute can have
	private int maxValueLengths[] = { 30, 30, 50, 10, 4 };
	
	//this maps string values to their IDs and full names. We need this because we cannot search for an ID given
	//a name, it is not a primary key, and secondly the way formatting works is that if the attribute is longer than
	//its maxValueLength, it is cutoff after those many characters. So we may not even have a full name to work with
	//in the first place.
	private HashMap<String, Pair<String,Integer> > abbreviatedResultMap;
	
	
	public SearchWindow(CraveGUI gui) {
		crave = gui;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.componentMap = new HashMap<Component, String>(4);
		
		this.abbreviatedResultMap = new HashMap<String, Pair<String, Integer> >();
		
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
	
	public GridBagConstraints getConstraints() { return this.constraints; }
	
	private void setConstraints(GridBagConstraints g) { this.constraints = g; }
	
	/**
	 * Adds labels, text fields, buttons, panels to frame.
	 */
	private void addComponentsToPane() {
		String[] typeVals = {"Any", "Appetizers", "Burrito", "Pizza", "Meat & Seafood", "Noodles & Rice", "Sandwiches", "Vegetables"};
		String[] originsVals = {"Any", "American", "Asian", "Italian", "Tex-Mex"};
		String[] orderByVals = {"Price", "Rating"};
		
		
		this.constraints = new GridBagConstraints();
		this.constraints.fill = GridBagConstraints.HORIZONTAL;
		this.constraints.anchor = GridBagConstraints.LINE_START;
		
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
        
        
        //create the scrollPane and its associated panel
        this.scrollPanePanel = new JPanel(new GridBagLayout());
        this.scrollPanePanel.setBackground(Color.WHITE);
        this.scrollPane = new JScrollPane(this.scrollPanePanel);
        this.scrollPane.setBackground(Color.WHITE);
        this.scrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Search Result(s)"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        System.out.println("SIZE: " + this.getSize().getWidth() + ", " + this.getSize().getHeight());
        this.scrollPane.setSize(700, 300);
        
        //initialize empty query
        resetResults();
        this.scrollPane.setVisible(true);
        
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
        this.masterPanel = new JPanel(new GridLayout(1, 2));
        this.masterPanel.add(leftPanel, "West");
        this.masterPanel.add(this.scrollPane, "East");
        pane.add(this.masterPanel, "Center");
        
        System.out.println("SIZE: " + leftPanel.getSize().getWidth() + ", " + leftPanel.getSize().getHeight());
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
			
			//we need to reset this....this is why we kept track of it using a global variable
			//(we need to call resetResults() and have modify to this panel)
			this.scrollPanePanel = new JPanel(new GridBagLayout());
			this.scrollPanePanel.setBackground(Color.WHITE);
			
			this.resetResults();
			
			//this contains useful information like the number of columns per tuple...things like that
			ResultSetMetaData metaData = set.getMetaData();
			
			System.out.println("Number of columns: " + metaData.getColumnCount());
			int count = 0;
			
			String value = null;
			
			//this is a fancy button..represents one output tuple....worry about formatting
			OutputTuple t = null;
			
			//this is an output attribute used to append spaces for formatting
			StringBuilder builder = new StringBuilder();
			
			//since we only have 5 output attributes we want and 7 output attributes from the query
			//(we also get dishID and restaurantID), we need to keep track of an index in the maxValueLength
			//array so we can format the given attribute
			int k = 0;
			
			//iterate over every tuple
			while(set.next())
			{
				
				//this is how we place a button....we set the location in the GridBagConstraints and
				//then will add the button to the scrollPanePanel using the constraints.
				this.constraints.gridy = count + 2;
				
				t = new OutputTuple(metaData.getColumnCount());
				
				//iterate over every column in the tuple
				for(int i = 1; i <= metaData.getColumnCount(); i++)
				{
					
					//i == 2 and i == 4 represent (dishID and restaurantID) respectively..skip these iterations
					if(i != 2 && i != 4)
					{
						
						//get the output attribute
						value = set.getString(i);
						
						//we need to adjust k so that it contains the correct index in the maxValueLength array
						if(i > 4) { k = i - 2; }
						else if(i > 2) { k = i - 1; }
						else { k = i; }
						
						//if there are too many characters in the output attribute we need to cut it off
						if(value.length() > this.maxValueLengths[k - 1])
						{

							builder.append(value.substring(0, this.maxValueLengths[k - 1]));
							
							//its ok though if we cut short an attribute we care about (dishName or restaurantName)
							//because we have the map to save us
							if(i == 1 || i == 3)
							{
								this.abbreviatedResultMap.put(value.substring(0, this.maxValueLengths[k - 1]), 
										new Pair<String, Integer>(value, Integer.parseInt(set.getString(i + 1))));
							}
						}
						else //the string isnt too long so append the whole thing
						{
							
							builder.append(value);
							
							//we need to pad with spaces for formatting...play with this
							for(int j = 0; j < this.maxValueLengths[k - 1] - value.length(); j++)
							{
								builder.append(" ");
							}
							
							//we still need to save the attributes and for the sake of abstracting away the process
							//of fetching the information for a resultWindow
							if(i == 1 || i == 3)
							{
								this.abbreviatedResultMap.put(value, new Pair<String, Integer>(value, Integer.parseInt(set.getString(i + 1))));
							}
						}
						
						//add the formatted attribute to the fancy button
						t.addLabel(builder.toString());
						builder = new StringBuilder();
					}
				}

				//this actually makes the outputtuple ready to go
				t.finalizeTuple(true);
				
				//make the buttons actually do something...open a resaurantWindow with info
				//from the map
				t.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Pair<String, Integer> r = SearchWindow.this.abbreviatedResultMap.get(((OutputTuple)e.getSource()).getLabels().get(2).trim());
						Pair<String, Integer> d = SearchWindow.this.abbreviatedResultMap.get(((OutputTuple)e.getSource()).getLabels().get(0).trim());
						new RestaurantWindow(SearchWindow.this.crave, r.getVal2(), d.getVal1());
					}
				});
				
				//add the button at the specified location from constraints
				this.scrollPanePanel.add(t, this.constraints);
				
				count++;
			}
						
			//remove the current scrollpane
			this.masterPanel.remove(this.scrollPane);
			
			//we NEED to remove and add the scrollpane because we need to specify the component the 
			//scrollPane focuses on through the constructor
			this.scrollPane = new JScrollPane(this.scrollPanePanel);
			this.scrollPane.setBackground(Color.WHITE);
			this.scrollPane.setPreferredSize(new Dimension(700, 300));
			this.masterPanel.add(this.scrollPane, "East");
			
			this.setPreferredSize(this.getSize());
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
		
		OutputTuple t = new OutputTuple(5);
		StringBuilder b = new StringBuilder();
		
		//add the titles to the first button and pad with spaces
		for(int i = 0; i < 5; i++)
		{
			b.append(this.titles[i]);
			for(int j = 0; j < this.maxValueLengths[i] - this.titles[i].length(); j++)
			{
				b.append(" ");
			}
			t.addLabel(b.toString());
			b = new StringBuilder();
		}
		
		//add to scrollPane
		t.finalizeTuple(false);
		this.constraints.gridy = 0;
		this.scrollPanePanel.add(t, this.constraints);
		
		//add the layer of ------------------s
		t = new OutputTuple(5);
		for(int i = 0; i < 700; i++) { b.append("-"); }
		t.addLabel(b.toString());
		t.finalizeTuple(false);
		this.constraints.gridy = 1;
		this.scrollPanePanel.add(t, this.constraints);
	}
	
}