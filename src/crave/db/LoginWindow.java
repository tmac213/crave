package crave.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * The frame on the Crave GUI that contains the login window
 */
public class LoginWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -1393846861777119117L;
	private CraveGUI crave;			//the master gui class creating this
	private JTextField user;		//the username field
	private JPasswordField pass;	//the password field
	
	public LoginWindow(CraveGUI gui) {
		crave = gui;
		setTitle("Crave - Cleveland Menu Database");	//frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addComponentsToPane();		//add all elements to frame
		setSize(500, 500);		//adjust frame
		setBackground(Color.BLACK);
		pack();							//make frame visible
		crave.centerFrame(this);
	    setVisible(true);
	}
	
	/**
	 * Sets the frame location to the center of the user's screen
	 */
	private void centerFrame() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		Point newLocation = new Point(middle.x - (getWidth() / 2), 
		                              middle.y - (getHeight() / 2));
		setLocation(newLocation);
    }
	
	/**
	 * Adds labels, text fields, buttons, panels to frame.
	 */
	private void addComponentsToPane() {
        
		/* Create the components of the login window */
        JTextField userText = new JTextField(15);
        JPasswordField pwText = new JPasswordField(15);
        JLabel titleText = new JLabel("Crave");
        JLabel detailText = new JLabel("Sign in to find the food you're craving!");
        JLabel userLabel = new JLabel("Username:    ");
        JLabel pwLabel = new JLabel("Password:    ");
        JLabel space = new JLabel("          ");
        JButton reg = new JButton("Sign Up");
        JButton login = new JButton("Sign In");
        
        /* Adjust labels */
        userLabel.setLabelFor(userText);		//set the labels to their
        pwLabel.setLabelFor(pwText);			//respective components
        
        /* Adjust text fields */
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailText.setAlignmentX(Component.CENTER_ALIGNMENT);
        userText.setAlignmentX(Component.CENTER_ALIGNMENT);
        pwText.setAlignmentX(Component.CENTER_ALIGNMENT);
        userText.setColumns(15);
        pwText.setColumns(15);
        
        /* Adjust button */
        login.addActionListener(this);			//set button responder to this window
        reg.addActionListener(this);
        reg.setActionCommand("register");

        user = userText;
        pass = pwText;		//set global reference to password field for validity check
        
        /* Create panels */
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20,75,15,75));
        
		JPanel userPanel = new JPanel();	// panel for username related components
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20,75,15,75));
        
        JPanel pwPanel = new JPanel();		// panel for password related components
        pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));
        pwPanel.setBorder(BorderFactory.createEmptyBorder(15,75,15,75));

        JPanel logPanel = new JPanel();		// panel for login button
        logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.X_AXIS));
        logPanel.setBorder(BorderFactory.createEmptyBorder(15,75,20,75));
        logPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        /* Add components to panels */
        titlePanel.add(titleText);
        titlePanel.add(detailText);
        userPanel.add(userLabel);
        userPanel.add(userText);
        pwPanel.add(pwLabel);
        pwPanel.add(pwText);
        logPanel.add(reg);
        logPanel.add(space);
        logPanel.add(login);
        
        /* Add the panels to the top-level content pane */
        Container pane = getContentPane();		//Outermost frame's c-pane
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(titlePanel);
        pane.add(userPanel);
        pane.add(pwPanel);
        pane.add(logPanel);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("register")) {
			crave.registerUser(this);
		}
		else {
			if (isPasswordCorrect()) {
				crave.loginSuccess(this, user.getText());
	        } 
			else {
	            JOptionPane.showMessageDialog(this,
	                "Invalid password. Try again.",
	                "Error Message",
	                JOptionPane.ERROR_MESSAGE);
	        }
		}
	}
	
	private boolean isPasswordCorrect() {
		char[] password = crave.dbAccess.queryPassword(user.getText(), crave.conn);
		char[] input = pass.getPassword();
		if (password.length != input.length) {
			return false;
		}
		else {
			return Arrays.equals(password, input);
		}
	}
	
}
