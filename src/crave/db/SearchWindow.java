package crave.db;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SearchWindow extends JFrame implements ActionListener {
	
	public SearchWindow() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		addComponentsToPane();
		
		setSize(700,300);
		pack();
		setVisible(true);
	}
	
	/**
	 * Adds labels, text fields, buttons, panels to frame.
	 */
	private void addComponentsToPane() {
		String[] typeVals = {"Any", "Burger", "Pizza", "Noodles", "Sushi"};
		String[] originsVals = {"Any", "Italian", "Chinese", "American", "Thai", "Indian"};
		String[] orderByVals = {"Price", "Rating"};
		
		/* Create the components of the login window */
        JTextField dishText = new JTextField();
        JComboBox types = new JComboBox(typeVals);
        JComboBox origins = new JComboBox(originsVals);
        JComboBox orderBy = new JComboBox(orderByVals);
        
        JLabel titleLabel = new JLabel("Crave");
        JLabel detailLabel = new JLabel("Search our database to find the food you're craving!");
        JLabel dishLabel = new JLabel("Words the dish's name contains?");
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
        dishText.setColumns(15);
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
        
        /* Add the panels to the top-level content pane */
        Container pane = getContentPane();		//Outermost frame's c-pane
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(titlePanel);
        pane.add(centerPanel);
        pane.add(searchPanel);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}
