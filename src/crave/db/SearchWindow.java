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
        
		/* Create the components of the login window */
        JTextField dishText = new JTextField();
        JComboBox types = new JComboBox(new String[]{"Burger", "Noodles", "Sushi", "Pizza"});
        JLabel dishLabel = new JLabel("Words the dish name may contain: ");
        JButton search = new JButton("Search");
        
        /* Adjust text fields */
        dishText.setColumns(15);
        
        /* Adjust button */
        search.addActionListener(this);			//set button responder to this window
        
        /* Create panels */
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
        
        /* Add components to panels */
        searchPanel.add(dishLabel);
        searchPanel.add(dishText);
        searchPanel.add(types);
        searchPanel.add(search);
        
        /* Add the panels to the top-level content pane */
        Container pane = getContentPane();		//Outermost frame's c-pane
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(searchPanel);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}
