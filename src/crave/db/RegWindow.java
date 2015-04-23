package crave.db;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RegWindow extends JFrame implements ActionListener {
	
	private CraveGUI crave;
	private JTextField name;
	private JTextField user;
	private JPasswordField pw;
	
	public RegWindow(CraveGUI gui) {
		crave = gui;
		setTitle("Crave - Cleveland Menu Database");	//frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addComponentsToPane();		//add all elements to frame
		setSize(500, 500);		//adjust frame
		pack();							//make frame visible
		crave.centerFrame(this);
	    setVisible(true);
	}
	
	private void addComponentsToPane() {
		JTextField nameText = new JTextField(20);
		JTextField userText = new JTextField(15);
        JPasswordField pwText = new JPasswordField(15);
        JLabel titleLabel = new JLabel("Join Crave today!");
        JLabel detailLabel = new JLabel("Enter your account information below.");
        JLabel nameLabel = new JLabel("Name:      ");
        JLabel nameDetail = new JLabel("    (ex. Bob Smith)");
        JLabel userLabel = new JLabel("Username:    ");
        JLabel userDetail = new JLabel("    (ex. abc123)");
        JLabel pwLabel = new JLabel("Password:    ");
        JLabel pwDetail = new JLabel("    (4-16 alphanumeric characters)");
        JLabel space = new JLabel("          ");
        JButton back = new JButton("Back");
        JButton register = new JButton("Register!");
        
        /* Adjust labels */
        nameLabel.setLabelFor(nameText);
        userLabel.setLabelFor(userText);		//set the labels to their
        pwLabel.setLabelFor(pwText);			//respective components
        nameDetail.setLabelFor(nameText);
        userDetail.setLabelFor(userText);
        pwDetail.setLabelFor(pwText);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        /* Adjust text boxes */
        nameText.setAlignmentX(Component.CENTER_ALIGNMENT);
        userText.setAlignmentX(Component.CENTER_ALIGNMENT);
        pwText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        /* Adjust buttons */
        register.addActionListener(this);
        back.addActionListener(this);
        back.setActionCommand("back");
        
        name = nameText;
        user = userText;
        pw = pwText;
        
        /* Create panels */
        JPanel titlePanel = new JPanel();		// panel for title and detail labels
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20,100,15,100));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20,100,15,100));
        
        JPanel labelPanel = new JPanel();		// panel for label related components
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(20,20,15,20));
        
        JPanel inputPanel = new JPanel();	// panel for input text related components
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        
        JPanel detailPanel = new JPanel();		// panel for detail label related components
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

        JPanel buttonPanel = new JPanel();		// panel for register button
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15,100,20,100));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        /* Add components to respective panels */
        titlePanel.add(titleLabel);
        titlePanel.add(detailLabel);
        labelPanel.add(nameLabel);
        inputPanel.add(nameText);
        detailPanel.add(nameDetail);
        labelPanel.add(userLabel);
        inputPanel.add(userText);
        detailPanel.add(userDetail);
        labelPanel.add(pwLabel);
        inputPanel.add(pwText);
        detailPanel.add(pwDetail);
        buttonPanel.add(back);
        buttonPanel.add(space);
        buttonPanel.add(register);
        centerPanel.add(labelPanel);
        centerPanel.add(inputPanel);
        centerPanel.add(detailPanel);
        
        /* Add the panels to the top-level content pane */
        Container pane = getContentPane();		//Outermost frame's c-pane
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(titlePanel);
        pane.add(centerPanel);
        pane.add(buttonPanel);
        
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("back")) {
			crave.reshowLogin(this);
		}
		else {
			String namearg = name.getText();
			String username = user.getText();
			char[] pwInput = pw.getPassword();
			if (isInputValid(username, pwInput)) {
				registerUser(namearg, username, pwInput);
				JOptionPane.showMessageDialog(this, "Congratulations! You're in!");
				crave.reshowLogin(this);
	        }
			else {
	            JOptionPane.showMessageDialog(this,
	                "Invalid Credentials:" + '\n' +
	                "Username: > 5 characters" + '\n' +
	                "Password: 4-16 alphanumeric characters.",
	                "Error Message",
	                JOptionPane.ERROR_MESSAGE);
	        }
		}
	}
	
	private void registerUser(String namearg, String username, char[] password) {
		//use DB to add user insertion
	}
	
	private boolean isInputValid(String username, char[] pwInput) {
		// Check username
		if (username.length() <= 5) {
			return false;
		}
		
		// Check password
		if (pwInput.length < 4 || pwInput.length > 16) {
			return false;
		}
		for (int i = 0; i < pwInput.length; i++) {
			if(!isCharValid(pwInput[i])) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isCharValid(char c) {
		return ((c >= 'A' && c <= 'Z') ||
				(c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9'));
	}

}
